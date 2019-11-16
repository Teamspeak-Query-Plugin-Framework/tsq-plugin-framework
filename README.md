# Teamspeak Query Plugin Framework | Mint

[![Build Status](https://travis-ci.org/Vortexdata/tsq-plugin-framework.svg?branch=v2.0)](https://travis-ci.org/Vortexdata/tsq-plugin-framework)
[![Issues](https://img.shields.io/github/issues/Vortexdata/tsq-plugin-framework?label=Issues)](https://github.com/Vortexdata/tsq-plugin-framework/issues)
[![Twitter](https://img.shields.io/twitter/url?color=1DA1F2&label=Twitter&logo=Twitter&logoColor=1DA1F2&style=flat-square&url=https%3A%2F%2Ftwitter.com%2FVortexdataNET)](https://twitter.com/VortexdataNET)


A java-based plugin framework for Teamspeak 3 servers.

![TSQPF Logo](https://i.imgur.com/HgMc6NV.png)

## 🏷️ Features
✅ Plugin API to add more features to your Teamspeak server<br/>
✅ Plugin cross-talk<br/>
✅ Plugin config system<br/>
✅ Reconnect strategies<br/>
✅ Easy plugin development<br/>
✅ Detailed log files and console output<br/>
✅ Heartbeat socket with status information<br/>

## 🚀 Getting Started

### 📦 Prerequisites

#### Cross-Platform
- Java 8 or newer
- Teamspeak 3/5 server with query access

#### Windows
No additional software required.

#### Linux
- **GNU Screen**

   Install using commands below
   
   ```bash
   apt-get install screen
   ```

#### OSX
No additional software required.

### 📥 Download

   Download the latest stable release [here](https://github.com/Vortexdata/tsq-plugin-framework/releases) and extract "net.vortexdata.tsqpf-X.X.X-jar-with-dependencies.jar" from the "dist" folder.
   
### 🛠️ Installation

   Copy "tsq-plugin-framework.jar" to a folder you want the directory structure to get generated in. Then create a startup script. Here are some examples for different operating systems.
   
   #### Windows
   
   Create a file ending with '.bat', copy and paste the code below, then run the script.
   
   ```batch
   @echo off
   TITLE Teamspeak Query Plugin Framework
   java -jar tsq-plugin-framework.jar -debug
   PAUSE
   ```

   #### Linux
   
   Create a file ending with '.sh' (_touch launch.sh_), copy and paste the code below. If required, assign required permissions (chmod 755 launch.sh).
   
   ```
   java -jar tsq-plugin-framework.jar -debug
   ```
   
   Now, create a new screen session:
   
   ```bash
   screen -S session_name
   ```
   
   > ⚠️ **Important**: If you run the framework outside a screen session, it will terminate as soon as you disconnect from the terminal. Also, although it not really being an issue, it is recommended not to run the script as root user.
   
   Once you've created the session, it should automaticall be attached. Now run the launch script:
   
   ```bash
   ./launch.sh
   ```
   
   The framework will generate its directory structure and terminate after completion. Navigate to to the main properties file and adjust it to fit your servers prefferences. Once complete, run the script again (be sure you're runnning it using a screen session) and wait for the boot process to complete.
   
### 🛠️ Start Parameters

There are a number of start parameters that are available, here's a list of them:

```-debug```: Set console logging to debug level (Does not affect file logging in any way!).<br>
```-reset-root```: Resets the auto-generated root user and prints the new credentials to console.

The default Java parameters are of course also supported. You can find the documentation [here](https://docs.oracle.com/javase/7/docs/technotes/tools/windows/java.html)
   
## 🔌 Installing Plugins

   Here's a list of all official TSQPF plugins developed by us. Contrary to community made plugins, these are verified and tested by VortexdataNET, meaning they are running stable and are safe to use.
   
   - AntiAFK (https://github.com/Teamspeak-Query-Plugin-Framework/tsqpf-plugin-antiafk)
   - DynamicChannel (https://github.com/Teamspeak-Query-Plugin-Framework/tsqpf-plugin-dynamicchannel)
   - RollingBanner (https://github.com/Teamspeak-Query-Plugin-Framework/tsqpf-plugin-rollingbanner)
   - WelcomeMessage (https://github.com/Teamspeak-Query-Plugin-Framework/tsqpf-plugin-welcomemessage)
   - PrivateChannel (https://github.com/Teamspeak-Query-Plugin-Framework/tsqpf-plugin-privatechannel)
   - Analytics (N/A)

## 💡 Developing Plugins

It is really ease to develop plugins for the framework. All you need is a version of the TSQPF api and you need to follow the plugin structure conventions. You can read more about how plugins are structured [in the wiki](https://github.com/Vortexdata/tsq-plugin-framework/wiki/Plugin-Structure)

The api comes with various tools allowing you to easily register chat or console commands, create a custom plugin config and many more. Again, this is all documented in the wiki.

## 🖥️ Using the Remote Shell 

In order to get started with using the integrated remote shell, you first of all need to make sure that its port is free and not used by any other service. If that is the case, then you need to download the latest version of the remote shell client which can be found [here](https://github.com/Vortexdata/tsqpf-remote-shell-client/releases).

### Connecting to a Framework

You basically just have to run it and enter your TSQPF ip address, port, a user name and its password. If that was successfully done, you should now be connected to the terminal. From this point on you can use all commands just like if you were directly using the terminal.

## 🔨 Contributing

If you want to see a new feature in a future update, you can always create a new feature request. Unfortunately, we currently do not support any third-party pull requests.

## 📖 Contributors

**TAXSET**: Project Lead, Framework Core<br>
**VarChar42**: Framework Core, Plugin Manager<br>
**profiluefter**: Remote Dashboard, Security Check<br>
**TheHolyWaffle**: Teamspeak Java API<br>


## ❤️ Supporting us

Your donation allows us to continue creating more content just like this. You can send credit card or bank donations using the secure PayPal transaction system: [URL_NEEDED]
