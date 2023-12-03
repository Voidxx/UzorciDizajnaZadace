package stanjePaketa;

import objekti.Paket;

public class Primatelj implements Observer{
	   private String receiverName;

	   public Primatelj(String receiverName) {
	       this.receiverName = receiverName;
	   }

	   @Override
	   public void update(Paket paket) {
	      if (paket.getVrijeme_preuzimanja() != null) {
	          System.out.println("Receiver " + receiverName + " received a package: " + paket.getOznaka() + " has been delivered.");
	      }
	   }
}
