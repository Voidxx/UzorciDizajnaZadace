package visitori;

import objekti.Vozilo;

public class VoziloVisitorImpl implements VoziloVisitor {
	   @Override
	   public void visit(Vozilo vozilo) {
	       double ukupnoKm = vozilo.getUkupnoKmPrijedeno();
	       long brojHitnihPaketa = vozilo.getUkrcani_paketi().stream().filter(p -> p.getUsluga_dostave().equals("H")).count();
	       long brojNeHitnihPaketa = vozilo.getUkrcani_paketi().size() - brojHitnihPaketa;
	       int brojIsporucenihPaketa = vozilo.getBrojIsporucenihPaketa();
	       double postotakKoristenjaProstora = vozilo.getTrenutni_teret_volumen() / vozilo.getKapacitet_m3() * 100;
	       double postotakKoristenjaTezine = vozilo.getTrenutni_teret_tezina() / vozilo.getKapacitet_kg() * 100;
	       int brojVoznji = vozilo.getObavljeneVoznje().size();
	       

	       System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s%n",vozilo.getOpis(), vozilo.getStatus(), ukupnoKm, brojHitnihPaketa, brojNeHitnihPaketa, brojIsporucenihPaketa, postotakKoristenjaProstora, postotakKoristenjaTezine, brojVoznji);
	   }
	}