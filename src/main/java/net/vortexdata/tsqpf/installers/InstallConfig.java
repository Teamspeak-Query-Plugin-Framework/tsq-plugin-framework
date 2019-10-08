package net.vortexdata.tsqpf.installers;


import net.vortexdata.tsqpf.configs.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

/**
 * Creates, edits and manages config files.
 *
 * @author Sandro Kierner
 * @since 1.0.0
 */
public class InstallConfig {

    /**
     * Creates a new config file at the desired path.
     *
     * @param config          Config object that needs new config file
     * @return true if creation was successful
     */
    public boolean create(Config config) {
        // Purge if file remains
        File file = new File(config.getPath());
        if (file.exists()) {
            file.delete();
        }

        Properties prop = new Properties();
        prop.putAll(config.getDefaultValues());

        File configFile = new File(config.getPath());
        configFile.getParentFile().mkdirs();
        try {
            FileOutputStream fileOut = new FileOutputStream(configFile, false);

            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date date = new Date();

            prop.store(fileOut, "Generated at: " + dateFormat.format(date));
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

}
