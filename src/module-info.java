module smp {
    exports gui;
    
    opens gui to javafx.fxml;

    requires java.desktop;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
}