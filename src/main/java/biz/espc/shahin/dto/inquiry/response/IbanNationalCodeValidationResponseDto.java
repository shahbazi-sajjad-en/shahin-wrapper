package biz.espc.shahin.dto.inquiry.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response for IBAN national code validation")
public class IbanNationalCodeValidationResponseDto {

    @Data
    @Schema(description = "Combined response wrapper")
    public static class Combined {
        @Schema(description = "Response data")
        private Response response;
    }

    @Data
    @Schema(description = "Validation response data")
    public static class Response {
        @Schema(description = "Validation result", example = "true")
        private Boolean isValid;

        @Schema(description = "Account holder name", example = "Ali Rezaei")
        private String accountHolderName;

        @Schema(description = "Account holder national code", example = "0235266321")
        private String accountHolderNationalCode;

        @Schema(description = "Response code", example = "0")
        private String responseCode;

        @Schema(description = "Response message", example = "Success")
        private String responseMessage;
    }

    public Combined getResponse() {
        // Implementation depends on actual response structure
        return new Combined();
    }
}