package biz.espc.shahin.dto.inquiry.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

// CustomerHasIbanRequestDto.java
@Data
@Schema(description = "Request to check if customer has an IBAN")
public class CustomerHasIbanRequestDto {

    @NotBlank(message = "accountOwnerType is required")
    @Schema(description = "Account owner type", example = "Iranian_Real", required = true,
            allowableValues = {"Iranian_Real", "Iranian_Legal", "Foreign_Real", "Foreign_Legal"})
    private String accountOwnerType;

    @NotBlank(message = "nationalCode is required")
    @Pattern(regexp = "^\\d{10}$", message = "nationalCode must be 10 digits")
    @Schema(description = "National code", example = "02352365520", required = true)
    private String nationalCode;

    @NotBlank(message = "iban is required")
    @Schema(description = "IBAN number", example = "IR370190000009633255556334", required = true)
    private String iban;

    @Schema(description = "Bank code", example = "BSI")
    private String bank;
}
