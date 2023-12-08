package objekti;

import java.io.Serializable;
import java.text.ParseException;

import app.PogreskeBrojac;
import citaci.CsvObjekt;
import tvrtka.Tvrtka;

public class VrstaPaketa implements CsvObjekt, Serializable {
    private static final long serialVersionUID = 8987011609684897096L;
	private String oznaka;
    private String opis;
    private double visina;
    private double sirina;
    private double duzina;
    private double max_tezina;
	private double cijena;
    private double cijena_hitno;
    private double cijenaP;
    private double cijenaT;
    


    
    public VrstaPaketa(String oznaka, String opis, double visina, double sirina, double duzina, double max_tezina,
			double cijena, double cijena_hitno, double cijenaP, double cijenaT) {
		super();
		this.oznaka = oznaka;
		this.opis = opis;
		this.visina = visina;
		this.sirina = sirina;
		this.duzina = duzina;
		this.max_tezina = max_tezina;
		this.cijena = cijena;
		this.cijena_hitno = cijena_hitno;
		this.cijenaP = cijenaP;
		this.cijenaT = cijenaT;
	}


	public String getOznaka() {
		return oznaka;
	}


	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}


	public String getOpis() {
		return opis;
	}


	public void setOpis(String opis) {
		this.opis = opis;
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


	public double getMax_tezina() {
		return max_tezina;
	}


	public void setMax_tezina(double d) {
		this.max_tezina = d;
	}


	public double getCijena() {
		return cijena;
	}


	public void setCijena(double d) {
		this.cijena = d;
	}


	public double getCijena_hitno() {
		return cijena_hitno;
	}


	public void setCijena_hitno(double d) {
		this.cijena_hitno = d;
	}


	public double getCijenaP() {
		return cijenaP;
	}


	public void setCijenaP(double d) {
		this.cijenaP = d;
	}


	public double getCijenaT() {
		return cijenaT;
	}


	public void setCijenaT(double d) {
		this.cijenaT = d;
	}

	@Override
	public void process(String linija) throws ParseException {
		Tvrtka tvrtka = Tvrtka.getInstance();
		   try {
		       validate(linija);
		   } catch (ParseException e) {
		       PogreskeBrojac.getInstance().dodajPogresku(e.getMessage(), linija);
		       return;
		   }
		 String[] vrijednosti = linija.split(";");
         this.setOznaka(vrijednosti[0]);
         this.setOpis(vrijednosti[1]);
         this.setVisina(Double.parseDouble(vrijednosti[2].replace(",", ".")));
         this.setSirina(Double.parseDouble(vrijednosti[3].replace(",", ".")));
         this.setDuzina(Double.parseDouble(vrijednosti[4].replace(",", ".")));
         if(vrijednosti[0].equals("X"))
        	 this.setMax_tezina(tvrtka.getMt());
         else
        	 this.setMax_tezina(Double.parseDouble(vrijednosti[6].replace(",", ".")));
         this.setCijena(Double.parseDouble(vrijednosti[6].replace(",", ".")));
         this.setCijena_hitno(Double.parseDouble(vrijednosti[7].replace(",", ".")));
         this.setCijenaP(Double.parseDouble(vrijednosti[8].replace(",", ".")));
         this.setCijenaT(Double.parseDouble(vrijednosti[9].replace(",", ".")));
         

		
	}
	
	public boolean imaVrijednosti() {
		   return oznaka != null;
		}
	
	private void validate(String linija) throws ParseException {
		   String[] vrijednosti = linija.split(";");

		   if (vrijednosti.length != 10) {
		       throw new ParseException("Redak sadrži " + vrijednosti.length + " vrijednosti, ali se očekuje 10 vrijednosti.", 0);
		   }

		   for (int i = 2; i <= 9; i++) {
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
		}

}
