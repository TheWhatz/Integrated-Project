package sit.int221.integratedprojectbe.dtos;

import lombok.AllArgsConstructor;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;


        import javax.validation.constraints.FutureOrPresent;
        import javax.validation.constraints.NotBlank;
        import javax.validation.constraints.NotEmpty;
        import javax.validation.constraints.Size;
        import java.util.Date;

import javax.validation.constraints.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditEventDTO {
    @Future(message = "must be a future date")
    private Date eventStartTime;

    @Size(max = 500, message = "size must be between 0 and 500")
    private String eventNotes;
}
