package objekti;

import java.util.List;
import java.util.NoSuchElementException;

public class PaketIteratorImpl implements PaketIterator {
	   private List<Paket> pakets;
	   private int currentIndex = 0;
	   private Paket lastElement;
	   
	   public PaketIteratorImpl(List<Paket> pakets) {
	       this.pakets = pakets;
	   }

	   @Override
	   public boolean hasNext() {
	       return currentIndex < pakets.size();
	   }

	   @Override
	   public Paket next() {
	       if (!hasNext()) {
	           throw new NoSuchElementException();
	       }
	       lastElement = pakets.get(currentIndex);
	       return lastElement;
	   }
	   @Override
	   public void remove() {
	       if (lastElement == null) {
	           throw new IllegalStateException();
	       }
	       pakets.remove(lastElement);
	       lastElement = null;
	   }
	   
	   public void add(Paket paket) {
	       pakets.add(paket);
	   }
	   
	  
	}