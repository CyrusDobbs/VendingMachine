public enum Coin {

    ONE_PENCE("One Pence", "1p", 1),
    TWO_PENCE("TwoPence", "2p", 2),
    FIVE_PENCE("FivePence", "5p", 5),
    TEN_PENCE("TenPence", "10p", 10),
    TWENTY_PENCE("TwentyPence", "20p", 20),
    FIFTY_PENCE("FiftyPence", "20p", 50),
    ONE_POUND("OnePound", "£1", 100),
    TWO_POUND("TwoPounds", "£2", 200);

    private String longName;
    private String shortName;
    private int value;

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }

    public int getValue() {
        return value;
    }

    Coin(String longName, String shortName, int value) {
        this.longName = longName;
        this.shortName = shortName;
        this.value = value;
    }
}
