package biz.espc.shahin.dto.account;

import java.math.BigDecimal;

/**
 * author: Ebrahim Sheyki
 * Created on: 2/7/2026  5:44 PM
 */
public record AccountBalanceDetail(BigDecimal availableBalance, BigDecimal effectiveBalance, String accountType) {
}
