package net.vortexdata.tsqpf.authenticator;

import java.util.HashMap;

/**
 * Represents a users account
 *
 * @author Sandro Kierner, Michael Wiesinger
 * @since 2.0.0
 */
public class User {

    private UserGroup group;
    private String username;
    private String password;
    public static final String CSV_SEPARATOR = ";";


    public User(String username, String password, UserGroup group) {
        this.username = username;
        this.password = password;
        this.group = group;
    }

    public UserGroup getGroup() {
        return group;
    }

    public String getUsername() {
        return username;
    }

    public String serialize() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(username);
        buffer.append(CSV_SEPARATOR);
        buffer.append(password);
        buffer.append(CSV_SEPARATOR);
        buffer.append(group.toString());
        return buffer.toString();
    }

    public String getPassword() {
        return password;
    }

}
