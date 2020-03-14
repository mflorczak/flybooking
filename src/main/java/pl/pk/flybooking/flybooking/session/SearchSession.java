package pl.pk.flybooking.flybooking.session;

import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/session")
@AllArgsConstructor
public class SearchSession {

    private SearchSessionService searchSessionService;

    @PostMapping()
    public ResponseEntity<Boolean> createSession() throws UnirestException {
       return ResponseEntity.ok(searchSessionService.getSessionKey());
    }

}
