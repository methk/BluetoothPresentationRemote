# Bluetooth Presentation Remote
Android app and Python Bluetooth server to scroll PDF pages in Linux.

## Server
`./Server` folder contains a python file which listens to incoming RFCOMM connections and executes `xdotool` commands to simulate arrows keydown.

Run Bluetooth python server with the command: `sudo python3 server.py`.

This server has been developed and tested on Debian GNU/Linux 10 (Buster). Commands work on PDF viewer in browsers (tested on Firefox, Google Chrome and Chromium), but don't work on native Linux PDF viewer.

## Client
`./Client` folder contains an Android Studio project with a simple Android app that connects to the server and sends commands to scroll pages down or up.

<img src="https://github.com/methk/BluetoothPresentationRemote/blob/master/res/screenshot.jpg" width="20%">
