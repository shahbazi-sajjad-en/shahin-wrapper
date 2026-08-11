package biz.espc.shahin.dto.inquiry.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Request for phone validity check")
public class PhoneValidityRequestDto {

    @NotBlank(message = "nationalCode is required")
    @Pattern(regexp = "^\\d{10}$", message = "nationalCode must be 10 digits")
    @Schema(description = "National code", example = "0235266321", required = true)
    private String nationalCode;

    @NotBlank(message = "mobileNumber is required")
    @Pattern(regexp = "^\\d{10,15}$", message = "mobileNumber must be 10-15 digits")
    @Schema(description = "Mobile number", example = "1234567890", required = true)
    private String mobileNumber;

    @Schema(description = "Bank code", example = "BSI")
    private String bank;
}
