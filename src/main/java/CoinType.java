public enum CoinType {

    TWO_POUND("Two Pound", "£2", 200),
    ONE_POUND("One Pound", "£1", 100),
    FIFTY_PENCE("Fifty Pence", "50p", 50),
    TWENTY_PENCE("Twenty Pence", "20p", 20),
    TEN_PENCE("Ten Pence", "10p", 10),
    FIVE_PENCE("Five Pence", "5p", 5),
    TWO_PENCE("Two Pence", "2p", 2),
    ONE_PENCE("One Pence", "1p", 1);

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

    CoinType(String longName, String shortName, int value) {
        this.longName = longName;
        this.shortName = shortName;
        this.value = value;
    }
}
