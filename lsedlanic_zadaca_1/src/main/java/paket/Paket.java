package paket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import app.PogreskeBrojac;
import citaci.CsvObjekt;
import citaci.CsvUtils;

public class Paket implements CsvObjekt{
    private String oznaka;
    private String vrijeme_prijema;
    private String posiljatelj;
    private String primatelj;
    private String vrsta_paketa;
    private double visina;
	private double sirina;
    private double duzina;
    private double tezina;
    private String usluga_dostave;
    private double iznos_pouzeca;
    private double izracunati_iznos_dostave;
    private String vrijeme_preuzimanja;
    
    

	public Paket(String oznaka, String vrijeme_prijema, String posiljatelj, String primatelj, String vrsta_paketa,
			double visina, double sirina, double duzina, double tezina, String usluga_dostave, double iznos_pouzeca,
			double izracunati_iznos_dostave, String vrijeme_preuzimanja) {
		super();
		this.oznaka = oznaka;
		this.vrijeme_prijema = vrijeme_prijema;
		this.posiljatelj = posiljatelj;
		this.primatelj = primatelj;
		this.vrsta_paketa = vrsta_paketa;
		this.visina = visina;
		this.sirina = sirina;
		this.duzina = duzina;
		this.tezina = tezina;
		this.usluga_dostave = usluga_dostave;
		this.iznos_pouzeca = iznos_pouzeca;
		this.izracunati_iznos_dostave = izracunati_iznos_dostave;
		this.vrijeme_preuzimanja = vrijeme_preuzimanja;
	}
	public double getIzracunati_iznos_dostave() {
		return izracunati_iznos_dostave;
	}
	public void setIzracunati_iznos_dostave(double izracunati_iznos_dostave) {
		this.izracunati_iznos_dostave = izracunati_iznos_dostave;
	}	

	public String getOznaka() {
		return oznaka;
	}
	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}
	public String getVrijeme_prijema() {
		return vrijeme_prijema;
	}
	public void setVrijeme_prijema(String vrijeme_prijema) {
		this.vrijeme_prijema = vrijeme_prijema;
	}
	public String getPosiljatelj() {
		return posiljatelj;
	}
	public void setPosiljatelj(String posiljatelj) {
		this.posiljatelj = posiljatelj;
	}
	public String getPrimatelj() {
		return primatelj;
	}
	public void setPrimatelj(String primatelj) {
		this.primatelj = primatelj;
	}
	public String getVrsta_paketa() {
		return vrsta_paketa;
	}
	public void setVrsta_paketa(String vrsta_paketa) {
		this.vrsta_paketa = vrsta_paketa;
	}
	public double getVisina() {
		return visina;
	}
	public void setVisina(double d) {
		this.visina = d;
	}
	public double getSirina() {
		return sirina;
	}
	public void setSirina(double d) {
		this.sirina = d;
	}
	public double getDuzina() {
		return duzina;
	}
	public void setDuzina(double d) {
		this.duzina = d;
	}
	public double getTezina() {
		return tezina;
	}
	public void setTezina(double d) {
		this.tezina = d;
	}
	public String getUsluga_dostave() {
		return usluga_dostave;
	}
	public void setUsluga_dostave(String usluga_dostave) {
		this.usluga_dostave = usluga_dostave;
	}
	public double getIznos_pouzeca() {
		return iznos_pouzeca;
	}
	public void setIznos_pouzeca(double d) {
		this.iznos_pouzeca = d;
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
		
        this.setOznaka(vrijednosti[0]);
        this.setVrijeme_prijema(vrijednosti[1]);
        this.setPosiljatelj(vrijednosti[2]);
        this.setPrimatelj(vrijednosti[3]);
        this.setVrsta_paketa(vrijednosti[4]);
        this.setVisina(Double.parseDouble(vrijednosti[5].replace(",", ".")));
        this.setSirina(Double.parseDouble(vrijednosti[6].replace(",", ".")));
        this.setDuzina(Double.parseDouble(vrijednosti[7].replace(",", ".")));
        this.setTezina(Double.parseDouble(vrijednosti[8].replace(",", ".")));
        this.setUsluga_dostave(vrijednosti[9]);
        this.setIznos_pouzeca(Double.parseDouble(vrijednosti[10].replace(",", ".")));
		
	}
	public String getVrijeme_preuzimanja() {
		return vrijeme_preuzimanja;
	}
	public void setVrijeme_preuzimanja(String vrijeme_preuzimanja) {
		this.vrijeme_preuzimanja = vrijeme_preuzimanja;
	}
	
	private void validate(String linija) throws ParseException {
		   String[] vrijednosti = linija.split(";");

		   if (vrijednosti.length != 11) {
			   throw new ParseException("Redak sadrži " + vrijednosti.length + " vrijednosti, ali se očekuje 11 vrijednosti.", 0);
		   }

		   for (int i = 5; i <= 8; i++) {
		       try {
		           Double.parseDouble(vrijednosti[i].replace(",", "."));
		       } catch (NumberFormatException e) {
		           throw new ParseException("Nevažeča vrijednost broja na indeksu:  " + i + ": " + e.getMessage(), 0);
		       }
		   }

		   for (int i = 0; i < 4; i++) {
		       if (vrijednosti[i].trim().isEmpty()) {
		           throw new ParseException("Nepostojeće polje na indeksu:  " + i, 0);
		       }
		   }

		   if (!Arrays.asList("S", "H", "P").contains(vrijednosti[9])) {
		       throw new ParseException("Nevažeća 'usluga_dostave': " + vrijednosti[9], 0);
		   }

		   SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
		   sdf.setLenient(false);
		   try {
		       sdf.parse(vrijednosti[1]);
		   } catch (ParseException e) {
		       throw new ParseException("Nevažeči date format u 'vrijeme_prijema': " + vrijednosti[1], 0);
		   }
		}
    
}
