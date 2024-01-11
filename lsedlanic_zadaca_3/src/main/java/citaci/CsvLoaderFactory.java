package citaci;

import java.util.HashMap;
import java.util.Map;

public class CsvLoaderFactory {
	   private static final Map<String, CsvLoader<?>> loaders = new HashMap<>();

	   static {
	       loaders.put("vozila", new VoziloLoader());
	       loaders.put("vrstePaketa", new VrstaPaketaLoader());
	       loaders.put("paketi", new PaketLoader());
	       loaders.put("mjesta", new MjestoLoader());
	       loaders.put("ulice", new UlicaLoader());
	       loaders.put("osobe", new OsobaLoader());
	       loaders.put("podrucja", new PodrucjeLoader());
	   }

	   public static CsvLoader<?> getLoader(String tip) {
	       return loaders.get(tip);
	   }
	}

