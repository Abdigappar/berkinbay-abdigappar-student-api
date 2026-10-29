package kz.berkinbay.studentapi.service;

import java.util.List;
import kz.berkinbay.studentapi.exception.StudentNotFoundException;
import kz.berkinbay.studentapi.model.Student;
import kz.berkinbay.studentapi.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Student findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Transactional
    public Student create(Student student) {
        student.setId(null); // the database assigns the id
        return repository.saveAndFlush(student);
    }

    @Transactional
    public Student update(Long id, Student changes) {
        Student existing = findById(id);
        existing.setFirstName(changes.getFirstName());
        existing.setLastName(changes.getLastName());
        existing.setEmail(changes.getEmail());
        existing.setGroupName(changes.getGroupName());
        existing.setCourse(changes.getCourse());
        return repository.saveAndFlush(existing);
    }

    @Transactional
    public void delete(Long id) {
        Student existing = findById(id);
        repository.delete(existing);
    }
}
