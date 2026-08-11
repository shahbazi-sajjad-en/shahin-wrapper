package biz.espc.shahin.dto.inquiry.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Request to check if customer has an account")
public class CustomerHasAccountRequestDto {

    @NotBlank(message = "bank is required")
    @Schema(description = "Bank code", example = "BSI", required = true)
    private String bank;

    @NotBlank(message = "nationalCode is required")
    @Pattern(regexp = "^\\d{10}$", message = "nationalCode must be 10 digits")
    @Schema(description = "National code", example = "11111111110", required = true)
    private String nationalCode;

    @NotBlank(message = "sourceAccount is required")
    @Schema(description = "Source account number", example = "9857463215243", required = true)
    private String sourceAccount;
}
