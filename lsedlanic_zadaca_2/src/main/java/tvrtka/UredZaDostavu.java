package tvrtka;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import objekti.Paket;
import objekti.Vozilo;

public class UredZaDostavu implements Serializable{
	private static final long serialVersionUID = 957947998376317661L;
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
    
	public static void setInstance(UredZaDostavu instance) {
		UredZaDostavu.instance = instance;
	}
    
    public void postaviVozila(List<Vozilo> vozila) {
    	this.listaVozila = vozila;
    }
    
    public List<Vozilo> dohvatiListuVozila() {
    	return this.listaVozila;
    }
    
    public Vozilo dohvatiVozilo(String registracija) {
    	   for(Vozilo vozilo : listaVozila) {
    	       if(vozilo.getRegistracija().equals(registracija))
    	           return vozilo;
    	   }
    	   return null;
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
