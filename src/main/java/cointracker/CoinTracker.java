package cointracker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class CoinTracker {

    private static final Logger logger = LogManager.getLogger(CoinTracker.class);

    private List<CoinBucket> coinBank;

    public CoinTracker(int[] initialCoins) {
        logger.info("INITIALISING.");
        coinBank = new ArrayList<>();

        for (CoinType coinType : CoinType.values()) {
            int noOfCoins = initialCoins[coinType.getIndex()];
            coinBank.add(new CoinBucket(coinType, noOfCoins));
        }

        logCoinBankState();
        logger.info("FINISHED INITIALISING.");
    }

    public void depositCoins(int[] depositedCoins) {
        logger.info("DEPOSITING.");

        for (CoinBucket coinBucket : coinBank) {
            int noOfCoins = depositedCoins[coinBucket.getCoinType().getIndex()];
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

        for (CoinBucket coinBucket : coinBank) {
            if (changeRequested == changeGiven) {
                break;
            }

            int idealNoOfCoinsNeeded = (changeRequested - changeGiven) / coinBucket.getCoinType().getValue();
            int noOfCoinsWithdrawn = coinBucket.withdrawCoins(idealNoOfCoinsNeeded);
            change[coinBucket.getCoinType().getIndex()] = noOfCoinsWithdrawn;
            changeGiven += noOfCoinsWithdrawn * coinBucket.getCoinType().getValue();

            if (noOfCoinsWithdrawn > 0) {
                logger.info("Given {} x {} coins in change.", noOfCoinsWithdrawn, coinBucket.getCoinType().getShortName());
            }
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

    public int[] getCoinBankState() {
        int[] coins = new int[8];

        for (CoinBucket coinBucket : coinBank) {
            coins[coinBucket.getCoinType().getIndex()] = coinBucket.getCoinTotal();
        }

        return coins;
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
            logger.info("CoinBankState: EMPTY!");
        } else {
            logger.info("CoinBankState: " + stringBuilder.toString());
        }
    }
}
