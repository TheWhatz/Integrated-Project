package sit.int221.integratedprojectbe.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DateTimeOverlapException extends RuntimeException {
    public  DateTimeOverlapException(String message) {
        super(message);
    }
}
