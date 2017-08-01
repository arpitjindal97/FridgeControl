package in.gvc.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

public class HeadListActivity extends AppCompatActivity
{
    ArrayList<String> list;

    HeadListAdapter adapter = null;
    ConnectThread mConnectThread;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.head_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Head List");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mConnectThread.cancel();
                onBackPressed();
            }
        });

        final BluetoothDevice bluetoothDevice = getIntent().getExtras().getParcelable("bluetoothDevice");
        mConnectThread = new ConnectThread(bluetoothDevice);
        mConnectThread.start();


        //Toast.makeText(this,"Bluetooth Name: "+bluetoothDevice.getName(),Toast.LENGTH_SHORT).show();

        ((TextView) findViewById(R.id.device_name)).setText(bluetoothDevice.getName());
        ((TextView) findViewById(R.id.device_address)).setText(bluetoothDevice.getAddress());
        if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED)
            ((ImageButton) findViewById(R.id.btn_pair)).setImageResource(
                    this.getResources().getIdentifier("in.gvc.bluetooth:mipmap/" + "unpair_icon",
                            null, null));

        ((ImageView) findViewById(R.id.imageView)).setImageResource(DeviceAdapter.getBluetoothID(
                bluetoothDevice.getBluetoothClass().getDeviceClass()));

        ((ImageView) findViewById(R.id.btn_pair)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED)
                {
                    try
                    {
                        Method method = bluetoothDevice.getClass().getMethod("removeBond", (Class[]) null);
                        method.invoke(bluetoothDevice, (Object[]) null);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                } else
                {
                    bluetoothDevice.createBond();
                }
            }
        });

        final RecyclerView radioGroup = (RecyclerView) findViewById(R.id.radioGroup);

        list = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            list.add("Head " + (i + 1));


        adapter = new HeadListAdapter(list);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        radioGroup.setLayoutManager(mLayoutManager);

        radioGroup.setItemAnimator(new DefaultItemAnimator());
        radioGroup.setAdapter(adapter);
        adapter.recyclerView = radioGroup;
        adapter.notifyDataSetChanged();

        ((Button) findViewById(R.id.prev_button)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });
        ((Button) findViewById(R.id.next_button)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putParcelable("bluetoothDevice", getIntent().getExtras().getParcelable("bluetoothDevice"));
                bundle.putInt("head_num", adapter.checked_pos+1);
                Intent mIntent = new Intent(HeadListActivity.this, CalibrationActivity.class);
                mIntent.putExtras(bundle);
                startActivity(mIntent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
