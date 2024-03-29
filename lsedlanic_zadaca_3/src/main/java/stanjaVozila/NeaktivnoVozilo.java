package stanjaVozila;

import app.VirtualnoVrijeme;
import objekti.Paket;
import objekti.Vozilo;

public class NeaktivnoVozilo implements Stanje{




	@Override
	public void dostaviPakete(Vozilo vozilo, int vi) {
		System.out.println("Vozilo: " + vozilo.getOpis() + " je trenutno neaktivno.");
		if(vozilo.isTrenutno_vozi()) {
			vozilo.setDostavaSat(VirtualnoVrijeme.getInstance().getSat());
		}
	}

	@Override
	public boolean vratiSeUUred(Vozilo vozilo) {
		System.out.println("Vozilo: " + vozilo.getOpis() + " je trenutno neaktivno.");		
		return false;
	}

	@Override
	public void ukrcajPakete(Vozilo vozilo, Paket paket) {
		System.out.println("Vozilo: " + vozilo.getOpis() + " je trenutno neaktivno.");		
		
	}

	@Override
	public boolean mozeSePonovoAktivirati() {
		return true;
	}
	


}
