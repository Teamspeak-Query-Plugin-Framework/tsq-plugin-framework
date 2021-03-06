/*
 *
 *  Teamspeak Query Plugin Framework
 *
 *  Copyright (C) 2019 - 2020 VortexdataNET
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

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
 * @author Sandro Kierner (sandro@vortexdata.net)
 * @author Michael Wiesinger (michael@vortexdata.net)
 * @since 2.0.0
 * @version $Id: $Id
 */
public class UserManager {

    private static ArrayList<User> users;
    private Logger logger;

    /**
     * <p>Constructor for UserManager.</p>
     *
     * @param logger a {@link net.vortexdata.tsqpf.console.Logger} object.
     */
    public UserManager(Logger logger) {

        this.logger = logger;
    }

    /**
     * Returns a user if username and password are correct.
     *
     * @param username The users username / unique identifier
     * @param password The users password
     * @return A valid user
     * @throws net.vortexdata.tsqpf.exceptions.InvalidCredentialsException if any.
     */
    public User authenticate(String username, String password) throws InvalidCredentialsException {
        User user;
        try {
            user = getUser(username);
            if (user.getPassword().equals(getPasswordHash(password)))
                return user;
        } catch (UserNotFoundException e) {
            // Ignore and proceed with InvalidCredentialsException
        }
        throw new InvalidCredentialsException();
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
     * @throws net.vortexdata.tsqpf.exceptions.UserAlreadyExistingException if any.
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
     * @throws net.vortexdata.tsqpf.exceptions.UserNotFoundException if any.
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

    /**
     * <p>doesRootUserExist.</p>
     *
     * @return a boolean.
     */
    public boolean doesRootUserExist() {
        try {
            getUser("root");
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }

    /**
     * Generates a new root user if it does not exist already.
     *
     * @return Newly generated root password in plain text.
     */
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

    /**
     * Deletes the specified user.
     *
     * @param username Used to identify which user should be deleted.
     * @return true if user has been successfully deleted.
     */
    public boolean deleteUser(String username) {
        return deleteUser(username, false);
    }

    /**
     * Deletes the specified user.
     *
     * @param username Used to identify which user should be deleted.
     * @param forcedelete Specifies, if user should be removed even if its flagged not to be deleteable.
     * @return true if user has been successfully deleted.
     */
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

    /**
     * Reloads the user data file and loads them to local list.
     *
     * @return true if reload was performed successfully.
     */
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
