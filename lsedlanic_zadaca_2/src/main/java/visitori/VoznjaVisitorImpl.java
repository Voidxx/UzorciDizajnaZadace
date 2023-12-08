package visitori;

import voznja.Voznja;

public class VoznjaVisitorImpl implements VoznjaVisitor {
	  @Override
	  public void visit(Voznja voznja) {
	       long brojHitnihPaketa =  voznja.getUkrcaniPaketi().stream().filter(p -> p.getUsluga_dostave().equals("H")).count();
	       long brojNeHitnihPaketa = voznja.getUkrcaniPaketi().size() - brojHitnihPaketa;
	       int brojIsporucenihPaketa = voznja.getDostavljeniPaketi().size();
	      System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", "Vrijeme početka", "Vrijeme povratka", "Trajanje", "Ukupno odvoženo km", "Broj hitnih paketa", "Broj obicnih paketa", "Broj isporucenih paketa", "% zauzeća prostora na početku vožnje", "% zauzeća težine na početku vožnje");
	      System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", voznja.getVrijemePocetka(), voznja.getVrijemePovratka(), voznja.getTrajanje(), voznja.getUkupnoKmPrijedeno(), brojHitnihPaketa, brojNeHitnihPaketa, brojIsporucenihPaketa, voznja.getPostotakZauzecaProstoraNaPocetkuVoznje(), voznja.getPostotakZauzecaTezineNaPocetkuVoznje());
	  }
	}