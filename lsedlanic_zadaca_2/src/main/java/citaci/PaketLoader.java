package citaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import paket.Paket;

public class PaketLoader implements CsvLoader<Paket>{
	   @Override
	   public List<Paket> loadCsv(String filePath) throws IOException, ParseException {
	       List<Paket> paketi = new ArrayList<>();
	       try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	           br.readLine();
	           String line;
	           while ((line = br.readLine()) != null) {
	        	   Paket paket = new Paket(null, null, null, null, null, 0, 0, 0, 0, null, 0, 0, null);
	        	   paket.process(line);
	               if(paket.imaVrijednosti())
	            	   paketi.add(paket);
	           }
	       }
	       return paketi;
	   }
}
