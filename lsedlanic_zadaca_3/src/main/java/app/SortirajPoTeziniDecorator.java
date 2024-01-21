package app;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import objekti.Paket;
import tvrtka.UredZaDostavu;

public class SortirajPoTeziniDecorator extends CommandHandlerDecorator {

    public SortirajPoTeziniDecorator(CommandHandler decoratedHandler) {
        super(decoratedHandler);
    }


    @Override
    public void provjeriKomandu(String command) {
    	sortPackages();
		decoratedHandler.provjeriKomandu(command);
		System.out.println("Paketi su sortirani prema težini.");
    }


    private void sortPackages() {
        List<Paket> paketi = UredZaDostavu.getInstance().getDostavljeniPaketi();
        Collections.sort(paketi, Comparator.comparing(Paket::getTezina));
       
    }

}