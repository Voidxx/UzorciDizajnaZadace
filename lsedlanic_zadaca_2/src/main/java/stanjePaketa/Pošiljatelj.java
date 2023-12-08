package stanjePaketa;

import java.io.Serializable;

import objekti.Paket;

public class Pošiljatelj implements Observer, Serializable{
	   private static final long serialVersionUID = -6462223752465534360L;
	private String imePosiljatelja;

	   public Pošiljatelj(String imePosiljatelja) {
	       this.imePosiljatelja = imePosiljatelja;
	   }

	   @Override
	   public void paketZaprimljen(Paket paket) {
		    	  System.out.println(imePosiljatelja + ", paket: " + paket.getOznaka() + "  je zaprimljen u tvrtku.");
	   }
	   
	   @Override
	   public void paketDostavljen(Paket paket) {
		    	  System.out.println(imePosiljatelja + ", paket: " + paket.getOznaka() + " je dostavljen primatelju. Hvala na poslovanju");
	   }
}
