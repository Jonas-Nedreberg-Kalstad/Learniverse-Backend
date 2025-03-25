package no.ntnu.idata2306.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.course.CourseResponseDto;
import no.ntnu.idata2306.dto.search.CategoryAndTopicsSearch;
import no.ntnu.idata2306.dto.search.SearchCriteria;
import no.ntnu.idata2306.dto.search.SearchResult;
import no.ntnu.idata2306.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@CrossOrigin()
@RequestMapping("/api/anonymous/search")
@Tag(name = "Search API", description = "Endpoints for searching")
public class SearchController {
    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * Searches for courses, categories, and topics based on the provided search criteria.
     * The search results are paginated.
     *
     * @param criteria the search criteria containing the course name, category name, and topic name to search for
     * @param page     the page number to retrieve (default is 0)
     * @param size     the number of records per page (default is 5)
     * @return ResponseEntity with the SearchResult object containing the scored courses, categories, and topics, along with pagination information
     */
    @Operation(summary = "Search for courses, categories, and topics", description = "Searches for courses, categories, and topics based on the provided search criteria. The results are paginated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SearchResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid search criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<SearchResult> search(@RequestBody SearchCriteria criteria,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        SearchResult result = this.searchService.search(criteria, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * Searches for courses based on the provided category ID and a list of topic IDs.
     * The search results are paginated.
     * If no courses are found, an empty list is returned.
     *
     * @param search the request object containing category ID and topic IDs.
     * @param page   the page number to retrieve (default is 0).
     * @param size   the number of records per page (default is 5).
     * @return ResponseEntity with a list of CourseResponseDto objects representing the courses that match the search criteria, or an empty list if no courses are found.
     */
    @Operation(summary = "Search for courses by topics and category", description = "Searches for courses based on the provided category ID and a list of topic IDs. The results are paginated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid search criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("findCourseByTopicsAndCategory")
    public ResponseEntity<List<CourseResponseDto>> searchTopicsAndCourses(@RequestBody CategoryAndTopicsSearch search,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<CourseResponseDto> result = this.searchService.topicAndCategoryOnlySearch(search, pageable);
        return ResponseEntity.ok(result);
    }
}
