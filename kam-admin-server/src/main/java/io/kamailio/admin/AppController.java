package io.kamailio.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AppController {

    @GetMapping("/")
    public Map<String, String> hello() {
        return Map.of("message", "Kamailio Dashboard API", "status", "ok");
    }
}
