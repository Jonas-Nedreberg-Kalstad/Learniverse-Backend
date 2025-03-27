package no.ntnu.idata2306.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseListResponseDto {
    private List<CourseResponseDto> courses;
    private int resultsFound;
}
