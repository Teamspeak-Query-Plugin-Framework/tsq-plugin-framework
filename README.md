# Teamspeak Query Plugin Framework

A java-based plugin framework for Teamspeak 3 servers.

## 🏷️ Features
✅ Plugin API to add more features to your Teamspeak server<br/>
✅ Plugin cross-talk<br/>
✅ Reconnect strategies<br/>
✅ Easy plugin development<br/>
✅ Setup wizard for easy-to-use installation<br/>
✅ Detailed log files and console output<br/>

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
   
   ```
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
   
   ```
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
   
   ```
   screen -S session_name
   ```
   
   > ⚠️ **Important**: If you run the framework outside a screen session, it will terminate as soon as you disconnect from the terminal. Also, although it not really being an issue, it is recommended not to run the script as root user.
   
   Once you've created the session, it should automaticall be attached. Now run the launch script:
   
   ```
   ./launch.sh
   ```
   
   The framework will generate its directory structure and terminate after completion. Navigate to to the main properties file and adjust it to fit your servers prefferences. Once complete, run the script again (be sure you're runnning it using a screen session) and wait for the boot process to complete.
   
## 🔌 Installing Plugins

   Here's a list of all official TSQPF plugins developed by us. Contrary to community made plugins, these are verified and tested by VortexdataNET, meaning they are running stable and are safe to use.
   
   - RollingBanner (https://github.com/Vortexdata/tsqpf-plugin-rollingbanner)
   - WelcomeMessage (https://github.com/Vortexdata/tsqpf-plugin-welcomemessage)
   - PrivateChannel (https://github.com/Vortexdata/tsqpf-plugin-privatechannel)
   - Analytics (N/A)

## 🖥️ Using the Remote Shell 

[DESCRIPTION NEEDED]

##
