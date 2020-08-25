package cointracker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/*
    This is the main class used for tracking the coins.
 */
public class CoinTracker {

    private static final Logger logger = LogManager.getLogger(CoinTracker.class);

    // Holds the coin buckets
    private List<CoinBucket> coinBank;

    // Constructor to initialise the Coin Tracker
    public CoinTracker(int[] initialCoins) {
        logger.info("INITIALISING.");
        coinBank = new ArrayList<>();

        // For each type of coin, create and populate a CoinBucket
        for (CoinType coinType : CoinType.values()) {
            int noOfCoins = initialCoins[coinType.getIndex()];
            coinBank.add(new CoinBucket(coinType, noOfCoins));
        }

        logCoinBankState();
        logger.info("FINISHED INITIALISING.");
    }

    public void depositCoins(int[] depositedCoins) {
        logger.info("DEPOSITING.");

        // Iterates through the CoinBuckets that hold each type of coin
        for (CoinBucket coinBucket : coinBank) {
            int noOfCoins = depositedCoins[coinBucket.getCoinType().getIndex()];

            // If there are coins, deposit them
            if (noOfCoins > 0) {
                coinBucket.depositCoins(noOfCoins);
                logger.info("Deposited {} x {} coins.", noOfCoins, coinBucket.getCoinType().getShortName());
            }
        }

        logCoinBankState();
        logger.info("FINISHED DEPOSITING.");
    }

    // Returns the minimum number of coins for the amount change requested
    public int[] returnChange(int changeRequested) {
        logger.info("GIVING CHANGE. ({})", changeRequested);

        // Initialise the change
        int[] change = new int[8];
        // Keep a count of the sum of change
        int changeSum = 0;

        for (CoinBucket coinBucket : coinBank) {
            // Stop if we have collected the correct amount of change
            if (changeRequested == changeSum) {
                break;
            }

            // Work out the the ideal number of coins in order to keep the total amount to a minimum
            int idealNoOfCoinsNeeded = (changeRequested - changeSum) / coinBucket.getCoinType().getValue();
            // Ask the bucket for the ideal amount and it will return the closest amount to it possible
            int noOfCoinsWithdrawn = coinBucket.withdrawCoins(idealNoOfCoinsNeeded);

            // If any coins are withdrawn then tally up and log
            if (noOfCoinsWithdrawn > 0) {
                change[coinBucket.getCoinType().getIndex()] = noOfCoinsWithdrawn;
                changeSum += noOfCoinsWithdrawn * coinBucket.getCoinType().getValue();
                logger.info("Given {} x {} coins in change.", noOfCoinsWithdrawn, coinBucket.getCoinType().getShortName());
            }
        }

        // Log whether full/short change is given
        if (changeSum == changeRequested) {
            logger.info("FULL change given.");
        } else {
            logger.info("SHORT change given. ({}/{})", changeSum, changeRequested);
        }
        logCoinBankState();
        logger.info("FINISHED GIVING CHANGE.");
        return change;
    }

    // Returns the state of the coinBank as an int array
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
