module com.vladislav {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires org.slf4j;
    requires java.sql;


    opens com.vladislav to javafx.fxml;
    opens com.vladislav.models to javafx.base;
    opens com.vladislav.controllers to javafx.fxml;
    exports com.vladislav;
    exports com.vladislav.models;
}
