package net.vortexdata.tsqpf.modules.uuid;

import net.vortexdata.tsqpf.framework.FrameworkContainer;

import java.io.*;
import java.util.UUID;

/**
 * @author Sandro Kierner
 * @since 2.0.0
 */
public class UuidManager {

    private String path;
    private FrameworkContainer frameworkContainer;
    private String loadedUUID;

    public UuidManager(FrameworkContainer frameworkContainer) {
        this.frameworkContainer = frameworkContainer;
        path = "sys//uuid.tsqpfd";
    }

    public void init() {
        loadedUUID = loadUuid();
    }

    public String loadUuid() {
        String lUUID = "";

        try {

            File sysDir = new File(path.split("//")[0]);
            if (!sysDir.isDirectory())
                sysDir.mkdirs();

            BufferedReader br = new BufferedReader(new FileReader(path));
            lUUID = br.readLine();
            if (lUUID != null && !lUUID.isEmpty()) {
                return lUUID;
            } else {
                frameworkContainer.getFrameworkLogger().printWarn("Could not get framework UUID, generating new one...");
                createUuid(generateUuid());
            }
        } catch (FileNotFoundException e) {
            frameworkContainer.getFrameworkLogger().printWarn("Could not find framework UUID save file, generating new one...");
            createUuid(generateUuid());
        } catch (IOException e) {
            frameworkContainer.getFrameworkLogger().printWarn("Could not read or write to UUID save file, please check your operating systems permissions.");
        }

        return lUUID;
    }

    private static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    private boolean createUuid(String newUuid) {

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(path, false));
            bw.write(newUuid);
            bw.flush();
            bw.close();
            return true;
        } catch (IOException e) {
            frameworkContainer.getFrameworkLogger().printError("Failed to save newly generated UUID to system directory.");
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    frameworkContainer.getFrameworkLogger().printError("Failed to finally close UUID save stream. Memory leaks could appear, please restart the framework manually and check your framework instance for corruption and try to check your operating systems permissions.");
                }
            }
        }

        return false;
    }

    public String getLoadedUUID() {
        return loadedUUID;
    }

}
