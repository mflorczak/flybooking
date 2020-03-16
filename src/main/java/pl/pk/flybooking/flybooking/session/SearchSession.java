package pl.pk.flybooking.flybooking.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping(path = "/session")
@AllArgsConstructor
public class SearchSession {

    private SearchSessionService searchSessionService;

    @PostMapping()
    public ResponseEntity<String> createSession() throws UnirestException, JsonProcessingException {
        return ResponseEntity.ok(searchSessionService.getSessionKey());
    }
    @GetMapping
    public void getData() throws IOException, UnirestException {
        searchSessionService.getJsonDataFromSession("734bf7ac-56b8-40cb-b787-b74f35e84fe1");
    }
    @GetMapping("/a")
    public void get() throws IOException, ParseException {
        searchSessionService.dataFromFile();
    }
}
