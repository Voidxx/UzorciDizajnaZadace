package stanjaVozila;

import objekti.Paket;
import objekti.Vozilo;

public class NeaktivnoVozilo implements Stanje{



	@Override
	public void dostaviPakete(Vozilo vozilo, int vi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean vratiSeUUred(Vozilo vozilo) {
		return false;
	}

	@Override
	public void ukrcajPakete(Vozilo vozilo, Paket paket) {
		// TODO Auto-generated method stub
		
	}

}
