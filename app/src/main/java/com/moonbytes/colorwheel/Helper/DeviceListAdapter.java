package com.moonbytes.colorwheel.Helper;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moonbytes.colorwheel.R;

import java.util.List;

/**
 * Created by dasteini on 27.01.16.
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceHolder> {
    private List<DeviceItem> devices;
    private LayoutInflater layoutInflater;
    private Context context;

    public DeviceListAdapter(Context context, List<DeviceItem> devices) {
        this.devices = devices;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;

    }

    @Override
    public DeviceListAdapter.DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_device, parent, false);
        DeviceHolder deviceHolder = new DeviceHolder(view);
        return deviceHolder;
    }

    @Override
    public void onBindViewHolder(DeviceListAdapter.DeviceHolder holder, int position) {
        DeviceItem d = devices.get(position);
        holder.deviceName.setText(d.getDeviceName());
        holder.deviceIp.setText(d.getIpAdress());

    }

    @Override
    public int getItemCount() {
        return devices.size();
    }



    public class DeviceHolder extends RecyclerView.ViewHolder {
        private TextView deviceName, deviceIp;
        private ImageView deviceIcon;

        public DeviceHolder(View itemView) {
            super(itemView);
            deviceName = (TextView) itemView.findViewById(R.id.deviceName);
            deviceIp = (TextView) itemView.findViewById(R.id.deviceIP);
            deviceIcon = (ImageView) itemView.findViewById(R.id.deviceIcon);
            /*
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, "OnLongClick", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            */
        }
    }
}
