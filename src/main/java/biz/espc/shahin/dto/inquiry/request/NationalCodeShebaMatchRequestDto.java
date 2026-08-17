package biz.espc.shahin.dto.inquiry.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Request for national code and Sheba matching")
public class NationalCodeShebaMatchRequestDto {

    @NotBlank(message = "nationalCode is required")
    @Pattern(regexp = "^\\d{10}$", message = "nationalCode must be 10 digits")
    @Schema(description = "National code", example = "0235426335", required = true)
    private String nationalCode;

    @NotBlank(message = "iban is required")
    @Schema(description = "IBAN number", example = "IR830120000002222222222222", required = true)
    private String iban;

    @Schema(description = "Bank code", example = "BSI")
    private String bank;
}
