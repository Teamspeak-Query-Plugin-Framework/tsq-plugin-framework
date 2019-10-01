package net.vortexdata.tsqpf.authenticator;

import net.vortexdata.tsqpf.exceptions.InvalidCredentialsException;
import net.vortexdata.tsqpf.exceptions.UserAlreadyExistingException;
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

    /**
     * Returns a user if username and password are correct.
     *
     * @param username                      The users username / unique identifier
     * @param password                      The users password
     * @return                              A valid user
     */
    public User authenticate(String username, String password) throws InvalidCredentialsException {

        // Try to get and validate login request



        return new User(username, password, UserGroup.ROOT, new HashMap<>(), "Test");
    }

    /**
     * Removes a user from userdata file
     *
     * @param username      The users username / unique identifier
     * @return              true if successfully removed
     */
    public boolean removeUser(String username) {
        return false;
    }

    /**
     * Creates a new user and saves it to userdata file
     *
     * @param username      The users username / unique identifier
     * @param password      Plain text password
     * @param group         The users group
     * @return              true if successfully created
     */
    public boolean createUser(String username, String password, UserGroup group) throws UserAlreadyExistingException {
        return false;
    }

    /**
     * Hashes a plain text password
     *
     * @param plainTextPassword      A plain text password
     * @return                       A hashed password
     */
    private String getHashedPassword(String plainTextPassword, String salt) {
        return "";
    }

    private boolean editUser(String previousUsername, User newUser) {
        boolean success = false;
        logger.printDebug("Trying to override userdata...");
        BufferedWriter bw = null;
        BufferedReader br = null;

        try {
            bw = new BufferedWriter(new FileWriter("sys//users//userdata.tsqpfd"));
            br = new BufferedReader(new FileReader("sys//users//userdata.tsqpfd"));

            ArrayList<String> oldLines = new ArrayList<>();
            String line = "init";
            while (!line.isEmpty() && !line.equalsIgnoreCase("")) {
                line = br.readLine();
                if (!line.equalsIgnoreCase("")) {
                    oldLines.add(line);
                }
            }
            br.close();
            br = null;

            for (String serializedString : oldLines) {
                String[] split = serializedString.split(";");
                if (split[0].equalsIgnoreCase(previousUsername)) {

                    bw.write(newUser.serialize() + "\n");
                    success = true;

                } else {
                    bw.write(serializedString + "\n");
                }
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

        return success;

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

    private User getUserFromSerializedString(String serializedUser) {
        String[] args = serializedUser.split(";");

        HashMap<String, String> info = new HashMap<>();
        info.put("telephone", args[4]);
        info.put("country", args[6]);
        info.put("fullName", args[3]);
        info.put("address", args[5]);

        return new User(args[0], args[1], UserGroup.valueOf(args[2]), info, args[7]);
    }

    private String loadUserSerializedData(String username) {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader("sys//users//userdata.tsqpfd"));

            String line = "init";
            while (line != null && !line.isEmpty()) {
                line = br.readLine();
                if (line != null && !line.isEmpty()) {

                }
            }

        } catch (FileNotFoundException e) {
            logger.printDebug("Failed to open userdata file, dumping details: " + e.getMessage());
        } catch (IOException e) {
            logger.printDebug("Failed to read line from user data, dumping details: " + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.printError("Failed to close BufferedReader, dumping details: " + e.getMessage());
                }
            }
        }
    }

    private User getUser() {

    }

}
