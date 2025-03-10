package no.ntnu.idata2306.service;

import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.search.*;
import no.ntnu.idata2306.model.Category;
import no.ntnu.idata2306.model.Course;
import no.ntnu.idata2306.model.Topic;
import no.ntnu.idata2306.repository.CategoryRepository;
import no.ntnu.idata2306.repository.CourseRepository;
import no.ntnu.idata2306.repository.TopicRepository;
import no.ntnu.idata2306.util.ScoreThresholdUtils;
import no.ntnu.idata2306.util.ScoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class SearchService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;

    @Autowired
    public SearchService(CourseRepository courseRepository, CategoryRepository categoryRepository, TopicRepository topicRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.topicRepository = topicRepository;
    }

    /**
     * Searches for courses, categories, and topics based on the provided search criteria and calculates their relevance scores.
     * The scores are calculated using the Levenshtein distance between the search terms and the names of the courses, categories, and topics.
     * The scores are normalized to a percentage to reflect the closeness of the match.
     * The results are filtered based on predefined score thresholds to ensure relevance.
     * The results are sorted by their scores in descending order to prioritize the most relevant matches.
     * The results paginated according to the provided pagination information.
     *
     * @param criteria  the search criteria containing the course name, category name, and topic name to search for.
     * @param pageable  the pagination information.
     * @return a SearchResult object containing the scored courses, categories, and topics, along with pagination information.
     */
    public SearchResult search(SearchCriteria criteria, Pageable pageable) {
        List<Course> courses = courseRepository.findByActiveTrue();
        List<Category> categories = categoryRepository.findAll();
        List<Topic> topics = topicRepository.findAll();

        List<ScoredCourse> scoredCourses = new ArrayList<>();
        List<ScoredCategory> scoredCategories = new ArrayList<>();
        List<ScoredTopic> scoredTopics = new ArrayList<>();

        List<String> correctWords;
        List<String> searchWords;

        for (Course course : courses) {
            correctWords = Arrays.asList(course.getCourseName().split(" "));
            searchWords = Arrays.asList(criteria.getCourseName().split(" "));
            double courseNameScore = ScoreUtils.calculateAverageNormalizedScore(correctWords, searchWords);
            if (courseNameScore > ScoreThresholdUtils.COURSE_SCORE_THRESHOLD) { // Longer names has more inaccuracies
                scoredCourses.add(new ScoredCourse(course, courseNameScore));
            }
        }

        for (Category category : categories) {
            correctWords = Arrays.asList(category.getCategory().split(" "));
            searchWords = Arrays.asList(criteria.getCategoryName().split(" "));
            double categoryNameScore = ScoreUtils.calculateAverageNormalizedScore(correctWords, searchWords);
            if (categoryNameScore > ScoreThresholdUtils.CATEGORY_SCORE_THRESHOLD) { // Shorter names is more accurate
                scoredCategories.add(new ScoredCategory(category, categoryNameScore));
            }
        }

        for (Topic topic : topics) {
            correctWords = Arrays.asList(topic.getTopic().split(" "));
            searchWords = Arrays.asList(criteria.getTopicName().split(" "));
            double topicNameScore = ScoreUtils.calculateAverageNormalizedScore(correctWords, searchWords);
            if (topicNameScore > ScoreThresholdUtils.TOPIC_SCORE_THRESHOLD) { // Shorter names is more accurate
                scoredTopics.add(new ScoredTopic(new Topic(topic.getId(), topic.getTopic(), topic.getCourses()), topicNameScore));
            }
        }

        // Sorts lists, setting highest score first
        scoredCourses.sort(Comparator.comparingDouble(ScoredCourse::getScore).reversed());
        scoredCategories.sort(Comparator.comparingDouble(ScoredCategory::getScore).reversed());
        scoredTopics.sort(Comparator.comparingDouble(ScoredTopic::getScore).reversed());

        int totalCourses = scoredCourses.size();
        int totalCategories = scoredCategories.size();
        int totalTopics = scoredTopics.size();

        int startIndex = (int) pageable.getOffset();

        int endCourseIndex = Math.min((startIndex + pageable.getPageSize()), totalCourses);
        int endCategoryIndex = Math.min((startIndex + pageable.getPageSize()), totalCategories);
        int endTopicIndex = Math.min((startIndex + pageable.getPageSize()), totalTopics);

        List<ScoredCourse> paginatedCourses = scoredCourses.subList(startIndex, endCourseIndex);
        List<ScoredCategory> paginatedCategories = scoredCategories.subList(startIndex, endCategoryIndex);
        List<ScoredTopic> paginatedTopics = scoredTopics.subList(startIndex, endTopicIndex);

        return new SearchResult(paginatedCourses, paginatedCategories, paginatedTopics, pageable);
    }
}
