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
import voznja.SegmentVoznje;
import voznja.VoznjaBuilder;

public class NajblizaDostava implements DostavaStrategija{

	@Override
	   public void odrediRedoslijed(Vozilo vozilo, VoznjaBuilder builder) {
		   boolean imaJosPaketaUVremenskomPeriodu = true;
		   VirtualnoVrijeme virtualnoVrijeme = VirtualnoVrijeme.getInstance();
    	   while (imaJosPaketaUVremenskomPeriodu) {
    	       if (vozilo.getDostavaSat() != null) {
    	           int brojPaketa = vozilo.getUkrcani_paketi().size();
    	           if (brojPaketa > 0) {
   	                   Paket paket = null;
	   	               double minDistance = Double.MAX_VALUE;
 	                  double[] trenutneKoordinate = new double[] {vozilo.getTrenutniLon(), vozilo.getTrenutniLat()};
 	                  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
 	                  double[] koordinatePaketa = new double[] {0, 0};
 	                  double udaljenostPaketa = 0;
 	                  String pocetniDateTime = null;
	   	               for (Paket closestPaket : vozilo.getUkrcani_paketi()) {
	    	        	  double[] gpsKoordinatePaketa = closestPaket.vratiPrimatelja().dobaviUlicuOveOsobe().izračunajGpsKoordinate(closestPaket.vratiPrimatelja().getKbr());
	   	                  double[] gpsKoordinatePocetkaUlice = new double[]{closestPaket.vratiPrimatelja().dobaviUlicuOveOsobe().getGps_lat_1(), closestPaket.vratiPrimatelja().dobaviUlicuOveOsobe().getGps_lon_1()};
	   	                  double dx = gpsKoordinatePocetkaUlice[1] - vozilo.getTrenutniLon();
	   	                  double dy = gpsKoordinatePocetkaUlice[0] - vozilo.getTrenutniLat();
	   	                  double udaljenostDoPocetkaUlice = Math.sqrt(dx * dx + dy * dy)*111;
	   	                  double dx2 = gpsKoordinatePaketa[1] - gpsKoordinatePocetkaUlice[1];
	   	                  double dy2 = gpsKoordinatePaketa[0] - gpsKoordinatePocetkaUlice[0];
	   	                  double udaljenostOdPocetkaDoPaketa = Math.sqrt(dx2 * dx2 + dy2 * dy2)*111;
	   	                  double udaljenost = udaljenostDoPocetkaUlice + udaljenostOdPocetkaDoPaketa;
	   	                  
	   	                  if(vozilo.getTrenutniLon() == gpsKoordinatePaketa[1] && vozilo.getTrenutniLat() == gpsKoordinatePaketa[0]) {
	   	                	  udaljenost = 0;
	   	                  }
	   	                  
	 	                  ZonedDateTime zdt1 = vozilo.getDostavaSat().instant().atZone(ZoneId.systemDefault());
	 	                  String formatiraniDateTime1 = zdt1.format(formatter);
	 	                  pocetniDateTime = formatiraniDateTime1;
	 	                  
	   	                  if (udaljenost < minDistance) {
		   	                  koordinatePaketa[0] = gpsKoordinatePaketa[0];
		   	                  koordinatePaketa[1] = gpsKoordinatePaketa[1];
		   	                  minDistance = udaljenost;
		   	                  udaljenostPaketa = udaljenost;
	   	                      paket = closestPaket;
	   	                      
	   	                  }
	   	               }
		   	           double durationInSeconds = minDistance / vozilo.getProsjecnaBrzina() * 60 * 60;
		   	           Duration trajanjeUSekundama = Duration.ofSeconds((long) durationInSeconds);
		   	           long seconds = trajanjeUSekundama.toSeconds();
	   	               Duration duration = Duration.ofSeconds(Tvrtka.getInstance().getVi()*60 + seconds);
    	               Instant sada = vozilo.getDostavaSat().instant();
    	               Instant kasnije = sada.plus(duration);
    	               if(kasnije.isBefore(virtualnoVrijeme.getVrijeme()) || kasnije.equals(virtualnoVrijeme.getVrijeme())){
    	            	  izvrsiDostavu(vozilo, builder, paket, trenutneKoordinate, formatter, koordinatePaketa,
								udaljenostPaketa, pocetniDateTime, kasnije);
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

	private void izvrsiDostavu(Vozilo vozilo, VoznjaBuilder builder, Paket paket, double[] trenutneKoordinate,
			DateTimeFormatter formatter, double[] koordinatePaketa, double udaljenostPaketa, String pocetniDateTime,
			Instant kasnije) {
		vozilo.setDostavaSat(Clock.fixed(kasnije, ZoneId.systemDefault())); 
		  ZonedDateTime zdt2 = vozilo.getDostavaSat().instant().atZone(ZoneId.systemDefault());
		  String formatiraniDateTime2 = zdt2.format(formatter);
		  SegmentVoznje segment = new SegmentVoznje(trenutneKoordinate, koordinatePaketa, udaljenostPaketa, pocetniDateTime, formatiraniDateTime2, ((int) Math.round(udaljenostPaketa/vozilo.getProsjecnaBrzina()*60)), Tvrtka.getInstance().getVi() , 0 , paket);
		  segment.izracunajTrajanjeSegmenta();
		  builder.dodajSegmentVoznje(segment);
		  vozilo.setTrenutniLon(koordinatePaketa[1]);
		  vozilo.setTrenutniLat(koordinatePaketa[0]);
		  System.out.println("Vozilo: " + vozilo.getOpis() + " je dostavilo paket: " + paket.getOznaka() + " Na vrijeme sata: " + formatiraniDateTime2 + " Trenutne koordinate: " + paket.vratiPrimatelja().dobaviUlicu(paket.vratiPrimatelja().getUlica()).getNaziv() + " KBR: " + paket.vratiPrimatelja().getKbr());
		  paket.getOvajPaket().notifyObservers("dostavljen");
		  vozilo.dodajPaketUIsporucene();
		  vozilo.dodajKmNaUkupno(udaljenostPaketa);
		  builder.dodajNaUkupnoKM(udaljenostPaketa);
		  
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