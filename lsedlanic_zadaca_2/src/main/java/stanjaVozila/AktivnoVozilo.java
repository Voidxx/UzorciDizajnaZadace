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
import objekti.Osoba;
import objekti.Paket;
import objekti.Vozilo;
import tvrtka.Tvrtka;
import tvrtka.UredZaPrijem;
import voznja.SegmentVoznje;
import voznja.Voznja;
import voznja.VoznjaBuilder;

public class AktivnoVozilo implements Stanje, Serializable{
	private static final long serialVersionUID = 864820141663529933L;
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
					VirtualnoVrijeme virtualnoVrijeme = VirtualnoVrijeme.getInstance();

	                double tezinaPaketa = paket.getTezina();
	                double volumenPaketa = paket.getVisina() * paket.getSirina() * paket.getDuzina();
	                if ((tezinaPaketa + vozilo.getTrenutni_teret_tezina() <= vozilo.getKapacitet_kg() && volumenPaketa + vozilo.getTrenutni_teret_volumen() <= vozilo.getKapacitet_m3())) {
	                    vozilo.setTrenutni_teret_tezina(vozilo.getTrenutni_teret_tezina() + tezinaPaketa);
	                    vozilo.setTrenutni_teret_volumen(vozilo.getTrenutni_teret_volumen() + volumenPaketa);
	                    vozilo.dodajPaketUVozilo(paket);
	                    builder.dodajUkrcanePakete(paket);
	                    System.out.println("Dodan paket: " + paket.getOznaka() + " u vozilo: " + vozilo.getOpis() + " na vrijeme: " + virtualnoVrijeme.getVrijemeDateTime() + " - " + "Trenutni teret: KG " + vozilo.getTrenutni_teret_tezina() + " Volumen " + vozilo.getTrenutni_teret_volumen());
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
	   VirtualnoVrijeme virtualnoVrijeme = VirtualnoVrijeme.getInstance();
	   String gpsKoordinateString[] = Tvrtka.getInstance().getGps().split(",");
 	   double[] gpsKoordinate = new double[] {0 , 0};
 	   int i = 0;
	   for(String koordinata : gpsKoordinateString) {
		   gpsKoordinate[i] = Double.parseDouble(koordinata);
		   i++;
	   }
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
          SegmentVoznje segment = new SegmentVoznje(trenutneKoordinate, gpsKoordinate, udaljenost, formatiraniDateTime1, formatiraniDateTime2, ((int) Math.round(udaljenost/vozilo.getProsjecnaBrzina()*60)), Tvrtka.getInstance().getVi() , 0 , null);
          builder.dodajSegmentVoznje(segment);
          vozilo.setTrenutniLon(gpsKoordinate[1]);
          vozilo.setTrenutniLat(gpsKoordinate[0]);
          
           vozilo.dodajKmNaUkupno(udaljenost);
           builder.dodajNaUkupnoKM(udaljenost);
	   	   builder.postaviVrijemePovratka(formatiraniDateTime2);
	   	   builder.postaviTrajanje();
           vozilo.setDostavaSat(null);
           vozilo.setTrenutno_vozi(false);
           System.out.println("Vozilo: " + vozilo.getOpis() + " je završilo sa dostavama.");
           Voznja voznja = builder.build();
           vozilo.dodajObavljenuVoznju(voznja);
           System.out.println("Vozilo: " + vozilo.getOpis() + " se vratilo u ured na vrijeme sata: " + formatiraniDateTime2);

       }

        return false;
		
	}

}
