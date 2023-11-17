module com.vladislav {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.vladislav to javafx.fxml;
    opens com.vladislav.models to javafx.base;
    exports com.vladislav;
}
