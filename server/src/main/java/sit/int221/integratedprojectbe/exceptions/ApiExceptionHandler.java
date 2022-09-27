package sit.int221.integratedprojectbe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({ ArgumentNotValidException.class })
    protected  ResponseEntity<Object> handleArgumentNotValidException(ArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, List<String>> fieldErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            if (fieldErrors.containsKey(fieldName)) {
                fieldErrors.get(fieldName).add(message);
            } else {
                List<String> messages = new ArrayList<>();
                messages.add(message);
                fieldErrors.put(fieldName, messages);
            }
        });

        return getErrorValidationResponseBody(HttpStatus.BAD_REQUEST, request, "Validation failed", fieldErrors);
    }
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    protected  ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request)
    {
        return getErrorResponseBody(
                HttpStatus.BAD_REQUEST,
                request,
                String.format("Path variable type mismatched. %s must be %s", ex.getName(),
                        ex.getParameter().getParameterType().getSimpleName())
        );
    }

    @ExceptionHandler({ResponseStatusException.class})
    protected  ResponseEntity<Object> handleResponseStatus(ResponseStatusException ex, HttpServletRequest request) {
        return getErrorResponseBody(ex.getStatus(), request, ex.getReason());
    }

    private static ResponseEntity<Object> getErrorResponseBody(
            HttpStatus status, HttpServletRequest request, String message) {
        return new ResponseEntity<>(
                new ApiError(status, request, message),
                status
        );
    }

    private static ResponseEntity<Object> getErrorValidationResponseBody(
            HttpStatus status, HttpServletRequest request, String message, Map fieldErrors) {
        return new ResponseEntity<>(
                new ApiValidationError(status, request, message, fieldErrors),
                status
        );
    }
}
