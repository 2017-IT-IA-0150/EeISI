package it.infocert.eigor.model.core.enums;

import static com.google.common.base.Preconditions.checkArgument;

public enum Iso4217CurrencyCode {

    AFN("AFN", 971, 2),
    ALL("ALL", 8, 2),
    AMD("AMD", 51, 2),
    ANG("ANG", 532, 2),
    AOA("AOA", 973, 2),
    ARS("ARS", 32, 2),
    AUD("AUD", 36, 2),
    AWG("AWG", 533, 2),
    AZN("AZN", 944, 2),
    BAM("BAM", 977, 2),
    BBD("BBD", 52, 2),
    BDT("BDT", 50, 2),
    BGN("BGN", 975, 2),
    BHD("BHD", 48, 3),
    BIF("BIF", 108, 0),
    BMD("BMD", 60, 2),
    BND("BND", 96, 2),
    BOB("BOB", 68, 2),
    BOV("BOV", 984, 2),
    BRL("BRL", 986, 2),
    BSD("BSD", 44, 2),
    BTN("BTN", 64, 2),
    BWP("BWP", 72, 2),
    BYN("BYN", 933, 2),
    BYR("BYR", 974, 0),
    BZD("BZD", 84, 2),
    CAD("CAD", 124, 2),
    CDF("CDF", 976, 2),
    CHE("CHE", 947, 2),
    CHF("CHF", 756, 2),
    CHW("CHW", 948, 2),
    CLF("CLF", 990, 4),
    CLP("CLP", 152, 0),
    CNY("CNY", 156, 2),
    COP("COP", 170, 2),
    COU("COU", 970, 2),
    CRC("CRC", 188, 2),
    CUC("CUC", 931, 2),
    CUP("CUP", 192, 2),
    CVE("CVE", 132, 0),
    CZK("CZK", 203, 2),
    DJF("DJF", 262, 0),
    DKK("DKK", 208, 2),
    DOP("DOP", 214, 2),
    DZD("DZD", 12, 2),
    EGP("EGP", 818, 2),
    ERN("ERN", 232, 2),
    ETB("ETB", 230, 2),
    EUR("EUR", 978, 2),
    FJD("FJD", 242, 2),
    FKP("FKP", 238, 2),
    GBP("GBP", 826, 2),
    GEL("GEL", 981, 2),
    GHS("GHS", 936, 2),
    GIP("GIP", 292, 2),
    GMD("GMD", 270, 2),
    GNF("GNF", 324, 0),
    GTQ("GTQ", 320, 2),
    GYD("GYD", 328, 2),
    HKD("HKD", 344, 2),
    HNL("HNL", 340, 2),
    HRK("HRK", 191, 2),
    HTG("HTG", 332, 2),
    HUF("HUF", 348, 2),
    IDR("IDR", 360, 2),
    ILS("ILS", 376, 2),
    INR("INR", 356, 2),
    IQD("IQD", 368, 3),
    IRR("IRR", 364, 2),
    ISK("ISK", 352, 0),
    JMD("JMD", 388, 2),
    JOD("JOD", 400, 3),
    JPY("JPY", 392, 0),
    KES("KES", 404, 2),
    KGS("KGS", 417, 2),
    KHR("KHR", 116, 2),
    KMF("KMF", 174, 0),
    KPW("KPW", 408, 2),
    KRW("KRW", 410, 0),
    KWD("KWD", 414, 3),
    KYD("KYD", 136, 2),
    KZT("KZT", 398, 2),
    LAK("LAK", 418, 2),
    LBP("LBP", 422, 2),
    LKR("LKR", 144, 2),
    LRD("LRD", 430, 2),
    LSL("LSL", 426, 2),
    LYD("LYD", 434, 3),
    MAD("MAD", 504, 2),
    MDL("MDL", 498, 2),
    MGA("MGA", 969, 1),
    MKD("MKD", 807, 2),
    MMK("MMK", 104, 2),
    MNT("MNT", 496, 2),
    MOP("MOP", 446, 2),
    MRO("MRO", 478, 1),
    MUR("MUR", 480, 2),
    MVR("MVR", 462, 2),
    MWK("MWK", 454, 2),
    MXN("MXN", 484, 2),
    MXV("MXV", 979, 2),
    MYR("MYR", 458, 2),
    MZN("MZN", 943, 2),
    NAD("NAD", 516, 2),
    NGN("NGN", 566, 2),
    NIO("NIO", 558, 2),
    NOK("NOK", 578, 2),
    NPR("NPR", 524, 2),
    NZD("NZD", 554, 2),
    OMR("OMR", 512, 3),
    PAB("PAB", 590, 2),
    PEN("PEN", 604, 2),
    PGK("PGK", 598, 2),
    PHP("PHP", 608, 2),
    PKR("PKR", 586, 2),
    PLN("PLN", 985, 2),
    PYG("PYG", 600, 0),
    QAR("QAR", 634, 2),
    RON("RON", 946, 2),
    RSD("RSD", 941, 2),
    RUB("RUB", 643, 2),
    RWF("RWF", 646, 0),
    SAR("SAR", 682, 2),
    SBD("SBD", 90, 2),
    SCR("SCR", 690, 2),
    SDG("SDG", 938, 2),
    SEK("SEK", 752, 2),
    SGD("SGD", 702, 2),
    SHP("SHP", 654, 2),
    SLL("SLL", 694, 2),
    SOS("SOS", 706, 2),
    SRD("SRD", 968, 2),
    SSP("SSP", 728, 2),
    STD("STD", 678, 2),
    SVC("SVC", 222, 2),
    SYP("SYP", 760, 2),
    SZL("SZL", 748, 2),
    THB("THB", 764, 2),
    TJS("TJS", 972, 2),
    TMT("TMT", 934, 2),
    TND("TND", 788, 3),
    TOP("TOP", 776, 2),
    TRY("TRY", 949, 2),
    TTD("TTD", 780, 2),
    TWD("TWD", 901, 2),
    TZS("TZS", 834, 2),
    UAH("UAH", 980, 2),
    UGX("UGX", 800, 0),
    USD("USD", 840, 2),
    USN("USN", 997, 2),
    UYI("UYI", 940, 0),
    UYU("UYU", 858, 2),
    UZS("UZS", 860, 2),
    VEF("VEF", 937, 2),
    VND("VND", 704, 0),
    VUV("VUV", 548, 0),
    WST("WST", 882, 2),
    XAF("XAF", 950, 0),
    XAG("XAG", 961, 0),
    XAU("XAU", 959, 0),
    XBA("XBA", 955, 0),
    XBB("XBB", 956, 0),
    XBC("XBC", 957, 0),
    XBD("XBD", 958, 0),
    XCD("XCD", 951, 2),
    XDR("XDR", 960, 0),
    XOF("XOF", 952, 0),
    XPD("XPD", 964, 0),
    XPF("XPF", 953, 0),
    XPT("XPT", 962, 0),
    XSU("XSU", 994, 0),
    XTS("XTS", 963, 0),
    XUA("XUA", 965, 0),
    XXX("XXX", 999, 0),
    YER("YER", 886, 2),
    ZAR("ZAR", 710, 2),
    ZMW("ZMW", 967, 2),
    ZWL("ZWL", 932, 2);

    private final String isoCode;
    private final int isoNumericCode;
    private final int decimalDigits;

    Iso4217CurrencyCode(String isoCode, int isoNumericCode, int decimalDigits) {
        checkArgument(decimalDigits >= 0);
        checkArgument(isoNumericCode >= 0);
        checkArgument(isoCode != null && isoCode.trim().length() == 3);
        this.isoCode = isoCode;
        this.isoNumericCode = isoNumericCode;
        this.decimalDigits = decimalDigits;
    }
}