package visitori;

import objekti.Vozilo;
import voznja.SegmentVoznje;
import voznja.Voznja;

public class VisitorImpl implements Visitor {

	@Override
	public void visitVozilo(Vozilo vozilo) {
	       double ukupnoKm = vozilo.getUkupnoKmPrijedeno();
	       long brojHitnihPaketa = vozilo.getUkrcani_paketi().stream().filter(p -> p.getUsluga_dostave().equals("H")).count();
	       long brojNeHitnihPaketa = vozilo.getUkrcani_paketi().size() - brojHitnihPaketa;
	       int brojIsporucenihPaketa = vozilo.getBrojIsporucenihPaketa();
	       double postotakKoristenjaProstora;
	       double postotakKoristenjaTezine;
	       if(vozilo.getUkrcani_paketi().size() == 0) {
		       postotakKoristenjaProstora = 0;
		       postotakKoristenjaTezine = 0;
	       }
	       else {
	       postotakKoristenjaProstora = vozilo.getTrenutni_teret_volumen() / vozilo.getKapacitet_m3() * 100;
	       postotakKoristenjaTezine = vozilo.getTrenutni_teret_tezina() / vozilo.getKapacitet_kg() * 100;
	       }
	       int brojVoznji = vozilo.getObavljeneVoznje().size();
	       

	       System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s%n",vozilo.getOpis(), vozilo.getStatus(), ukupnoKm, brojHitnihPaketa, brojNeHitnihPaketa, brojIsporucenihPaketa, postotakKoristenjaProstora, postotakKoristenjaTezine, brojVoznji);
		
	}

	@Override
	public void visitVoznja(Voznja voznja) {
	       long brojHitnihPaketa =  voznja.getDostavljeniPaketi().stream().filter(p -> p.getUsluga_dostave().equals("H")).count();
	       long brojNeHitnihPaketa = voznja.getDostavljeniPaketi().size() - brojHitnihPaketa;
	       int brojIsporucenihPaketa = voznja.getDostavljeniPaketi().size();
	      System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s %-40s %-40s%n", "Vrijeme početka", "Vrijeme povratka", "Trajanje", "Ukupno odvoženo km", "Broj hitnih paketa", "Broj obicnih paketa", "Broj isporucenih paketa", "% zauzeća prostora na početku vožnje", "% zauzeća težine na početku vožnje");
	      System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s %-40s %-40s%n", voznja.getVrijemePocetka(), voznja.getVrijemePovratka(), voznja.getTrajanje(), voznja.getUkupnoKmPrijedeno(), brojHitnihPaketa, brojNeHitnihPaketa, brojIsporucenihPaketa, voznja.getPostotakZauzecaProstoraNaPocetkuVoznje(), voznja.getPostotakZauzecaTezineNaPocetkuVoznje());		
	}

	@Override
	public void visitSegmentVoznje(SegmentVoznje segmentVoznje) {
		   if(segmentVoznje.getPaketZaDostaviti() != null) {
		       System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", 
		           segmentVoznje.getVrijemePocetka(), 
		           segmentVoznje.getVrijemeKraja(), 
		           segmentVoznje.getUdaljenostKM(), 
		           segmentVoznje.getTrajanjeVoznje(), 
		           segmentVoznje.getTrajanjeIsporuke(), 
		           segmentVoznje.getUkupnoTrajanjeSegmenta(), 
		           segmentVoznje.getPaketZaDostaviti().getOznaka());
			   }
		   else {
		       System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", 
			           segmentVoznje.getVrijemePocetka(), 
			           segmentVoznje.getVrijemeKraja(), 
			           segmentVoznje.getUdaljenostKM(), 
			           segmentVoznje.getTrajanjeVoznje(), 
			           segmentVoznje.getTrajanjeIsporuke(), 
			           segmentVoznje.getUkupnoTrajanjeSegmenta(), 
			           "-");
		   }
		
	}

}
