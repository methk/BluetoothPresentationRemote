import os
from bluetooth import *

abspath = os.path.abspath(__file__)
dname = os.path.dirname(abspath)
os.chdir(dname)

server_socket = BluetoothSocket(RFCOMM)
server_socket.bind(("", PORT_ANY))
server_socket.listen(1)

port = server_socket.getsockname()[1]
UUID = "94f39d29-7d6d-437d-973b-fba39e49d4ee"

advertise_service(server_socket, "bluetoothServer",
                  service_id = UUID,
                  service_classes = [UUID, SERIAL_PORT_CLASS],
                  profiles = [SERIAL_PORT_PROFILE]
)

print("Listening...")

while True:
    client_socket, client_info = server_socket.accept()
    print("Connected device: %s" % client_info[0])
    try:
        while True:
            command = client_socket.recv(1024).decode("utf-8")
            if len(command) == 0:
                break

            print("Command: %s" % command)
            if command == "right":
                os.system('xdotool key Right Return')
            elif command == "left":
                os.system('xdotool key Left Return')
            else:
                print("Command not found.")
    except IOError:
        pass

    client_socket.close()
    print("Connection closed.")

server_socket.close()
