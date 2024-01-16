package app;

public class NepostojecaKomandaHandler implements CommandHandler{

	@Override
	public void provjeriKomandu(String command) {
		System.out.println("Nepostojeća komanda!");
		
	}

	@Override
	public void handleCommand(String command) {
	}

	@Override
	public void setNext(CommandHandler nextHandler) {		
	}

}
