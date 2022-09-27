package sit.int221.integratedprojectbe.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditEventCategoryDTO {
    @NotBlank(message = "must be not blank")
    @NotNull(message = "must be not null")
    @Size(min = 1, max = 100, message = "size must be between 1 and 100")
    private String categoryName;

    @NotNull(message = "must be not null")
    @Min(value = 1 , message = "size must be between 1 and 480")
    @Max(value = 480 , message = "size must be between 1 and 480")
    private Integer eventDuration;

    @Size(max = 500, message = "size must be between 0 and 500")
    private String eventCategoryDesc;
}
