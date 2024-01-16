package app;

import java.util.regex.Pattern;

import objekti.Vozilo;
import tvrtka.UredZaDostavu;
import visitori.Visitor;
import visitori.VisitorImpl;

public class SVHandler implements CommandHandler{
	 private CommandHandler nextHandler;

	@Override
	public void handleCommand(String command) {
		   System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", "Oznaka vozila" , "Status", "Ukupno odvoženo km", "Broj hitnih paketa", "Broj običnih paketa", "Broj isporučenih paketa", "% zauzeća prostora", "% zauzeća težine,", "broj vožnji");

		   Visitor visitor = new VisitorImpl();
		   for (Vozilo vozilo : UredZaDostavu.getInstance().dohvatiListuVozila()) {
		       vozilo.accept(visitor);
		   }
	}

	@Override
	public void setNext(CommandHandler nextHandler) {
		this.nextHandler = nextHandler;
		
	}

	@Override
	public void provjeriKomandu(String command) {
		if (Pattern.matches("^SV$", command)) {
			this.handleCommand(command);
		}
		else {
			nextHandler.provjeriKomandu(command);
		}
	}

}
