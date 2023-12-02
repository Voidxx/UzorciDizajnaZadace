package citaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import objekti.Osoba;

public class OsobaLoader implements CsvLoader<Osoba>{
	   @Override
	   public List<Osoba> loadCsv(String filePath) throws IOException, ParseException {
	       List<Osoba> osobe = new ArrayList<>();
	       try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	           br.readLine();
	           String line;
	           while ((line = br.readLine()) != null) {
	        	   Osoba osoba = new Osoba();
	        	   osoba.process(line);
	               if(osoba.imaVrijednosti())
	            	   osobe.add(osoba);
	           }
	       }
	       return osobe;
	   }
}
