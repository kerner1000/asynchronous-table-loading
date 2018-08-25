/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved.
 *
 * Contributors:
 * Dr. Alexander Kerner - initial API and implementation
 *
 *******************************************************************************/

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class LazyLoadingStringProperty extends SimpleStringProperty {

    public static final String DEFAULT_LOADING_STRING = "Loading..";
    private static final ExecutorService exe = Executors.newCachedThreadPool();
    private String loadingString = DEFAULT_LOADING_STRING;

    private boolean loaded = false;

    public LazyLoadingStringProperty() {

    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(final boolean loaded) {
        this.loaded = loaded;
    }

    public String getLoadingString() {
        return loadingString;
    }

    public void setLoadingString(final String loadingString) {
        this.loadingString = loadingString;
    }

    @Override
    public String getValue() {
        if (!loaded) {
            startLoadingService();
            return loadingString;
        }
        return super.getValue();
    }

    protected void startLoadingService() {


        final Task<String> s = LazyLoadingStringProperty.this.createTask();

        exe.submit(s);


        s.setOnFailed(e -> {
            setLoaded(true);
            setValue(s.getException().getLocalizedMessage());

        });

        s.setOnSucceeded(e -> {
            setLoaded(true);
            setValue(s.getValue());

        });

    }

    protected abstract Task<String> createTask();
}
