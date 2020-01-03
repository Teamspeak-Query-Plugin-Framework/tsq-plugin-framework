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

/**
 * Represents a users account
 *
 * @author Sandro Kierner (sandro@vortexdata.net)
 * @author Michael Wiesinger (michael@vortexdata.net)
 * @since 2.0.0
 */
public class User {

    public static final String CSV_SEPARATOR = ";";
    private UserGroup group;
    private String username;
    private String password;


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
