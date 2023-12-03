package objekti;

import java.text.ParseException;

import app.PogreskeBrojac;
import citaci.CsvObjekt;
import tvrtka.Tvrtka;

public class Osoba implements CsvObjekt{
	private String osoba;
	private int grad;
	private int ulica;
	private int kbr;

	
	
	
	public String getOsoba() {
		return osoba;
	}

	public void setOsoba(String osoba) {
		this.osoba = osoba;
	}

	public int getGrad() {
		return grad;
	}
	
	public Mjesto dobaviMjesto(int id) {
		for(Mjesto mjesto : Tvrtka.getInstance().getMjesta()) {
			if(mjesto.getId() == id)
				return mjesto;
		}
		return null;
	}
	
	public Podrucje dobaviPodrucje() {
		for(Podrucje podrucje : Tvrtka.getInstance().getPodrucja()) {
			if(podrucje.getChildren().contains(this.dobaviMjesto(this.getGrad())) && podrucje.getChildren().contains(this.dobaviUlicu(this.getUlica()))) {
				return podrucje;
			}
		}
		return null;
	}

	public void setGrad(int grad) {
		this.grad = grad;
	}

	public int getUlica() {
		return ulica;
	}
	
	public Ulica dobaviUlicu(int id) {
		for(Ulica ulica : Tvrtka.getInstance().getUlice()) {
			if(ulica.getId() == id)
				return ulica;
		}
		return null;
	}

	public void setUlica(int ulica) {
		this.ulica = ulica;
	}

	public int getKbr() {
		return kbr;
	}

	public void setKbr(int kbr) {
		this.kbr = kbr;
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
		
		this.setOsoba(vrijednosti[0]);
		this.setGrad(Integer.parseInt(vrijednosti[1]));
		this.setUlica(Integer.parseInt(vrijednosti[2]));
		this.setKbr(Integer.parseInt(vrijednosti[3]));
	}
	
	private void validate(String linija) throws ParseException {
		   String[] vrijednosti = linija.split(";");

		   if (vrijednosti.length != 4) {
		       throw new ParseException("Redak sadrži " + vrijednosti.length + " vrijednosti, ali se očekuje 4 vrijednosti.", 0);
		   }

		   for (int i = 0; i < 4; i++) {
		       if (vrijednosti[i].trim().isEmpty()) {
		           throw new ParseException("Nepostojeće polje na indeksu: " + i, 0);
		       }
		   }

		   try {
		       Integer.parseInt(vrijednosti[1]);
		       Integer.parseInt(vrijednosti[2]);
		       Integer.parseInt(vrijednosti[3]);
		   } catch (NumberFormatException e) {
		       throw new ParseException("Nevažeći integer na indeksu: " + e.getMessage(), 0);
		   }
		}

	@Override
	public boolean imaVrijednosti() {
		return this.osoba != null;
	}

}
