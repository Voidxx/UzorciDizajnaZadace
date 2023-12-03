package stanjePaketa;

import java.util.ArrayList;
import java.util.List;

import objekti.Paket;

public class Subject {
	   private List<Observer> observers = new ArrayList<>();
	   private Paket paket;

	   public void attach(Observer observer) {
	       observers.add(observer);
	   }

	   public void detach(Observer observer) {
	       observers.remove(observer);
	   }

	   public void notifyObservers() {
	       for (Observer observer : observers) {
	           observer.update(paket);
	       }
	   }

	   public void setPaket(Paket paket) {
	       this.paket = paket;
	       notifyObservers();
	   }
}
