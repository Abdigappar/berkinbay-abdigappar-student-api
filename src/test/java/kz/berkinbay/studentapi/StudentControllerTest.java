package kz.berkinbay.studentapi;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.berkinbay.studentapi.model.Student;
import kz.berkinbay.studentapi.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/** API tests with personal test data of Berkinbay Abdigappar (ID 37409). */
@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    private static final String BASE = "/api/berkinbay-abdigappar/students";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    private Student abdigappar() {
        return new Student("Abdigappar", "Berkinbay", "abdigappar.berkinbay@example.com", "DevOps-37409", 3);
    }

    @Test
    void rootPageShowsStudentName() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.student").value("Wrong Name"))
                .andExpect(jsonPath("$.studentId").value("37409"));
    }

    @Test
    void createsStudent() throws Exception {
        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(abdigappar())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName").value("Abdigappar"))
                .andExpect(jsonPath("$.lastName").value("Berkinbay"));
    }

    @Test
    void returnsAllStudents() throws Exception {
        repository.save(abdigappar());
        repository.save(new Student("Test", "Student", "test.student.37409@example.com", "DevOps-37409", 2));

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void searchesStudentsByLastName() throws Exception {
        repository.save(abdigappar());
        repository.save(new Student("Test", "Student", "test.student.37409@example.com", "DevOps-37409", 2));

        mockMvc.perform(get(BASE + "/search").param("lastName", "berkinbay"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("Abdigappar"));
    }

    @Test
    void returnsStudentById() throws Exception {
        Student saved = repository.save(abdigappar());

        mockMvc.perform(get(BASE + "/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("abdigappar.berkinbay@example.com"));
    }

    @Test
    void updatesStudent() throws Exception {
        Student saved = repository.save(abdigappar());
        Student changes = abdigappar();
        changes.setCourse(4);
        changes.setGroupName("DevOps-37409-updated");

        mockMvc.perform(put(BASE + "/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changes)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.course").value(4))
                .andExpect(jsonPath("$.groupName").value("DevOps-37409-updated"));
    }

    @Test
    void deletesStudent() throws Exception {
        Student saved = repository.save(abdigappar());

        mockMvc.perform(delete(BASE + "/" + saved.getId()))
                .andExpect(status().isNoContent());
        mockMvc.perform(get(BASE + "/" + saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void returns404ForNonexistentStudent() throws Exception {
        mockMvc.perform(get(BASE + "/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("not found")));
    }

    @Test
    void rejectsInvalidStudent() throws Exception {
        Student invalid = abdigappar();
        invalid.setEmail("not-an-email");

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}
