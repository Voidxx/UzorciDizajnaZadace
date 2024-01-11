package stanjePaketa;

import objekti.Paket;

public class Pošiljatelj implements Observer, Cloneable{
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

		  @Override
		  public Object clone() throws CloneNotSupportedException {
		      return super.clone();
		  }
}
