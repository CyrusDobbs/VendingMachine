package testharness;

import cointracker.CoinTracker;
import cointracker.CoinType;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.*;

public class TestHarness {

    private static List<Item> items;
    private static List<TrackerConfig> trackerConfigs;

    private static Scanner in = new Scanner(System.in);
    private static DecimalFormat df = new DecimalFormat("0.00");

    // Regex to use in input validation
    private static final String ONE_TWO_REGEX = "1|2";
    private static final String INTEGER_REGEX_AND_MINUS_ONE = "\\d+|-1";

    public static void main(String[] args) {

        // Load data from csv
        items = getItemsFromFile();
        trackerConfigs = getTrackerConfigsFromFile();

        // Set up Coin Tracker
        CoinTracker coinTracker = getCoinTracker();

        // Buy item loop
        boolean buyingItems = true;
        while (buyingItems) {
            buyItem(coinTracker);

            println("");
            println("-- Would you like to buy another item? --");
            println("1. Yes");
            println("2. No & exit");

            if (getParsedIntegerInput(ONE_TWO_REGEX) != 1) {
                buyingItems = false;
            }
        }
    }

    // Loads items from file
    private static List<Item> getItemsFromFile() {
        List<Item> items = new ArrayList<>();
        try (
                Reader reader = Files.newBufferedReader(new File("items.csv").toPath());
                CSVReader csvReader = new CSVReader(reader);
        ) {
            String[] itemRecord;
            while ((itemRecord = csvReader.readNext()) != null) {
                Item item = new Item(itemRecord[0],
                        Integer.parseInt(itemRecord[1]));
                items.add(item);
            }
        } catch (IOException e) {
            println("Error reading items.");
            System.exit(0);
        }
        return items;
    }

    // Loads existing tracker configs from file
    private static List<TrackerConfig> getTrackerConfigsFromFile() {
        List<TrackerConfig> configs = new ArrayList<>();
        try (
                Reader reader = Files.newBufferedReader(new File("tracker_configs.csv").toPath());
                CSVReader csvReader = new CSVReader(reader);
        ) {
            String[] configRecord;
            while ((configRecord = csvReader.readNext()) != null) {
                TrackerConfig trackerConfig = new TrackerConfig(
                        configRecord[0],
                        new int[]{
                                Integer.parseInt(configRecord[1]),
                                Integer.parseInt(configRecord[2]),
                                Integer.parseInt(configRecord[3]),
                                Integer.parseInt(configRecord[4]),
                                Integer.parseInt(configRecord[5]),
                                Integer.parseInt(configRecord[6]),
                                Integer.parseInt(configRecord[7]),
                                Integer.parseInt(configRecord[8])}
                );
                configs.add(trackerConfig);
            }
        } catch (IOException e) {
            println("Error reading Coin Tracker configurations.");
            System.exit(0);
        }
        return configs;
    }

    // Asks the user to choose from selecting an existing Coin Tracker or if they would like a random Coin Tracker
    private static CoinTracker getCoinTracker() {
        println("-- Welcome to the Coin Tracker interactive test-harness --");
        println("");
        println("-- Please select an option: --");
        println("1. Load existing Coin Tracker configuration");
        println("2. Random amounts of coins");

        int selection = getParsedIntegerInput(ONE_TWO_REGEX);
        clearConsole();

        if (selection == 1) {
            return loadExistingCoinTracker();
        } else {
            return getRandomCoinTracker();
        }
    }

    // Displays configurations and the user selects their choice
    private static CoinTracker loadExistingCoinTracker() {
        println("-- Please select a Coin Tracker to load --");
        println("");

        String regex = "";
        int trackerCount = 1;
        for (TrackerConfig trackerConfig : trackerConfigs) {
            print(trackerCount + ". " + trackerConfig.getName() + " ");
            printCoins(trackerConfig.getConfig());
            regex += trackerCount++ + "|";
        }

        int[] config = trackerConfigs.get(getParsedIntegerInput(regex) - 1).getConfig();
        return new CoinTracker(config);
    }

    // Returns a 'random' Coin Tracker
    private static CoinTracker getRandomCoinTracker() {
        Random random = new Random();
        int[] coins = new int[]{
                random.nextInt(20),
                random.nextInt(20),
                random.nextInt(20),
                random.nextInt(20),
                random.nextInt(20),
                random.nextInt(20),
                random.nextInt(20),
                random.nextInt(20)
        };
        return new CoinTracker(coins);
    }

    private static void buyItem(CoinTracker coinTracker) {
        clearConsole();

        // Display coins
        print("Coins in the machine: ");
        printCoins(coinTracker.getCoinBankState());

        // Select item
        Item item = selectItem();

        // Select which coins to pay with
        int[] coins = selectCoinsToPayWith(item.getPrice());
        int coinSum = sumCoins(coins);
        int changeDue = coinSum - item.getPrice();

        // Deposit coins to vending machine
        coinTracker.depositCoins(coins);
        println("Payed " + toPounds(coinSum));
        print("In these coins ");
        printCoins(coins);
        println("For " + item.getName() + " costing " + toPounds(item.getPrice()));
        println("");

        // Calculate and get change
        int[] changeGiven = coinTracker.returnChange(changeDue);
        int changeGivenSum = sumCoins(changeGiven);
        println("Received " + toPounds(changeGivenSum) + " change");
        print("In these coins ");
        printCoins(changeGiven);
        if (changeGivenSum < changeDue) {
            println("The machine did not have the correct amount of change and short changed you.");
        }
    }

    private static Item selectItem() {
        // Display options
        println("");
        println("-- Items --");
        StringBuilder regex = new StringBuilder();
        int itemCount = 1;
        for (Item item : items) {
            println(itemCount + ". " + item.getName() + " " + toPounds(item.getPrice()));
            regex.append(itemCount++).append("|");
        }
        println("");

        // Take input
        println("Which item would you like to purchase?");
        Item item = items.get(getParsedIntegerInput(regex.toString()) - 1);
        clearConsole();
        return item;
    }

    // Displays selected coins while asking the user to input how many of each coin they want to pay with.
    private static int[] selectCoinsToPayWith(int price) {
        println("");
        println("-- Please select coins to pay with --");

        int[] coins = new int[8];
        int total = 0;
        for (CoinType coinType : CoinType.values()) {
            if (coinType.getIndex() != 0) {
                clearConsole();
            }

            print("Selected coins: ");
            printCoins(coins);
            println("Current total: " + toPounds(total));
            println("Item price: " + toPounds(price));
            println("");
            println("(Enter '-1' to stop selecting more coins)");
            println("How many " + coinType.getShortName() + "'s?");
            int noOfCoins = getParsedIntegerInput(INTEGER_REGEX_AND_MINUS_ONE);

            if (noOfCoins == -1) {
                break;
            }

            coins[coinType.getIndex()] = noOfCoins;
            total += noOfCoins * coinType.getValue();
        }

        // If the user doesn't select enough coins to pay for the item then ask again
        if (total < price) {
            clearConsole();
            println("-- You selected " + toPounds(total) + " but the item costs " + toPounds(price) + " --");
            return selectCoinsToPayWith(price);
        }
        clearConsole();
        return coins;
    }

    // Takes an int array of coins and sums their value
    private static int sumCoins(int[] coins) {
        int sum = 0;
        for (CoinType coinType : CoinType.values()) {
            sum += coins[coinType.getIndex()] * coinType.getValue();
        }
        return sum;
    }

    // Formats pence to pounds
    private static String toPounds(int pence) {
        return "£" + df.format((double) pence / 100);
    }

    // Formats and prints an int array of coins
    private static void printCoins(int[] coins) {
        println("|" + "£2: " + coins[0]
                + "|" + "£1: " + coins[1]
                + "|" + "50p: " + coins[2]
                + "|" + "20p: " + coins[3]
                + "|" + "10p: " + coins[4]
                + "|" + "5p: " + coins[5]
                + "|" + "2p: " + coins[6]
                + "|" + "1p: " + coins[7] + "|"
        );
    }

    private static void println(String string) {
        System.out.println(string);
    }

    private static void print(String string) {
        System.out.print(string);
    }

    private static void clearConsole() {
        for (int i = 0; i < 100; i++) {
            println("");
        }
    }

    // Validates and returns user input
    private static int getParsedIntegerInput(String regex) {
        while (true) {
            try {
                return Integer.parseInt(in.next(regex));
            } catch (InputMismatchException e) {
                in.next();
                println("That is not an option.");
            }
        }
    }
}
