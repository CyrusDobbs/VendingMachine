import java.util.ArrayList;
import java.util.List;

public class VendingMachine {

    private List<CoinBucket> coinBank;

    private int costOfItem;
    private int currentlyEntered;

    public VendingMachine(int[] coins, int cost, int currentlyEntered) {
        coinBank = new ArrayList<>();
        int count = 0;
        for (Coin coin : Coin.values()) {
            coinBank.add(new CoinBucket(coin.getValue(), coins[count]));
        }
    }

    public void depositCoins(int value) {
        coinBank.stream().filter(b -> b.getCoinValue() == value).findFirst().get().addCoin();
    }
}
