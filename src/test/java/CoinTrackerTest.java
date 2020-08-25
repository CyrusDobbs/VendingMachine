import cointracker.CoinTracker;
import org.junit.Assert;
import org.junit.Test;

public class CoinTrackerTest {

    @Test
    public void depositsCoinsToEmptyBankAndGivesChange() {
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
    public void givesChangeFromInitialisedCoinBank() {
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
    public void givesShortChangeWhenCoinsRunOut() {
        int[] coinsToInitialise = new int[]{7, 8, 3, 2, 99, 4, 5, 0};
        CoinTracker coinTracker = new CoinTracker(coinsToInitialise);

        int[] change = coinTracker.returnChange(99999);
        Assert.assertArrayEquals(change, coinsToInitialise);
    }

    @Test
    public void givesShortChangeWhenNotEnough1ps() {
        int[] coinsToInitialise = new int[]{6, 5, 2, 12, 0, 3, 0, 1};
        CoinTracker coinTracker = new CoinTracker(coinsToInitialise);

        int[] change = coinTracker.returnChange(12);
        Assert.assertArrayEquals(change, new int[]{0, 0, 0, 0, 0, 2, 0, 1});
    }

    @Test
    public void returnsCorrectCoinBankState() {
        int[] coinsToInitialise = new int[]{940, 0, 0, 1, 9999, 17, 3, 0};
        CoinTracker coinTracker = new CoinTracker(coinsToInitialise);
        int[] state = coinTracker.getCoinBankState();

        Assert.assertArrayEquals(coinsToInitialise, state);
    }

    @Test
    public void returnsCorrectEmptyCoinBankState() {
        int[] coinsToInitialise = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        CoinTracker coinTracker = new CoinTracker(coinsToInitialise);
        int[] state = coinTracker.getCoinBankState();

        Assert.assertArrayEquals(coinsToInitialise, state);
    }
}