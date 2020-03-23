package pl.pk.flybooking.flybooking.internationalization;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ApiError {

    private ZonedDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String trace;
    private String path;

    private static ApiError createBaseApiError(HttpServletRequest req, HttpStatus status) {
        ApiError apiError = new ApiError();
        apiError.setTimestamp(ZonedDateTime.now());
        apiError.setStatus(status.value());
        apiError.setError(status.getReasonPhrase());
        apiError.setPath(req.getRequestURI());
        return apiError;
    }

    public static class Builder {

        private ApiError apiError;

        public Builder badRequest(HttpServletRequest req) {
            this.apiError = createBaseApiError(req, HttpStatus.BAD_REQUEST);
            return this;
        }

        public Builder internalError(HttpServletRequest req) {
            this.apiError = createBaseApiError(req, HttpStatus.INTERNAL_SERVER_ERROR);
            return this;
        }

        public Builder message(String string) {
            apiError.setMessage(string);
            return this;
        }

        public Builder trace(String trace) {
            apiError.setTrace(trace);
            return this;
        }

        public ApiError build() {
            return apiError;
        }
    }
}
