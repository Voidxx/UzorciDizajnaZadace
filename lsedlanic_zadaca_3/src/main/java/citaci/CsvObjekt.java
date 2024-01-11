package citaci;

import java.text.ParseException;

public interface CsvObjekt {
		void process(String line) throws ParseException;
		boolean imaVrijednosti();
}
