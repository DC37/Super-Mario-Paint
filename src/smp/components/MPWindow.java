package smp.components;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import static smp.components.Constants.*;
import smp.components.bottomPanel.BottomPanel;
import smp.components.controls.Controls;
import smp.components.instrumentLine.InstrumentPanel;
import smp.components.staff.Staff;
import smp.components.staff.StaffFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

/**
 * 
 * @author RehdBlob
 * @since 2012.08.07
 */
public class MPWindow extends JFrame {
	
	
	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -8618539640178025658L;
	
	private InstrumentPanel panel;
	private StaffFrame staffFrame;
	private Staff staff;
	private BottomPanel bPanel;
	private Controls controls;
	
	
	/**
	 * Sets the background of the window to Color.GRAY,
	 * the window size to 800x600, and the default
	 * close operation to exit when the x is hit.
	 */
	public MPWindow() {
		super("Super Mario Paint v1.00");
		setLayout(new BorderLayout());
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFocusable(true);
		getContentPane().setBackground(new Color(192, 192, 192));
		
		
		/* I'll change this eventually. */
		setResizable(true);
	}
	
	public void addThings() {
		/*
		this.add(panel);
		this.add(staff);
		this.add(controls);
		this.add(bPanel);
		*/
		
	}
}
