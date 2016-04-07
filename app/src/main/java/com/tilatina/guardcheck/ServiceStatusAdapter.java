package com.tilatina.guardcheck;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tilatina.guardcheck.Utillities.ServiceStatus;

import java.util.List;

/**
 * Created by jaime on 7/04/16.
 */
public class ServiceStatusAdapter extends RecyclerView.Adapter<ServiceStatusAdapter.GuardHolder> {

    private List<ServiceStatus> serviceStatuses;

    public class GuardHolder extends RecyclerView.ViewHolder {
        public TextView name, statusDate, nextTo;

        public GuardHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            statusDate = (TextView) view.findViewById(R.id.statusDate);
            nextTo = (TextView) view.findViewById(R.id.nextTo);
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
        ServiceStatus serviceStatus = serviceStatuses.get(position);
        holder.name.setText(serviceStatus.getName());
        holder.statusDate.setText(serviceStatus.getStatusDate());
        holder.nextTo.setText(serviceStatus.getNextTo());
    }

    @Override
    public int getItemCount() {
        return serviceStatuses.size();
    }
}
