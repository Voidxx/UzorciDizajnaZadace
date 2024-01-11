package citaci;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface CsvLoader<T> {
	List<T> loadCsv(String filePath) throws IOException, ParseException;
}
