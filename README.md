# Teamspeak Query Plugin Hook

A java-based plugin hook for Teamspeak 3 servers.

## Features
- Setup wizzard for easy-to-use installation
- Detailed log files and console output
- Can be configured to automaticall reconnect to query after connectivity problems

## Getting Started

### Prerequisites

#### Cross-Platform
In order to run this hook, you need a Java runtime of version 1.8 or newer.

#### Windows
No additional software required.

#### Linux
- **GNU Screen**:
   Insall 
   ```
   @echo off
   TITLE Teamspeak Query Plugin Hook
   java -jar tsq-plugin-hook.jar -setup -debug
   PAUSE
   ```

#### OSX
No additional software required.

### Download
- **Option 1 (Standalone Jar):**
   Download the latest release from [our website](https://projects.vortexdata.net/tsq-plugin-hook). This ensures you are running a stable release and avoid bugs carried over from phase-one development.
- **Option 2 (Compile own version):**
   Clone the latest stable version of the source code and run it trough our compiler. It should output a Jar file named "tsq-plugin-hook".
   
### Installation

- **Step 1**
   Copy "tsq-plugin-hook.jar" to a folder you want the directory structer to be generated in.
   
- **Step 2**
   Create a startup script. Here are some examples for different operating systems and some things you should consider.
   
   #### Windows
   
   Create a file ending with '.bat', copy and paste the code below, then run the script.
   
   ```
   @echo off
   TITLE Teamspeak Query Plugin Hook
   java -jar tsq-plugin-hook.jar -setup -debug
   PAUSE
   ```

   #### Linux
   
   Create a file ending with '.sh' (_touch launch.sh_), copy and paste the code below. If required, assign required permissions (chmod 755 launch.sh)
   
   ```
   @echo off
   TITLE Teamspeak Query Plugin Hook
   java -jar tsq-plugin-hook.jar -setup -debug
   PAUSE
   ```
