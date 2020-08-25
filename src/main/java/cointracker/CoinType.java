package cointracker;

/*
    This class holds the types of coins tracked by the system and their properties.
    If the business were to expand overseas they could create a new CoinType to be used.
 */
public enum CoinType {

    TWO_POUND(0, "£2", 200),
    ONE_POUND(1, "£1", 100),
    FIFTY_PENCE(2, "50p", 50),
    TWENTY_PENCE(3, "20p", 20),
    TEN_PENCE(4, "10p", 10),
    FIVE_PENCE(5, "5p", 5),
    TWO_PENCE(6, "2p", 2),
    ONE_PENCE(7, "1p", 1);

    private int index;
    private String shortName;
    private int value;

    public int getIndex() {
        return index;
    }

    public String getShortName() {
        return shortName;
    }

    public int getValue() {
        return value;
    }

    CoinType(int index, String shortName, int value) {
        this.index = index;
        this.shortName = shortName;
        this.value = value;
    }
}
