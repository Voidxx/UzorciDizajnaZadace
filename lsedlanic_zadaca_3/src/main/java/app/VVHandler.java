package app;

import java.util.regex.Pattern;

import objekti.Vozilo;
import tvrtka.UredZaDostavu;
import visitori.Visitor;
import visitori.VisitorImpl;
import voznja.Voznja;

public class VVHandler implements CommandHandler{
	 private CommandHandler nextHandler;

	@Override
	public void handleCommand(String command) {
		   String[] parts = command.split(" ");
		   if (parts.length > 1) {
		       String registracija = parts[1];
		       Vozilo vozilo = UredZaDostavu.getInstance().dohvatiVozilo(registracija);
		       if (vozilo != null) {
				   Visitor visitor = new VisitorImpl();
		           for (Voznja voznja : vozilo.getObavljeneVoznje()) {
		               voznja.accept(visitor);
		           }
		       } else {
		           System.out.println("Vozilo s registracijom " + registracija + " ne postoji.");
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
		if (Pattern.matches("^VV\\s[A-Ža-ž0-9]+$", command)) {
			this.handleCommand(command);
		}
		else {
			nextHandler.provjeriKomandu(command);
		}
	}

}
