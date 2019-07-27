package net.vortexdata.tsManagementBot.installers;

import java.io.*;
import java.nio.file.*;
import java.text.*;
import java.util.*;

public class InstallConfig {

    public boolean create(String path, HashMap<String, String> defaultValues) {
        // Purge if file remains
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        Properties prop = new Properties();
        prop.putAll(defaultValues);

        File newFile = new File(path);
        try {
            FileOutputStream fileOut = new FileOutputStream(newFile);

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
