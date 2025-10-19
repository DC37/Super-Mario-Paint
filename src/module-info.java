module smp {

    requires transitive java.desktop;
    
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    
    exports gui;
    exports gui.clipboard;
    exports gui.components;
    exports gui.components.buttons;
    exports gui.components.staff;
    exports gui.components.toppanel;
    exports gui.loaders;
    
    exports backend.editing;
    exports backend.editing.commands;
    exports backend.saving.ams;
    exports backend.saving.mpc;
    exports backend.songs;
    exports backend.sound;
    
    opens gui to javafx.fxml;
    
}