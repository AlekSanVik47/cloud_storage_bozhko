package main.java.ru.cloudstorage.cloudstoragebozhko.testcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/example")
    public ResponseEntity<String> example() {
        return ResponseEntity.ok("Hello World");
    }
}
