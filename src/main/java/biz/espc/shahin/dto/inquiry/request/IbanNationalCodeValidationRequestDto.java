package biz.espc.shahin.dto.inquiry.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Request for IBAN national code validation")
public class IbanNationalCodeValidationRequestDto {

    @NotBlank(message = "nationalCode is required")
    @Pattern(regexp = "^\\d{10}$", message = "nationalCode must be 10 digits")
    @Schema(description = "National code", example = "0235266321", required = true)
    private String nationalCode;

    @NotBlank(message = "iban is required")
    @Schema(description = "IBAN number", example = "IR830120000002222222222222", required = true)
    private String iban;

    @Schema(description = "Birth date in YYYYMMDD format", example = "13770101")
    @Pattern(regexp = "^\\d{8}$", message = "birthDate must be 8 digits in YYYYMMDD format")
    private String birthDate;

    @Schema(description = "Bank code", example = "BSI")
    private String bank;
}