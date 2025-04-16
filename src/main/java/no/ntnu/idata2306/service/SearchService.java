package no.ntnu.idata2306.service;

import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.course.CourseListResponseDto;
import no.ntnu.idata2306.dto.course.CourseResponseDto;
import no.ntnu.idata2306.dto.course.details.CategoryDto;
import no.ntnu.idata2306.dto.course.details.TopicDto;
import no.ntnu.idata2306.dto.search.request.CategoryAndTopicsSearch;
import no.ntnu.idata2306.dto.search.request.SearchCriteria;
import no.ntnu.idata2306.dto.search.response.*;
import no.ntnu.idata2306.dto.user.UserResponseDto;
import no.ntnu.idata2306.mapper.UserMapper;
import no.ntnu.idata2306.mapper.course.CourseMapper;
import no.ntnu.idata2306.mapper.course.details.CategoryMapper;
import no.ntnu.idata2306.mapper.course.details.TopicMapper;
import no.ntnu.idata2306.model.User;
import no.ntnu.idata2306.model.course.details.Category;
import no.ntnu.idata2306.model.course.Course;
import no.ntnu.idata2306.model.course.details.Topic;
import no.ntnu.idata2306.repository.UserRepository;
import no.ntnu.idata2306.repository.course.details.CategoryRepository;
import no.ntnu.idata2306.repository.course.CourseRepository;
import no.ntnu.idata2306.repository.course.details.TopicRepository;
import no.ntnu.idata2306.util.ScoreThresholdUtils;
import no.ntnu.idata2306.util.ScoreUtils;
import no.ntnu.idata2306.util.SearchUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class SearchService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    @Autowired
    public SearchService(CourseRepository courseRepository, CategoryRepository categoryRepository, TopicRepository topicRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    /**
     * Searches for courses, categories, and topics based on the provided search criteria and calculates their relevance scores.
     * The scores are calculated using Damerau-Levenshtein distance for fuzzy matching.
     * The scores are normalized to a percentage to reflect the closeness of the match.
     * The results are filtered based on predefined score thresholds to ensure relevance.
     * The results are sorted by their scores in descending order to prioritize the most relevant matches.
     * The results paginated according to the provided pagination information.
     * BK-trees are used to optimize the search process by efficiently finding close matches.
     *
     * @param criteria  the search criteria containing the course name, category name, and topic name to search for.
     * @param pageable  the pagination information.
     * @return a SearchResult object containing the scored courses, categories, and topics, or null if no results are found.
     */
    public SearchResult multiParameterSearch(SearchCriteria criteria, Pageable pageable) {
        // Validate and prepare search inputs
        String courseName = criteria.getCourseName() != null ? criteria.getCourseName().trim().toLowerCase() : "";
        String topicName = criteria.getTopicName() != null ? criteria.getTopicName().trim().toLowerCase() : "";
        String categoryName = criteria.getCategoryName() != null ? criteria.getCategoryName().trim().toLowerCase() : "";

        List<ScoredCourse> courses = this.courseSearch(courseName, pageable);

        List<ScoredTopic> topics = this.topicSearch(topicName, pageable);

        List<ScoredCategory> categories = this.categorySearch(categoryName, pageable);

        if (courses.isEmpty() && topics.isEmpty() && categories.isEmpty()) {
            return null;
        }

        return new SearchResult(courses, categories, topics);
    }

    /**
     * Searches for courses based on the provided course name and paginates the results.
     * Uses the hybrid similarity approach with BK-Tree for efficient candidate retrieval.
     * Course names tend to be longer and may contain multiple words, making them well-suited
     * for our approach which uses Damerau-Levenshtein distance for fuzzy matching.
     *
     * @param courseName the name of the course to search for.
     * @param pageable   the pagination information.
     * @return a paginated list of scored courses based on the search criteria.
     */
    public List<ScoredCourse> courseSearch(String courseName, Pageable pageable) {
        if (courseName == null || courseName.isEmpty()) {
            return List.of();
        }
        
        List<Course> courses = courseRepository.findByActiveTrue();
        
        return SearchUtils.genericSearch(
                courseName,
                pageable,
                courses,
                Course::getCourseName,
                course -> {
                    List<String> correctWords = Arrays.asList(course.getCourseName().toLowerCase().split("\\s+"));
                    List<String> searchWords = Arrays.asList(courseName.toLowerCase().split("\\s+"));
                    return ScoreUtils.calculateSimilarityScore(correctWords, searchWords);
                },
                ScoreThresholdUtils.COURSE_SCORE_THRESHOLD,
                Course::getId,
                (course, score) -> {
                    CourseResponseDto courseDto = CourseMapper.INSTANCE.courseToResponseCourseDto(course);
                    return new ScoredCourse(courseDto, score);
                }
        );
    }

    /**
     * Searches for categories based on the provided category name and paginates the results.
     * Category names vary in length but are typically short to medium phrases.
     * The hybrid similarity approach works well here by using Damerau-Levenshtein distance for fuzzy matching.
     *
     * @param categoryName the name of the category to search for.
     * @param pageable     the pagination information.
     * @return a paginated list of scored categories based on the search criteria.
     */
    public List<ScoredCategory> categorySearch(String categoryName, Pageable pageable) {
        if (categoryName == null || categoryName.isEmpty()) {
            return List.of();
        }
        
        List<Category> categories = categoryRepository.findAll();
        
        return SearchUtils.genericSearch(
                categoryName,
                pageable,
                categories,
                category -> category.getCategory().toLowerCase(),
                category -> {
                    List<String> correctWords = Arrays.asList(category.getCategory().toLowerCase().split("\\s+"));
                    List<String> searchWords = Arrays.asList(categoryName.toLowerCase().split("\\s+"));
                    
                    return ScoreUtils.calculateSimilarityScore(correctWords, searchWords);
                },
                ScoreThresholdUtils.CATEGORY_SCORE_THRESHOLD,
                Category::getId,
                (category, score) -> {
                    CategoryDto categoryDto = CategoryMapper.INSTANCE.categoryToCategoryDto(category);
                    return new ScoredCategory(categoryDto, score);
                }
        );
    }

    /**
     * Searches for topics based on the provided topic name and paginates the results.
     * Topic names can be short terms or medium-length phrases.
     * The hybrid similarity approach using Damerau-Levenshtein distance automatically adapts 
     * to the length and structure of the text.
     *
     * @param topicName the name of the topic to search for.
     * @param pageable  the pagination information.
     * @return a paginated list of scored topics based on the search criteria.
     */
    public List<ScoredTopic> topicSearch(String topicName, Pageable pageable) {
        if (topicName == null || topicName.isEmpty()) {
            return List.of();
        }
        
        List<Topic> topics = topicRepository.findAll();
        
        return SearchUtils.genericSearch(
                topicName,
                pageable,
                topics,
                topic -> topic.getTopic().toLowerCase(),
                topic -> {
                    List<String> correctWords = Arrays.asList(topic.getTopic().toLowerCase().split("\\s+"));
                    List<String> searchWords = Arrays.asList(topicName.toLowerCase().split("\\s+"));
                    return ScoreUtils.calculateSimilarityScore(correctWords, searchWords);

                },
                ScoreThresholdUtils.TOPIC_SCORE_THRESHOLD,
                Topic::getId,
                (topic, score) -> {
                    TopicDto topicDto = TopicMapper.INSTANCE.topicToTopicDto(topic);
                    return new ScoredTopic(topicDto, score);
                }
        );
    }

    /**
     * Searches for users based on the provided full name and paginates the results.
     * Usernames require special handling since first and last names may be entered in any order.
     * The word-level fuzzy matching with Damerau-Levenshtein distance is especially valuable here.
     *
     * @param userFullName the full name of the user to search for.
     * @param pageable     the pagination information.
     * @return a paginated list of scored users based on the search criteria.
     */
    public List<ScoredUser> userSearch(String userFullName, Pageable pageable) {
        if (userFullName == null || userFullName.isEmpty()) {
            return List.of();
        }
        
        List<User> users = this.userRepository.findByDeletedFalse();
        
        return SearchUtils.genericSearch(
                userFullName.toLowerCase(),
                pageable,
                users,
                user -> (user.getFirstName() + " " + user.getLastName()).toLowerCase(),
                user -> {
                    String userName = (user.getFirstName() + " " + user.getLastName()).toLowerCase();
                    List<String> correctWords = Arrays.asList(userName.split("\\s+"));
                    List<String> searchWords = Arrays.asList(userFullName.toLowerCase().split("\\s+"));
                    
                    return ScoreUtils.calculateSimilarityScore(correctWords, searchWords);
                },
                ScoreThresholdUtils.USER_SCORE_THRESHOLD,
                User::getId,
                (user, score) -> {
                    UserResponseDto userResponseDto = UserMapper.INSTANCE.userToUserResponseDto(user);
                    return new ScoredUser(userResponseDto, score);
                }
        );
    }

    /**
     * Filtering for courses based on the provided category ID and a list of topic IDs, and converts them to CourseResponseDto objects.
     * If no courses are found, an empty list is returned.
     *
     * @param request  the request object containing category ID and topic IDs, difficulty level ID, and max price.
     * @param pageable the pagination information.
     * @return a CourseListResponseDto object representing the courses that match the filtering criteria, or an empty object if no courses are found.
     */
    public CourseListResponseDto advancedIdsAndMaxPriceFiltering(CategoryAndTopicsSearch request, Pageable pageable) {
        Page<Course> coursesPage = this.courseRepository.searchCoursesByTopicsAndCategory(request.getCategoryId(), request.getTopicIds(),
                                                                                          request.getDifficultyLevelId(), request.getMaxPrice(), pageable);
        if (coursesPage.isEmpty()) {
            return new CourseListResponseDto(Collections.emptyList(), 0);
        }
        List<CourseResponseDto> courses =  coursesPage.stream()
                .map(CourseMapper.INSTANCE::courseToResponseCourseDto)
                .toList();

        return new CourseListResponseDto(courses, (int)coursesPage.getTotalElements());
    }
}
