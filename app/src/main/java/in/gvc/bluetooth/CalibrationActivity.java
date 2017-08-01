package in.gvc.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CalibrationActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calibration_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

       BluetoothDevice bluetoothDevice = getIntent().getExtras().getParcelable("bluetoothDevice");
       int head_num = getIntent().getExtras().getInt("head_num");

        toolbar.setTitle("Head "+head_num);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

    }
}
