package sit.int221.integratedprojectbe.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDTO {
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 100, message = "size must be between 1 and 100")
    private String bookingName;

    @NotBlank(message = "must not be blank")
    @Email(regexp = "^[^(.)][a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,3}",
            message = "Email must be well formed")
    @Size(min = 1, max =50 , message = "size must be between 1 and 50")
    private String bookingEmail;

    @NotNull(message = "must not be null")
    private Integer categoryId;

    @NotNull(message = "must not be null")
    @Future(message = "must be a future date")
    private Date eventStartTime;

    @Size(max = 500, message = "size must be between 0 and 500")
    private String eventNotes;

//    private Integer eventDuration;
}
