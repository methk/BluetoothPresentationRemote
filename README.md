# Bluetooth Presentation Remote
Android app and Python Bluetooth server to scroll PDF pages in Linux.

## Server
`./Server` folder contains a python file which listens to incoming RFCOMM connections and executes `xdotool` commands to simulate arrows keydown.

Run Bluetooth python server with the command: `sudo python3 server.py`.

This server has been developed and tested on Debian GNU/Linux 10 (Buster).

## Client
`./Client` folder contains an Android Studio project with a simple Android app that connects to the server and sends commands to scroll pages down or up.

![App screenshot](https://...)
