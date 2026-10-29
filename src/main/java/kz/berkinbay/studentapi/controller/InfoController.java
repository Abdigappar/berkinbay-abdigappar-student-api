package kz.berkinbay.studentapi.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Root page: shows whose application this is and which version is running. */
@RestController
public class InfoController {

    private final String studentName;
    private final String studentId;
    private final String version;

    public InfoController(@Value("${app.student.full-name}") String studentName,
                          @Value("${app.student.id}") String studentId,
                          ObjectProvider<BuildProperties> buildProperties) {
        this.studentName = studentName;
        this.studentId = studentId;
        BuildProperties build = buildProperties.getIfAvailable();
        this.version = build != null ? build.getVersion() : "unknown";
    }

    @GetMapping("/")
    public Map<String, Object> info() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("application", "berkinbay-abdigappar-student-api");
        body.put("student", studentName);
        body.put("studentId", studentId);
        body.put("version", version);
        body.put("studentsEndpoint", "/api/berkinbay-abdigappar/students");
        body.put("health", "/actuator/health");
        return body;
    }
}
