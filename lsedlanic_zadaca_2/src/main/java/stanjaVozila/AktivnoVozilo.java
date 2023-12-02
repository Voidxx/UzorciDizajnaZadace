package stanjaVozila;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.VirtualnoVrijeme;
import objekti.Paket;
import objekti.Vozilo;
import tvrtka.UredZaDostavu;
import tvrtka.UredZaPrijem;
import voznja.Voznja;
import voznja.VoznjaBuilder;

public class AktivnoVozilo implements Stanje{
	private VoznjaBuilder builder = new VoznjaBuilder();
	
	
	public VoznjaBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(VoznjaBuilder builder) {
		this.builder = builder;
	}

	@Override
	public void ukrcajPakete(Vozilo vozilo) {
        List<Paket> paketiZaDostavu = new ArrayList<>(UredZaPrijem.getInstance().dobaviListuPaketaZaDostavu());
        for (Paket paket : paketiZaDostavu) {
            double tezinaPaketa = paket.getTezina();
            double volumenPaketa = paket.getVisina() * paket.getSirina() * paket.getDuzina();
            if ((tezinaPaketa + vozilo.getTrenutni_teret_tezina() <= vozilo.getKapacitet_kg() && volumenPaketa + vozilo.getTrenutni_teret_volumen() <= vozilo.getKapacitet_m3())) {
            	vozilo.setTrenutni_teret_tezina(vozilo.getTrenutni_teret_tezina() + tezinaPaketa);
            	vozilo.setTrenutni_teret_volumen(vozilo.getTrenutni_teret_volumen() + volumenPaketa);
            	vozilo.dodajPaketUVozilo(paket);
                UredZaPrijem.getInstance().dobaviListuPaketaZaDostavu().remove(paket);
                System.out.println("Dodan paket: " + paket.getOznaka() + " u vozilo: " + vozilo.getOpis() + " na vrijeme: " + VirtualnoVrijeme.getVrijemeDateTime() + " - " + "Trenutni teret: KG " + vozilo.getTrenutni_teret_tezina() + " Volumen " + vozilo.getTrenutni_teret_volumen());
                builder.dodajUkrcanePakete(paket);
            }
        }
		
	}

	@Override
	public void dostaviPakete(Vozilo vozilo, int vi) {
		   boolean imaJosPaketaUVremenskomPeriodu = true;
    	   while (imaJosPaketaUVremenskomPeriodu) {
    	       if (vozilo.getDostavaSat() != null) {
    	           int brojPaketa = vozilo.getUkrcani_paketi().size();
    	           if (brojPaketa > 0) {
    	               Duration duration = Duration.ofMinutes(vi);
    	               Instant sada = vozilo.getDostavaSat().instant();
    	               Instant kasnije = sada.plus(duration);
    	               if(kasnije.isBefore(VirtualnoVrijeme.getVrijeme()) || kasnije.equals(VirtualnoVrijeme.getVrijeme())){
    	            	  vozilo.setDostavaSat(Clock.fixed(kasnije, ZoneId.systemDefault())); 
    	                  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
    	                  ZonedDateTime zdt = vozilo.getDostavaSat().instant().atZone(ZoneId.systemDefault());
    	                  String formatiraniDateTime = zdt.format(formatter);

    	                  System.out.println("Vozilo: " + vozilo.getOpis() + " je dostavilo paket: " + vozilo.getUkrcani_paketi().get(0).getOznaka() + " Na vrijeme sata: " + formatiraniDateTime);
    	                  vozilo.getUkrcani_paketi().get(0).setVrijeme_preuzimanja(vozilo.getVrijemeDostaveDateTime());
    	                  UredZaDostavu.getInstance().dodajDostavljeniPaket(vozilo.getUkrcani_paketi().get(0));
    	                  builder.dodajDostavljenePakete(vozilo.getUkrcani_paketi().get(0));
    	                  UredZaPrijem.getInstance().nadodajNaUkupniIznosDostavu(vozilo.getUkrcani_paketi().get(0).getIznos_pouzeca());
    	                  vozilo.setPrikupljeniNovac(vozilo.getPrikupljeniNovac() + vozilo.getUkrcani_paketi().get(0).getIznos_pouzeca());
    	                  vozilo.getUkrcani_paketi().remove(0);
    	               }
    	               else {
    	            	   imaJosPaketaUVremenskomPeriodu = false;
    	               }
    	           }
    	           else {
    	               imaJosPaketaUVremenskomPeriodu = vozilo.vratiSeUUred();
    	           }
    	       }
    	       else {
    	    	   imaJosPaketaUVremenskomPeriodu = false;
    	       }
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
