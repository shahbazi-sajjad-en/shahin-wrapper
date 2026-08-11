package biz.espc.shahin.enumeration.bank;


import java.util.Arrays;
import java.util.Optional;

public enum Bank {

    CBI("010"),
    BMK("011"),
    MEL("012"),
    REF("013"),
    BSP("015"),
    BKV("016"),
    BMI("017"),
    TEJ("018"),
    BSI("019"),
    BTS("020"),
    PST("021"),
    BTT("022"),
    AYN("051"),
    ANS("053"),
    PAR("054"),
    ENB("055"),
    SAM("056"),
    PAS("057"),
    GHA("058"),
    SIN("059"),
    MHR("060"),
    SHR("061"),
    BKA("062"),
    GAR("064"),
    DEY("066"),
    IRZ("069"),
    RES("070"),
    NOR("075"),
    IRV("078");

    private final String ibanCode;

    Bank(String ibanCode) {
        this.ibanCode = ibanCode;
    }

    public static Optional<Bank> fromIban(String iban) {

        if (iban == null) return Optional.empty();

        String clean = iban.replaceAll("\\s+", "");

        if (!clean.startsWith("IR") || clean.length() < 7)
            return Optional.empty();

        String bankCode = clean.substring(4, 7);

        return Arrays.stream(values())
                .filter(bank -> bank.ibanCode.equals(bankCode))
                .findFirst();
    }
}