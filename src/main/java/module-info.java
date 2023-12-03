module com.vladislav {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires org.slf4j;
    requires java.sql;
    requires java.desktop;


    opens com.vladislav to javafx.fxml;
    opens com.vladislav.models to javafx.base;
    exports com.vladislav;
    exports com.vladislav.models;
    opens com.vladislav.controllers.admin to javafx.fxml;
    opens com.vladislav.controllers.primary to javafx.fxml;
    exports com.vladislav.controllers;
    opens com.vladislav.controllers to javafx.base, javafx.fxml;
}
