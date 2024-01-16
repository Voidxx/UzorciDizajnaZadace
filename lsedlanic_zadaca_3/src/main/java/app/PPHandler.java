package app;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import objekti.DioPodrucja;
import objekti.Mjesto;
import objekti.Podrucje;
import objekti.Ulica;
import tvrtka.Tvrtka;

public class PPHandler implements CommandHandler{
	 private CommandHandler nextHandler;

	@Override
	public void handleCommand(String command) {
  	  for (Podrucje podrucje : Tvrtka.getInstance().getPodrucja()) {
	      System.out.println(podrucje.getId());
	      Set<Mjesto> printedMjesta = new HashSet<>();
	      for (DioPodrucja dioPodrucja : podrucje.getChildren()) {
	          if (dioPodrucja instanceof Mjesto) {
	              Mjesto mjesto = (Mjesto) dioPodrucja;
	              if (!printedMjesta.contains(mjesto) && mjesto.getNaziv() != null) {
	                System.out.println("    " + mjesto.getNaziv());
	                printedMjesta.add(mjesto);
	              }
	          } else if(dioPodrucja instanceof Ulica) {
	              Ulica ulica = (Ulica) dioPodrucja;
	              System.out.println("          " + ulica.getNaziv());
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
		if (Pattern.matches("^PP$", command)){
			this.handleCommand(command);
		}
		else {
			nextHandler.provjeriKomandu(command);
		}
	}

}
