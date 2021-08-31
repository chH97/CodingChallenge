package de.hvrmn.codingchallenge.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hvrmn.codingchallenge.R;
import de.hvrmn.codingchallenge.model.Car;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private final LayoutInflater inflater;
    private List<Car> cars;
    private OnItemClickListener<Car> listener;

    public RecyclerAdapter(Context context, List<Car> cars, OnItemClickListener<Car> listener) {
        super();
        inflater = LayoutInflater.from(context);
        this.cars = cars;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(inflater.inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.bind(cars.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public int getItemPosition(Car car) {
        return cars.indexOf(car);
    }
}
