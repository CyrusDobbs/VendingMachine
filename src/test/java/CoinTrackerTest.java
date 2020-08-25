import cointracker.CoinTracker;
import org.junit.Assert;
import org.junit.Test;

public class CoinTrackerTest {

    @Test
    public void depositCoinsToEmptyBankAndGetChange() {
        CoinTracker coinTracker = new CoinTracker(new int[8]);
        int[] coinsToDeposit = new int[]{1, 1, 1, 1, 2, 4, 1, 1};
        coinTracker.depositCoins(coinsToDeposit);
        int[] change = coinTracker.returnChange(127);
        int[] change2 = coinTracker.returnChange(36);
        int[] change3 = coinTracker.returnChange(250);
        int[] change4 = coinTracker.returnChange(99);

        Assert.assertArrayEquals(change, new int[]{0, 1, 0, 1, 0, 1, 1, 0});
        Assert.assertArrayEquals(change2, new int[]{0, 0, 0, 0, 2, 3, 0, 1});
        Assert.assertArrayEquals(change3, new int[]{1, 0, 1, 0, 0, 0, 0, 0});
        Assert.assertArrayEquals(change4, new int[]{0, 0, 0, 0, 0, 0, 0, 0});
    }

    @Test
    public void getChangeFromInitialisedCoinBank() {
        int[] coinsToInitialise = new int[]{1, 1, 1, 1, 2, 4, 1, 1};
        CoinTracker coinTracker = new CoinTracker(coinsToInitialise);

        int[] change = coinTracker.returnChange(127);
        int[] change2 = coinTracker.returnChange(36);
        int[] change3 = coinTracker.returnChange(250);
        int[] change4 = coinTracker.returnChange(99);

        Assert.assertArrayEquals(change, new int[]{0, 1, 0, 1, 0, 1, 1, 0});
        Assert.assertArrayEquals(change2, new int[]{0, 0, 0, 0, 2, 3, 0, 1});
        Assert.assertArrayEquals(change3, new int[]{1, 0, 1, 0, 0, 0, 0, 0});
        Assert.assertArrayEquals(change4, new int[]{0, 0, 0, 0, 0, 0, 0, 0});
    }

    @Test
    public void returnsCorrectCoinBankState() {
        int[] coinsToInitialise = new int[]{940, 0, 0, 1, 9999, 17, 3, 0};
        CoinTracker coinTracker = new CoinTracker(coinsToInitialise);
        int[] state = coinTracker.getCoinBankState();

        Assert.assertArrayEquals(coinsToInitialise, state);
    }

}