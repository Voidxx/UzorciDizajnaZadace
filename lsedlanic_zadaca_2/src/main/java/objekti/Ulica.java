package objekti;

import java.text.ParseException;
import java.util.List;

import app.PogreskeBrojac;
import citaci.CsvObjekt;

public class Ulica implements CsvObjekt, DioPodrucja{
	private int id;
	private String naziv;
	private double gps_lat_1;
	private double gps_lon_1;
	private double gps_lat_2;
	private double gps_lon_2;
	private int najv_kucni_broj;
	
	
	
	


	public Ulica(int id, String naziv, double gps_lat_1, double gps_lon_1, double gps_lat_2, double gps_lon_2,
			int najv_kucni_broj) {
		super();
		this.id = id;
		this.naziv = naziv;
		this.gps_lat_1 = gps_lat_1;
		this.gps_lon_1 = gps_lon_1;
		this.gps_lat_2 = gps_lat_2;
		this.gps_lon_2 = gps_lon_2;
		this.najv_kucni_broj = najv_kucni_broj;
	}

	@Override
	public void process(String linija) throws ParseException {
		linija = linija.replaceAll("\\s", "");
		  if (linija.trim().isEmpty()) {
		      return;
		  }
		 try {
		     validate(linija);
		 } catch (ParseException e) {
		     PogreskeBrojac.getInstance().dodajPogresku(e.getMessage(), linija);
		     return;
		 }
		 //potrebna dodatna validacjia
		String[] vrijednosti = linija.split(";");
		
		this.setId(Integer.parseInt(vrijednosti[0]));
		this.setNaziv(vrijednosti[1]);
		this.setGps_lat_1(Double.parseDouble(vrijednosti[2]));
		this.setGps_lon_1(Double.parseDouble(vrijednosti[3]));
		this.setGps_lat_2(Double.parseDouble(vrijednosti[4]));
		this.setGps_lon_2(Double.parseDouble(vrijednosti[5]));
		this.setNajv_kucni_broj(Integer.parseInt(vrijednosti[6]));
		
	}

	@Override
	public boolean imaVrijednosti() {
		return id != 0;
	}

	
	private void validate(String linija) throws ParseException {
		  String[] vrijednosti = linija.split(";");


		  // Skip lines with less than seven fields
		  if (vrijednosti.length < 7) {
		      throw new ParseException("Redak sadrži " + vrijednosti.length + " vrijednosti, ali se očekuje 7 vrijednosti.", 0);
		  }

		  for (int i = 0; i < 7; i++) {
		      if (vrijednosti[i].trim().isEmpty()) {
		          throw new ParseException("Nepostojeće polje na indeksu: " + i, 0);
		      }
		  }

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

	public double getGps_lat_1() {
		return gps_lat_1;
	}

	public void setGps_lat_1(double gps_lat_1) {
		this.gps_lat_1 = gps_lat_1;
	}

	public double getGps_lon_1() {
		return gps_lon_1;
	}

	public void setGps_lon_1(double gps_lon_1) {
		this.gps_lon_1 = gps_lon_1;
	}

	public double getGps_lat_2() {
		return gps_lat_2;
	}

	public void setGps_lat_2(double gps_lat_2) {
		this.gps_lat_2 = gps_lat_2;
	}

	public double getGps_lon_2() {
		return gps_lon_2;
	}

	public void setGps_lon_2(double gps_lon_2) {
		this.gps_lon_2 = gps_lon_2;
	}

	public int getNajv_kucni_broj() {
		return najv_kucni_broj;
	}

	public void setNajv_kucni_broj(int najv_kucni_broj) {
		this.najv_kucni_broj = najv_kucni_broj;
	}

	 @Override
	 public void add(DioPodrucja component) {
	   throw new UnsupportedOperationException("Ulica nema drugih komponenata");
	 }

	 @Override
	 public void remove(DioPodrucja component) {
	   throw new UnsupportedOperationException("Ulica nema drugih komponenata");
	 }

	 @Override
	 public DioPodrucja getChild(int index) {
	   throw new UnsupportedOperationException("Ulica nema drugih komponenata");
	 }

	 @Override
	 public int getNumChildren() {
	   return 0;
	 }
	 
	 @Override
	 public List<DioPodrucja> getChildren() {
	   return null;
	 }
	 
	 public Ulica getUlica(int id) {
		 if(id == this.id) {
			 return this;
		 }
		 return null;
	 }
	
	

}