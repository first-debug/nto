module com.vladislav {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.sql;
    requires org.slf4j;


    opens com.vladislav to javafx.fxml, com.vladislav.controllers;
    opens com.vladislav.models to javafx.base, javafx.scene;
    opens com.vladislav.controllers to javafx.fxml;
    exports com.vladislav;
    exports com.vladislav.models;
}
