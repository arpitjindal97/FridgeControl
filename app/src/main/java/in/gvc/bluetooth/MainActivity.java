package in.gvc.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
{


    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    RecyclerView recyclerViewPaired;
    ArrayList<BluetoothDevice> pairedList = new ArrayList<BluetoothDevice>();
    DeviceAdapter deviveAdapter = new DeviceAdapter(pairedList);


    RecyclerView recyclerViewAvailable;
    ArrayList<BluetoothDevice> availableList = new ArrayList<BluetoothDevice>();
    DeviceAdapter deviceAdapterAvailable = new DeviceAdapter(availableList);


    in.gvc.bluetooth.BroadcastReceiver broadcastReceiver = new BroadcastReceiver();

    public Button scan_button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        recyclerViewPaired = findViewById(R.id.paired_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewPaired.setLayoutManager(mLayoutManager);
        recyclerViewPaired.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPaired.setAdapter(deviveAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewPaired.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerViewPaired.addItemDecoration(dividerItemDecoration);


        recyclerViewAvailable = findViewById(R.id.available_list);
        recyclerViewAvailable.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAvailable.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAvailable.setAdapter(deviceAdapterAvailable);
        dividerItemDecoration = new DividerItemDecoration(recyclerViewAvailable.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerViewAvailable.addItemDecoration(dividerItemDecoration);


        broadcastReceiver.main_context = this;

        final Switch switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                toggleBluetooth(switch1);
            }
        });



        scan_button = findViewById(R.id.scan_button);
        scan_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                checkBluetoothPermissions();
                if(scan_button.getText().equals("Start Scan"))
                    mBluetoothAdapter.startDiscovery();
                else
                    mBluetoothAdapter.cancelDiscovery();
            }
        });

        if (mBluetoothAdapter.isEnabled())
        {
            makeDiscoverable();
            onActivityResult(1000, 0, null);
        } else
        {
            /*RelativeLayout frameLayout = findViewById(R.id.content_main);
            frameLayout.removeAllViews();

            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View v = inflater.inflate(R.layout.off_bluetooth, null);

            */
            findViewById(R.id.off_bluetooth).setVisibility(View.VISIBLE);
            findViewById(R.id.on_bluetooth).setVisibility(View.INVISIBLE);
        }


        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

        registerReceiver(broadcastReceiver, filter);

    }

    public void send_on(View view)
    {
        String str = "on";
        try
        {
            write(str);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void send_off(View view)
    {
        String str = "off";
        try
        {
            write(str);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private OutputStream outputStream;
    private InputStream inStream;

    private void init() throws IOException
    {
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null)
        {
            if (blueAdapter.isEnabled())
            {
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();

                if (bondedDevices.size() > 0)
                {
                    Object[] devices = (Object[]) bondedDevices.toArray();
                    BluetoothDevice device = (BluetoothDevice) devices[0];
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inStream = socket.getInputStream();
                }

                Log.i("ARPIT", "No appropriate paired devices.");
            } else
            {
                Log.i("ARPIT", "Bluetooth is disabled.");
            }
        }
    }

    public void write(String s) throws IOException
    {
        outputStream.write(s.getBytes());
    }

    public void run()
    {
        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytes = 0;
        int b = BUFFER_SIZE;

        while (true)
        {
            try
            {
                bytes = inStream.read(buffer, bytes, BUFFER_SIZE - bytes);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void toggleBluetooth(Switch switch1)
    {
        if (mBluetoothAdapter.isEnabled())
        {
            mBluetoothAdapter.disable();

            switch1.setText("Bluetooth is OFF");
            //findViewById(R.id.device_name).setVisibility(View.INVISIBLE);

            while (pairedList.size() > 0)
                pairedList.remove(0);
            deviveAdapter.notifyDataSetChanged();


            findViewById(R.id.off_bluetooth).setVisibility(View.VISIBLE);
            findViewById(R.id.on_bluetooth).setVisibility(View.INVISIBLE);

        } else
        {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            startActivityForResult(intent, 1000);

            //findViewById(R.id.device_name).setVisibility(View.VISIBLE);

            makeDiscoverable();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1000)
        {
            /*RelativeLayout frameLayout = findViewById(R.id.content_main);
            frameLayout.removeAllViews();

            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View v = inflater.inflate(R.layout.content_main, null);

            frameLayout.addView(v);*/

            findViewById(R.id.off_bluetooth).setVisibility(View.INVISIBLE);
            findViewById(R.id.on_bluetooth).setVisibility(View.VISIBLE);

            //recyclerViewPaired.setVisibility(View.VISIBLE);
            ((Switch) findViewById(R.id.switch1)).setChecked(true);
            ((Switch) findViewById(R.id.switch1)).setText("Bluetooth is ON");
            ((TextView) findViewById(R.id.device_name)).setText("Your Device (" + mBluetoothAdapter.getName()
                    + ") is currently visible to nearby devices.");

            updatePairedList();


        }
    }
    void updatePairedList()
    {
        List<BluetoothDevice> pairedDevices = new ArrayList<>(mBluetoothAdapter.getBondedDevices());



            while (pairedList.size() > 0)
                pairedList.remove(0);

            pairedList.addAll(pairedDevices);

            Log.i("ARPIT", "paired devices size : " + pairedList.size());
            deviveAdapter.notifyDataSetChanged();

    }
    void updateAvailableList()
    {
        for(int i=0;i<availableList.size();i++)
        {
            BluetoothDevice device = availableList.get(i);
            if(device.getBondState() == BluetoothDevice.BOND_BONDED)
                availableList.remove(i--);
        }
        deviceAdapterAvailable.notifyDataSetChanged();
    }

    @Override
    public void onPause()
    {
        if (mBluetoothAdapter != null)
        {
            if (mBluetoothAdapter.isDiscovering())
            {
                mBluetoothAdapter.cancelDiscovery();
            }
        }

        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(broadcastReceiver);

        super.onDestroy();
    }

    public void checkBluetoothPermissions()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int permissionCheck = 0;

            permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");

            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0)
            {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        } else
        {
            Log.d("ARPIT", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    void makeDiscoverable()
    {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 180);
        startActivity(discoverableIntent);
    }

    public void toggleScan(View view)
    {
        scan_button = findViewById(R.id.scan_button);
        checkBluetoothPermissions();
        if(scan_button.getText().equals("Start Scan"))
            mBluetoothAdapter.startDiscovery();
        else
            mBluetoothAdapter.cancelDiscovery();
    }
}
