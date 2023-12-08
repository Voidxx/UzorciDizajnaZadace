package stanjePaketa;

import java.io.Serializable;

import objekti.Paket;

public class Primatelj implements Observer, Serializable{
	   private static final long serialVersionUID = 8249374058834550583L;
	private String imePrimatelja;

	   public Primatelj(String imePrimatelja) {
	       this.imePrimatelja = imePrimatelja;
	   }

	   @Override
	   public void paketZaprimljen(Paket paket) {
		    	  System.out.println(imePrimatelja + ", paket: " + paket.getOznaka() + " je zaprimljen u tvrtku.");
	   }
	   
	   @Override
	   public void paketDostavljen(Paket paket) {
		    	  System.out.println(imePrimatelja + ", paket: " + paket.getOznaka() + " Vam je dostavljen. Uživajte!");
	   }
}
