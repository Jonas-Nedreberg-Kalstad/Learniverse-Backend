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
     * The scores are calculated using the Levenshtein distance between the search terms and the names of the courses, categories, and topics.
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
    public SearchResult search(SearchCriteria criteria, Pageable pageable) {

        List<ScoredCourse> courses = this.courseSearch(criteria.getCourseName(), pageable);

        List<ScoredTopic> topics = this.topicSearch(criteria.getTopicName(), pageable);

        List<ScoredCategory> categories = this.categorySearch(criteria.getCategoryName(), pageable);

        if (courses.isEmpty() && topics.isEmpty() && categories.isEmpty()){
            return null;
        }

        return new SearchResult(courses, categories, topics);
    }


    /**
     * Searches for courses based on the provided course name and paginates the results.
     * The scores are calculated using the Levenshtein distance between the search terms and the names of the courses.
     * The scores are normalized to a percentage to reflect the closeness of the match.
     * The results are filtered based on predefined score thresholds to ensure relevance.
     * BK-trees are used to optimize the search process by efficiently finding close matches.
     *
     * @param courseName the name of the course to search for.
     * @param pageable   the pagination information.
     * @return a paginated list of scored courses based on the search criteria.
     */
    private List<ScoredCourse> courseSearch(String courseName, Pageable pageable) {
        List<Course> courses = courseRepository.findByActiveTrue();
        return SearchUtils.genericSearch(
                courseName.toLowerCase(),
                pageable,
                courses,
                course -> course.getCourseName().toLowerCase(),
                course -> ScoreUtils.calculateAverageNormalizedScore(
                        Arrays.asList(course.getCourseName().toLowerCase().split(" ")),
                        Arrays.asList(courseName.toLowerCase().split(" "))
                ),
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
     * The scores are calculated using the Levenshtein distance between the search terms and the names of the categories.
     * The scores are normalized to a percentage to reflect the closeness of the match.
     * The results are filtered based on predefined score thresholds to ensure relevance.
     * BK-trees are used to optimize the search process by efficiently finding close matches.
     *
     * @param categoryName the name of the category to search for.
     * @param pageable     the pagination information.
     * @return a paginated list of scored categories based on the search criteria.
     */
    public List<ScoredCategory> categorySearch(String categoryName, Pageable pageable) {
        List<Category> categories = categoryRepository.findAll();
        return SearchUtils.genericSearch(
                categoryName.toLowerCase(),
                pageable,
                categories,
                category -> category.getCategory().toLowerCase(),
                category -> ScoreUtils.calculateAverageNormalizedScore(
                        Arrays.asList(category.getCategory().toLowerCase().split(" ")),
                        Arrays.asList(categoryName.toLowerCase().split(" "))
                ),
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
     * The scores are calculated using the Levenshtein distance between the search terms and the names of the topics.
     * The scores are normalized to a percentage to reflect the closeness of the match.
     * The results are filtered based on predefined score thresholds to ensure relevance.
     * BK-trees are used to optimize the search process by efficiently finding close matches.
     *
     * @param topicName the name of the topic to search for.
     * @param pageable  the pagination information.
     * @return a paginated list of scored topics based on the search criteria.
     */
    public List<ScoredTopic> topicSearch(String topicName, Pageable pageable) {
        List<Topic> topics = topicRepository.findAll();
        return SearchUtils.genericSearch(
                topicName.toLowerCase(),
                pageable,
                topics,
                topic -> topic.getTopic().toLowerCase(),
                topic -> ScoreUtils.calculateAverageNormalizedScore(
                        Arrays.asList(topic.getTopic().toLowerCase().split(" ")),
                        Arrays.asList(topicName.toLowerCase().split(" "))
                ),
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
     * The scores are calculated using the Levenshtein distance between the search terms and the full names of the users.
     * The scores are normalized to a percentage to reflect the closeness of the match.
     * The results are filtered based on predefined score thresholds to ensure relevance.
     * BK-trees are used to optimize the search process by efficiently finding close matches.
     *
     * @param userFullName the full name of the user to search for.
     * @param pageable     the pagination information.
     * @return a paginated list of scored users based on the search criteria.
     */
    public List<ScoredUser> userSearch(String userFullName, Pageable pageable) {
        List<User> users = this.userRepository.findByDeletedFalse();
        return SearchUtils.genericSearch(
                userFullName.toLowerCase(),
                pageable,
                users,
                user -> (user.getFirstName() + " " + user.getLastName()).toLowerCase(),
                user -> ScoreUtils.calculateAverageNormalizedScore(
                        Arrays.asList((user.getFirstName() + " " + user.getLastName()).toLowerCase().split(" ")),
                        Arrays.asList(userFullName.toLowerCase().split(" "))
                ),
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
