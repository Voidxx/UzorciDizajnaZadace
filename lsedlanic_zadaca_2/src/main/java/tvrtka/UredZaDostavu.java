package tvrtka;

import java.util.ArrayList;
import java.util.List;

import objekti.Paket;
import objekti.Vozilo;

public class UredZaDostavu {
	private static UredZaDostavu instance = null;
	private List<Vozilo> listaVozila = null;
	private List<Paket> dostavljeniPaketi = new ArrayList<Paket>();

	
    private UredZaDostavu() {

    }

    public static UredZaDostavu getInstance() {
        if (instance == null) {
            instance = new UredZaDostavu();
        }
        return instance;
    }
    
    
    public void postaviVozila(List<Vozilo> vozila) {
    	this.listaVozila = vozila;
    }
    
    public List<Vozilo> dohvatiListuVozila() {
    	return this.listaVozila;
    }

	public List<Paket> getDostavljeniPaketi() {
		return dostavljeniPaketi;
	}

	public void setDostavljeniPaketi(List<Paket> dostavljeniPaketi) {
		this.dostavljeniPaketi = dostavljeniPaketi;
	}
	
	public void dodajDostavljeniPaket(Paket paket) {
		this.dostavljeniPaketi.add(paket);
	}
}
