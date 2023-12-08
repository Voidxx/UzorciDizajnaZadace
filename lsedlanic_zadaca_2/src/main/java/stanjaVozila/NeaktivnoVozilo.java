package stanjaVozila;

import java.io.Serializable;

import objekti.Paket;
import objekti.Vozilo;

public class NeaktivnoVozilo implements Stanje, Serializable{



	private static final long serialVersionUID = -513191394183632590L;

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
