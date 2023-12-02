package visitori;

import voznja.Voznja;

public class VoznjaVisitorImpl implements VoznjaVisitor {
	  @Override
	  public void visit(Voznja voznja) {
	      System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", "Vrijeme početka", "Vrijeme povratka", "Trajanje", "Ukupno odvoženo km", "Broj paketa u vozilu", "% zauzeća prostora na početku vožnje", "% zauzeća težine na početku vožnje");
	      System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", voznja.getVrijemePocetka(), voznja.getVrijemePovratka(), voznja.getTrajanje(), voznja.getUkupnoKmPrijedeno(), voznja.getUkrcaniPaketi().size(), voznja.getPostotakZauzecaProstoraNaPocetkuVoznje(), voznja.getPostotakZauzecaTezineNaPocetkuVoznje());
	  }
	}