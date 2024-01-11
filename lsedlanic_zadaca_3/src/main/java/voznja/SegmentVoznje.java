package voznja;

import objekti.Paket;
import visitori.Host;
import visitori.Visitor;

public class SegmentVoznje implements Host, Cloneable {
	private double[] odGPS;
	private double[] doGPS;
	private double udaljenostKM;
	private String vrijemePocetka;
	private int trajanjeVoznje;
	private int trajanjeIsporuke;
	private int ukupnoTrajanjeSegmenta;
	private Paket paketZaDostaviti;
	private String vrijemeKraja;
	
	

	public SegmentVoznje(double[] odGPS, double[] doGPS, double udaljenostKM, String vrijemePocetka, String vrijemeKraja,
			int trajanjeVoznje, int trajanjeIsporuke, int ukupnoTrajanjeSegmenta, Paket paketZaDostaviti) {
		super();
		this.odGPS = odGPS;
		this.doGPS = doGPS;
		this.udaljenostKM = udaljenostKM;
		this.vrijemePocetka = vrijemePocetka;
		this.vrijemeKraja = vrijemeKraja;
		this.trajanjeVoznje = trajanjeVoznje;
		this.trajanjeIsporuke = trajanjeIsporuke;
		this.ukupnoTrajanjeSegmenta = ukupnoTrajanjeSegmenta;
		this.paketZaDostaviti = paketZaDostaviti;
	}
	
	@Override
	 public void accept(Visitor visitor) {
	     visitor.visitSegmentVoznje(this);
	 }
	
	public String getVrijemeKraja() {
		return vrijemeKraja;
	}


	public void setVrijemeKraja(String vrijemeKraja) {
		this.vrijemeKraja = vrijemeKraja;
	}


	public void izracunajTrajanjeSegmenta() {
		this.ukupnoTrajanjeSegmenta = this.trajanjeVoznje + this.trajanjeIsporuke;
	}
	

	
	public int getUkupnoTrajanjeSegmenta() {
		return ukupnoTrajanjeSegmenta;
	}


	public void setUkupnoTrajanjeSegmenta(int ukupnoTrajanjeSegmenta) {
		this.ukupnoTrajanjeSegmenta = ukupnoTrajanjeSegmenta;
	}


	public Paket getPaketZaDostaviti() {
		return paketZaDostaviti;
	}


	public void setPaketZaDostaviti(Paket paketZaDostaviti) {
		this.paketZaDostaviti = paketZaDostaviti;
	}


	public double[] getOdGPS() {
		return odGPS;
	}
	public void setOdGPS(double[] odGPS) {
		this.odGPS = odGPS;
	}
	public double[] getDoGPS() {
		return doGPS;
	}
	public void setDoGPS(double[] doGPS) {
		this.doGPS = doGPS;
	}
	public double getUdaljenostKM() {
		return udaljenostKM;
	}
	public void setUdaljenostKM(double udaljenostKM) {
		this.udaljenostKM = udaljenostKM;
	}
	public String getVrijemePocetka() {
		return vrijemePocetka;
	}
	public void setVrijemePocetka(String vrijemePocetka) {
		this.vrijemePocetka = vrijemePocetka;
	}
	public int getTrajanjeVoznje() {
		return trajanjeVoznje;
	}
	public void setTrajanjeVoznje(int trajanjeVoznje) {
		this.trajanjeVoznje = trajanjeVoznje;
	}
	public int getTrajanjeIsporuke() {
		return trajanjeIsporuke;
	}
	public void setTrajanjeIsporuke(int trajanjeIsporuke) {
		this.trajanjeIsporuke = trajanjeIsporuke;
	}

	
	  @Override
	  public Object clone() throws CloneNotSupportedException {
	      return super.clone();
	  }
}
