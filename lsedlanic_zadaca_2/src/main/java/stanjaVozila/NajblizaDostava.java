package stanjaVozila;

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
import voznja.VoznjaBuilder;

public class NajblizaDostava implements DostavaStrategija {
	   @Override
	   public void odrediRedoslijed(Vozilo vozilo, VoznjaBuilder builder) {
		   boolean imaJosPaketaUVremenskomPeriodu = true;
    	   while (imaJosPaketaUVremenskomPeriodu) {
    	       if (vozilo.getDostavaSat() != null) {
    	           int brojPaketa = vozilo.getUkrcani_paketi().size();
    	           if (brojPaketa > 0) {
   	                   Paket paket = null;
	   	               double minDistance = Double.MAX_VALUE;
	   	               for (Paket closestPaket : vozilo.getUkrcani_paketi()) {
	    	        	  double[] gpsKoordinate = closestPaket.vratiPrimatelja().dobaviUlicuOveOsobe().izračunajGpsKoordinate(closestPaket.vratiPrimatelja().getKbr());
	   	                  double dx = gpsKoordinate[1] - vozilo.getTrenutniLon();
	   	                  double dy = gpsKoordinate[0] - vozilo.getTrenutniLat();
	   	                  double udaljenost = Math.sqrt(dx * dx + dy * dy);
	   	                  if (udaljenost < minDistance) {
	   	                      minDistance = udaljenost * 111;
	   	                      paket = closestPaket;
	   	                      vozilo.setTrenutniLon(gpsKoordinate[1]);
	   	                      vozilo.setTrenutniLat(gpsKoordinate[0]);
	   	                  }
	   	               }
	   	               Duration duration = Duration.ofMinutes(Tvrtka.getInstance().getVi() + ((int) Math.round(minDistance/vozilo.getProsjecnaBrzina()*60)));
    	               Instant sada = vozilo.getDostavaSat().instant();
    	               Instant kasnije = sada.plus(duration);
    	               if(kasnije.isBefore(VirtualnoVrijeme.getVrijeme()) || kasnije.equals(VirtualnoVrijeme.getVrijeme())){
    	            	  vozilo.setDostavaSat(Clock.fixed(kasnije, ZoneId.systemDefault())); 
    	                  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
    	                  ZonedDateTime zdt = vozilo.getDostavaSat().instant().atZone(ZoneId.systemDefault());
    	                  String formatiraniDateTime = zdt.format(formatter);

    	                  System.out.println("Vozilo: " + vozilo.getOpis() + " je dostavilo paket: " + paket.getOznaka() + " Na vrijeme sata: " + formatiraniDateTime + " Trenutne koordinate: " + paket.vratiPrimatelja().dobaviUlicu(paket.vratiPrimatelja().getUlica()).getNaziv() + " KBR: " + paket.vratiPrimatelja().getKbr());
    	                  paket.getOvajPaket().notifyObservers("dostavljen");
    	                  
    	                  paket.setVrijeme_preuzimanja(vozilo.getVrijemeDostaveDateTime());
    	                  UredZaDostavu.getInstance().dodajDostavljeniPaket(paket);
    	                  UredZaPrijem.getInstance().nadodajNaUkupniIznosDostavu(paket.getIznos_pouzeca());
    	                  vozilo.setPrikupljeniNovac(vozilo.getPrikupljeniNovac() + paket.getIznos_pouzeca());
    	                  builder.dodajDostavljenePakete(paket);
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