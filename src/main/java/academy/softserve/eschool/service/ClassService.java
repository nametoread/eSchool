package academy.softserve.eschool.service;

import academy.softserve.eschool.dto.ClassDTO;
import academy.softserve.eschool.dto.NYTransitionDTO;

import java.util.List;

public interface ClassService {
    List<ClassDTO> findClassesByStatus(boolean status);
    ClassDTO findClassById(int id);
    void saveClass(ClassDTO classDTO);
    void updateClass(int id, ClassDTO classDTO);
    void addNewYearClasses();
    List<ClassDTO> getActiveClassesWithStudents();
    String updateClassName(String className);
    List<ClassDTO> getActiveClassesWithoutStudents();
    void updateClassStatusById(List<NYTransitionDTO> transitionDTOS, boolean status);
}
