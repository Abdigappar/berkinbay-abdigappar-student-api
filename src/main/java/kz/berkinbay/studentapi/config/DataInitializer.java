package kz.berkinbay.studentapi.config;

import kz.berkinbay.studentapi.model.Student;
import kz.berkinbay.studentapi.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/** Prints the student's name at startup and adds personal sample data to an empty database. */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Value("${app.student.full-name}")
    private String studentName;

    @Value("${app.student.id}")
    private String studentId;

    @Bean
    CommandLineRunner seedStudents(StudentRepository repository,
                                   @Value("${app.seed.enabled:true}") boolean seedEnabled) {
        return args -> {
            if (!seedEnabled || repository.count() > 0) {
                return;
            }
            repository.save(new Student("Abdigappar", "Berkinbay",
                    "abdigappar.berkinbay.37409@example.com", "DevOps-37409", 3));
            log.info("Sample record for {} (ID {}) added", studentName, studentId);
        };
    }

    @EventListener(ApplicationReadyEvent.class)
    public void announce() {
        log.info("Student API by {} (ID {}) is ready", studentName, studentId);
    }
}
