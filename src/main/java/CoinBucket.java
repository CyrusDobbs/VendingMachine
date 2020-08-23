public class CoinBucket {

    private int coinValue;
    private int noOfCoins;

    public CoinBucket(int coinValue, int noOfCoins) {
        this.coinValue = coinValue;
        this.noOfCoins = noOfCoins;
    }

    public int getCoinValue() {
        return coinValue;
    }

    public int getNoOfCoins() {
        return noOfCoins;
    }

    public void addCoin() {
        noOfCoins++;
    }
}
