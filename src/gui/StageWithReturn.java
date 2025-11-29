package gui;

import javafx.stage.Stage;

/**
 * A stage that returns a value when closed.
 * 
 * Used for the different implementations of {@link gui.Dialog}.
 * 
 * @author rozlynd
 * @since 2024.09.26
 */
public class StageWithReturn<T> extends Stage {
    
    protected T returnValue;
    
    public T showAndReturn(T def) {
        super.showAndWait();
        return (returnValue == null) ? def : returnValue;
    }

}
