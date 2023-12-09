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
	    	        	  double[] gpsKoordinatePaketa = paket.vratiPrimatelja().dobaviUlicuOveOsobe().izračunajGpsKoordinate(paket.vratiPrimatelja().getKbr());
	   	                  double[] gpsKoordinatePocetkaUlice = new double[]{paket.vratiPrimatelja().dobaviUlicuOveOsobe().getGps_lat_1(), paket.vratiPrimatelja().dobaviUlicuOveOsobe().getGps_lon_1()};
	   	                  double dx = gpsKoordinatePocetkaUlice[1] - vozilo.getTrenutniLon();
	   	                  double dy = gpsKoordinatePocetkaUlice[0] - vozilo.getTrenutniLat();
	   	                  double udaljenostDoPocetkaUlice = Math.sqrt(dx * dx + dy * dy)*111;
	   	                  double dx2 = gpsKoordinatePaketa[1] - gpsKoordinatePocetkaUlice[1];
	   	                  double dy2 = gpsKoordinatePaketa[0] - gpsKoordinatePocetkaUlice[0];
	   	                  double udaljenostOdPocetkaDoPaketa = Math.sqrt(dx2 * dx2 + dy2 * dy2)*111;
	   	                  double udaljenost = udaljenostDoPocetkaUlice + udaljenostOdPocetkaDoPaketa;
 	                  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
 	                  ZonedDateTime zdt1 = vozilo.getDostavaSat().instant().atZone(ZoneId.systemDefault());
 	                  String formatiraniDateTime1 = zdt1.format(formatter);
   	                  if(vozilo.getTrenutniLon() == gpsKoordinatePaketa[1] && vozilo.getTrenutniLat() == gpsKoordinatePaketa[0]) {
   	                	  udaljenost = 0;
   	                  }
   	                  
		   	           double durationInSeconds = udaljenost / vozilo.getProsjecnaBrzina() * 60 * 60;
		   	           Duration trajanjeUSekundama = Duration.ofSeconds((long) durationInSeconds);
		   	           long seconds = trajanjeUSekundama.toSeconds();
	   	               Duration duration = Duration.ofSeconds(Tvrtka.getInstance().getVi()*60 + seconds);
    	               Instant sada = vozilo.getDostavaSat().instant();
    	               Instant kasnije = sada.plus(duration);
    	               
    	               if(kasnije.isBefore(virtualnoVrijeme.getVrijeme()) || kasnije.equals(virtualnoVrijeme.getVrijeme())){
    	            	  izvrsiDostavu(vozilo, builder, paket, gpsKoordinatePaketa, udaljenost, formatter,
								formatiraniDateTime1, kasnije);
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

	private void izvrsiDostavu(Vozilo vozilo, VoznjaBuilder builder, Paket paket, double[] gpsKoordinatePaketa,
			double udaljenost, DateTimeFormatter formatter, String formatiraniDateTime1, Instant kasnije) {
		vozilo.setDostavaSat(Clock.fixed(kasnije, ZoneId.systemDefault())); 
		  ZonedDateTime zdt2 = vozilo.getDostavaSat().instant().atZone(ZoneId.systemDefault());
		  String formatiraniDateTime2 = zdt2.format(formatter);
		  double[] trenutneKoordinate = new double[] {vozilo.getTrenutniLon(), vozilo.getTrenutniLat()};
		  SegmentVoznje segment = new SegmentVoznje(trenutneKoordinate, gpsKoordinatePaketa, udaljenost, formatiraniDateTime1, formatiraniDateTime2, ((int) Math.round(udaljenost/vozilo.getProsjecnaBrzina()*60)), Tvrtka.getInstance().getVi() , 0 , paket);
		  segment.izracunajTrajanjeSegmenta();
		  builder.dodajSegmentVoznje(segment);
		  vozilo.setTrenutniLon(gpsKoordinatePaketa[1]);
		  vozilo.setTrenutniLat(gpsKoordinatePaketa[0]);
		  System.out.println("Vozilo: " + vozilo.getOpis() + " je dostavilo paket: " + paket.getOznaka() + " Na vrijeme sata: " + formatiraniDateTime2 + " Trenutne koordinate: " + paket.vratiPrimatelja().dobaviUlicu(paket.vratiPrimatelja().getUlica()).getNaziv() + " KBR: " + paket.vratiPrimatelja().getKbr());
		  paket.getOvajPaket().notifyObservers("dostavljen");
		  vozilo.dodajPaketUIsporucene();
		  vozilo.dodajKmNaUkupno(udaljenost);
		  builder.dodajNaUkupnoKM(udaljenost);
		  
		  paket.setVrijeme_preuzimanja(vozilo.getVrijemeDostaveDateTime());
		  UredZaDostavu.getInstance().dodajDostavljeniPaket(paket);
		  UredZaPrijem.getInstance().nadodajNaUkupniIznosDostavu(paket.getIznos_pouzeca());
		  vozilo.setPrikupljeniNovac(vozilo.getPrikupljeniNovac() + paket.getIznos_pouzeca());
		  builder.dodajDostavljenePakete(paket);
		  vozilo.setTrenutni_teret_tezina(vozilo.getTrenutni_teret_tezina() - paket.getTezina());
		  double volumenPaketa = paket.getVisina() * paket.getSirina() * paket.getDuzina();
		  vozilo.setTrenutni_teret_volumen(vozilo.getTrenutni_teret_volumen() - volumenPaketa);
		  vozilo.getUkrcani_paketi().remove(paket);
	}
	}