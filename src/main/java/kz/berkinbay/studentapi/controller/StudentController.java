package kz.berkinbay.studentapi.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import kz.berkinbay.studentapi.model.Student;
import kz.berkinbay.studentapi.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * CRUD endpoints. Personalised base URL:
 * http://localhost:8081/api/berkinbay-abdigappar/students
 */
@RestController
@RequestMapping("/api/berkinbay-abdigappar/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Student> getAll() {
        return service.findAll();
    }

    /** GET /api/berkinbay-abdigappar/students/search?lastName=Berkinbay */
    @GetMapping("/search")
    public List<Student> searchByLastName(@RequestParam String lastName) {
        return service.findByLastName(lastName);
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Student> create(@Valid @RequestBody Student student) {
        Student created = service.create(student);
        URI location = URI.create("/api/berkinbay-abdigappar/students/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @Valid @RequestBody Student student) {
        return service.update(id, student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
