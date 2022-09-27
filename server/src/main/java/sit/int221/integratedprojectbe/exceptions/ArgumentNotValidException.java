package sit.int221.integratedprojectbe.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;

@Getter @Setter
public class ArgumentNotValidException extends RuntimeException {
    private BindingResult bindingResult;
    public ArgumentNotValidException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
