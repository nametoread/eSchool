package service;

import static org.junit.Assert.assertEquals;
import academy.softserve.eschool.dto.SubjectDTO;
import academy.softserve.eschool.model.Clazz;
import academy.softserve.eschool.model.Lesson;
import academy.softserve.eschool.model.Subject;
import academy.softserve.eschool.repository.ClassRepository;
import academy.softserve.eschool.repository.LessonRepository;
import academy.softserve.eschool.repository.SubjectRepository;
import academy.softserve.eschool.service.ScheduleServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a test class for {@link ScheduleServiceImpl}
 * {@link Mockito} mock framework is used in conjunction with {@link org.junit.runners.JUnit4}
 *
 *  @author Mariana Vorotniak
 */
@RunWith(MockitoJUnitRunner.class)
public class ScheduleServiceImplTest {

    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private ClassRepository classRepository;
    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    private List<SubjectDTO> list = new ArrayList<>();
    private LocalDate start, end;
    private DayOfWeek dayOfWeek;
    private Clazz clazz;
    private List<Subject> subjectList = new ArrayList<>();

    @Before
    public void init()
    {
        subjectList.add(new Subject(1, "Історія України", ""));
        subjectList.add(new Subject(2, "Інформатика", ""));
        subjectList.add(new Subject(8, "Біологія", ""));
        subjectList.add(new Subject(9, "Математика", ""));

        list.add(new SubjectDTO(1, "Історія України", "Гуманітарний навчальний предмет. Починає вивчатись із 5-го класу"));
        list.add(new SubjectDTO(1, "Історія України", "Гуманітарний навчальний предмет. Починає вивчатись із 5-го класу"));
        list.add(new SubjectDTO(9, "Математика", ""));
        list.add(new SubjectDTO(8, "Біологія", "Природничий навчальний предмет. Починає вивчатись із 6-го класу"));
        list.add(new SubjectDTO(8, "Біологія", "Природничий навчальний предмет. Починає вивчатись із 6-го класу"));

        start = LocalDate.of(2018, Month.NOVEMBER, 19);
        end = LocalDate.of(2018, Month.NOVEMBER, 24);

        dayOfWeek = DayOfWeek.MONDAY;

        clazz = new Clazz(2, "7-А", "", 2018, true);
    }

    /**
     * Save schedule test function - checks if all the information was stored.
     * @result Schedule will be created without any errors
     */
    @Test
    public void testSaveFunction()
    {
        Mockito.when(subjectRepository.findAll()).thenReturn(subjectList);
        List<Lesson> lessons = scheduleService.saveFunction(list, start, end, dayOfWeek, clazz);
        assertEquals("Adding schedule for monday", 5, lessons.size());
    }

    @After
    public void destroy()
    {
        list.clear();
        start = null;
        end = null;
        dayOfWeek = null;
        clazz = null;
    }

}
