package stanjePaketa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import objekti.Paket;

public class Subject implements Serializable {
	   private static final long serialVersionUID = 3391429024373667420L;
	private List<Observer> observers = new ArrayList<>();
	   private Paket paket;

	   public void attach(Observer observer) {
	       observers.add(observer);
	   }

	   public void detach(Observer observer) {
	       observers.remove(observer);
	   }

	   public void notifyObservers(String poruka) {
	       for (Observer observer : observers) {
	    	   if(poruka.equals("zaprimljen"))
	    		   observer.paketZaprimljen(paket);
	    	   else if(poruka.equals("dostavljen"))
	    		   observer.paketDostavljen(paket);
	       }
	   }

	   public void setPaket(Paket paket) {
	       this.paket = paket;
	   }
}
