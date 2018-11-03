package academy.softserve.eschool.service;

import academy.softserve.eschool.dto.EditUserDTO;
import academy.softserve.eschool.dto.NYTransitionDTO;
import academy.softserve.eschool.dto.StudentDTO;
import academy.softserve.eschool.model.Clazz;
import academy.softserve.eschool.model.Role;
import academy.softserve.eschool.model.Student;
import academy.softserve.eschool.model.User;
import academy.softserve.eschool.repository.ClassRepository;
import academy.softserve.eschool.repository.StudentRepository;
import academy.softserve.eschool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.loading.ClassLoaderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static academy.softserve.eschool.auxiliary.LoginGeneratorController.transliteration;
import static academy.softserve.eschool.auxiliary.PasswordGenerator.generatePassword;

@Service
public class StudentService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    StudentRepository studentRepository;

    public StudentDTO getOne(Student s){
        return StudentDTO.builder().firstname(s.getFirstName())
                .lastname(s.getLastName())
                .patronymic(s.getPatronymic())
                .login(s.getLogin())
                .dateOfBirth(s.getDateOfBirth())
                .classe(s.getClasses().stream().filter(Clazz::isActive).findFirst().orElseGet(Clazz::new).getName())
                .email(s.getEmail())
                .avatar(s.getAvatar())
                .phone(s.getPhone()).build();
    }

    public List<StudentDTO> getAll(List<Student> students){
        return students.stream().map(i->StudentDTO.builder().Id(i.getId())
                .firstname(i.getFirstName())
                .lastname(i.getLastName())
                .patronymic(i.getPatronymic())
                .dateOfBirth(i.getDateOfBirth())
                .classe(i.getClasses().stream().filter(Clazz::isActive).findAny().orElseGet(Clazz::new).getName())
                .email(i.getEmail())
                .phone(i.getPhone()).build()
        ).collect(Collectors.toCollection(ArrayList::new));
    }

    public void updateStudent(User oldUser, EditUserDTO edited){

        oldUser.setFirstName(edited.getFirstname());
        oldUser.setLastName(edited.getLastname());
        oldUser.setPatronymic(edited.getPatronymic());
        oldUser.setDateOfBirth(edited.getDateOfBirth());
        oldUser.setAvatar(edited.getAvatar());
        oldUser.setEmail(edited.getEmail());
        oldUser.setPhone(edited.getPhone());
        if((oldUser.getPassword().equals(edited.getOldPass()) || edited.getOldPass().equals("adminchangedpass"))
                && edited.getNewPass().length()>0){
            oldUser.setPassword(edited.getNewPass());
        }
        oldUser.setLogin(edited.getLogin());
        userRepository.save(oldUser);
    }

    public Student addOne(StudentDTO studentDTO) {
        Student student = Student.builder()
                .lastName(studentDTO.getLastname())
                .firstName(studentDTO.getFirstname())
                .patronymic(studentDTO.getPatronymic())
                .login(transliteration(studentDTO.getLastname()))
                .password(generatePassword(7))
                .phone(studentDTO.getPhone())
                .email(studentDTO.getEmail())
                .dateOfBirth(studentDTO.getDateOfBirth())
                .role(Role.ROLE_USER)
                .build();
        Clazz clazz = classRepository.getOne(Integer.valueOf(studentDTO.getClassId()));
        student.getClasses().add(clazz);
        return studentRepository.save(student);
    }

    public void studentClassesRebinding(List<NYTransitionDTO> nyTransitionDTOS){
         for (NYTransitionDTO nDTO : nyTransitionDTOS){
             List<Student> studentList = studentRepository.findByClazzId(nDTO.getOldClassId());
             for (Student student : studentList) {
                 List<Clazz> clazzes = student.getClasses();
                 clazzes.add(classRepository.findById(nDTO.getNewClassId()).orElse(null));
                 student.setClasses(clazzes);
                 studentRepository.save(student);
             }
         }
    }
}
