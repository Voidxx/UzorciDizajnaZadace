package citaci;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public interface CsvObjekt {
		void process(String line) throws ParseException;
}
