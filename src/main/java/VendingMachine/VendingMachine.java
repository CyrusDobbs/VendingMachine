package VendingMachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class VendingMachine {

    private static final Logger logger = LogManager.getLogger(VendingMachine.class);

    private List<CoinBucket> coinBank;

    public VendingMachine(int[] initialCoins) {
        logger.info("INITIALISING.");
        coinBank = new ArrayList<>();

        int coinIndex = 0;
        for (CoinType coinType : CoinType.values()) {
            int noOfCoins = initialCoins[coinIndex++];
            coinBank.add(new CoinBucket(coinType, noOfCoins));
        }

        logCoinBankState();
        logger.info("FINISHED INITIALISING.");
    }

    public void depositCoins(int[] depositedCoins) {
        logger.info("DEPOSITING.");

        int coinIndex = 0;
        for (CoinBucket coinBucket : coinBank) {
            int noOfCoins = depositedCoins[coinIndex++];
            if (noOfCoins > 0) {
                coinBucket.depositCoins(noOfCoins);
                logger.info("Deposited {} x {} coins.", noOfCoins, coinBucket.getCoinType().getShortName());
            }
        }

        logCoinBankState();
        logger.info("FINISHED DEPOSITING.");
    }

    public int[] returnChange(int changeRequested) {
        logger.info("GIVING CHANGE. ({})", changeRequested);
        int[] change = new int[8];
        int changeGiven = 0;

        int coinIndex = 0;
        for (CoinBucket coinBucket : coinBank) {
            if (changeRequested == changeGiven) {
                break;
            }

            int idealNoOfCoinsNeeded = (changeRequested - changeGiven) / coinBucket.getCoinType().getValue();
            int coinsWithdrawn = coinBucket.withdrawCoins(idealNoOfCoinsNeeded);
            change[coinIndex++] = coinsWithdrawn;
            changeGiven += coinsWithdrawn * coinBucket.getCoinType().getValue();

            logger.info("Given {} x {} coins in change.", coinsWithdrawn, coinBucket.getCoinType().getShortName());
        }

        if (changeGiven == changeRequested) {
            logger.info("FULL change given.");
        } else {
            logger.info("SHORT change given. ({}/{})", changeGiven, changeRequested);
        }
        logCoinBankState();
        logger.info("FINISHED GIVING CHANGE.");
        return change;
    }

    private void logCoinBankState() {
        StringBuilder stringBuilder = new StringBuilder();

        boolean noCoins = true;
        for (CoinBucket coinBucket : coinBank) {
            if (coinBucket.getCoinTotal() != 0) {
                noCoins = false;
            }

            stringBuilder.append(coinBucket.getCoinType().getShortName()).append(": ").append(coinBucket.getCoinTotal()).append(" | ");
        }
        stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length() - 1);

        if (noCoins) {
            logger.info("CoinBank State: EMPTY!");
        } else {
            logger.info("CoinBank State: " + stringBuilder.toString());
        }
    }
}
