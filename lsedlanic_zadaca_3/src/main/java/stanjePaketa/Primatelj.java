package stanjePaketa;

import objekti.Paket;

public class Primatelj implements Observer, Cloneable{
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
		  @Override
		  public Object clone() throws CloneNotSupportedException {
		      return super.clone();
		  }
	   
	   
}
