package in.gvc.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HeadListAdapter extends RecyclerView.Adapter<HeadListAdapter.MyViewHolder>
{
    public RecyclerView recyclerView;
    public int checked_pos = -1;
    private List<String> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name;
        public RadioButton radioBut;
        public LinearLayout rootView;

        public MyViewHolder(final View view)
        {
            super(view);
            name = (TextView) view.findViewById(R.id.textCode);
            radioBut = (RadioButton) ((LinearLayout) view).getChildAt(0);
            rootView = (LinearLayout) view;
        }
    }

    public String getItem(int pos)
    {
        return moviesList.get(pos);
    }


    public HeadListAdapter(List<String> moviesList)
    {
        this.moviesList = moviesList;

    }

    @Override
    public HeadListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.radio_button, parent, false);

        return new HeadListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final String movie = moviesList.get(position);

        holder.name.setText(movie);
        holder.radioBut.setChecked(false);
        holder.radioBut.setClickable(false);

        if (checked_pos != -1 && checked_pos == position)
            holder.radioBut.setChecked(true);

        holder.rootView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view1)
            {
                Toast.makeText(view1.getContext(), holder.name.getText() + " clicked", Toast.LENGTH_SHORT).show();

                //Log.i("ARPIT", "checked_pos before :" + checked_pos);

                if (checked_pos != -1)
                {
                    try
                    {
                        HeadListAdapter.MyViewHolder nestedView = (HeadListAdapter.MyViewHolder)
                                recyclerView.findViewHolderForAdapterPosition(checked_pos);
                        nestedView.radioBut.setChecked(false);
                    } catch (NullPointerException ee)
                    {
                    }
                }

                holder.radioBut.setChecked(true);
                checked_pos = position;
                //Log.i("ARPIT", "checked pos after: " + checked_pos);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return moviesList.size();
    }

}
