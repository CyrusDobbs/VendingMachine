import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class VendingMachine {

    private Map<CoinType, Integer> coinBank;

    private static final Logger logger = LogManager.getLogger(VendingMachine.class);

    public VendingMachine(int[] initialCoins) {
        logger.info("INITIALISING.");
        coinBank = new LinkedHashMap<>();

        int coinIndex = 0;
        for (CoinType coinType : CoinType.values()) {
            int noOfCoins = initialCoins[coinIndex++];
            coinBank.put(coinType, noOfCoins);
        }

        logCoinBankState();
        logger.info("FINISHED INITIALISING.");
    }

    public void depositCoins(int[] depositedCoins) {
        logger.info("DEPOSITING.");

        int coinIndex = 0;
        for (CoinType coinType : CoinType.values()) {
            int noOfCoins = depositedCoins[coinIndex++];
            if (noOfCoins > 0) {
                coinBank.computeIfPresent(coinType, (k, v) -> v + noOfCoins);
                logger.info("Deposited {} x {} coins.", noOfCoins, coinType.getShortName());
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
        for (CoinType coinType : CoinType.values()) {
            if (changeRequested == changeGiven) {
                break;
            }

            int idealNoOfCoinsNeeded = (changeRequested - changeGiven) / coinType.getValue();
            int noOfCoinsInBank = coinBank.get(coinType);
            int noOfCoinsToReturn = Math.min(idealNoOfCoinsNeeded, noOfCoinsInBank);

            if (noOfCoinsToReturn > 0) {
                coinBank.computeIfPresent(coinType, (k, v) -> v - noOfCoinsToReturn);
                logger.info("Given {} x {} coins in change.", noOfCoinsToReturn, coinType.getShortName());
                change[coinIndex] = noOfCoinsToReturn;
                changeGiven += noOfCoinsToReturn * coinType.getValue();
            }
            coinIndex++;
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
        for (Map.Entry<CoinType, Integer> entry : coinBank.entrySet()) {
            if (entry.getValue() != 0) {
                noCoins = false;
            }
            stringBuilder.append(entry.getKey().getShortName()).append(": ").append(entry.getValue()).append(" | ");
        }
        stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length() - 1);

        if (noCoins) {
            logger.info("CoinBank State: EMPTY!");
        } else {
            logger.info("CoinBank State: " + stringBuilder.toString());
        }
    }
}
