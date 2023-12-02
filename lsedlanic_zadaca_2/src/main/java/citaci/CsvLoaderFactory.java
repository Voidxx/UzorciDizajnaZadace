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


//In the context of your code, 
//the CsvLoaderFactory class is the Factory class. 
//It defines the getLoader(String extension) method, 
//which returns a CsvLoader interface. The VoziloLoader class is a ConcreteCreator class
//that provides the implementation for this method, creating and returning an instance of
//a ConcreteProduct (VoziloLoader).


//The CsvLoader interface is the Product interface, 
//and the VoziloLoader class is a ConcreteProduct. 
//The CsvLoader interface defines a family of algorithms (how to load a CSV file into an object),
//and the VoziloLoader class provides a specific implementation of this algorithm