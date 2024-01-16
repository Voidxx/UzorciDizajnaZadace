package app;

public interface CommandHandler {
	   void provjeriKomandu(String command);
	   void handleCommand(String command);
	   void setNext(CommandHandler nextHandler);
	}
