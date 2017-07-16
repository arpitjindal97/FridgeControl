package in.gvc.bluetooth;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.lang.reflect.Method;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.MyViewHolder>
{
    public Context context;
    private List<BluetoothDevice> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name, mac;
        public ImageButton pairButton;
        public ImageView imageView;

        public MyViewHolder(View view)
        {
            super(view);
            name = (TextView) view.findViewById(R.id.device_name);
            mac = (TextView) view.findViewById(R.id.device_address);
            pairButton = view.findViewById(R.id.btn_pair);
            imageView = (ImageView)view.findViewById(R.id.imageView);
        }
    }


    public DeviceAdapter(List<BluetoothDevice> moviesList)
    {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_item_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        final BluetoothDevice movie = moviesList.get(position);
        holder.name.setText(movie.getName());
        holder.mac.setText(movie.getAddress());
        if (movie.getBondState() == BluetoothDevice.BOND_BONDED)
            holder.pairButton.setImageResource(
                    context.getResources().getIdentifier("in.gvc.bluetooth:mipmap/" + "unpair_icon",
                            null, null));
        holder.pairButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (movie.getBondState() == BluetoothDevice.BOND_BONDED)
                {
                    try
                    {
                        Method method = movie.getClass().getMethod("removeBond", (Class[]) null);
                        method.invoke(movie, (Object[]) null);
                    } catch (Exception e)
                    {e.printStackTrace();
                    }

                    //holder.pairButton.setText("Pair");
                }
                else
                {
                    movie.createBond();
                }
                //Toast.makeText(holder.pairButton.getContext(), movie.getName() + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
        int id=0;

        id = getBluetoothID(movie.getBluetoothClass().getDeviceClass());

        Log.i("ARPIT",movie.getBluetoothClass().getDeviceClass() +"");
        holder.imageView.setImageResource(id);
    }
    @Override
    public int getItemCount()
    {
        return moviesList.size();
    }


    public int getBluetoothID(int something)
    {
        if(something == (BluetoothClass.Device.PHONE_SMART))
        {
            return context.getResources().getIdentifier("in.gvc.bluetooth:mipmap/" + "phone_smart_icon", null, null);
        }
        else if(something == (BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET))
        {
            return context.getResources().getIdentifier("in.gvc.bluetooth:mipmap/" + "headset_mic_icon", null, null);
        }
        else if(something == (BluetoothClass.Device.COMPUTER_LAPTOP))
        {
            return context.getResources().getIdentifier("in.gvc.bluetooth:mipmap/" + "laptop_icon", null, null);
        }
        else if(something == (BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER))
        {
            return context.getResources().getIdentifier("in.gvc.bluetooth:mipmap/" + "smart_tv", null, null);
        }
        else if(something == (BluetoothClass.Device.PHONE_CELLULAR))
        {
            return context.getResources().getIdentifier("in.gvc.bluetooth:mipmap/" + "phone_smart_icon", null, null);
        }
        else if(something == (BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE))
        {
            return context.getResources().getIdentifier("in.gvc.bluetooth:mipmap/" + "speaker_icon", null, null);
        }
        else if(something == (BluetoothClass.Device.WEARABLE_WRIST_WATCH))
        {
            return context.getResources().getIdentifier("in.gvc.bluetooth:mipmap/" + "wrist_watch_icon", null, null);
        }
        else if(something == 7936)
        {
            return context.getResources().getIdentifier("in.gvc.bluetooth:mipmap/" + "developer_board_icon", null, null);
        }
        else
        {
            return context.getResources().getIdentifier("in.gvc.bluetooth:mipmap/" + "other_device_icon", null, null);
        }
    }
}
