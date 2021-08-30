package de.hvrmn.codingchallenge.model;

import java.util.ArrayList;
import java.util.List;

public class Dataset {

    private List<Car> placemarks;

    public Dataset() {
        placemarks = new ArrayList<>();
    }

    public Dataset(List<Car> placemarks) {
        this.placemarks = placemarks;
    }

    public List<Car> getPlacemarks() {
        return placemarks;
    }

    public void setPlacemarks(List<Car> placemarks) {
        this.placemarks = placemarks;
    }
}
