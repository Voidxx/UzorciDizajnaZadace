package app;

import java.util.regex.Pattern;

import objekti.Vozilo;
import stanjaVozila.NeaktivnoVozilo;
import stanjaVozila.NeispravnoVozilo;
import tvrtka.UredZaDostavu;

public class PSHandler implements CommandHandler{
	 private CommandHandler nextHandler;

	@Override
	public void handleCommand(String command) {
		String[] parts = command.split(" ");
		String vehicleId = parts[1];
		String status = parts[2];
		
		Vozilo vozilo = UredZaDostavu.getInstance().dohvatiVozilo(vehicleId);
		if (vozilo != null) {
		   if ("A".equals(status)) {
			   if(vozilo.getState().mozeSePonovoAktivirati())
				   vozilo.setState(vozilo.getAktivnoStanje());
			   else
				   System.out.println("Nemožete neispravno vozilo ponovno postaviti kao aktivno/neaktivno.");
		   } else if ("NI".equals(status)) {
			   if(vozilo.getNeispravnoStanje() == null) {
			   NeispravnoVozilo neispravnoVozilo = new NeispravnoVozilo();
		       vozilo.setState(neispravnoVozilo);
		       vozilo.setNeispravnoStanje(neispravnoVozilo);
			   }
			   else {
				   vozilo.setState(vozilo.getNeispravnoStanje());
			   }
		   } else if ("NA".equals(status)) {
			   if(vozilo.getNeaktivnoStanje() == null) {
				   NeaktivnoVozilo neaktivnoVozilo = new NeaktivnoVozilo();
				   vozilo.setState(neaktivnoVozilo);
				   vozilo.setNeaktivnoStanje(neaktivnoVozilo);
			   }
			   else {
				   vozilo.setState(vozilo.getNeaktivnoStanje());
			   }
		   }
		} else {
		   System.out.println("Nema vozila sa opisom  " + vehicleId + ".");
		}	}

	@Override
	public void setNext(CommandHandler nextHandler) {
		this.nextHandler = nextHandler;
		
	}

	@Override
	public void provjeriKomandu(String command) {
		if (Pattern.matches("^PS\\s[A-Ža-ž0-9]+\\s(A|NI|NA)$", command)) {
			this.handleCommand(command);
		}
		else {
			nextHandler.provjeriKomandu(command);
		}
	}

}
