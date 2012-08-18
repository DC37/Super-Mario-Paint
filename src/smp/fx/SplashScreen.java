package smp.fx;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class SplashScreen extends JFrame implements Runnable {

	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = 6705972583468020200L;
	
	private JLabel loading;
	
	@Override
	public void run() {
		loading = new JLabel("Loading!");
		loading.setBorder(null);
		add(loading, BorderLayout.CENTER);
		setSize(new Dimension(810, 630));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
