package stanjaVozila;

import java.io.Serializable;

import objekti.Paket;
import objekti.Vozilo;

public class NeispravnoVozilo implements Stanje, Serializable{

	private static final long serialVersionUID = -1833828642466371968L;

	@Override
	public void ukrcajPakete(Vozilo vozilo, Paket paket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dostaviPakete(Vozilo vozilo, int vi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean vratiSeUUred(Vozilo vozilo) {
		return false;
	}

}
