package app;

import java.util.regex.Pattern;

import tvrtka.Tvrtka;
import tvrtka.UredZaDostavu;
import tvrtka.UredZaPrijem;

public class LSHandler implements CommandHandler{
	 private CommandHandler nextHandler;

	@Override
	public void handleCommand(String command) {
		   SystemStateProxy systemStateProxy = new SystemStateProxy();
		   SystemState systemState = systemStateProxy.loadState("stanjeSustava.ser");
		   if (systemState != null) {
		       UredZaDostavu.setInstance(systemState.getUredZaDostavu());
		       UredZaPrijem.setInstance(systemState.getUredZaPrijem());
		       Tvrtka.setInstance(systemState.getTvrtka());
		       PogreskeBrojac.setInstance(systemState.getPogreskeBrojac());
			   VirtualnoVrijeme virtualnoVrijeme = VirtualnoVrijeme.getInstance();
		       virtualnoVrijeme.setInstance(systemState.getVirtualnoVrijeme());
		   }
	}

	@Override
	public void setNext(CommandHandler nextHandler) {
		this.nextHandler = nextHandler;
		
	}

	@Override
	public void provjeriKomandu(String command) {
		if (Pattern.matches("^LS$", command)) {
			this.handleCommand(command);
		}
		else {
			nextHandler.provjeriKomandu(command);
		}
	}

}
