package com.example.doneit.convalidation;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.doneit.R;

import java.util.List;

public class DeviceBluetoothAdapter extends ArrayAdapter<BluetoothDevice> {


    public DeviceBluetoothAdapter(@NonNull Context context, int resource,
                                  List<BluetoothDevice> bluetoothDeviceList) {
        super(context, resource, bluetoothDeviceList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item_device, null);
        TextView deviceName = convertView.findViewById(R.id.deviceName);
        TextView deviceMac = convertView.findViewById(R.id.deviceMac);
        BluetoothDevice bd = getItem(position);

        if (bd != null) {
            deviceName.setText(bd.getName());
            deviceMac.setText(bd.getAddress());
        }

        return convertView;
    }

}
