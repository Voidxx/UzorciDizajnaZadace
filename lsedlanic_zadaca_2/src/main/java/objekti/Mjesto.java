package objekti;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import app.PogreskeBrojac;
import citaci.CsvObjekt;
import tvrtka.Tvrtka;

public class Mjesto implements CsvObjekt, DioPodrucja, Serializable{
	private static final long serialVersionUID = 1288863708856800208L;
	private int id;
	private String naziv;
	private ArrayList<Integer> uliceUBrojevima = new ArrayList<Integer>();
	private ArrayList<Ulica> ulice = new ArrayList<Ulica>() ;

	
	
	@Override
    public int getIndentationLevel() {
        return 1; 
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	

	public ArrayList<Ulica> getUlice() {
		return ulice;
	}
	

	public ArrayList<Integer> getUliceUBrojevima() {
		return uliceUBrojevima;
	}

	public void setUliceUBrojevima(ArrayList<Integer> uliceUBrojevima) {
		this.uliceUBrojevima = uliceUBrojevima;
	}

	public void setUlice(ArrayList<Ulica> ulice) {
		this.ulice = ulice;
	}
	
	public void dodajUlicu(Ulica ulica) {
		this.ulice.add(ulica);
	}
	
	public Ulica dobaviUlicu(int id) {
		for(Ulica ulica : ulice) {
			if(ulica.getId() == id) {
				return ulica;
			}
		}
		return null;
	}

	public Mjesto dobaviMjesto(int id) {
		if(this.id == id) {
			return this;
		}
		return null;
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
		
		this.setId(Integer.parseInt(vrijednosti[0]));
		this.setNaziv(vrijednosti[1]);
		String[] uliceString = vrijednosti[2].split(",");
		ArrayList<Integer> ulice = new ArrayList<Integer>();
		for(String string: uliceString) {
			ulice.add(Integer.parseInt(string));
			for(Ulica ulica : Tvrtka.getInstance().getUlice()) {
				if(Integer.parseInt(string) == ulica.getId()) {
					this.dodajUlicu(ulica);
				}
			}	
		}
		this.setUliceUBrojevima(ulice);
		
		
	}

	@Override
	public boolean imaVrijednosti() {
		return this.id != 0 && this.naziv != null && !this.naziv.isEmpty() && this.ulice != null && !this.ulice.isEmpty();
	}
	
	private void validate(String linija) throws ParseException {
		   String[] vrijednosti = linija.split(";");

		   if (vrijednosti.length != 3) {
		       throw new ParseException("Redak sadrži " + vrijednosti.length + " vrijednosti, ali se očekuje 3 vrijednosti.", 0);
		   }



		   for (int i = 0; i < 2; i++) {
		       if (vrijednosti[i].trim().isEmpty()) {
		           throw new ParseException("Nepostojeće polje na indeksu:  " + i, 0);
		       }
		   }

		   try {
		       Integer.parseInt(vrijednosti[0]);
		   } catch (NumberFormatException e) {
		       throw new ParseException("Nevažeći integer na indeksu 0: " + e.getMessage(), 0);
		   }


		   if (vrijednosti[1].trim().isEmpty()) {
		       throw new ParseException("Nevažeći naziv na indeksu 1", 0);
		   }
		   this.setNaziv(vrijednosti[1].trim());
		   

		   String[] uliceString = vrijednosti[2].split(",");
		   ArrayList<Integer> ulice = new ArrayList<Integer>();
		   for(String string: uliceString) {
		       if (string.trim().isEmpty()) {
		           throw new ParseException("Nevažeča ulica", id);
		       }
		       if (!string.equals(string.trim())) {
		           throw new ParseException("Nevažeča ulica", id);
		       }
		       ulice.add(Integer.parseInt(string.trim()));
		   
		}
	}

	 @Override
	 public void add(DioPodrucja component) {
	   if (component instanceof Ulica) {
	     ulice.add((Ulica) component);
	   } else {
	     throw new IllegalArgumentException("Mjesto can only contain Ulica objects");
	   }
	 }

	 @Override
	 public void remove(DioPodrucja component) {
	   if (component instanceof Ulica) {
	     ulice.remove(component);
	   } else {
	     throw new IllegalArgumentException("Mjesto can only contain Ulica objects");
	   }
	 }

	 @Override
	 public DioPodrucja getChild(int index) {
	   return ulice.get(index);
	 }

	 @Override
	 public int getNumChildren() {
	   return ulice.size();
	 }
	 
	 @Override
	 public List<DioPodrucja> getChildren() {
	   return null;
	 }
	 
	 @Override
	 public String toString() {
	    return "Mjesto{" +
	        "id=" + id +
	        ", naziv='" + naziv + '\'' +
	        ", uliceUBrojevima=" + uliceUBrojevima +
	        ", ulice=" + ulice +
	        '}';
	 }
	 
}
