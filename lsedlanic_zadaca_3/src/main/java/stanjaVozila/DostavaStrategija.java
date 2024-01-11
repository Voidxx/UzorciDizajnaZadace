package stanjaVozila;

import objekti.Vozilo;
import voznja.VoznjaBuilder;

public interface DostavaStrategija {
	void odrediRedoslijed(Vozilo vozilo, VoznjaBuilder builder);
	}