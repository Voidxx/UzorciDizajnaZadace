package voznja;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import objekti.Paket;

public class VoznjaBuilder {
	private List<Paket> ukrcaniPaketi;
	  private List<Paket> dostavljeniPaketi;
	  private String vrijemePovratkaUUred;
	  private String vrijemePocetka;
	  private double ukupnoKmPrijedeno;
	  private double postotakZauzecaProstoraNaPocetkuVoznje;
	  private double postotakZauzecaTezineNaPocetkuVoznje;
	  private String trajanje;
	  private List<SegmentVoznje> segmentiVoznje = new ArrayList<SegmentVoznje>();

	  public VoznjaBuilder() {
	      this.ukrcaniPaketi = new ArrayList<>();
	      this.dostavljeniPaketi = new ArrayList<>();
	  }

	  public VoznjaBuilder dodajUkrcanePakete(Paket paket) {
	      this.ukrcaniPaketi.add(paket);
	      return this;
	  }

	  public VoznjaBuilder dodajDostavljenePakete(Paket paket) {
	      this.dostavljeniPaketi.add(paket);
	      return this;
	  }
	  
	  public VoznjaBuilder postaviPostotakZauzecaProstora(double zauzece) {
		  this.postotakZauzecaProstoraNaPocetkuVoznje = zauzece;
		  return this;
	  }
	  public VoznjaBuilder postaviPostotakZauzecaTezine(double zauzece) {
		  this.postotakZauzecaTezineNaPocetkuVoznje = zauzece;
		  return this;
	  }

	  public VoznjaBuilder postaviVrijemePovratka(String VrijemePovratka) {
	      this.vrijemePovratkaUUred = VrijemePovratka;
	      return this;
	  }
	  
	  public VoznjaBuilder postaviVrijemePocetka(String VrijemePocetka) {
	      this.vrijemePocetka = VrijemePocetka;
	      return this;
	  }
	  
	  public VoznjaBuilder dodajSegmentVoznje(SegmentVoznje segment) {
		  this.segmentiVoznje.add(segment);
		  return this;
	  }
	  
	  public VoznjaBuilder postaviTrajanje() {
		   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
		   LocalDateTime startTime = LocalDateTime.parse(vrijemePocetka, formatter);
		   LocalDateTime endTime = LocalDateTime.parse(vrijemePovratkaUUred, formatter);
		   Duration duration = Duration.between(startTime, endTime);
		   this.trajanje = String.format("%d sati %d minuta", duration.toHours(), duration.toMinutesPart());
		   return this;
	  }

	  public Voznja build() {
	      return new Voznja(ukrcaniPaketi, dostavljeniPaketi, vrijemePovratkaUUred, vrijemePocetka, ukupnoKmPrijedeno, postotakZauzecaProstoraNaPocetkuVoznje, postotakZauzecaTezineNaPocetkuVoznje, trajanje, segmentiVoznje);
	  }
}
