package stanjaVozila;

import objekti.Paket;
import objekti.Vozilo;

public class NeispravnoVozilo implements Stanje{

	@Override
	public void ukrcajPakete(Vozilo vozilo, Paket paket) {
		System.out.println("Vozilo je: " + vozilo.getOpis() + "  trenutno neispravno.");
		
	}

	@Override
	public void dostaviPakete(Vozilo vozilo, int vi) {
		System.out.println("Vozilo je: " + vozilo.getOpis() + "  trenutno neispravno.");
		
	}

	@Override
	public boolean vratiSeUUred(Vozilo vozilo) {
		System.out.println("Vozilo je: " + vozilo.getOpis() + "  trenutno neispravno.");

		return false;
	}

	@Override
	public boolean mozeSePonovoAktivirati() {
		
		return false;
	}
	

}
