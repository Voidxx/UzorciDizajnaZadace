package tvrtka;

import java.util.ArrayList;
import java.util.List;

import objekti.Paket;
import objekti.VrstaPaketa;

public class UredZaPrijem {
	private static UredZaPrijem instance = null;
	private List<VrstaPaketa> vrstePaketa = new ArrayList<VrstaPaketa>();
	private List<Paket> ocekivaniPaketi = new ArrayList<Paket>();
	private List<Paket> paketiSpremniZaDostavu = new ArrayList<Paket>();
	private double ukupniIznosOdSvihDostava = 0;
	
    private UredZaPrijem() {

    }

    public static UredZaPrijem getInstance() {
        if (instance == null) {
            instance = new UredZaPrijem();
        }
        return instance;
    }
	
    public void postaviVrstePaketa(List<VrstaPaketa> vrstePaketa) {
    	this.vrstePaketa = vrstePaketa;
    }
    
    public void postaviOcekivanePakete(List<Paket> paketi) {
    	this.ocekivaniPaketi = paketi;
    }
    
    public List<Paket> dobaviListuOcekivanihPaketa(){
    	return this.ocekivaniPaketi;
    }
    
    public double dobaviUkupniIznosOdDostave(){
    	return this.ukupniIznosOdSvihDostava;
    }
    
    public void nadodajNaUkupniIznosDostavu(double d) {
    	this.ukupniIznosOdSvihDostava = this.ukupniIznosOdSvihDostava + d;
    }
    
    public void dodajPaketUSpremneZaDostavu(Paket paket) {
        this.paketiSpremniZaDostavu.add(paket);

        if(!paket.getUsluga_dostave().equals("P")) {
            for (VrstaPaketa vrstaPaketa : vrstePaketa) {
                if(paket.getVrsta_paketa().equals(vrstaPaketa.getOznaka())) {
                    if(!paket.getVrsta_paketa().equals("X")) {
                        if(!paket.getUsluga_dostave().equals("H")) {
                            this.nadodajNaUkupniIznosDostavu(vrstaPaketa.getCijena());
                            paket.setIzracunati_iznos_dostave(vrstaPaketa.getCijena());
                        }
                        else {
                            this.nadodajNaUkupniIznosDostavu(vrstaPaketa.getCijena_hitno());
                            paket.setIzracunati_iznos_dostave(vrstaPaketa.getCijena_hitno());
                        }
                    }
                    else {
                        if(!paket.getUsluga_dostave().equals("H")) {
                            this.nadodajNaUkupniIznosDostavu(vrstaPaketa.getCijena() + (paket.getDuzina()*paket.getSirina()*paket.getVisina()*vrstaPaketa.getCijenaP()) + (paket.getTezina()*vrstaPaketa.getCijenaT()));
                            paket.setIzracunati_iznos_dostave(vrstaPaketa.getCijena() + (paket.getDuzina()*paket.getSirina()*paket.getVisina()*vrstaPaketa.getCijenaP()) + (paket.getTezina()*vrstaPaketa.getCijenaT()));
                        }
                        else {
                            this.nadodajNaUkupniIznosDostavu(vrstaPaketa.getCijena_hitno() + (paket.getDuzina()*paket.getSirina()*paket.getVisina()*vrstaPaketa.getCijenaP()) + (paket.getTezina()*vrstaPaketa.getCijenaT()));
                            paket.setIzracunati_iznos_dostave(vrstaPaketa.getCijena_hitno() + (paket.getDuzina()*paket.getSirina()*paket.getVisina()*vrstaPaketa.getCijenaP()) + (paket.getTezina()*vrstaPaketa.getCijenaT()));
                        }
                    }
                    break; 
                }
            }
        }
    }
    
    public List<Paket> dobaviListuPaketaZaDostavu(){
    	return this.paketiSpremniZaDostavu;
    }
    
    public void makniPaketIzListeZaDostavu(Paket paket) {
    	this.paketiSpremniZaDostavu.remove(paket);
    }
    
    
    public VrstaPaketa getVrstaPaketa(String vrsta) {
    	   for (VrstaPaketa vrstaPaketa : vrstePaketa) {
    	       if (vrstaPaketa.getOznaka().equals(vrsta)) {
    	           return vrstaPaketa;
    	       }
    	   }
    	   return null;
    	}
}
