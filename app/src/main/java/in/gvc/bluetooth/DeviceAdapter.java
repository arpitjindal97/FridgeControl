package in.gvc.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.MyViewHolder>
{

    private List<BluetoothDevice> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name, mac;
        public Button pairButton;

        public MyViewHolder(View view)
        {
            super(view);
            name = (TextView) view.findViewById(R.id.device_name);
            mac = (TextView) view.findViewById(R.id.device_address);
            pairButton = (Button) view.findViewById(R.id.btn_pair);
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
            holder.pairButton.setText("Unpair");
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

                    holder.pairButton.setText("Pair");
                }
                else
                {
                    movie.createBond();
                }
                //Toast.makeText(holder.pairButton.getContext(), movie.getName() + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return moviesList.size();
    }
}
