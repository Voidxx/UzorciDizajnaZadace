package app;

import java.util.regex.Pattern;

import tvrtka.Tvrtka;
import tvrtka.UredZaDostavu;
import tvrtka.UredZaPrijem;

public class SSHandler implements CommandHandler{
	 private CommandHandler nextHandler;

	@Override
	public void handleCommand(String command) {
		   SystemState systemState = new SystemState(UredZaDostavu.getInstance(), UredZaPrijem.getInstance(), Tvrtka.getInstance(), PogreskeBrojac.getInstance(), VirtualnoVrijeme.getInstance());
		   systemState.saveState("stanjeSustava.ser");
	}

	@Override
	public void setNext(CommandHandler nextHandler) {
		this.nextHandler = nextHandler;
		
	}

	@Override
	public void provjeriKomandu(String command) {
		if (Pattern.matches("^SS$", command))  {
			this.handleCommand(command);
		}
		else {
			nextHandler.provjeriKomandu(command);
		}
	}

}
