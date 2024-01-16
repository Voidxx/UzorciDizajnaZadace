package app;

public class QHandler implements CommandHandler{
	 private CommandHandler nextHandler;

	@Override
	public void setNext(CommandHandler nextHandler) {
		this.nextHandler = nextHandler;
		
	}

	@Override
	public void provjeriKomandu(String command) {
		if ("Q".equals(command)) {
			this.handleCommand(command);
		}
		else {
			nextHandler.provjeriKomandu(command);
		}
	}

	@Override
	public void handleCommand(String command) {
		throw new RuntimeException("Izlazim iz programa...");
	}

}
