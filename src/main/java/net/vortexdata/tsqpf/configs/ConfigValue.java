package net.vortexdata.tsqpf.configs;

public class ConfigValue {

    private CheckType type;
    private String value;
    private String key;

    public ConfigValue(String key, String value, CheckType type) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public CheckType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    public boolean check() {
        try {
            if (type == CheckType.CHAR) {
                value.charAt(0);
            } else if (type == CheckType.DOUBLE) {
                Double.parseDouble(value);
            } else if (type == CheckType.FLOAT) {
                Float.parseFloat(value);
            } else if (type == CheckType.INTEGER) {
                Integer.parseInt(value);
            } else if (type == CheckType.BOOLEAN) {
                Boolean.parseBoolean(value);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
