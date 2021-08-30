package de.hvrmn.codingchallenge.datafetching;

import de.hvrmn.codingchallenge.model.Dataset;

public interface DataFetcherCallback {
    void onDataRetrieved(Dataset dataset);
    void onError();
}
