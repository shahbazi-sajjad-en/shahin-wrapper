package biz.espc.shahin.dto.inquiry.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Request for national identity information")
public class NationalIdentityRequestDto {

    @NotBlank(message = "nationalCode is required")
    @Pattern(regexp = "^\\d{10}$", message = "nationalCode must be 10 digits")
    @Schema(description = "National code", example = "0235266321", required = true)
    private String nationalCode;

    @NotBlank(message = "birthDate is required")
    @Pattern(regexp = "^\\d{8}$", message = "birthDate must be 8 digits in YYYYMMDD format")
    @Schema(description = "Birth date in YYYYMMDD format", example = "13390801", required = true)
    private String birthDate;

    @Schema(description = "Bank code", example = "BSI")
    private String bank;
}
