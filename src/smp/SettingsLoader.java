package smp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import smp.stateMachine.Settings;

/**
 * Loads the settings of Super Mario Paint from the file
 * settings.txt, which is supposedly a serialized object.
 * @author RehdBlob
 * @since 2012.10.23
 */
public class SettingsLoader implements Loader {

    public Settings set;

    /**
     * Loads the settings from some file.
     */
    private void load() {
        FileInputStream fin;
        ObjectInputStream oIn;
        try {
            fin = new FileInputStream("settings.txt");
            oIn = new ObjectInputStream(fin);
            set = (Settings) oIn.readObject();
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the settings into some file named "settings.txt"
     */
    public void save() {
        FileOutputStream fout = null;
        ObjectOutputStream oOut = null;
        try {
            fout = new FileOutputStream("settings.txt");
            oOut = new ObjectOutputStream(fout);
            oOut.writeObject(this);
            oOut.close();
            fout.close();
        } catch (FileNotFoundException e) {
            System.err.println("Could not save settings!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Could not save settings!");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        load();
    }

    @Override
    public double getLoadStatus() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setLoadStatus(double d) {
        // TODO Auto-generated method stub

    }
}
