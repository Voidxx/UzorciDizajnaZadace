package stanjaVozila;

import objekti.Paket;
import objekti.Vozilo;

public interface Stanje {
	   void ukrcajPakete(Vozilo vozilo, Paket paket);
	   void dostaviPakete(Vozilo vozilo, int vi);
	   boolean vratiSeUUred(Vozilo vozilo);
	   boolean mozeSePonovoAktivirati();
}
