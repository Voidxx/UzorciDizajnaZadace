package citaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import objekti.Paket;
import objekti.Vozilo;

public class VoziloLoader implements CsvLoader<Vozilo> {
	   @Override
	   public List<Vozilo> loadCsv(String filePath) throws IOException, ParseException {
	       List<Vozilo> vozila = new ArrayList<>();
	       try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	           br.readLine();
	           String line;
	           while ((line = br.readLine()) != null) {
	               Vozilo vozilo = new Vozilo(line, line, 0, 0, 0, 0, new ArrayList<Paket>(), 0, false, null, 0, 0, line, null);
	               vozilo.process(line);
	               if(vozilo.imaVrijednosti())
	                  vozila.add(vozilo);
	           }
	       }
	       return vozila;
	   }
}
	   