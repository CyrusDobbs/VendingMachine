import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class TestHarness {

    private static int[] machineCoins;
    private static int[] customerCoins;

    private static Scanner in = new Scanner(System.in);

    public static final String ONE_TWO_REGEX = "1|2";
    public static final String INTEGER_REGEX = "\\d+";

    public static void main(String[] args) {
        println("-- Welcome to the Vending Machine interactive test-harness --");
        println("");
        println("-- Please select an option: --");
        println("1. Random Vending Machine");
        println("2. Configure Vending Machine");

        int selection = getParsedIntegerInput(ONE_TWO_REGEX);

        VendingMachine vendingMachine;
        if (selection == 1) {
            vendingMachine = getRandomVendingMachine();
        } else {
            vendingMachine = configureVendingMachine();
        }


    }

    private static VendingMachine configureVendingMachine() {
        println("");
        println("-- Please select how many of each coin to initialise the vending machine with --");

        int[] coins = new int[8];

        int coinIndex = 0;
        for (CoinType coinType : CoinType.values()) {
            println("How many " + coinType.getShortName() + "'s?");
            coins[coinIndex++] = getParsedIntegerInput(INTEGER_REGEX);
        }

        machineCoins = coins;
        return new VendingMachine(coins);
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

    private static int getParsedIntegerInput(String regex) {
        while (true) {
            try {
                return Integer.parseInt(in.next(regex));
            } catch (InputMismatchException e) {
                in.next();
                System.out.println("That is not an option.");
            }
        }
    }
}
