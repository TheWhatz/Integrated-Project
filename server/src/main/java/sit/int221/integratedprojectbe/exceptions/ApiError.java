package sit.int221.integratedprojectbe.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ApiError {
    private ZonedDateTime timestamp;
    private Integer status;
    private String path;
    private String error;
    private String message;

    public ApiError (HttpStatus status, HttpServletRequest request, String message) {
        this.timestamp = ZonedDateTime.now();
        this.status = status.value();
        this.path = request.getRequestURI();
        this.error = status.getReasonPhrase();
        this.message = message;
    }

    public String convertToJson() throws JsonProcessingException {
        if (this == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper.writeValueAsString(this);
    }
}
