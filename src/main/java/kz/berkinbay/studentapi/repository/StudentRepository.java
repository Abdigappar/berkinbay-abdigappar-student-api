package kz.berkinbay.studentapi.repository;

import java.util.List;
import kz.berkinbay.studentapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByLastNameIgnoreCase(String lastName);
}
