package voznja;

import java.util.ArrayList;
import java.util.List;

import objekti.Paket;
import visitori.Host;
import visitori.Visitor;

public class Voznja implements Host, Cloneable {
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

	@Override
	public void accept(Visitor visitor) {
	       visitor.visitVoznja(this);
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


	@Override
	public Object clone() throws CloneNotSupportedException {
	 Voznja cloned = (Voznja) super.clone();
	 cloned.ukrcaniPaketi = new ArrayList<>(this.ukrcaniPaketi);
	 cloned.dostavljeniPaketi = new ArrayList<>(this.dostavljeniPaketi);
	 cloned.VrijemePovratka = this.VrijemePovratka;
	 cloned.vrijemePocetka = this.vrijemePocetka;
	 cloned.ukupnoKmPrijedeno = this.ukupnoKmPrijedeno;
	 cloned.postotakZauzecaProstoraNaPocetkuVoznje = this.postotakZauzecaProstoraNaPocetkuVoznje;
	 cloned.postotakZauzecaTezineNaPocetkuVoznje = this.postotakZauzecaTezineNaPocetkuVoznje;
	 cloned.trajanje = this.trajanje;
	 cloned.segmentiVoznje = new ArrayList<>(this.segmentiVoznje);
	 return cloned;
	}

}
