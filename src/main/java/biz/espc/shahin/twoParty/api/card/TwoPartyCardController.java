package biz.espc.shahin.twoParty.api.card;

//import biz.espc.shahin.dto.twoWay.card.CardResponseDto;
//import biz.espc.shahin.dto.twoWay.card.GetCardInfoRequestDto;
import biz.espc.shahin.twoParty.service.card.TwoPartyCardService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: Ebrahim Sheyki
 * Created on: 1/28/2026  9:30 AM
 */
@RestController
@RequestMapping(path = "two-party/card/api")
public class TwoPartyCardController {

    private final TwoPartyCardService cardService;

    private TwoPartyCardController(TwoPartyCardService cardService) {
        this.cardService = cardService;
    }

//    @PostMapping("validate-card-number-by-nationalCode")
//    public Mono<CardResponseDto.Combined> validateCardNumber(@RequestBody CardInfoRequestDto requestDto) {
//        return cardService.validateCardNumber(requestDto);
//    }

}
