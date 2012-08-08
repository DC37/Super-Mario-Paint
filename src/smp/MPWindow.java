package smp;

import javax.swing.JFrame;
import javax.swing.JTextField;

import java.awt.Color;

public class MPWindow extends JFrame {
	
	/**
	 * Default serial version id.
	 */
	private static final long serialVersionUID = 1L;

	private JTextField nameEntry;
	
	/**
	 * Sets the background of the window to Color.GRAY,
	 * the window size to 800x600, and the default
	 * close operation to exit when the x is hit.
	 */
	public MPWindow() {
		super("Super Mario Paint v1.00");
		setSize(800, 630);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFocusable(true);
		getContentPane().setBackground(Color.GRAY);
		
		
		/* I'll change this eventually. */
		setResizable(false);
	}
	
	
}
