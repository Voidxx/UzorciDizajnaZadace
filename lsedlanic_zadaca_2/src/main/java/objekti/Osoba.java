package objekti;

import java.text.ParseException;

import citaci.CsvObjekt;

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

	public void setGrad(int grad) {
		this.grad = grad;
	}

	public int getUlica() {
		return ulica;
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
		//potrebna validacija
		String[] vrijednosti = linija.split(";");
		
		this.setOsoba(vrijednosti[0]);
		this.setGrad(Integer.parseInt(vrijednosti[1]));
		this.setUlica(Integer.parseInt(vrijednosti[2]));
		this.setKbr(Integer.parseInt(vrijednosti[3]));
	}

	@Override
	public boolean imaVrijednosti() {
		// TODO Auto-generated method stub
		return false;
	}

}
