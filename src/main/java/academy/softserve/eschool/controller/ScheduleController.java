package academy.softserve.eschool.controller;

import academy.softserve.eschool.dto.ScheduleDTO;
import academy.softserve.eschool.repository.LessonRepository;
import academy.softserve.eschool.service.ScheduleServiceImpl;
import academy.softserve.eschool.wrapper.GeneralResponseWrapper;
import academy.softserve.eschool.wrapper.Status;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

//END POINT  /classes/{id}/schedule

@RestController
@RequestMapping("")
@Api(value = "Schedule Endpoint", description = "Crate a schedule for a semester")
public class ScheduleController {

    @Autowired
    ScheduleServiceImpl scheduleService;

    @Autowired
    LessonRepository lessonRepository;

    @ApiOperation(value = "Creates a schedule for a class")
    @ApiResponses(
            value={
                    @ApiResponse(code = 201, message = "Schedule successfully created"),
                    @ApiResponse(code = 400, message = "Bad request"),
                    @ApiResponse(code = 500, message = "Server error")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/classes/{classId}/schedule")
    //todo bk ++
    public GeneralResponseWrapper<ScheduleDTO> postSchedule(
            @ApiParam(value = "id of class", required = true) @PathVariable("classId") final int classId,
            @ApiParam(value = "schedule object", required = true) @RequestBody ScheduleDTO scheduleDTO)//create a shedule for a class with this id
    {
        //todo bk use java8 date api
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String start = dateFormat.format(scheduleDTO.getStartOfSemester());
        String end = dateFormat.format(scheduleDTO.getEndOfSemester());
        lessonRepository.deleteScheduleByBounds(start, end, scheduleDTO.getClassName().getId());
        scheduleService.saveSchedule(scheduleDTO);
        GeneralResponseWrapper<ScheduleDTO> response;
        response = new GeneralResponseWrapper<>(new Status(201, "OK"), null);
        return response;
    }

    @ApiOperation(value = "Gets schedule for the class with id")
    @ApiResponses(
            value={
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 400, message = "Bad request"),
                    @ApiResponse(code = 500, message = "Server error")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/classes/{classId}/schedule")
    public GeneralResponseWrapper<ScheduleDTO> getSchedule(@ApiParam(value = "id of class", required = true) @PathVariable("classId") final int classId){

        GeneralResponseWrapper<ScheduleDTO> response;
        response = new GeneralResponseWrapper<>(new Status(200, "OK"), scheduleService.getScheduleByClassId(classId));
        return response;
    }
}