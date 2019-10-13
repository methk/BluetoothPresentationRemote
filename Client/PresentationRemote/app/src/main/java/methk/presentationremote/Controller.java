package methk.presentationremote;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class Controller extends AppCompatActivity {

    // Widgets
    private Button btnOn, btnOff, btnDis;

    // Bluetooth
    private String address = null;
    private ProgressDialog progress;
    private BluetoothAdapter myBluetooth = null;
    private BluetoothSocket btSocket = null;
    private boolean isBTConnected = false;
    static final UUID myUUID = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newInt = getIntent();
        address = newInt.getStringExtra(MainActivity.EXTRA_ADDRESS); // MAC address of the chosen device

        setContentView(R.layout.activity_controller);

        // Initialize widgets
        btnOn = findViewById(R.id.next_btn);
        btnOff = findViewById(R.id.prev_btn);
        btnDis = findViewById(R.id.disc_btn);

        new ConnectBT().execute(); // Connection class

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSlide();
            }
        });
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousSlide();
            }
        });
        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnect();
            }
        });
    }

    private void nextSlide() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(new byte[] {'r', 'i', 'g', 'h', 't'});
            } catch (IOException e) {
                toast("Error Sending Data");
            }
        }
    }

    private void previousSlide() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(new byte[] {'l', 'e', 'f', 't'});
            } catch (IOException e) {
                toast("Error Sending Data");
            }
        }
    }

    private void toast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    // If go back disconnect
    @Override
    public void onBackPressed() {
        disconnect();
        finish();
        return;
    }

    // Disconnection
    private void disconnect() {
        previousSlide();

        if (btSocket != null) { // If bluetooth socket is taken then disconnect
            try {
                btSocket.close(); // Close bluetooth connection
            } catch (IOException e) {
                toast("Error Closing Socket");
            }
        }

        finish();
    }



    private class ConnectBT extends AsyncTask<Void, Void, Void> { // UI thread

        private boolean connectionSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Controller.this, "Connecting...", "Please wait!");  // Connection loading dialog
        }

        @Override
        protected Void doInBackground(Void... devices) { // Connect with bluetooth socket
            try {
                if (btSocket == null || !isBTConnected) { // If socket is not taken or device not connected
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice device = myBluetooth.getRemoteDevice(address); // Connect to the chosen MAC address
                    btSocket = device.createRfcommSocketToServiceRecord(myUUID); // This connection is not secure (mitm attacks)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery(); // Discovery process is heavy
                    btSocket.connect();
                }
            } catch (IOException e) {
                Log.e("Exception", e.getMessage());
                connectionSuccess = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) { // After doInBackground
            super.onPostExecute(result);

            if (!connectionSuccess) {
                toast("Connection Failed. Try again.");
                finish();
            } else {
                toast("Connected.");
                isBTConnected = true;
            }

            progress.dismiss();
        }
    }
}
