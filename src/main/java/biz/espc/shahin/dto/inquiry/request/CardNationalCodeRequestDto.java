package biz.espc.shahin.dto.inquiry.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Request for card and national code verification")
public class CardNationalCodeRequestDto {

    @NotBlank(message = "nationalCode is required")
    @Pattern(regexp = "^\\d{10}$", message = "nationalCode must be 10 digits")
    @Schema(description = "National code", required = true)
    private String nationalCode;

    @NotBlank(message = "card is required")
    @Pattern(regexp = "^\\d{16}$", message = "card must be 16 digits")
    @Schema(description = "Card number (16 digits)", required = true)
    private String card;

    @Schema(description = "Bank code", example = "BSI")
    private String bank;
}
