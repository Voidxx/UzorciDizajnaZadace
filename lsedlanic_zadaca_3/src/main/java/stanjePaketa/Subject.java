package stanjePaketa;

import java.util.ArrayList;
import java.util.List;

import objekti.Paket;

public class Subject implements Cloneable {
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
	   @Override
	   public Object clone() throws CloneNotSupportedException {
	    Subject cloned = (Subject) super.clone();
	    cloned.observers = new ArrayList<>(this.observers);
	    cloned.paket = (Paket) this.paket.clone();
	    return cloned;
	   }
}
