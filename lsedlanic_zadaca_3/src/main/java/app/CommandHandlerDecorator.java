package app;

public abstract class CommandHandlerDecorator implements CommandHandler {
    protected CommandHandler decoratedHandler;

    public CommandHandlerDecorator(CommandHandler decoratedHandler) {
        this.decoratedHandler = decoratedHandler;
    }

    @Override
    public void provjeriKomandu(String command) {
        decoratedHandler.provjeriKomandu(command);
    }

    @Override
    public void handleCommand(String command) {
        decoratedHandler.handleCommand(command);
    }
    
    @Override
    public void setNext(CommandHandler nextHandler) {
        decoratedHandler.setNext(nextHandler);
    }
}