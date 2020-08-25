package testharness;

public class TrackerConfig {

    private String name;
    private int[] config;

    public TrackerConfig(String name, int[] config) {
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
