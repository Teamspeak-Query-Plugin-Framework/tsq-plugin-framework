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
        Object cache;
        try {
            if (type == CheckType.CHAR) {
                cache = value.charAt(0);
            } else if (type == CheckType.DOUBLE) {
                cache = Double.parseDouble(value);
            } else if (type == CheckType.FLOAT) {
                cache = Float.parseFloat(value);
            } else if (type == CheckType.INTEGER) {
                cache = Integer.parseInt(value);
            } else if (type == CheckType.BOOLEAN) {
                if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false"))
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
