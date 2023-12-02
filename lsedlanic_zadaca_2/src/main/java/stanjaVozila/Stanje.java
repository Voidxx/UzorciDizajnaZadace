package stanjaVozila;

import objekti.Vozilo;

public interface Stanje {
	   void ukrcajPakete(Vozilo vozilo);
	   void dostaviPakete(Vozilo vozilo, int vi);
	   boolean vratiSeUUred(Vozilo vozilo);
}
