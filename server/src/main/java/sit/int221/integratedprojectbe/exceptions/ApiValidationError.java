package sit.int221.integratedprojectbe.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.Map;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ApiValidationError extends ApiError {
    private Map fieldErrors;

    public ApiValidationError(HttpStatus status, HttpServletRequest request, String message) {
        super(status, request, message);
    }

    public ApiValidationError(HttpStatus status, HttpServletRequest request, String message, Map fieldError) {
        super(status, request, message);
        this.fieldErrors = fieldError;
    }
}
