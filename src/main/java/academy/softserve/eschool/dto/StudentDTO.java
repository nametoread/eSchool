package academy.softserve.eschool.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    @ApiModelProperty(notes = "Id")
    private int Id;

    @ApiModelProperty(notes = "contains student first name")
    private String firstname;

    @ApiModelProperty(notes = "contains student last name")
    private String lastname;

    @ApiModelProperty(notes = "contains a patronymic")
    private String patronymic;

    @ApiModelProperty(notes = "contains student class")
    private String classe;

    @ApiModelProperty(notes = "contains date of birth of the student")
    private Date dateOfBirth;

    @ApiModelProperty(notes = "student login")
    private String login;

    @ApiModelProperty(notes = "student email")
    private String email;

    @ApiModelProperty(notes = "student phone")
    private String phone;
}
