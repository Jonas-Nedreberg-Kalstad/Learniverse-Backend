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
import no.ntnu.idata2306.util.datastructure.BKTree;
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
     * BK-trees are used to optimize the search process by efficiently finding close matches.
     *
     * @param criteria  the search criteria containing the course name, category name, and topic name to search for.
     * @param pageable  the pagination information.
     * @return a SearchResult object containing the scored courses, categories, and topics, along with pagination information.
     */
    public SearchResult search(SearchCriteria criteria, Pageable pageable) {
        BKTree courseTree = new BKTree();
        BKTree categoryTree = new BKTree();
        BKTree topicTree = new BKTree();

        List<Course> courses = courseRepository.findByActiveTrue();
        List<Category> categories = categoryRepository.findAll();
        List<Topic> topics = topicRepository.findAll();

        for (Course course : courses) {
            courseTree.add(course.getCourseName());
        }

        for (Category category : categories) {
            categoryTree.add(category.getCategory());
        }

        for (Topic topic : topics) {
            topicTree.add(topic.getTopic());
        }

        List<String> courseResults = courseTree.search(criteria.getCourseName(), 20);
        List<String> categoryResults = categoryTree.search(criteria.getCategoryName(), 20);
        List<String> topicResults = topicTree.search(criteria.getTopicName(), 20);

        List<ScoredCourse> scoredCourses = new ArrayList<>();
        List<ScoredCategory> scoredCategories = new ArrayList<>();
        List<ScoredTopic> scoredTopics = new ArrayList<>();

        for (String courseName : courseResults) {
            Course course = courses.stream().filter(c -> c.getCourseName().equals(courseName)).findFirst().orElse(null);
            if (course != null) {
                double courseNameScore = ScoreUtils.calculateAverageNormalizedScore(
                        Arrays.asList(course.getCourseName().split(" ")),
                        Arrays.asList(criteria.getCourseName().split(" "))
                );
                if (courseNameScore > ScoreThresholdUtils.COURSE_SCORE_THRESHOLD) {
                    scoredCourses.add(new ScoredCourse(course, courseNameScore));
                }
            }
        }

        for (String categoryName : categoryResults) {
            Category category = categories.stream().filter(c -> c.getCategory().equals(categoryName)).findFirst().orElse(null);
            if (category != null) {
                double categoryNameScore = ScoreUtils.calculateAverageNormalizedScore(
                        Arrays.asList(category.getCategory().split(" ")),
                        Arrays.asList(criteria.getCategoryName().split(" "))
                );
                if (categoryNameScore > ScoreThresholdUtils.CATEGORY_SCORE_THRESHOLD) {
                    scoredCategories.add(new ScoredCategory(category, categoryNameScore));
                }
            }
        }

        for (String topicName : topicResults) {
            Topic topic = topics.stream().filter(t -> t.getTopic().equals(topicName)).findFirst().orElse(null);
            if (topic != null) {
                double topicNameScore = ScoreUtils.calculateAverageNormalizedScore(
                        Arrays.asList(topic.getTopic().split(" ")),
                        Arrays.asList(criteria.getTopicName().split(" "))
                );
                if (topicNameScore > ScoreThresholdUtils.TOPIC_SCORE_THRESHOLD) {
                    scoredTopics.add(new ScoredTopic(new Topic(topic.getId(), topic.getTopic(), topic.getCourses()), topicNameScore));
                }
            }
        }

        scoredCourses.sort(Comparator.comparingDouble(ScoredCourse::getScore).reversed());
        scoredCategories.sort(Comparator.comparingDouble(ScoredCategory::getScore).reversed());
        scoredTopics.sort(Comparator.comparingDouble(ScoredTopic::getScore).reversed());

        int totalCourses = scoredCourses.size();
        int totalCategories = scoredCategories.size();
        int totalTopics = scoredTopics.size();

        int startCourseIndex = (int) pageable.getOffset();
        int endCourseIndex = Math.min((startCourseIndex + pageable.getPageSize()), totalCourses);

        int startCategoryIndex = (int) pageable.getOffset();
        int endCategoryIndex = Math.min((startCategoryIndex + pageable.getPageSize()), totalCategories);

        int startTopicIndex = (int) pageable.getOffset();
        int endTopicIndex = Math.min((startTopicIndex + pageable.getPageSize()), totalTopics);

        List<ScoredCourse> paginatedCourses = scoredCourses.subList(startCourseIndex, endCourseIndex);
        List<ScoredCategory> paginatedCategories = scoredCategories.subList(startCategoryIndex, endCategoryIndex);
        List<ScoredTopic> paginatedTopics = scoredTopics.subList(startTopicIndex, endTopicIndex);

        return new SearchResult(paginatedCourses, paginatedCategories, paginatedTopics, pageable);
    }
}
