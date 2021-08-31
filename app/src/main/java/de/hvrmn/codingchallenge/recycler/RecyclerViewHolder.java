package de.hvrmn.codingchallenge.recycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hvrmn.codingchallenge.R;
import de.hvrmn.codingchallenge.model.Car;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private final TextView nameTextView;
    private final TextView addressTextView;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.name);
        addressTextView = itemView.findViewById(R.id.address);
    }

    public void bind(Car car, OnItemClickListener<Car> listener) {

        nameTextView.setText(car.getName());
        addressTextView.setText(car.getAddress());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(car, view);
            }
        });
    }
}
