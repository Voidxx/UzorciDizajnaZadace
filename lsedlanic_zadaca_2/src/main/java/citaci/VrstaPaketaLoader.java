package citaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import objekti.VrstaPaketa;

public class VrstaPaketaLoader implements CsvLoader<VrstaPaketa>{
	   @Override
	   public List<VrstaPaketa> loadCsv(String filePath) throws IOException, ParseException {
	       List<VrstaPaketa> vrstePaketa = new ArrayList<>();
	       try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	           br.readLine();
	           String line;
	           while ((line = br.readLine()) != null) {
	        	   VrstaPaketa vrstaPaketa = new VrstaPaketa(null, null, 0, 0, 0, 0, 0, 0, 0, 0);
	        	   vrstaPaketa.process(line);
	               if(vrstaPaketa.imaVrijednosti())
	            	   vrstePaketa.add(vrstaPaketa);
	           }
	       }
	       return vrstePaketa;
	   }
}
