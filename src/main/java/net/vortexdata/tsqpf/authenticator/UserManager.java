package net.vortexdata.tsqpf.authenticator;

import net.vortexdata.tsqpf.console.Logger;
import net.vortexdata.tsqpf.exceptions.InvalidCredentialsException;
import net.vortexdata.tsqpf.exceptions.UserAlreadyExistingException;
import net.vortexdata.tsqpf.exceptions.UserNotFoundException;
import net.vortexdata.tsqpf.utils.HashUtils;
import net.vortexdata.tsqpf.utils.RandomString;

import java.io.*;
import java.util.ArrayList;

/**
 * Manages all users and database
 *
 * @author Sandro Kierner
 * @author Michael Wiesinger
 * @since 2.0.0
 */
public class UserManager {

    private static ArrayList<User> users;
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
        //FIXME Authorization should be done here!
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
    public boolean createUser(String username, String password, UserGroup group) throws UserAlreadyExistingException {
        String hashedPassword = getPasswordHash(password);

        boolean success = false;

        try {
            loadUserSerializedData(username);
            throw new UserAlreadyExistingException();
        } catch (UserNotFoundException e) {
            // continue
        }

        User newUser = new User(username, hashedPassword, group);
        saveUser(newUser);

        users.add(newUser);

        return true;
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

            bw = new BufferedWriter(new FileWriter("sys//users//userdata.tsqpfd", false));
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
            bw = new BufferedWriter(new FileWriter("sys//users//userdata.tsqpfd", true));
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
        String[] args = serializedUser.split(User.CSV_SEPARATOR);
        if (args.length != 3) return null;

        return new User(args[0], args[1], UserGroup.valueOf(args[2]));
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

        return HashUtils.sha_256(text);

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

        RandomString randomString = new RandomString(16);
        String generatedPassword = randomString.nextString();

        try {
            createUser("root", generatedPassword, UserGroup.ROOT);
            logger.printDebug("New root user successfully generated.");
        } catch (UserAlreadyExistingException e) {
            logger.printError("Root user already exists.");
        }

        System.out.println("====================[ IMPORTANT ]====================");
        System.out.println("> Please write down or save this information");
        System.out.println("> ");
        System.out.println("> Root user: root");
        System.out.println("> Root password: " + generatedPassword);
        System.out.println("> ");
        System.out.println("=====================================================");

        return generatedPassword;

    }

    public boolean deleteUser(String username) {
        return deleteUser(username, false);
    }

    public boolean deleteUser(String username, boolean forcedelete) {

        if (!forcedelete) {
            if (username.equalsIgnoreCase("ROOT"))
                return false;
        }

        boolean success = false;
        logger.printDebug("Trying to delete user...");
        BufferedWriter bw = null;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader("sys//users//userdata.tsqpfd"));

            ArrayList<String> oldLines = new ArrayList<>();
            String line = "init";
            while (line != null && !line.isEmpty() && !line.equalsIgnoreCase("")) {
                line = br.readLine();
                if (line != null && !line.equalsIgnoreCase("")) {
                    oldLines.add(line);
                }
            }

            try {
                br.close();
                br = null;
            } catch (Exception e) {
                logger.printError("Failed to close BufferedReader: " + e.getMessage());
            }

            bw = new BufferedWriter(new FileWriter("sys//users//userdata.tsqpfd", false));
            for (String serializedString : oldLines) {
                String[] split = serializedString.split(";");
                if (!split[0].equalsIgnoreCase(username)) {
                    bw.write(serializedString + "\n");
                } else {
                    success = true;
                }
            }

        } catch (IOException e) {
            logger.printError("Failed to open userdata file, dumping details: ");
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
