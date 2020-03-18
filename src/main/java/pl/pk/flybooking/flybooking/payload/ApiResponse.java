package pl.pk.flybooking.flybooking.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiResponse {

    private Boolean success;
    private String message;
}
