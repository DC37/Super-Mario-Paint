package smp.fx;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;


/**
 * Until I can figure out how to do JavaFX preload/splash screens,
 * this will be a placeholder class.
 * @author RehdBlob
 * @since 2012.08.17
 */
public class SplashScreen extends JFrame implements Runnable {

    /**
     * Generated serial ID
     */
    private static final long serialVersionUID = 6705972583468020200L;

    /**
     * A button that displays the load status.
     */
    private JButton loading;

    /**
     * Tells whether it is legal to update the status.
     */
    private boolean isUpdateable;

    /**
     * The nice looking splash screen that we may be implementing
     * in the near future.
     */
    private Image splashScreen;

    /**
     * Displays a dummy window that says "Loading!"
     */
    @Override
    public void run() {
        loading = new JButton("Loading: 0.00");
        loading.setBorder(BorderFactory.createEmptyBorder());
        loading.setContentAreaFilled(false);
        // loading.add(splashScreen);
        add(loading, BorderLayout.CENTER);
        setSize(new Dimension(400, 400));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        this.setTitle("Super Mario Paint");
        setVisible(true);
        setUpdateable(true);
    }

    /**
     * @param tf Whether the splash screen may begin updating statuses.
     */
    public void setUpdateable(boolean tf) {
        isUpdateable = tf;
    }

    /**
     * Sets the loading progress to some value between 0 and 100%.
     * @param d The sum of all of the loader thread statuses.
     * @param numOfThreads The number of threads.
     */
    public void updateStatus(double d, int numOfThreads) {
        if (isUpdateable)
            loading.setText(String.format("Loading: %3.2f",
                    (d / numOfThreads)));
    }


}
