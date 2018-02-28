import javafx.beans.property.ReadOnlyStringProperty;
import javafx.concurrent.Task;

public class ExampleBean {

    private ReadOnlyStringProperty p1;

    private ReadOnlyStringProperty p2;

    private ReadOnlyStringProperty p3;

    public ExampleBean() {
        p1 = new LazyLoadingStringProperty() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() {
                        return "P1";
                    }
                };
            }
        };
        p2 = new LazyLoadingStringProperty() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() {
                        throw new RuntimeException("Bam!");
                    }
                };
            }
        };
        p3 = new LazyLoadingStringProperty() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() {
                        return "P3";
                    }
                };
            }
        };
    }

    public final ReadOnlyStringProperty p1Property() {
        return this.p1;
    }

    public final ReadOnlyStringProperty p2Property() {
        return this.p2;
    }

    public final ReadOnlyStringProperty p3Property() {
        return this.p3;
    }
}
