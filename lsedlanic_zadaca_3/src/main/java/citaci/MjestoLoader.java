package citaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import objekti.Mjesto;

public class MjestoLoader implements CsvLoader<Mjesto>{

	   @Override
	   public List<Mjesto> loadCsv(String filePath) throws IOException, ParseException {
	       List<Mjesto> mjesta = new ArrayList<>();
	       try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	           br.readLine();
	           String line;
	           while ((line = br.readLine()) != null) {
	        	   Mjesto mjesto = new Mjesto();
	        	   mjesto.process(line);
	               if(mjesto.imaVrijednosti())
	            	   mjesta.add(mjesto);
	           }
	       }
	       return mjesta;
	   }

}
