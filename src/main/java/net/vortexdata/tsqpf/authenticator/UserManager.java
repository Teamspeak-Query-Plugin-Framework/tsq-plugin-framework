package net.vortexdata.tsqpf.authenticator;

import net.vortexdata.tsqpf.console.Logger;
import net.vortexdata.tsqpf.exceptions.InvalidCredentialsException;
import net.vortexdata.tsqpf.exceptions.UserAlreadyExistingException;
import net.vortexdata.tsqpf.exceptions.UserNotFoundException;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Manages all users and database
 *
 * @author Sandro Kierner
 * @since 2.0.0
 */
public class UserManager {

    private ArrayList<User> users;
    private Logger logger;

    public UserManager(Logger logger) {
        this.logger = logger;
    }

    /**
     * Returns a user if username and password are correct.
     *
     * @param username The users username / unique identifier
     * @param password The users password
     * @return A valid user
     */
    public User authenticate(String username, String password) throws InvalidCredentialsException {

        User user;
        try {
            user = getUser(username);
        } catch (UserNotFoundException e) {
            throw new InvalidCredentialsException();
        }

        return user;
    }

    /**
     * Removes a user from userdata file
     *
     * @param username The users username / unique identifier
     * @return true if successfully removed
     */
    public boolean removeUser(String username) {
        return false;
    }

    /**
     * Creates a new user and saves it to userdata file
     *
     * @param username The users username / unique identifier
     * @param password Plain text password
     * @param group    The users group
     * @return true if successfully created
     */
    public boolean createUser(String username, String password, UserGroup group, HashMap<String, String> info) throws UserAlreadyExistingException {
        String hashedPassword = getPasswordHash(password);

        boolean success = false;

        User newUser = getUserFromSerializedString(username + ";" + password + ";" + group.toString() + ";" + info.get("fullName") + ";" + info.get("telephone") + ";" + info.get("address") + ";" + info.get("country") + ";");
        saveUser(newUser);

        users.add(newUser);

        return false;
    }

    /**
     * Edit a user entry in userdata
     *
     * @param previousUsername The previous username before any changes where done
     * @param newUser          New user object overriding old user data
     * @return true if user was edited successfully.
     */
    private boolean editUser(String previousUsername, User newUser) {
        boolean success = false;
        logger.printDebug("Trying to override userdata...");
        BufferedWriter bw = null;
        BufferedReader br = null;

        try {
            bw = new BufferedWriter(new FileWriter("sys//users//userdata.tsqpfd", false));
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

        reloadUsers();

        return success;

    }

    /**
     * Saves a new user. DO NOT USE TWO TIMES WITH SAME USER!!
     *
     * @param user Saves a user to user data
     * @return true if saved successfully
     */
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

        reloadUsers();

        return success;
    }

    /**
     * Returns a user object from a serialized user string
     *
     * @param serializedUser Serialized user string
     * @return User object from serialized string
     */
    private User getUserFromSerializedString(String serializedUser) {
        String[] args = serializedUser.split(";");

        HashMap<String, String> info = new HashMap<>();
        info.put("telephone", args[4]);
        info.put("country", args[6]);
        info.put("fullName", args[3]);
        info.put("address", args[5]);

        return new User(args[0], args[1], UserGroup.valueOf(args[2]), info);
    }

    /**
     * Returns serialized save data from user data
     *
     * @param username Username required to find entry
     * @return Serialized data from user data
     */
    private String loadUserSerializedData(String username) throws UserNotFoundException {
        String serializedString = "default";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader("sys//users//userdata.tsqpfd"));

            String line = "init";
            while (line != null && !line.isEmpty()) {
                line = br.readLine();
                if (line != null && !line.isEmpty()) {

                    String[] args = line.split(";");
                    if (args[0].equalsIgnoreCase(username)) {
                        serializedString = line;
                        break;
                    }

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

        if (serializedString.equals("default"))
            throw new UserNotFoundException();

        return serializedString;
    }

    /**
     * Returns a user object from user data identified by only the username
     *
     * @param username Username required to find entry
     * @return User object from database
     */
    public User getUser(String username) throws UserNotFoundException {
        return getUserFromSerializedString(loadUserSerializedData(username));
    }


    /**
     * Hashes a plain text password
     *
     * @param text A plain text password
     * @return A hashed password
     */
    public String getPasswordHash(String text) {

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encodedString = digest.digest(
                text.getBytes(StandardCharsets.UTF_8));

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < encodedString.length; i++) {
            String hex = Integer.toHexString(0xff & encodedString[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();

    }

    public boolean doesRootUserExist() {
        try {
            getUser("root");
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }

    public String generateRootUser() {
        byte[] array = new byte[10];
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));

        try {
            HashMap<String, String> info = new HashMap<>();
            info.put("fullName", "Root User");
            info.put("telephone", "N/A");
            info.put("address", "N/A");
            info.put("country", "N/A");
            createUser("root", getPasswordHash("testpassword"), UserGroup.ROOT, info);
            logger.printDebug("New root user successfully generated.");
        } catch (UserAlreadyExistingException e) {
            logger.printError("Root user already exists.");
        }

        return generatedString;

    }

    public boolean reloadUsers() {

        String currentLine = "init";
        users = null;
        users = new ArrayList<>();

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader("sys//users//userdata.tsqpfd"));
            while (currentLine != null && !currentLine.isEmpty()) {
                currentLine = br.readLine();
                if (currentLine != null && !currentLine.isEmpty())
                    users.add(getUserFromSerializedString(currentLine));
            }
        } catch (FileNotFoundException e) {
            logger.printError("Could not find user data, creating new file...");
            File databaseFile = new File("sys//users//userdata.tsqpfd");
            databaseFile.getParentFile().mkdirs();
            try {
                databaseFile.createNewFile();
            } catch (IOException ex) {
                logger.printError("Failed to create user data file. Please check your systems permissions.");
            }
        } catch (IOException e) {
            logger.printError("Failed to fetch line from user data.");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.printError("Failed to close BufferedReader after loading users from database.");
                }
            }
        }

        return true;
    }

}
