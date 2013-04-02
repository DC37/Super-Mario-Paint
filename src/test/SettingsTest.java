package test;

import static org.junit.Assert.*;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;

import smp.stateMachine.Settings;

/**
 * Settings tester for Super Mario Paint.
 * @author RehdBlob
 * @since 2013.04.02
 */
public class SettingsTest extends TestCase {

    @Test
    public void testSettingsSerialize() {
        try {
            Settings.LIM_96_MEASURES = false;
            Settings.ADV_MODE = true;
            Settings.NEG_TEMPO_FUN = true;
            Settings.save();

            Settings.LIM_96_MEASURES = true;
            Settings.ADV_MODE = false;
            Settings.NEG_TEMPO_FUN = false;

            Settings.load();
            assertEquals(Settings.LIM_96_MEASURES, false);
            assertEquals(Settings.ADV_MODE, true);
            assertEquals(Settings.NEG_TEMPO_FUN, true);
        } catch (IOException e) {
            fail("IOException occurred.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException occurred.");
            e.printStackTrace();
        }
    }

}
