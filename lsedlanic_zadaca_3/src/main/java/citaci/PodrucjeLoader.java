package citaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import objekti.Podrucje;

public class PodrucjeLoader implements CsvLoader<Podrucje>{
	   @Override
	   public List<Podrucje> loadCsv(String filePath) throws IOException, ParseException {
	       List<Podrucje> podrucja = new ArrayList<>();
	       try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	           br.readLine();
	           String line;
	           while ((line = br.readLine()) != null) {
	        	   Podrucje podrucje = new Podrucje();
	        	   podrucje.process(line);
	               if(podrucje.imaVrijednosti())
	            	   podrucja.add(podrucje);
	           }
	       }
	       return podrucja;
	   }
}
