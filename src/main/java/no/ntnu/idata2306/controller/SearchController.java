package no.ntnu.idata2306.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.idata2306.dto.course.CourseListResponseDto;
import no.ntnu.idata2306.dto.course.CourseResponseDto;
import no.ntnu.idata2306.dto.search.request.CategoryAndTopicsSearch;
import no.ntnu.idata2306.dto.search.request.UserSearchCriteria;
import no.ntnu.idata2306.dto.search.response.ScoredUser;
import no.ntnu.idata2306.dto.search.request.SearchCriteria;
import no.ntnu.idata2306.dto.search.response.SearchResult;
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
@RequestMapping("/api")
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
    @PostMapping("/anonymous/search")
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
    @PostMapping("/anonymous/search/findCourseByFilteringIdsAndMaxPrice")
    public ResponseEntity<CourseListResponseDto> findCourseByFilteringIdsAndMaxPrice(@RequestBody CategoryAndTopicsSearch search,
                                                                                  @RequestParam(defaultValue = "0") int page,
                                                                                  @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        CourseListResponseDto result = this.searchService.advancedIdsAndMaxPriceFiltering(search, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * Searches for users based on the provided full name and paginates the results.
     * The results are filtered based on predefined score thresholds to ensure relevance.
     *
     * @param search the search criteria containing the full name of the user to search for.
     * @param page   the page number to retrieve (default is 0).
     * @param size   the number of records per page (default is 5).
     * @return ResponseEntity with the list of scored users based on the search criteria.
     */
    @Operation(summary = "Search for users by full name", description = "Searches for users based on the provided full name. The results are paginated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScoredUser.class))),
            @ApiResponse(responseCode = "400", description = "Invalid search criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/admin/search/userFullName")
    public ResponseEntity<List<ScoredUser>> searchUsers(@RequestBody UserSearchCriteria search,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ScoredUser> result = this.searchService.userSearch(search.getFullName().toLowerCase(), pageable);
        return ResponseEntity.ok(result);
    }
}
