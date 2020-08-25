package cointracker;

/*
    This class is responsible for holding the coins alongside their type in order to be used by CoinTracker.
 */
public class CoinBucket {

    private CoinType coinType;
    private int coinTotal;

    public CoinBucket(CoinType coinType, int coinTotal) {
        this.coinType = coinType;
        this.coinTotal = coinTotal;
    }

    public CoinType getCoinType() {
        return coinType;
    }

    public int getCoinTotal() {
        return coinTotal;
    }

    public void depositCoins(int amount) {
        coinTotal += amount;
    }

    // Takes a desired amount of coins and returns the closest possible amount.
    public int withdrawCoins(int desiredAmount) {
        if (desiredAmount <= coinTotal) {
            coinTotal -= desiredAmount;
            return desiredAmount;
        } else {
            int availableCoins = coinTotal;
            coinTotal = 0;
            return availableCoins;
        }
    }
}
