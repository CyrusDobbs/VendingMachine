package TestHarness;

import VendingMachine.VendingMachine;
import VendingMachine.CoinType;

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

    private static int[] machineCoins;

    private static Scanner in = new Scanner(System.in);
    private static DecimalFormat df = new DecimalFormat("0.00");

    private static final String ONE_TWO_REGEX = "1|2";
    private static final String ONE_THREE_REGEX = "1|2|3";
    private static final String INTEGER_REGEX = "\\d+";

    public static void main(String[] args) {

        items = getItemsFromFile();
        machineConfigs = getMachineConfigsFromFile();

        println("-- Welcome to the Vending Machine interactive test-harness --");
        println("");
        println("-- Please select an option: --");
        println("1. Load existing Vending Machine");
        println("2. Random Vending Machine");

        int selection = getParsedIntegerInput(ONE_TWO_REGEX);

        VendingMachine vendingMachine;
        if (selection == 1) {
            vendingMachine = loadExistingVendingMachine();
        } else if (selection == 2) {
            vendingMachine = getRandomVendingMachine();
        }

        while (true) {
            clearConsole();
            print("Machine coins: ");
            printCoins(machineCoins);
            Item item = selectItem();
            int[] coins = selectCoinsToPay();
        }

    }

    private static int[] selectCoinsToPay() {
        println("");
        println("-- Please select coins to pay with --");

        int[] coins = new int[8];
        double total = 0;

        int coinIndex = 0;
        for (CoinType coinType : CoinType.values()) {
            println("Current total: £" + total);
            println("How many " + coinType.getShortName() + "'s?");
            int noOfCoins = getParsedIntegerInput(INTEGER_REGEX);
            coins[coinIndex++] = noOfCoins;
            total += (double) (noOfCoins * coinType.getValue()) / 100;
        }

        return coins;
    }

    private static Item selectItem() {
        println("");
        println("-- Items --");
        String regex = "";
        int itemCount = 1;
        for (Item item : items) {
            println(itemCount + ". " + item.getName() + ": £" + df.format((double) item.getPrice() / 100));
            regex += itemCount++ + "|";
        }
        regex += itemCount + "|";

        println("");
        println("Which item would you like to purchase?");
        return items.get(getParsedIntegerInput(regex) - 1);
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

    private static VendingMachine loadExistingVendingMachine() {
        println("-- Please select a machine to load --");

        String regex = "";
        int machineCount = 1;
        for (MachineConfig machineConfig : machineConfigs) {
            print(machineCount + ". " + machineConfig.getName() + "- ");
            printCoins(machineConfig.getConfig());
            regex += machineCount++ + "|";
        }

        int[] config = machineConfigs.get(getParsedIntegerInput(regex) - 1).getConfig();
        machineCoins = config;
        return new VendingMachine(config);
    }

    private static void printCoins(int[] machineConfig) {
        println("|" + "£2: " + machineConfig[0]
                + "|" + "£1: " + machineConfig[1]
                + "|" + "50p: " + machineConfig[2]
                + "|" + "20p: " + machineConfig[3]
                + "|" + "10p: " + machineConfig[4]
                + "|" + "5p: " + machineConfig[5]
                + "|" + "2p: " + machineConfig[6]
                + "|" + "1p: " + machineConfig[7] + "|"
        );
    }

    private static VendingMachine getRandomVendingMachine() {
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

        machineCoins = coins;
        return new VendingMachine(coins);
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
