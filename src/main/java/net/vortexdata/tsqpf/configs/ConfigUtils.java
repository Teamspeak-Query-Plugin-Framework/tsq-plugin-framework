package net.vortexdata.tsqpf.configs;

import java.util.*;

/**
 * Util class for configs
 *
 * @author Sandro Kierner
 * @since 2.1.0
 * @version $Id: $Id
 */
public class ConfigUtils {

    public static HashMap<String, String> getHashmapFromArray(ArrayList<ConfigValue> configValues) {
        HashMap<String, String> mappedValues = new HashMap<>();
        for (ConfigValue value : configValues) {
            mappedValues.put(value.getKey(), value.getValue());
        }
        return mappedValues;
    }

    public static ArrayList<ConfigValue> getArrayFromHashmap(ArrayList<ConfigValue> defaultVaules, HashMap<String, String> loadedValues) {
        ArrayList<ConfigValue> parsedValues = new ArrayList<>();
        for (ConfigValue value : defaultVaules) {
            for (String key : loadedValues.keySet()) {
                if (value.getKey().equalsIgnoreCase(key)) {
                    parsedValues.add(new ConfigValue(
                        value.getKey(), loadedValues.get(key), value.getType()
                    ));
                }
            }
        }
        return parsedValues;
    }

}
