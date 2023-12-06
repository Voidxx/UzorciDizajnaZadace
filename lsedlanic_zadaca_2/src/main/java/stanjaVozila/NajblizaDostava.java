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
import stanjePaketa.Pošiljatelj;
import stanjePaketa.Primatelj;
import stanjePaketa.Subject;
import tvrtka.Tvrtka;
import tvrtka.UredZaDostavu;
import tvrtka.UredZaPrijem;
import voznja.VoznjaBuilder;

public class NajblizaDostava implements DostavaStrategija {
	   @Override
	   public void odrediRedoslijed(Vozilo vozilo, VoznjaBuilder builder) {
		   boolean imaJosPaketaUVremenskomPeriodu = true;
		   Subject subject = new Subject();
    	   while (imaJosPaketaUVremenskomPeriodu) {
    	       if (vozilo.getDostavaSat() != null) {
    	           int brojPaketa = vozilo.getUkrcani_paketi().size();
    	           if (brojPaketa > 0) {
    	               Duration duration = Duration.ofMinutes(Tvrtka.getInstance().getVi());
    	               Instant sada = vozilo.getDostavaSat().instant();
    	               Instant kasnije = sada.plus(duration);
    	               if(kasnije.isBefore(VirtualnoVrijeme.getVrijeme()) || kasnije.equals(VirtualnoVrijeme.getVrijeme())){
    	            	  vozilo.setDostavaSat(Clock.fixed(kasnije, ZoneId.systemDefault())); 
    	                  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
    	                  ZonedDateTime zdt = vozilo.getDostavaSat().instant().atZone(ZoneId.systemDefault());
    	                  String formatiraniDateTime = zdt.format(formatter);
      	                  Paket paket = vozilo.uzmiIzReda();
    	                  //obavijestavanje
    	                  Pošiljatelj posiljatelj = new Pošiljatelj(paket.getPosiljatelj());
    	                  Primatelj primatelj = new Primatelj(paket.getPrimatelj());
    	                  
    	                  subject.attach(primatelj);
    	                  subject.attach(posiljatelj);
    	                  subject.setPaket(paket);
    	                  double[] gpsKoordinate = paket.vratiPrimatelja().dobaviUlicu(paket.vratiPrimatelja().getUlica()).izračunajGpsKoordinate(paket.vratiPrimatelja().getKbr());
    	                  
    	                  vozilo.setTrenutniLon(gpsKoordinate[0]);
    	                  vozilo.setTrenutniLat(gpsKoordinate[1]);
    	                  System.out.println("Vozilo: " + vozilo.getOpis() + " je dostavilo paket: " + paket.getOznaka() + " Na vrijeme sata: " + formatiraniDateTime + " Trenutne koordinate: " + paket.vratiPrimatelja().dobaviUlicu(brojPaketa).getNaziv() + " KBR: " + paket.vratiPrimatelja().getKbr());

    	                  
    	                  vozilo.getUkrcani_paketi().get(0).setVrijeme_preuzimanja(vozilo.getVrijemeDostaveDateTime());
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