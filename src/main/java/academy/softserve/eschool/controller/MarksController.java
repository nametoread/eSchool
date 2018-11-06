package academy.softserve.eschool.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import academy.softserve.eschool.dto.MarkDTO;
import academy.softserve.eschool.dto.MarkDataPointDTO;
import academy.softserve.eschool.dto.MarkTypeDTO;
import academy.softserve.eschool.service.base.MarkServiceBase;
import academy.softserve.eschool.wrapper.GeneralResponseWrapper;
import academy.softserve.eschool.wrapper.Status;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/marks")
@Api(value = "Operations about marks", description="Operations about marks")
@RequiredArgsConstructor
public class MarksController {
	
	@NonNull
	private MarkServiceBase markService;
	
	/**
	 * Returns list of {@link MarkDataPointDTO} wrapped in {@link GeneralResponseWrapper}
	 * @param studentId if specified marks are filtered by user id
	 * @param subjectId if specified marks are filtered by subject id
	 * @param classId if specified marks are filtered by class id
	 * @param periodStart if specified only marks received after this date are returned
	 * @param periodEnd if specified only marks received before this date are returned
	 * @return list of {@link MarkDataPointDTO} wrapped in {@link GeneralResponseWrapper}
	 */
	@PreAuthorize("hasRole('TEACHER')")
	@GetMapping("")
	@ApiOperation(value = "Get marks by date filtered by specified params")
	GeneralResponseWrapper<List<MarkDataPointDTO>> getMarks (
			//todo bk Don't you see that IDEA marks @ApiParam 'required = false' by grey color??
			//todo bk Look into javaDocs and remove the option: 'Path parameters will always be set as required, whether you set this property or not'
			@ApiParam(value = "filter results by student id") @RequestParam(value = "student_id", required = false) Integer studentId,
			@ApiParam(value = "filter results by subject id") @RequestParam(value = "subject_id", required = false) Integer subjectId,
			@ApiParam(value = "filter results by class id") @RequestParam(value = "class_id", required = false) Integer classId,
			@ApiParam(value = "get marks received after specified date, accepts date in format 'yyyy-MM-dd'") @RequestParam(value = "period_start", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate periodStart,
			@ApiParam(value = "get marks received before specified date, accepts date in format 'yyyy-MM-dd'") @RequestParam(value = "period_end", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate periodEnd){
		
		return new GeneralResponseWrapper<>(
				new Status(HttpStatus.OK.value(), "OK"),
				markService.getFilteredByParams(subjectId, classId, studentId, periodStart, periodEnd));
	}

	@ApiOperation(value = "Save mark of students by lesson")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Mark successfully created"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Server error")
	})
    @PreAuthorize("hasRole('TEACHER')")
	@PostMapping
	public GeneralResponseWrapper<MarkDTO> postMark(
		@ApiParam(value = "mark,note,lesson's id and student's id", required = true) @RequestBody MarkDTO markDTO){
		markService.saveMark(markDTO);
		GeneralResponseWrapper<MarkDTO> response;
		response = new GeneralResponseWrapper<>(new Status(201, "OK"), markDTO);
		return response;
	}

	@ApiOperation("Update mark's type of lesson")
	@PreAuthorize("hasRole('TEACHER')")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Server error")
	})
	@PutMapping("/lessons/{idLesson}/marktype")
	public GeneralResponseWrapper editType(
			@ApiParam(value = "id of lesson", required = true) @PathVariable int idLesson,
			@ApiParam(value = "type of mark", required = true) @RequestBody MarkTypeDTO markType){
		markService.updateType(idLesson, markType.getMarkType());
		GeneralResponseWrapper<MarkTypeDTO> response;
		response = new GeneralResponseWrapper<>(new Status(201, "OK"), null);
		return response;
	}
}
