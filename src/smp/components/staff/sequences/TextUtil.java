package smp.components.staff.sequences;

import java.util.ArrayList;


/**
 * Pulled from MPCTxtTools; this should be useful in decoding
 * Mario Paint Composer songs.
 * @author RehdBlob
 * @since MPCTxtTools 1.05
 * @since 2011.09.02
 */
public class TextUtil
{
    /**
     * @since MPCTxtTools 1.05
     * @since 2011.09.02
     * @param t The song text file to be "cleaned," that is, the
     * song file that has unnecessarily empty note lines.
     * @return The "cleaned" string.
     */
    public static String clean(String t) {
        int i = t.indexOf("++++++");
        if (i == -1)
            return t.substring(0,t.indexOf('"')) + t.substring(t.indexOf('"') + 1);
        String out = "";
        while(t.indexOf(':')!=-1) {
            String piece = t.substring(0,t.indexOf(':')+1);
            if(piece.indexOf("++++++")!= -1 & piece.indexOf("++++++")==0) {
                out += ":";
                t = t.substring(t.indexOf(':')+1);
            } else {
                out += piece;
                t = t.substring(t.indexOf(':')+1);
            }
        }
        out += t;
        return out;
    }

    /**
     * @since MPCTxtTools 1.05
     * @since 2011.11.08
     * @param s The song text file that is to be "chopped" into
     * note lines.
     * @return All note lines that have data in them contained in an
     * <code>ArrayList{@literal <String>}</code>.
     */
    public static ArrayList<String> chop(String s) {
        ArrayList<String> result = new ArrayList<String>();
        while (s.length() != 0 & s.indexOf(':') != -1)
        {
            result.add(s.substring(0,s.indexOf(':')+1));
            s = s.substring(s.indexOf(':')+1);
            if (s.indexOf('+')==-1)
                break;
        }
        return result;
    }

    /**
     * @since MPCTxtTools 1.05
     * @since 2011.11.08
     * @param read The song text file that is to be "sliced" into
     * just the note data.
     * @return The song text file with the time signature and tempo removed.
     */
    public static String slice(String read) {
        return read.substring(read.indexOf('*')+1,read.lastIndexOf(':')+1);
    }

    /**
     * @since MPCTxtTools 1.05
     * @since 2011.11.08
     * @return The time signature of the song text file.
     */
    public static String getStart(String read) {
        return read.substring(0,read.indexOf('*') + 1);
    }

    /**
     * @since MPCTxtTools 1.05
     * @since 2011.11.08
     * @return The tempo of the song text file.
     */
    public static String getEnd(String read) {
        return read.substring(read.lastIndexOf(':') + 1);
    }

    /**
     * Extracts song data from a Mario Paint Composer file.
     * @since Super Mario Paint 1.00
     * @since 2012.09.01
     * @param read The String that is to be read.
     * @return Three Strings in an ArrayList: The first is always
     * the time signature, the second, the song data, and the third,
     * the tempo.
     */
    public static ArrayList<String> extract(String read) {
        ArrayList<String> out = new ArrayList<String>();
        read = clean(read);
        out.add(getStart(read));
        out.add(slice(read));
        out.add(getEnd(read));
        return out;
    }
}
