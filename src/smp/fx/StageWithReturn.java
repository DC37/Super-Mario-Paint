package smp.fx;

import javafx.stage.Stage;

/**
 * A stage that returns a value when closed.
 * 
 * Used for the different implementations of {@link smp.fx.Dialog}.
 * 
 * @author rozlynd
 * @since 2024.09.26
 */
public class StageWithReturn<T> extends Stage {
    
    protected T returnValue;
    
    public T showAndReturn() {
        super.showAndWait();
        return returnValue;
    }

}
