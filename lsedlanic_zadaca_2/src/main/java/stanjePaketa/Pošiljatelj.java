package stanjePaketa;

import objekti.Paket;

public class Pošiljatelj implements Observer{
	   private String senderName;

	   public Pošiljatelj(String senderName) {
	       this.senderName = senderName;
	   }

	   @Override
	   public void paketZaprimljen(Paket paket) {
		    	  System.out.println(senderName + ", paket: " + paket.getOznaka() + "  je zaprimljen u tvrtku.");
	   }
	   
	   @Override
	   public void paketDostavljen(Paket paket) {
		    	  System.out.println(senderName + ", paket: " + paket.getOznaka() + " je dostavljen primatelju. Hvala na poslovanju");
	   }
}
