package citaci;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class CsvUtils {
    public static int convertMetersToMillimeters(String meters) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number number = format.parse(meters);
        return (int) (number.doubleValue() * 1000);
    }
    
    public static int convertMeters3ToMillimeters3(String meters) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number number = format.parse(meters);
        return (int) (number.doubleValue() * 1000000000);
    }

    public static int convertKilogramsToGrams(String kilograms) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number number = format.parse(kilograms);
        return (int) (number.doubleValue() * 1000);
    }

    public static String removeCommas(String input) {
        String noCommas = input.replace(",", "");
        return noCommas.replaceAll("\\.0*$", "");
    }
}