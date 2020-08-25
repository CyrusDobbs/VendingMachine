package testharness;

public class MachineConfig {

    private String name;
    private int[] config;

    public MachineConfig(String name, int[] config) {
        this.name = name;
        this.config = config;
    }

    public String getName() {
        return name;
    }

    public int[] getConfig() {
        return config;
    }
}
