package citaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import objekti.Ulica;

public class UlicaLoader implements CsvLoader<Ulica>{
	   @Override
	   public List<Ulica> loadCsv(String filePath) throws IOException, ParseException {
	       List<Ulica> ulice = new ArrayList<>();
	       try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	           br.readLine();
	           String line;
	           while ((line = br.readLine()) != null) {
	        	   Ulica ulica = new Ulica(0, line, 0, 0, 0, 0, 0);
	        	   ulica.process(line);
	               if(ulica.imaVrijednosti())
	            	   ulice.add(ulica);
	           }
	       }
	       return ulice;
	   }
}
