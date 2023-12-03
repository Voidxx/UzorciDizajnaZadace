package objekti;

public interface PaketIterator {
	   boolean hasNext();
	   Paket next();
	   void remove();
	   void add(Paket paket);
	}