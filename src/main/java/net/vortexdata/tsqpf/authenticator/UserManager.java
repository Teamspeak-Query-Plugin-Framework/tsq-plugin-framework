package net.vortexdata.tsqpf.authenticator;

import net.vortexdata.tsqpf.modules.PluginLogger;
import sun.awt.image.BufferedImageDevice;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UserManager {

    private ArrayList<User> users;
    private PluginLogger logger;

    public UserManager(PluginLogger logger) {
        this.logger = logger;
    }

    public User authenticate(String username, String password) {

        // Try to get and validate login request

        return new User(username, password, UserGroup.ROOT, new HashMap<>());
    }

    public boolean removeUser() {
        return false;
    }

    public boolean createUser(String username, String password, UserGroup group) {
        return false;
    }

    private String getHashedPassword() {
        return "";
    }

    private boolean editUser(String previousUsername, User newUser) {
        boolean success;
        logger.printDebug("Trying to override userdata...");
        BufferedWriter bw = null;
        BufferedReader br = null;

        try {
            bw = new BufferedWriter(new FileWriter("sys//users//userdata.tsqpfd"));
            br = new BufferedReader(new FileReader("sys//users//userdata.tsqpfd"));

            while (br.readLine() != null && !br.readLine().isEmpty()) {
                String serializedUser = br.readLine();
            }

        } catch (IOException e) {
            logger.printError("Failed to open userdata file, dumping details: ");
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    logger.printError("Failed to close BufferedWriter, dumping details: " + e.getMessage());
                }
            }

            if (bw != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.printError("Failed to close BufferedReader, dumping details: " + e.getMessage());
                }
            }
        }

    }

    private boolean saveUser(User user) {
        boolean success = false;
        logger.printDebug("Trying to save userdata...");
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("sys//users//userdata.tsqpfd"));
            bw.write(user.serialize() + "\n");
            logger.printDebug("User data saved.");
            success = true;
        } catch (IOException e) {
            logger.printError("Could not save user data, dumping details: " + e.getMessage());
            success = false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    logger.printError("Failed to close BufferedWriter, dumping details: " + e.getMessage());
                }
            }
        }

        return success;
    }

    private User getUser(String serializedUser) {

        String[] args = serializedUser.split(";");

        HashMap<String, String> info = new HashMap<>();
        info.put("telephone", args[4]);
        info.put("country", args[6]);
        info.put("fullName", args[3]);
        info.put("address", args[5]);



        return new User(args[0], args[1], UserGroup.valueOf(args[2]), info);
    }

}
