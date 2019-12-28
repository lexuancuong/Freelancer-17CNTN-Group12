package com.example.freelancer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;

public class JobAdapter extends ArrayAdapter<Job> {
    private Context _context;
    private int _resource;
    private List<Job> _list_job;

    public JobAdapter(@NonNull Context context, int resource, @NonNull List<Job> objects) {
        super(context, resource, objects);
        _context = context;
        _resource = resource;
        _list_job = objects;
    }

    @Override
    public int getCount() {
        return _list_job.size();
    }

    private class ViewHolder{
        TextView _txt_job_name;
        TextView _txt_job_price;
        TextView _txt_job_type;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this._context);
            convertView = inflater.inflate(_resource, parent,false);
            holder = new ViewHolder();

            //map
            holder._txt_job_name = convertView.findViewById(R.id.txt_job_name);
            holder._txt_job_price = convertView.findViewById(R.id.txt_job_price);
            holder._txt_job_type = convertView.findViewById(R.id.txt_job_type);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        //get value
        Job temp = _list_job.get(position);
        //holder._img_job.setImageResource(temp.get_image());
        holder._txt_job_name.setText(temp.get_name().toString());
        holder._txt_job_type.setText(temp.get_type().toString());
        if (temp.get_price().size() > 1)
            holder._txt_job_price.setText(String.valueOf(Collections.min(temp.get_price()))+" - "+String.valueOf(Collections.max(temp.get_price())));

        return convertView;
    }
}
