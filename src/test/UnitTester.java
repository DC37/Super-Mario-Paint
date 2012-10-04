package test;

import junit.textui.TestRunner;;

/**
 * JUnit tester. This program is getting rather large.
 * @author RehdBlob
 * @since 2012.10.04
 */
public class UnitTester {

    /**
     * @param unused Unused Strings.
     */
    public static void main(String[] unused) {
        TestRunner.run(ImagesTest.class);
        TestRunner.run(SoundsTest.class);
    }

}
