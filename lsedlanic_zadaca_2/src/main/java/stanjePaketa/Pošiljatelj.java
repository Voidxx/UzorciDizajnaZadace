package stanjePaketa;

import objekti.Paket;

public class Pošiljatelj implements Observer{
	   private String senderName;

	   public Pošiljatelj(String senderName) {
	       this.senderName = senderName;
	   }

	   @Override
	   public void update(Paket paket) {
		      if (paket.getVrijeme_preuzimanja() != null) {
	       System.out.println("Sender " + senderName + " received a package: " + paket.getOznaka() + " has been delivered.");
		      }
	   }
}
