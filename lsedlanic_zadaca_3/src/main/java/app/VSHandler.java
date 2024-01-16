package app;

import java.util.regex.Pattern;

import objekti.Vozilo;
import tvrtka.UredZaDostavu;
import visitori.Visitor;
import visitori.VisitorImpl;
import voznja.SegmentVoznje;

public class VSHandler implements CommandHandler{
	 private CommandHandler nextHandler;

	@Override
	public void handleCommand(String command) {
		   String[] parts = command.split(" ");
		   if (parts.length > 1) {
			   System.out.println(parts[0] + " " +  parts[1] + " " + parts[2]);
		       String voziloRegistracija = parts[1];
		       int brojVoznje = Integer.parseInt(parts[2]);
		       Vozilo vozilo = UredZaDostavu.getInstance().dohvatiVozilo(voziloRegistracija);
			   System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", 
				       "Vrijeme početka", 
				       "Vrijeme kraja", 
				       "Udaljenost (km)", 
				       "Trajanje vožnje", 
				       "Trajanje isporuke", 
				       "Ukupno trajanje segmenta", 
				       "Paket za dostaviti");
		       if (vozilo != null) {
				   Visitor visitor = new VisitorImpl();
		           if(vozilo.getObavljeneVoznje().get(brojVoznje - 1) != null)
		           for (SegmentVoznje segmentVoznje : vozilo.getObavljeneVoznje().get(brojVoznje - 1).getSegmentiVoznje()) {
		        	   
		               segmentVoznje.accept(visitor);
		           }
		       } else {
		           System.out.println("Vozilo s registracijom " + voziloRegistracija + " ne postoji.");
		       }
		   } else {
		       System.out.println("Nevažeća komanda.");
		   }
	}

	@Override
	public void setNext(CommandHandler nextHandler) {
		this.nextHandler = nextHandler;
		
	}

	@Override
	public void provjeriKomandu(String command) {
		if (Pattern.matches("^VS\\s[A-Ža-ž0-9]+\\s\\d+$", command)) {
			this.handleCommand(command);
		}
		else {
			nextHandler.provjeriKomandu(command);
		}
	}

}
