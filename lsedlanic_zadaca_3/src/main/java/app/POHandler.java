package app;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import objekti.Paket;
import objekti.Vozilo;
import stanjePaketa.Pošiljatelj;
import stanjePaketa.Primatelj;
import stanjePaketa.Subject;
import tvrtka.UredZaDostavu;
import tvrtka.UredZaPrijem;

public class POHandler implements CommandHandler{
	 private CommandHandler nextHandler;

	@Override
	public void handleCommand(String command) {
    	String[] dijelovi = command.split("'");
    	String[] drugidijelovi = dijelovi[2].trim().split(" ");
    	System.out.println(dijelovi[1] + drugidijelovi[0] +  drugidijelovi[1]);
    	String posiljateljIliPrimatelj = dijelovi[1];
    	String oznakaPaketa = drugidijelovi[0];
    	String status = drugidijelovi[1];
    	
    	List<Paket> listaPaketaZaProvjeru = new ArrayList<Paket>();
    	for(Paket paket : UredZaPrijem.getInstance().dobaviListuPaketaZaDostavu()) {
    		if(paket.vratiPosiljatelja() != null)
    		listaPaketaZaProvjeru.add(paket);
    	}
    	for(Paket paket : UredZaPrijem.getInstance().dobaviListuOcekivanihPaketa()) {
    	for(Vozilo vozilo : UredZaDostavu.getInstance().dohvatiListuVozila()) {
    		if(vozilo.getUkrcani_paketi().contains(paket) && paket.vratiPosiljatelja() != null) {
    			listaPaketaZaProvjeru.add(paket);
    		}
    	}
    	}
    	
    	
    	for(Paket paket : listaPaketaZaProvjeru) {
    		Subject subject = paket.getOvajPaket();
    		if(status.equals("N")) {
	    		if(paket.vratiPosiljatelja().getOsoba().equals(posiljateljIliPrimatelj) && oznakaPaketa.equals(paket.getOznaka())) {
	    			Pošiljatelj posiljatelj = paket.getPosiljateljObserver();
	    			subject.detach(posiljatelj);
	    		}
	    		if(paket.vratiPrimatelja().getOsoba().equals(posiljateljIliPrimatelj) && oznakaPaketa.equals(paket.getOznaka())) {
	    			Primatelj primatelj = paket.getPrimateljObserver();
	    			subject.detach(primatelj);
	    		}
    		}
    		else if(status.equals("D")) {
        		if(paket.vratiPosiljatelja().getOsoba().equals(posiljateljIliPrimatelj) && oznakaPaketa.equals(paket.getOznaka())) {
        			Pošiljatelj posiljatelj = paket.getPosiljateljObserver();
        			subject.attach(posiljatelj);
        		}
        		if(paket.vratiPrimatelja().getOsoba().equals(posiljateljIliPrimatelj) && oznakaPaketa.equals(paket.getOznaka())) {
        			Primatelj primatelj = paket.getPrimateljObserver();
        			subject.attach(primatelj);
        		}
    		}
    	}	
	}

	@Override
	public void setNext(CommandHandler nextHandler) {
		this.nextHandler = nextHandler;
		
	}

	@Override
	public void provjeriKomandu(String command) {
		if (Pattern.matches("^PO\\s'.*\\s.*'\\s[A-Ža-ž0-9]+\\s(D|N)$", command)) {
			this.handleCommand(command);
		}
		else {
			nextHandler.provjeriKomandu(command);
		}
	}

}
