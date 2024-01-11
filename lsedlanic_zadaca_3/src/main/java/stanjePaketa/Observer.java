package stanjePaketa;

import objekti.Paket;

public interface Observer {
	   void paketZaprimljen(Paket paket);
	   void paketDostavljen(Paket paket);
	}