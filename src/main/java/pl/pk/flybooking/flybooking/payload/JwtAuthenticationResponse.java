package pl.pk.flybooking.flybooking.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
public class JwtAuthenticationResponse {

    @Setter
    private String accessToken;
    private String tokenType = "Bearer ";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
