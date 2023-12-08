package stanjaVozila;

import java.io.Serializable;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import app.VirtualnoVrijeme;
import objekti.Paket;
import objekti.Vozilo;
import tvrtka.Tvrtka;
import tvrtka.UredZaDostavu;
import tvrtka.UredZaPrijem;
import voznja.SegmentVoznje;
import voznja.VoznjaBuilder;

public class RedoslijedUkrcavanja implements DostavaStrategija, Serializable {
	   private static final long serialVersionUID = -113283048340559364L;

	@Override
	   public void odrediRedoslijed(Vozilo vozilo, VoznjaBuilder builder) {
		   boolean imaJosPaketaUVremenskomPeriodu = true;;
		   VirtualnoVrijeme virtualnoVrijeme = VirtualnoVrijeme.getInstance();
    	   while (imaJosPaketaUVremenskomPeriodu) {
    	       if (vozilo.getDostavaSat() != null) {
    	           int brojPaketa = vozilo.getUkrcani_paketi().size();
    	           if (brojPaketa > 0) {
    	        	   Paket paket = vozilo.getUkrcani_paketi().get(0);
    	        	   double[] gpsKoordinate = paket.vratiPrimatelja().dobaviUlicuOveOsobe().izračunajGpsKoordinate(paket.vratiPrimatelja().getKbr());
    	         	   double dx = (gpsKoordinate[1] - vozilo.getTrenutniLon());
    	         	   double dy = (gpsKoordinate[0] - vozilo.getTrenutniLat());
    	        	   double udaljenost = Math.sqrt(dx * dx + dy * dy) * 111;
 	                  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
 	                  ZonedDateTime zdt1 = vozilo.getDostavaSat().instant().atZone(ZoneId.systemDefault());
 	                  String formatiraniDateTime1 = zdt1.format(formatter);
    	        	   
	   	               Duration duration = Duration.ofMinutes(Tvrtka.getInstance().getVi() + ((int) Math.round(udaljenost/vozilo.getProsjecnaBrzina()*60)));
    	               Instant sada = vozilo.getDostavaSat().instant();
    	               Instant kasnije = sada.plus(duration);
    	               if(kasnije.isBefore(virtualnoVrijeme.getVrijeme()) || kasnije.equals(virtualnoVrijeme.getVrijeme())){
    	            	  vozilo.setDostavaSat(Clock.fixed(kasnije, ZoneId.systemDefault())); 
    	                  ZonedDateTime zdt2 = vozilo.getDostavaSat().instant().atZone(ZoneId.systemDefault());
    	                  String formatiraniDateTime2 = zdt2.format(formatter);
    	                  double[] trenutneKoordinate = new double[] {vozilo.getTrenutniLon(), vozilo.getTrenutniLat()};
    	                  SegmentVoznje segment = new SegmentVoznje(trenutneKoordinate, gpsKoordinate, udaljenost, formatiraniDateTime1, formatiraniDateTime2, ((int) Math.round(udaljenost/vozilo.getProsjecnaBrzina()*60)), Tvrtka.getInstance().getVi() , 0 , paket);
    	                  builder.dodajSegmentVoznje(segment);
    	                  vozilo.setTrenutniLon(gpsKoordinate[1]);
    	                  vozilo.setTrenutniLat(gpsKoordinate[0]);
    	                  System.out.println("Vozilo: " + vozilo.getOpis() + " je dostavilo paket: " + paket.getOznaka() + " Na vrijeme sata: " + formatiraniDateTime2 + " Trenutne koordinate: " + paket.vratiPrimatelja().dobaviUlicu(paket.vratiPrimatelja().getUlica()).getNaziv() + " KBR: " + paket.vratiPrimatelja().getKbr());
    	                  paket.getOvajPaket().notifyObservers("dostavljen");
    	                  vozilo.dodajPaketUIsporucene();
    	                  vozilo.dodajKmNaUkupno(udaljenost);
    	                  builder.dodajNaUkupnoKM(udaljenost);
    	                  
    	                  paket.setVrijeme_preuzimanja(vozilo.getVrijemeDostaveDateTime());
    	                  UredZaDostavu.getInstance().dodajDostavljeniPaket(paket);
    	                  builder.dodajDostavljenePakete(paket);
    	                  UredZaPrijem.getInstance().nadodajNaUkupniIznosDostavu(paket.getIznos_pouzeca());
    	                  vozilo.setPrikupljeniNovac(vozilo.getPrikupljeniNovac() + paket.getIznos_pouzeca());
  	                      vozilo.setTrenutni_teret_tezina(vozilo.getTrenutni_teret_tezina() - paket.getTezina());
  		                  double volumenPaketa = paket.getVisina() * paket.getSirina() * paket.getDuzina();
  	                      vozilo.setTrenutni_teret_volumen(vozilo.getTrenutni_teret_volumen() - volumenPaketa);
    	                  vozilo.getUkrcani_paketi().remove(paket);
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
	}