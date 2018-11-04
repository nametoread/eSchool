package academy.softserve.eschool.controller;
import academy.softserve.eschool.dto.HomeworkDTO;
import academy.softserve.eschool.dto.JournalMarkDTO;
import academy.softserve.eschool.dto.MarkDTO;
import academy.softserve.eschool.service.JournalServiceImpl;
import academy.softserve.eschool.wrapper.GeneralResponseWrapper;
import academy.softserve.eschool.wrapper.Status;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/homeworks")
@Api(value = "Homework's Endpoint", description = "Get homeworks")
public class HomeworkController {
    @Autowired
    JournalServiceImpl journalServiceImpl;

    @GetMapping("/subjects/{idSubject}/classes/{idClass}")
    @ApiOperation(value = "Get homeworks by subject and class")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 400, message = "Bad data"),
                    @ApiResponse(code = 500, message = "Server error")
            }
    )
    public GeneralResponseWrapper<List<HomeworkDTO>> getHomeworks(
            @ApiParam(value = "id of subject", required = true) @PathVariable int idSubject,
            @ApiParam(value = "id of class", required = true) @PathVariable int idClass
    ){
        GeneralResponseWrapper<List<HomeworkDTO>> response;
        response = new GeneralResponseWrapper<>(new Status(200, "OK"), journalServiceImpl.getHomework(idSubject,idClass));
        return response;
    }

    @ApiOperation(value = "Save homework")
    @PostMapping
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "OK"),
                    @ApiResponse(code = 400, message = "Bad data"),
                    @ApiResponse(code = 500, message = "Server error")
            }
    )
    public GeneralResponseWrapper<HomeworkDTO>  postMark(
            @ApiParam(value = "id of lesson,description of homework and file", required = true)@RequestBody HomeworkDTO homeworkDTO){
        GeneralResponseWrapper<HomeworkDTO> response;
        response = new GeneralResponseWrapper<>(new Status(201, "OK"), homeworkDTO);
        return response;
    }
}
