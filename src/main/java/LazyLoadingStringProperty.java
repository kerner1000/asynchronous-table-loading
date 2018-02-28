/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved.
 *
 * Contributors:
 * Dr. Alexander Kerner - initial API and implementation
 *
 *******************************************************************************/

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Service;
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
            Platform.runLater(() -> startLoadingService());
            return loadingString;
        }
        return super.getValue();
    }

    protected void startLoadingService() {

        final Service<String> s = new Service<String>() {

            @Override
            protected Task<String> createTask() {
                return LazyLoadingStringProperty.this.createTask();
            }
        };

        s.setExecutor(exe);

        s.setOnFailed(e -> {
            setValue(s.getException().getLocalizedMessage());
            setLoaded(true);
        });

        s.setOnSucceeded(e -> {
            setValue(s.getValue());
            setLoaded(true);
        });
        s.start();
        // System.err.println("Started");
    }

    protected abstract Task<String> createTask();
}
