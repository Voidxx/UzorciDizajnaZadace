package citaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public abstract class CsvReader<T extends CsvObjekt> {
    protected abstract T createObject();
    public List<T> readCsv(String filePath) throws IOException, ParseException {
        List<T> objects = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                T object = createObject();
                object.process(line);
                if(object.imaVrijednosti())
                	objects.add(object);
            }
        }
        return objects;
    }
}