package in.gvc.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.MyViewHolder> {

private List<BluetoothDevice> moviesList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView name,mac;

    public MyViewHolder(View view) {
        super(view);
        name = (TextView) view.findViewById(R.id.device_name);
        mac = (TextView) view.findViewById(R.id.device_address);
    }
}


    public DeviceAdapter(List<BluetoothDevice> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_item_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BluetoothDevice movie = moviesList.get(position);
        holder.name.setText(movie.getName());
        holder.mac.setText(movie.getAddress());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
