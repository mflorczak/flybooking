package pl.pk.flybooking.flybooking.internationalization;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.pk.flybooking.flybooking.exception.GenericValidationException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class InternationalizedErrorHandler {
    private static final String UNEXPECTED_ERROR = "Exception.Unexpected";
    private final MessageSource messageSource;

    public InternationalizedErrorHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex, Locale locale) {
        return new ApiError.Builder().badRequest(request)
                .message(annotationErrorMessage(ex.getBindingResult(), messageSource, locale))
                .build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingServletRequestParameter(HttpServletRequest request, MissingServletRequestParameterException ex, Locale locale) {
        String msg = messageSource.getMessage(ex.getClass().getSimpleName(), convertSingleValuesToArray(ex.getParameterType(), ex.getParameterName()) , locale);
        return new ApiError.Builder().badRequest(request)
                .message(msg)
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleExceptions(HttpServletRequest request, Exception ex, Locale locale) {
        return new ApiError.Builder().internalError(request)
                .message(messageSource.getMessage(UNEXPECTED_ERROR, null, locale))
                .trace(ex.getClass() + "\n" + ex.getStackTrace()[0].toString())
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleAuthenticationException(HttpServletRequest request, AuthenticationException ex, Locale locale) {
        return new ApiError.Builder().authError(request)
                .message(messageSource.getMessage(ex.getClass().getSimpleName(), null, locale))
                .trace(ex.getClass() + "\n" + ex.getStackTrace()[0].toString())
                .build();

    }

    @ExceptionHandler(GenericValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleGenericValidationException(HttpServletRequest request, GenericValidationException ex, Locale locale) {
        return new ApiError.Builder().badRequest(request)
                .message(getMessageForGenericError(ex, locale))
                .trace(Arrays.toString(ex.getStackTrace()))
                .build();
    }

    private String annotationErrorMessage(BindingResult result, MessageSource messageSource, Locale locale) {
        return result.getAllErrors()
                .stream()
                .map(objectError -> messageSource.getMessage(Objects.requireNonNull(objectError.getCode()),
                        convertSingleValuesToArray(Objects.requireNonNull(result.getFieldError()).getField()), locale))
                .collect(Collectors.joining(", "));
    }

    private String[] convertSingleValuesToArray(String... values) {
        return values;
    }

    private String getMessageForGenericError(GenericValidationException ex, Locale locale) {
        try {
            return messageSource.getMessage(String.join(".", ex.getClass().getSimpleName(), ex.getMessage()), ex.getArgs(), locale);
        } catch (NoSuchMessageException e) {
            return ex.getMessage();
        }
    }
}
