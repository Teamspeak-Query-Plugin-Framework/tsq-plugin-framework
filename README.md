# Teamspeak Query Plugin Hook

A java-based plugin hook for Teamspeak 3 servers.

## Features
- Setup wizzard for easy-to-use installation
- Detailed log files and console output
- Can be configured to automaticall reconnect to query after connectivity problems

## Getting Started

### Prerequisites

In order to run this hook, you need a Java runtime of version 1.8 or newer.

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
   
   - **On Windows**
   ```
   @echo off
   TITLE Teamspeak Query Plugin Hook
   java -jar tsq-plugin-hook.jar -setup -debug
   PAUSE
   ```
