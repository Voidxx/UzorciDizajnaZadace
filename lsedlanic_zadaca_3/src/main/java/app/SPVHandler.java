package app;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import stanjeAplikacije.TvrtkaHandler;
import stanjeAplikacije.UredZaDostavuHandler;
import stanjeAplikacije.UredZaPrijemHandler;
import stanjeAplikacije.VirtualnoVrijemeHandler;
import tvrtka.Tvrtka;

public class SPVHandler implements CommandHandler{
	 private CommandHandler nextHandler;

	@Override
	public void handleCommand(String command) {
		   String stateName = command.substring(5, command.length() - 1);
		   Map<String, Object> state = new HashMap<>();
		   TvrtkaHandler tvrtkaHandler = new TvrtkaHandler();
		   VirtualnoVrijemeHandler virtualnoVrijemeHandler = new VirtualnoVrijemeHandler();
		   UredZaPrijemHandler uredZaPrijemHandler = new UredZaPrijemHandler();
		   UredZaDostavuHandler uredZaDostavuHandler = new UredZaDostavuHandler();

		   tvrtkaHandler.setNext(virtualnoVrijemeHandler);
		   virtualnoVrijemeHandler.setNext(uredZaPrijemHandler);
		   uredZaPrijemHandler.setNext(uredZaDostavuHandler);

		   tvrtkaHandler.handleSave(state);
		   virtualnoVrijemeHandler.handleSave(state);
		   uredZaPrijemHandler.handleSave(state);
		   uredZaDostavuHandler.handleSave(state);

		   Tvrtka.getInstance().getSavedStates().put(stateName, state);
	}

	@Override
	public void setNext(CommandHandler nextHandler) {
		this.nextHandler = nextHandler;
		
	}

	@Override
	public void provjeriKomandu(String command) {
		if (Pattern.matches("^SPV\\s'.*'$", command)) {
			this.handleCommand(command);
		}
		else {
			nextHandler.provjeriKomandu(command);
		}
	}

}
