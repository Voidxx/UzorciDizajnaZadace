package voznja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import objekti.Paket;
import visitori.VoznjaVisitor;

public class Voznja implements Serializable {
	  private static final long serialVersionUID = 4166751526845134755L;
	private List<Paket> ukrcaniPaketi;
	  private List<Paket> dostavljeniPaketi;
	  private String VrijemePovratka;
	  private String vrijemePocetka;
	  private double ukupnoKmPrijedeno;
	  private double postotakZauzecaProstoraNaPocetkuVoznje;
	  private double postotakZauzecaTezineNaPocetkuVoznje;
	  private String trajanje;
	  private List<SegmentVoznje> segmentiVoznje = new ArrayList<SegmentVoznje>();

	  
	  
	  
	public List<SegmentVoznje> getSegmentiVoznje() {
		return segmentiVoznje;
	}



	public void setSegmentiVoznje(List<SegmentVoznje> segmentiVoznje) {
		this.segmentiVoznje = segmentiVoznje;
	}



	public List<Paket> getUkrcaniPaketi() {
		return ukrcaniPaketi;
	}



	public void setUkrcaniPaketi(List<Paket> ukrcaniPaketi) {
		this.ukrcaniPaketi = ukrcaniPaketi;
	}



	public List<Paket> getDostavljeniPaketi() {
		return dostavljeniPaketi;
	}



	public void setDostavljeniPaketi(List<Paket> dostavljeniPaketi) {
		this.dostavljeniPaketi = dostavljeniPaketi;
	}



	public String getVrijemePovratka() {
		return VrijemePovratka;
	}



	public void setVrijemePovratka(String vrijemePovratka) {
		VrijemePovratka = vrijemePovratka;
	}



	public String getVrijemePocetka() {
		return vrijemePocetka;
	}



	public void setVrijemePocetka(String vrijemePocetka) {
		this.vrijemePocetka = vrijemePocetka;
	}



	public double getUkupnoKmPrijedeno() {
		return ukupnoKmPrijedeno;
	}



	public void setUkupnoKmPrijedeno(double ukupnoKmPrijedeno) {
		this.ukupnoKmPrijedeno = ukupnoKmPrijedeno;
	}
	
	public void dodajNaUkupnoKM(double km) {
		this.ukupnoKmPrijedeno = this.ukupnoKmPrijedeno + km;
	}



	public double getPostotakZauzecaProstoraNaPocetkuVoznje() {
		return postotakZauzecaProstoraNaPocetkuVoznje;
	}



	public void setPostotakZauzecaProstoraNaPocetkuVoznje(double postotakZauzecaProstoraNaPocetkuVoznje) {
		this.postotakZauzecaProstoraNaPocetkuVoznje = postotakZauzecaProstoraNaPocetkuVoznje;
	}



	public double getPostotakZauzecaTezineNaPocetkuVoznje() {
		return postotakZauzecaTezineNaPocetkuVoznje;
	}



	public void setPostotakZauzecaTezineNaPocetkuVoznje(double postotakZauzecaTezineNaPocetkuVoznje) {
		this.postotakZauzecaTezineNaPocetkuVoznje = postotakZauzecaTezineNaPocetkuVoznje;
	}



	public String getTrajanje() {
		return trajanje;
	}



	public void setTrajanje(String trajanje) {
		this.trajanje = trajanje;
	}


	public void accept(VoznjaVisitor visitor) {
	       visitor.visit(this);
	   }

	public Voznja(List<Paket> ukrcaniPaketi, List<Paket> dostavljeniPaketi, String vrijemePovratka,
			String vrijemePocetka, double ukupnoKmPrijedeno, double postotakZauzecaProstoraNaPocetkuVoznje,
			double postotakZauzecaTezineNaPocetkuVoznje, String trajanje, List<SegmentVoznje> segmentiVoznje) {
		this.ukrcaniPaketi = ukrcaniPaketi;
		this.dostavljeniPaketi = dostavljeniPaketi;
		VrijemePovratka = vrijemePovratka;
		this.vrijemePocetka = vrijemePocetka;
		this.ukupnoKmPrijedeno = ukupnoKmPrijedeno;
		this.postotakZauzecaProstoraNaPocetkuVoznje = postotakZauzecaProstoraNaPocetkuVoznje;
		this.postotakZauzecaTezineNaPocetkuVoznje = postotakZauzecaTezineNaPocetkuVoznje;
		this.trajanje = trajanje;
		this.segmentiVoznje = segmentiVoznje;
	}
	  


}
