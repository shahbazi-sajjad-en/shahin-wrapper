package biz.espc.shahin.dto.account;


import biz.espc.shahin.enumeration.bank.Bank;

/**
 * author: Ebrahim Sheyki
 * modified on: 1/31/2026  03:45 PM
 *  Converting to immutable object using record
 */
public record Account(String accountType, String accountTypeName, String accountNumber, Bank bank) {
}
