package in.gvc.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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
                //showToast("Enabled");

                //showEnabled();
            }

        } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
        {
            mDeviceList = new ArrayList<BluetoothDevice>();

            //mProgressDlg.show();
            Toast.makeText(main_context,"Scanning Started",Toast.LENGTH_LONG).show();
            Log.i("ARPIT","Scanning Started ... ");
            main_context.scan_button.setText("Cancel Scanning");

        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
        {
            //mProgressDlg.dismiss();

            /*Intent newIntent = new Intent(BroadcastReceiver.this, DeviceListActivity.class);

            newIntent.putParcelableArrayListExtra("device.list", mDeviceList);

            startActivity(newIntent);*/

            Toast.makeText(context,"Scanning Finished",Toast.LENGTH_LONG).show();
            main_context.scan_button.setText("Start Scan");
            while(main_context.availableList.size()>0)
                main_context.availableList.remove(0);

            main_context.availableList.addAll(mDeviceList);
            main_context.deviceAdapterAvailable.notifyDataSetChanged();
            Log.i("ARPIT","Scanning Finished "+main_context.availableList.size()+" devices found");

        } else if (BluetoothDevice.ACTION_FOUND.equals(action))
        {
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            mDeviceList.add(device);
            Log.i("ARPIT","found device : "+device.getName());

            //Toast.makeText(main_context,"Found device " + device.getName(),Toast.LENGTH_LONG).show();
        }
    }
};