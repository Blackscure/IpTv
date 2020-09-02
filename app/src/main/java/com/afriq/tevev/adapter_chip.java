package com.afriq.tevev;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adapter_chip extends RecyclerView.Adapter<adapter_chip.ViewHolder> {
    private final Context context;
    private final List<Object> chipItem;

    public adapter_chip(Context context, List<Object> chipItem,ClickedListener listener) {
        this.context = context;
        this.chipItem = chipItem;
        this.clickedListener=listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chips,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Model_chips model=(Model_chips)chipItem.get(position);
        holder.chip.setText(model.getName());
        holder.chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filter=model.getName();
                clickedListener.onClicked(filter);


            }
        });
    }

    @Override
    public int getItemCount() {
        return chipItem.size();
    }
    private ClickedListener clickedListener;
    public interface ClickedListener{
        void onClicked(String filter);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        com.google.android.material.chip.Chip chip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chip=itemView.findViewById(R.id.chip);

        }
    }

}
