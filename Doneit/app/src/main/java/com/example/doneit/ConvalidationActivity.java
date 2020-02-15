package com.example.doneit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doneit.convalidation.Convalidation;
import com.example.doneit.convalidation.ConvalidationService;
import com.example.doneit.convalidation.DeviceBluetoothAdapter;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.example.doneit.constants.Client.SHARED_LOGIN;

public class ConvalidationActivity extends AppCompatActivity {

    public static final String TAG = "SHISH";

    private class ServerBluetooth extends AsyncTask<Void, Void, Void> {
        private final BluetoothServerSocket mmServerSocket;
        String message = "";

        public ServerBluetooth() {
            print("ServerSocket has been created... smartphone is listening");
            BluetoothServerSocket tmp = null;
            try {
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Server", MY_UUID);
                Toast.makeText(ConvalidationActivity.this, "Listening", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            BluetoothSocket socket = null;
            while (true) {
                try {
                    print("Server socket is listening....");
                    socket = mmServerSocket.accept();
                    print("Connessione stablita!");
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }
                if (socket != null) {
                    readCode(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            return null;
        }


        private void readCode(BluetoothSocket socket) {
            try {

                byte[] buffer = new byte[1024];
                int bytes;
                InputStream inFromServer = socket.getInputStream();

                bytes = inFromServer.read(buffer);
                message = new String(buffer, 0, bytes);
                receivedKey = message;
                Log.d(TAG, "Message :: " + message);
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            giveUserResult();
        }
    }

    private class ConnectThread extends AsyncTask<Void, Void, Void> {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private final String validationKey;


        public ConnectThread(BluetoothDevice device, String validationKey) {
            print("Connecting to... " + device.toString() + " with name: " + device.getName());
            BluetoothSocket tmp = null;
            mmDevice = device;
            this.validationKey = validationKey;

            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            bluetoothAdapter.cancelDiscovery();

            try {

                print("Provo a connettermi");
                mmSocket.connect();
                print("Connessione ok!");
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return null;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            sendCode(mmSocket);
            return null;
        }

        public void sendCode(BluetoothSocket socket) {
            try {

                OutputStream outputStream = socket.getOutputStream();
                byte[] data = validationKey.getBytes();
                outputStream.write(data);

            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class ConvalidationAskingTask extends AsyncTask<Void, Void, Void> {


        public ConvalidationAskingTask(String token, Long id) {
        }

        ConvalidationService convalidationService = new ConvalidationService();
        private String key;

        @Override
        protected Void doInBackground(Void... voids) {
            print("getting information from Server");
            key = convalidationService.getConvalidationKey(token, todoId);
            print("KEY: " + key);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startClientSocket(currentBdDevice, key);
        }
    }

    private class ConvalidationSendingTask extends AsyncTask<Void, Void, Void> {
        private Convalidation convalidation;
        private ConvalidationService convalidationService = new ConvalidationService();
        private JSONObject result;

        public ConvalidationSendingTask(Convalidation c) {
            convalidation = c;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            print("Asking for convalidation");
            result = convalidationService.askForConvalidation(convalidation, token);
            print("Convalidation terminated!");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (result == null) {
                endConvalidation(false);
            } else {
                endConvalidation(true);
            }

        }
    }


    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter bluetoothAdapter;
    public static final int REQUEST_ENABLE_BT = 1;
    private String receivedKey;

    private Button btnServer;
    private Button btnClient;
    private TextView todoTitleTextView;

    private String todoTitle;
    private Long todoId;
    private boolean isOwner;
    private String username;
    private String token;

    private DeviceBluetoothAdapter deviceBluetoothAdapter;
    private List<BluetoothDevice> devicesList = new ArrayList<>();
    private BluetoothDevice currentBdDevice;

    private AlertDialog alert11;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                print("Discovery Started");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                print("Discovery Ended");
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                print(device.toString());
                addDevice(device);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convalidation);


        todoTitle = getIntent().getStringExtra("todoName");
        todoId = getIntent().getLongExtra("todoId", -1);
        isOwner = getIntent().getBooleanExtra("Owner", false);

        SharedPreferences shared = getSharedPreferences(SHARED_LOGIN, MODE_PRIVATE);
        username = shared.getString("username", "pippo");
        token = shared.getString("token", "shish");

        checkPermissions();
        bindElementsAndSetListeners();
        checkBluetoothStatusAndActive();

    }

    private void endConvalidation(boolean success) {
        if (!alert11.isShowing()) {
            alert11.show();
        }
        if (success) {
            alert11.setTitle("Convalidazione terminata!");
            alert11.setMessage("Complimenti! Hai guadagnato nuovi CFU. Grazie per usare DoneIt");

            //todo settare graficamente il todo come convalidato

        } else {
            alert11.setTitle("Errore! Riprovare");
            alert11.setMessage("Qualcosa è andato storto. Riprova a convalidare");
        }
    }

    private void addDevice(BluetoothDevice bluetoothDevice) {
        if (!devicesList.contains(bluetoothDevice) && bluetoothDevice.getName() != null) {
            devicesList.add(bluetoothDevice);
            deviceBluetoothAdapter.notifyDataSetChanged();
        }

    }

    private void bindElementsAndSetListeners() {


        todoTitleTextView = findViewById(R.id.todoTitle);
        btnServer = findViewById(R.id.btnServer);
        btnClient = findViewById(R.id.btnClient);

        todoTitleTextView.setText(todoTitle);

        if (isOwner) {
            btnClient.setVisibility(View.GONE);
        } else {
            btnServer.setVisibility(View.GONE);
        }

        btnServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDiscoverable(300);
            }
        });


        btnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectDevices();
            }
        });
    }

    private void detectDevices() {
        createDialog();
        print("Scanning");
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
        cleanDeviceList();
        boolean b = bluetoothAdapter.startDiscovery();
        print(b + "");
    }

    private void createDialog() {
        deviceBluetoothAdapter = new DeviceBluetoothAdapter
                (ConvalidationActivity.this, R.layout.list_item_device, devicesList);
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Seleziona dispositivo");
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bluetoothAdapter.cancelDiscovery();
            }
        });
        builderSingle.setAdapter(deviceBluetoothAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentBdDevice = deviceBluetoothAdapter.getItem(which);
                bluetoothAdapter.cancelDiscovery();
                getValidationKeyFromServer();
            }
        });

        builderSingle.show();
    }

    private void giveUserResult() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Ci siamo quasi!");
        builder1.setMessage("Contatto ricevuto! Ancora un momento...");
        builder1.setCancelable(true);
        alert11 = builder1.create();
        alert11.show();

        print("Ora invio la stringa al server");
        sendConvalidationKeyToServer();
    }

    private void sendConvalidationKeyToServer() {
        Convalidation convalidation = new Convalidation();
        convalidation.setKey(receivedKey);
        convalidation.setTodo(todoId);
        convalidation.setProponent(username);

        print("sendingTask creato");
        ConvalidationSendingTask convalidationSendingTask = new ConvalidationSendingTask(convalidation);
        convalidationSendingTask.execute();
    }

    private void getValidationKeyFromServer() {
        print("Getting validationKey from server...");

        ConvalidationAskingTask convalidationTask = new ConvalidationAskingTask(token, todoId);
        convalidationTask.execute();
    }

    private void cleanDeviceList() {
        devicesList.clear();
        deviceBluetoothAdapter.notifyDataSetChanged();
    }

    private void checkBluetoothStatusAndActive() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth non supportato", Toast.LENGTH_SHORT).show();
            finish();
        }
        //chiede di attivarlo
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

    private void findPairedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.v(TAG, deviceName);
            }
        }
    }

    private void checkPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        3);
            }
        } else {
            Log.d(TAG, "COARSE location si");
        }

    }

    private void makeDiscoverable(int length) {
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, length);
        startActivityForResult(discoverableIntent, length);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 1 || resultCode != RESULT_OK) {
            checkBluetoothStatusAndActive();
        }
        if (resultCode > RESULT_CANCELED) {
            print("Bluetooth discovery mode");
            startServerSocket();
        } else {
            print("No discovery");
        }

    }

    private void startServerSocket() {
        Toast.makeText(ConvalidationActivity.this, "Server Attivo... listening", Toast.LENGTH_SHORT).show();
        ServerBluetooth acceptThread = new ServerBluetooth();
        acceptThread.execute();
    }

    //Si connette al dispositivo del proprietario del TodoObject e manda il token ottenuto dal server
    private void startClientSocket(BluetoothDevice bd, String validationKey) {
        Toast.makeText(ConvalidationActivity.this, "Server Attivo... listening", Toast.LENGTH_SHORT).show();
        ConnectThread connectThread = new ConnectThread(bd, validationKey);
        connectThread.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(mReceiver);
    }


    public static void print(String text) {
        Log.d(TAG, text);
    }
}
