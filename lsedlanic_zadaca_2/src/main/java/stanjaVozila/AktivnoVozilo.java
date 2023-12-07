package stanjaVozila;

import app.VirtualnoVrijeme;
import objekti.Osoba;
import objekti.Paket;
import objekti.Vozilo;
import tvrtka.Tvrtka;
import tvrtka.UredZaPrijem;
import voznja.Voznja;
import voznja.VoznjaBuilder;

public class AktivnoVozilo implements Stanje{
	private VoznjaBuilder builder = new VoznjaBuilder();
	boolean jeLiOsobaPrimatelj = false;
	Osoba primatelj = null;
    private DostavaStrategija dostavaStrategija;
    


    public void setDostavaStrategija(DostavaStrategija dostavaStrategija) {
        this.dostavaStrategija = dostavaStrategija;
    }

    public DostavaStrategija getDostavaStrategija() {
    	return this.dostavaStrategija;
    }

	
	public VoznjaBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(VoznjaBuilder builder) {
		this.builder = builder;
	}

	@Override
	public void ukrcajPakete(Vozilo vozilo, Paket paket) {
	                double tezinaPaketa = paket.getTezina();
	                double volumenPaketa = paket.getVisina() * paket.getSirina() * paket.getDuzina();
	                if ((tezinaPaketa + vozilo.getTrenutni_teret_tezina() <= vozilo.getKapacitet_kg() && volumenPaketa + vozilo.getTrenutni_teret_volumen() <= vozilo.getKapacitet_m3())) {
	                    vozilo.setTrenutni_teret_tezina(vozilo.getTrenutni_teret_tezina() + tezinaPaketa);
	                    vozilo.setTrenutni_teret_volumen(vozilo.getTrenutni_teret_volumen() + volumenPaketa);
	                    vozilo.dodajPaketUVozilo(paket);
	                    builder.dodajUkrcanePakete(paket);
	                    System.out.println("Dodan paket: " + paket.getOznaka() + " u vozilo: " + vozilo.getOpis() + " na vrijeme: " + VirtualnoVrijeme.getVrijemeDateTime() + " - " + "Trenutni teret: KG " + vozilo.getTrenutni_teret_tezina() + " Volumen " + vozilo.getTrenutni_teret_volumen());
	                    UredZaPrijem.getInstance().dobaviListuPaketaZaDostavu().remove(paket);
  
	              }
	          }

	

	@Override
	public void dostaviPakete(Vozilo vozilo, int vi) {
		if(Tvrtka.getInstance().getIsporuka() == 2) {
		   dostavaStrategija = new NajblizaDostava();
		   dostavaStrategija.odrediRedoslijed(vozilo, builder);
		}
		else if(Tvrtka.getInstance().getIsporuka() == 1) {
		   dostavaStrategija = new RedoslijedUkrcavanja();
		   dostavaStrategija.odrediRedoslijed(vozilo, builder);
		}
        
	}

	@Override
	public boolean vratiSeUUred(Vozilo vozilo) {
		builder.postaviVrijemePovratka(VirtualnoVrijeme.getVrijemeDateTime());
		builder.postaviTrajanje();
        vozilo.setDostavaSat(null);
        vozilo.setTrenutno_vozi(false);
        System.out.println("Vozilo: " + vozilo.getOpis() + " je završilo sa dostavama.");
        Voznja voznja = builder.build();
        vozilo.dodajObavljenuVoznju(voznja);
        return false;
		
	}

}
