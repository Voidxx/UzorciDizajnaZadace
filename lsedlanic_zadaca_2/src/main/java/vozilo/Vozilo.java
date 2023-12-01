package vozilo;

import java.text.ParseException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import app.PogreskeBrojac;
import app.VirtualnoVrijeme;
import citaci.CsvObjekt;
import paket.Paket;
import tvrtka.UredZaDostavu;
import tvrtka.UredZaPrijem;

public class Vozilo implements CsvObjekt {
    private String registracija;
    private String opis;
    private double kapacitet_kg;
    private double kapacitet_m3;
    private double trenutni_teret_tezina;
    private double trenutni_teret_volumen;
    private List<Paket> ukrcani_paketi;
    private int redoslijed;
    private boolean trenutno_vozi;
    private Clock dostavaSat;
    private double prikupljeniNovac;
    private int prosjecnaBrzina;
    private String status;
    private String podrucjaPoRangu;
    


	
	
	public Vozilo(String registracija, String opis, double kapacitet_kg, double kapacitet_m3,
			double trenutni_teret_tezina, double trenutni_teret_volumen, List<Paket> ukrcani_paketi, int redoslijed,
			boolean trenutno_vozi, Clock dostavaSat, double prikupljeniNovac) {
		this.registracija = registracija;
		this.opis = opis;
		this.kapacitet_kg = kapacitet_kg;
		this.kapacitet_m3 = kapacitet_m3;
		this.trenutni_teret_tezina = trenutni_teret_tezina;
		this.trenutni_teret_volumen = trenutni_teret_volumen;
		this.ukrcani_paketi = ukrcani_paketi;
		this.redoslijed = redoslijed;
		this.trenutno_vozi = trenutno_vozi;
		this.dostavaSat = dostavaSat;
		this.setPrikupljeniNovac(prikupljeniNovac);
	}
	
	
	public int getProsjecnaBrzina() {
		return prosjecnaBrzina;
	}


	public void setProsjecnaBrzina(int prosjecnaBrzina) {
		this.prosjecnaBrzina = prosjecnaBrzina;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getPodrucjaPoRangu() {
		return podrucjaPoRangu;
	}


	public void setPodrucjaPoRangu(String podrucjaPoRangu) {
		this.podrucjaPoRangu = podrucjaPoRangu;
	}


	public String getRegistracija() {
		return registracija;
	}
	public void setRegistracija(String registracija) {
		this.registracija = registracija;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	public double getKapacitet_kg() {
		return kapacitet_kg;
	}
	public void setKapacitet_kg(double d) {
		this.kapacitet_kg = d;
	}
	public double getKapacitet_m3() {
		return kapacitet_m3;
	}
	public void setKapacitet_m3(double d) {
		this.kapacitet_m3 = d;
	}
	public int getRedoslijed() {
		return redoslijed;
	}
	public void setRedoslijed(int redoslijed) {
		this.redoslijed = redoslijed;
	}

	@Override
	public void process(String linija) throws ParseException {
		 try {
		     validate(linija);
		 } catch (ParseException e) {
		     PogreskeBrojac.getInstance().dodajPogresku(e.getMessage(), linija);
		     return;
		 }
		 String[] vrijednosti = linija.split(";");
         this.setRegistracija(vrijednosti[0]);
         this.setOpis(vrijednosti[1]);
         this.setKapacitet_kg(Double.parseDouble(vrijednosti[2].replace(",", ".")));
         this.setKapacitet_m3(Double.parseDouble(vrijednosti[3].replace(",", ".")));
         this.setRedoslijed(Integer.parseInt(vrijednosti[4]));
         
		
	}
	
	public boolean imaVrijednosti() {
		   return registracija != null;
		}
	
	
	private void validate(String linija) throws ParseException {
		   String[] vrijednosti = linija.split(";");

		   if (vrijednosti.length != 8) {
		       throw new ParseException("Redak sadrži " + vrijednosti.length + " vrijednosti, ali se očekuje 8 vrijednosti.", 0);
		   }

		   for (int i = 2; i <= 3; i++) {
		       try {
		           Double.parseDouble(vrijednosti[i].replace(",", "."));
		       } catch (NumberFormatException e) {
		           throw new ParseException("Nevažeča vrijednost broja na indeksu:  " + i + ": " + e.getMessage(), 0);
		       }
		   }

		   for (int i = 0; i < 2; i++) {
		       if (vrijednosti[i].trim().isEmpty()) {
		           throw new ParseException("Nepostojeće polje na indeksu:  " + i, 0);
		       }
		   }

		   try {
		       Integer.parseInt(vrijednosti[4]);
		   } catch (NumberFormatException e) {
		       throw new ParseException("Nevažeći integer na indeksu 4: " + e.getMessage(), 0);
		   }
		}

	public double getTrenutni_teret_volumen() {
		return trenutni_teret_volumen;
	}

	public void setTrenutni_teret_volumen(double trenutni_teret_volumen) {
		this.trenutni_teret_volumen = trenutni_teret_volumen;
	}

	public double getTrenutni_teret_tezina() {
		return trenutni_teret_tezina;
	}

	public void setTrenutni_teret_tezina(double trenutni_teret_tezina) {
		this.trenutni_teret_tezina = trenutni_teret_tezina;
	}

	public List<Paket> getUkrcani_paketi() {
		return ukrcani_paketi;
	}

	public void setUkrcani_paketi(List<Paket> ukrcani_paketi) {
		this.ukrcani_paketi = ukrcani_paketi;
	}
	
	public void dodajPaketUVozilo(Paket paket) {
		this.ukrcani_paketi.add(paket);
	}
	
	public void makniPaketIzVozila(Paket paket) {
		this.ukrcani_paketi.remove(paket);
	}

	public boolean isTrenutno_vozi() {
		return trenutno_vozi;
	}

	public void setTrenutno_vozi(boolean trenutno_vozi) {
		this.trenutno_vozi = trenutno_vozi;
	}


	public Clock getDostavaSat() {
		return dostavaSat;
	}


	public void setDostavaSat(Clock dostavaSat) {
		this.dostavaSat = dostavaSat;
	}
	
    public Instant getVrijeme() {
        Instant sada = this.dostavaSat.instant();
        return sada;
    }
	
    public String getVrijemeDostaveDateTime() {
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
    	Instant sada = dostavaSat.instant();

    	ZonedDateTime zonedDateTime = sada.atZone(ZoneId.systemDefault());
    	LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();

    	String formatiraniDateTime = localDateTime.format(formatter);
        return formatiraniDateTime;
    }
    
    public void azurirajDostavu(int vi) {
    	   boolean imaJosPaketaUVremenskomPeriodu = true;
    	   while (imaJosPaketaUVremenskomPeriodu) {
    	       if (dostavaSat != null) {
    	           int brojPaketa = getUkrcani_paketi().size();
    	           if (brojPaketa > 0) {
    	               Duration duration = Duration.ofMinutes(vi);
    	               Instant sada = dostavaSat.instant();
    	               Instant kasnije = sada.plus(duration);
    	               if(kasnije.isBefore(VirtualnoVrijeme.getVrijeme()) || kasnije.equals(VirtualnoVrijeme.getVrijeme())){
    	            	  dostavaSat = Clock.fixed(kasnije, ZoneId.systemDefault());
    	                  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
    	                  ZonedDateTime zdt = dostavaSat.instant().atZone(ZoneId.systemDefault());
    	                  String formatiraniDateTime = zdt.format(formatter);

    	                  System.out.println("Vozilo: " + this.opis + " je dostavilo paket: " + this.ukrcani_paketi.get(0).getOznaka() + " Na vrijeme sata: " + formatiraniDateTime);
    	                  this.ukrcani_paketi.get(0).setVrijeme_preuzimanja(this.getVrijemeDostaveDateTime());
    	                  UredZaDostavu.getInstance().dodajDostavljeniPaket(this.ukrcani_paketi.get(0));
    	                  UredZaPrijem.getInstance().nadodajNaUkupniIznosDostavu(this.ukrcani_paketi.get(0).getIznos_pouzeca());
    	                  this.prikupljeniNovac = this.prikupljeniNovac + this.ukrcani_paketi.get(0).getIznos_pouzeca();
    	                  this.ukrcani_paketi.remove(0);
    	               }
    	               else {
    	            	   imaJosPaketaUVremenskomPeriodu = false;
    	               }
    	           }
    	           else {
    	               dostavaSat = null;
    	               this.trenutno_vozi = false;
    	               System.out.println("Vozilo: " + this.opis + " je završilo sa dostavama.");
    	               imaJosPaketaUVremenskomPeriodu = false;
    	           }
    	       }
    	       else {
    	    	   imaJosPaketaUVremenskomPeriodu = false;
    	       }
    	   }
    	}

	public double getPrikupljeniNovac() {
		return prikupljeniNovac;
	}

	public void setPrikupljeniNovac(double prikupljeniNovac) {
		this.prikupljeniNovac = prikupljeniNovac;
	}
	
	public void dodajNovac(double novac) {
		this.prikupljeniNovac = this.prikupljeniNovac + novac;
	}

}
