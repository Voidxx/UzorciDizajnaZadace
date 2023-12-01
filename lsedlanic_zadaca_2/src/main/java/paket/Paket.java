package paket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import app.PogreskeBrojac;
import citaci.CsvObjekt;
import tvrtka.UredZaPrijem;

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
        
        if(vrijednosti[4].equals("X")) {
        this.setVisina(Double.parseDouble(vrijednosti[5].replace(",", ".")));
        this.setSirina(Double.parseDouble(vrijednosti[6].replace(",", ".")));
        this.setDuzina(Double.parseDouble(vrijednosti[7].replace(",", ".")));
        }
        else {
        	VrstaPaketa vrstaPaketa = UredZaPrijem.getInstance().getVrstaPaketa(vrijednosti[4]);
            this.setVisina(vrstaPaketa.getVisina());
            this.setSirina(vrstaPaketa.getSirina());
            this.setDuzina(vrstaPaketa.getDuzina());
        }
        
        
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
	
	public boolean imaVrijednosti() {
		   return oznaka != null;
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
		   
		   if(!List.of("A", "B", "C", "D", "E", "X").contains(vrijednosti[4])) {
			   throw new ParseException("Redak sadrži " + vrijednosti[4] + " vrstu paketa, ali se očekuje A,B,C,D,E ili X.", 0);
		   }

		   for (int i = 0; i < 4; i++) {
		       if (vrijednosti[i].trim().isEmpty()) {
		           throw new ParseException("Nepostojeće polje na indeksu:  " + i, 0);
		       }
		   }

		   if (!Arrays.asList("S", "H", "P", "R").contains(vrijednosti[9])) {
		       throw new ParseException("Nevažeća 'usluga_dostave': " + vrijednosti[9], 0);
		   }
		   

		   
		   VrstaPaketa vrstaPaketa = UredZaPrijem.getInstance().getVrstaPaketa(vrijednosti[4]);
		   if (vrstaPaketa != null) {
		   double maxTezina = vrstaPaketa.getMax_tezina();
		   if (Double.parseDouble(vrijednosti[8].replace(",", ".")) > maxTezina) {	
		       throw new ParseException("Vrijednost tezine premašuje maksimalnu vrijednost za vrstu paketa '" + vrstaPaketa.getOznaka() + "'.", 0);
		   	}		   
		   }
		   if (vrijednosti[4].equals("X")) {
		       VrstaPaketa vrstaPaketaX = UredZaPrijem.getInstance().getVrstaPaketa("X");
		       if (vrstaPaketaX != null) {
		           double maxSirina = vrstaPaketaX.getSirina();
		           double maxDuzina = vrstaPaketaX.getDuzina();
		           double maxVisina = vrstaPaketaX.getVisina();

		           if (Double.parseDouble(vrijednosti[6].replace(",", ".")) > maxSirina || Double.parseDouble(vrijednosti[7].replace(",", ".")) > maxDuzina || Double.parseDouble(vrijednosti[5].replace(",", ".")) > maxVisina) {
		               throw new ParseException("Vrijednosti sirine, duzine ili visine premašuju maksimalne vrijednosti za vrstu paketa 'X'.", 0);
		           }
		       }
		   }

		   if(!vrijednosti[9].equals("P") && Double.parseDouble(vrijednosti[10].replace(",", ".")) != 0) {
			   throw new ParseException("Ne može se odrediti iznos pouzeća za uslugu dostave koja nije P!", 0);
		   }
		   
		   if(!vrijednosti[4].equals("X")) {
			   if(Double.parseDouble(vrijednosti[5].replace(",", ".")) != 0 || Double.parseDouble(vrijednosti[6].replace(",", ".")) != 0 || Double.parseDouble(vrijednosti[7].replace(",", ".")) != 0) {
				   throw new ParseException("Vrijednosti visine, sirine i duzine kod tipskih paketa su predodređene i nemogu se mijenjati.", 0);
			   }
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
