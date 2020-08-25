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
    private static List<MachineConfig> machineConfigs;

    private static Scanner in = new Scanner(System.in);
    private static DecimalFormat df = new DecimalFormat("0.00");

    private static final String ONE_TWO_REGEX = "1|2";
    private static final String ONE_THREE_REGEX = "1|2|3";
    private static final String INTEGER_REGEX = "\\d+";
    private static final String INTEGER_REGEX_AND_MINUS_ONE = "\\d+|-1";

    public static void main(String[] args) {

        items = getItemsFromFile();
        machineConfigs = getMachineConfigsFromFile();

        println("-- Welcome to the Vending Machine interactive test-harness --");
        println("");
        println("-- Please select an option: --");
        println("1. Load existing Vending Machine");
        println("2. Random Vending Machine");

        int selection = getParsedIntegerInput(ONE_TWO_REGEX);
        clearConsole();

        CoinTracker coinTracker;
        if (selection == 1) {
            coinTracker = loadExistingVendingMachine();
        } else {
            coinTracker = getRandomVendingMachine();
        }

        while (true) {
            buyItem(coinTracker);
        }

    }

    private static void buyItem(CoinTracker coinTracker) {
        clearConsole();
        print("Coins in the machine: ");
        printCoins(coinTracker.getCoinBankState());
        Item item = selectItem();
        int[] coins = selectCoinsToPay(item.getPrice());
        int coinSum = sumCoins(coins);
        int changeDue = coinSum - item.getPrice();

        coinTracker.depositCoins(coins);
        println("Payed " + toPounds(coinSum));
        print("In these coins ");
        printCoins(coins);
        println("For a " + item.getName() + " costing " + toPounds(item.getPrice()));
        println("");
        int[] changeGiven = coinTracker.returnChange(changeDue);
        int changeGivenSum = sumCoins(changeGiven);
        println("Received " + toPounds(changeGivenSum) + " change");
        print("In these coins ");
        printCoins(changeGiven);
        if (changeGivenSum < changeDue) {
            println("The machine did not have the correct amount of change and short changed you.");
        }

        println("");
        println("-- Would you like to buy another item? --");
        println("1. Yes");
        println("2. No & exit");

        int selection = getParsedIntegerInput(ONE_TWO_REGEX);

        if (selection == 2) {
            System.exit(0);
        }
    }

    private static int sumCoins(int[] coins) {
        int sum = 0;
        for (CoinType coinType : CoinType.values()) {
            sum += coins[coinType.getIndex()] * coinType.getValue();
        }
        return sum;
    }

    private static int[] selectCoinsToPay(int price) {
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

        if (total < price) {
            clearConsole();
            println("-- You selected " + toPounds(total) + " but the item costs " + toPounds(price) + " --");
            return selectCoinsToPay(price);
        }
        clearConsole();
        return coins;
    }

    private static String toPounds(int pence) {
        return "£" + df.format((double) pence / 100);
    }

    private static Item selectItem() {
        println("");
        println("-- Items --");
        String regex = "";
        int itemCount = 1;
        for (Item item : items) {
            println(itemCount + ". " + item.getName() + " " + toPounds(item.getPrice()));
            regex += itemCount++ + "|";
        }
        // regex = regex.substring(0, regex.length() - 2);
        println("");
        println("Which item would you like to purchase?");
        Item item = items.get(getParsedIntegerInput(regex) - 1);
        clearConsole();
        return item;
    }


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

    private static List<MachineConfig> getMachineConfigsFromFile() {
        List<MachineConfig> configs = new ArrayList<>();

        try (
                Reader reader = Files.newBufferedReader(new File("machines.csv").toPath());
                CSVReader csvReader = new CSVReader(reader);
        ) {
            String[] configRecord;
            while ((configRecord = csvReader.readNext()) != null) {
                MachineConfig machineConfig = new MachineConfig(
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
                configs.add(machineConfig);
            }
        } catch (IOException e) {
            println("Error reading machine configurations.");
            System.exit(0);
        }

        return configs;
    }

    private static CoinTracker loadExistingVendingMachine() {
        println("-- Please select a machine to load --");
        println("");

        String regex = "";
        int machineCount = 1;
        for (MachineConfig machineConfig : machineConfigs) {
            print(machineCount + ". " + machineConfig.getName() + " ");
            printCoins(machineConfig.getConfig());
            regex += machineCount++ + "|";
        }

        int[] config = machineConfigs.get(getParsedIntegerInput(regex) - 1).getConfig();
        return new CoinTracker(config);
    }

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

    private static CoinTracker getRandomVendingMachine() {
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
