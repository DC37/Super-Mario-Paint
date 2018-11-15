package smp.models.stateMachine;

import java.io.File;
import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import smp.components.InstrumentIndex;
import smp.components.Values;
import smp.models.staff.StaffArrangement;
import smp.models.staff.StaffNoteLine;
import smp.models.staff.StaffSequence;

public class Variables {
	/** from <code>Staff</code> */
	public static ObjectProperty<StaffSequence> theSequence = new SimpleObjectProperty<>(new StaffSequence());

	public static class WindowLines extends ArrayList<ObjectProperty<StaffNoteLine>> {
		private static final long serialVersionUID = -2815381084664525320L;
		//====Models====
		private DoubleProperty measureLineNum;
		public WindowLines(int capacity) {
			for (int i = 0; i < capacity; i++)
				this.add(new SimpleObjectProperty<StaffNoteLine>(theSequence.get().getLine(i)));
			this.measureLineNum = StateMachine.getMeasureLineNum();
			setupModelRelations();
		}

		private void setupModelRelations() {
			theSequence.addListener(new ChangeListener<StaffSequence>() {
				@Override
				public void changed(ObservableValue<? extends StaffSequence> observable, StaffSequence oldValue,
						StaffSequence newSequence) {
					for (int i = 0; i < size(); i++)
						get(i).set(newSequence.getLine(i));
				}
			});
			this.measureLineNum.addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					for(int i = 0; i < size(); i++) {
						StaffNoteLine newWindowLine = theSequence.get().getLine(newValue.intValue() + i);
						get(i).set(newWindowLine);
					}
				}
			});
		}
	}
	public static WindowLines windowLines = new WindowLines(Values.NOTELINES_IN_THE_WINDOW);
	/** from <code>Staff</code> */
	public static ObjectProperty<StaffArrangement> theArrangement = new SimpleObjectProperty<>(new StaffArrangement());
	/** from <code>Staff</code> */
	public static ObjectProperty<File> theSequenceFile = new SimpleObjectProperty<>(null);
	/** @since 1.3 */
	public static ObjectProperty<InstrumentIndex> selectedInstrument = new SimpleObjectProperty<>(InstrumentIndex.MARIO);
	/** from <code>Staff</code> */
	public static StringProperty theSequenceName = new SimpleStringProperty("");	
	/** from <code>Staff</code> */
	public static StringProperty theArrangementName = new SimpleStringProperty("");
	public static IntegerProperty arrangementListSelectedIndex = new SimpleIntegerProperty(-1);
	
	public static IntegerProperty defaultVelocity = new SimpleIntegerProperty(Values.DEFAULT_VELOCITY);
	
	//OPTIONS
	/** The default volume that appears on the options' volume slider. */
	public static IntegerProperty optionsDefaultVolume = new SimpleIntegerProperty(Values.DEFAULT_VELOCITY);
	/** The number string in the tempo-multiplier text field. */
	public static StringProperty optionsTempoMultiplier = new SimpleStringProperty("");
	/** The soundfont string that is selected in the current-soundfont combobox. */
	public static StringProperty optionsCurrentSoundfont = new SimpleStringProperty("");
	/** The checked value in the bind checkbox. */
	public static BooleanProperty optionsBindSoundfont = new SimpleBooleanProperty(false);
	/** The soundfont in the current-soundfont combobox that, when selected, will toggle the bind checkbox. */
	public static StringProperty optionsBindedSoundfont = new SimpleStringProperty("");
}
