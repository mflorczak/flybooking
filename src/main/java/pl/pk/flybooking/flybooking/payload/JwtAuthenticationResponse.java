package pl.pk.flybooking.flybooking.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponse {

    private String accessToken;
    private String username;
    private String emailAddress;

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = "Bearer " + accessToken;
    }
}
