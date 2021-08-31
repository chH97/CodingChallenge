package de.hvrmn.codingchallenge.recycler;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hvrmn.codingchallenge.R;
import de.hvrmn.codingchallenge.model.Car;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private final TextView nameTextView;
    private final TextView addressTextView;
    private final TextView vinTextView;
    private final TextView coordinatesTextView;
    private final TextView fuelTextView;
    private final TextView engineTextView;
    private final TextView interiorExteriorTextView;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.name);
        addressTextView = itemView.findViewById(R.id.address);
        vinTextView = itemView.findViewById(R.id.vin);
        coordinatesTextView = itemView.findViewById(R.id.coordinates);
        fuelTextView = itemView.findViewById(R.id.fuel);
        engineTextView = itemView.findViewById(R.id.engine);
        interiorExteriorTextView = itemView.findViewById(R.id.in_exterior);
    }

    public void bind(Car car, OnItemClickListener<Car> listener, Context context) {
        nameTextView.setText(car.getName());
        addressTextView.setText(car.getAddress());
        vinTextView.setText(car.getVin());
        coordinatesTextView.setText(context.getString(R.string.coordinates_string, car.getCoordinates().get(1), car.getCoordinates().get(0)));
        fuelTextView.setText(String.valueOf(car.getFuel()));
        engineTextView.setText(car.getEngineType());
        interiorExteriorTextView.setText(context.getString(R.string.interior_exterior_string, car.getInterior(), car.getExterior()));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(car, view);
            }
        });
    }
}
