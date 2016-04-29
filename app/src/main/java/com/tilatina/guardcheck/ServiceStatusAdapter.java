package com.tilatina.guardcheck;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tilatina.guardcheck.Utillities.ServiceStatus;

import java.util.List;

/**
 * Created by jaime on 7/04/16.
 */
public class ServiceStatusAdapter extends RecyclerView.Adapter<ServiceStatusAdapter.GuardHolder> {

    private List<ServiceStatus> serviceStatuses;

    public class GuardHolder extends RecyclerView.ViewHolder {
        public TextView name, nextTo;
        public Button statusColorButton;

        public GuardHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            nextTo = (TextView) view.findViewById(R.id.nextTo);
            statusColorButton = (Button) view.findViewById(R.id.statusColorButton);
        }
    }

    public ServiceStatusAdapter(List<ServiceStatus> serviceStatuses) {
        this.serviceStatuses = serviceStatuses;
    }

    @Override
    public GuardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.services_row, parent, false);

        return new GuardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GuardHolder holder, int position) {
        //Log.d("JAIME...", String.format("Posición %s", position));
        if (((position+1) % 2) == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#DADADA"));
        }
        ServiceStatus serviceStatus = serviceStatuses.get(position);
        holder.name.setText(serviceStatus.getName());
        holder.nextTo.setText(String.format("%.1f km", Double.parseDouble(serviceStatus.getNextTo())));

        if (serviceStatus.getstatusColor().equals("R")) {
            holder.statusColorButton.setBackgroundColor(Color.RED);
        }
        if (serviceStatus.getstatusColor().equals("Y")) {
            holder.statusColorButton.setBackgroundColor(Color.YELLOW);
        }
        if (serviceStatus.getstatusColor().equals("G")) {
            holder.statusColorButton.setBackgroundColor(Color.GREEN);
        }

    }

    @Override
    public int getItemCount() {
        return serviceStatuses.size();
    }
}
