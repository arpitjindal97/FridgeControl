package in.gvc.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BroadcastReceiver extends android.content.BroadcastReceiver
{
    List<BluetoothDevice> mDeviceList;
    MainActivity main_context;

    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();

        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
        {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            if (state == BluetoothAdapter.STATE_ON)
            {
            }

        } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
        {
            mDeviceList = new ArrayList<BluetoothDevice>();

            Log.i("ARPIT","Scanning Started ... ");
            main_context.scan_button.setText("Cancel Scanning");

            while(main_context.availableList.size()>0)
                main_context.availableList.remove(0);


        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
        {
            main_context.scan_button.setText("Start Scan");


            main_context.deviceAdapterAvailable.notifyDataSetChanged();

        } else if (BluetoothDevice.ACTION_FOUND.equals(action))
        {
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if(device == null)
                return;

            if (device.getBondState() == BluetoothDevice.BOND_BONDED )
                return;

            if(main_context.availableList.contains(device))
                return;

            main_context.availableList.add(device);

            main_context.deviceAdapterAvailable.notifyDataSetChanged();

        }

        else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
            final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

            if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {

                //Toast.makeText(context,"Paired",Toast.LENGTH_SHORT).show();
                main_context.updatePairedList();
                main_context.updateAvailableList();

            } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){

                main_context.updatePairedList();
                main_context.updateAvailableList();
            }else if (state == BluetoothDevice.BOND_BONDING && prevState == BluetoothDevice.BOND_NONE){

                //main_context.updatePairedList();
                //main_context.updateAvailableList();
            }

        }

    }
};