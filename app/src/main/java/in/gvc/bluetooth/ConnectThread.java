package in.gvc.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

class ConnectThread extends Thread
{

    private ConnectThread mConnectThread;
    private final BluetoothSocket mmSocket;
    private ConnectedThread mConnectedThread;
    private final BluetoothDevice mmDevice;
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public ConnectThread(BluetoothDevice device)
    {
        BluetoothSocket tmp = null;
        mmDevice = device;
        try
        {

            tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);

                /*Method m = mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
                tmp = (BluetoothSocket) m.invoke(device, 1);*/
            Log.i("ARPIT", "BTTTTTTTTTTT  Address   " + mmDevice.getAddress());
            BluetoothDevice hxm = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mmDevice.getAddress());
//                Method m;
//                m = hxm.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
//                tmp = (BluetoothSocket)m.invoke(hxm, Integer.valueOf(1));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        mmSocket = tmp;
    }

    public void run()
    {

        try
        {
            mmSocket.connect();

            // Reset the ConnectThread because we're done
            /*synchronized (MainActivity.this) {
                mConnectThread = null;
            }*/

            // Cancel the thread that completed the connection
            if (mConnectThread != null)
            {
                mConnectThread.cancel();
                mConnectThread = null;
            }

            // Cancel any thread currently running a connection
            if (mConnectedThread != null)
            {
                mConnectedThread.cancel();
                mConnectedThread = null;
            }

            ConnectedThread mConnectedThread = new ConnectedThread(mmSocket);
            mConnectedThread.start();
            mConnectedThread.write("sent from arpit".getBytes());
        } catch (IOException connectException)
        {
            try
            {
                connectException.printStackTrace();
                mmSocket.close();
            } catch (IOException closeException)
            {
                closeException.printStackTrace();
            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public void cancel()
    {
        try
        {
            mmSocket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}


class ConnectedThread extends Thread
{
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private Handler mHandler;
    private final OutputStream mmOutStream;

    public ConnectedThread(BluetoothSocket socket)
    {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try
        {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run()
    {
        byte[] buffer = new byte[1024];
        int begin = 0;
        int bytes = 0;
        while (true)
        {
            try
            {
                bytes += mmInStream.read(buffer, bytes, buffer.length - bytes);
                for (int i = begin; i < bytes; i++)
                {
                    if (buffer[i] == "1010101100000001000000100000000100001110".getBytes()[0])
                    {
                        mHandler.obtainMessage(1, begin, i, buffer).sendToTarget();
                        begin = i + 1;
                        if (i == bytes - 1)
                        {
                            bytes = 0;
                            begin = 0;
                        }
                    }
                }
            } catch (IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }

    public void write(byte[] bytes)
    {
        try
        {
            mmOutStream.write(bytes);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void cancel()
    {
        try
        {
            mmSocket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
