import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class VendingMachineTest {

    @Test
    public void depositCoinsToEmptyBankAndGetChange() {
        VendingMachine vendingMachine = new VendingMachine(new int[8]);
        int[] coinsToDeposit = new int[]{1, 1, 1, 1, 2, 4, 1, 1};
        vendingMachine.depositCoins(coinsToDeposit);
        int[] change = vendingMachine.returnChange(127);
        int[] change2 = vendingMachine.returnChange(36);
        int[] change3 = vendingMachine.returnChange(250);
        int[] change4 = vendingMachine.returnChange(99);
        Assert.assertArrayEquals(change, new int[]{0, 1, 0, 1, 0, 1, 1, 0});
        Assert.assertArrayEquals(change2, new int[]{0, 0, 0, 0, 2, 3, 0, 1});
        Assert.assertArrayEquals(change3, new int[]{1, 0, 1, 0, 0, 0, 0, 0});
        Assert.assertArrayEquals(change4, new int[]{0, 0, 0, 0, 0, 0, 0, 0});
    }

    @Test
    public void getChangeFromInitialisedCoinBank() {
        int[] coinsToInitialise = new int[]{1, 1, 1, 1, 2, 4, 1, 1};
        VendingMachine vendingMachine = new VendingMachine(coinsToInitialise);

        int[] change = vendingMachine.returnChange(127);
        int[] change2 = vendingMachine.returnChange(36);
        int[] change3 = vendingMachine.returnChange(250);
        int[] change4 = vendingMachine.returnChange(99);
        Assert.assertArrayEquals(change, new int[]{0, 1, 0, 1, 0, 1, 1, 0});
        Assert.assertArrayEquals(change2, new int[]{0, 0, 0, 0, 2, 3, 0, 1});
        Assert.assertArrayEquals(change3, new int[]{1, 0, 1, 0, 0, 0, 0, 0});
        Assert.assertArrayEquals(change4, new int[]{0, 0, 0, 0, 0, 0, 0, 0});
    }

}