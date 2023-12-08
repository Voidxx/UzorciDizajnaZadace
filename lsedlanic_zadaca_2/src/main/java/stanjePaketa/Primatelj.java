package stanjePaketa;

import objekti.Paket;

public class Primatelj implements Observer{
	   private String receiverName;

	   public Primatelj(String receiverName) {
	       this.receiverName = receiverName;
	   }

	   @Override
	   public void paketZaprimljen(Paket paket) {
		    	  System.out.println(receiverName + ", paket: " + paket.getOznaka() + " je zaprimljen u tvrtku.");
	   }
	   
	   @Override
	   public void paketDostavljen(Paket paket) {
		    	  System.out.println(receiverName + ", paket: " + paket.getOznaka() + " Vam je dostavljen. Uživajte!");
	   }
}
