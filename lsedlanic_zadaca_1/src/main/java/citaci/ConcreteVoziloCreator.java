package citaci;

import java.text.ParseException;
import java.util.ArrayList;

import paket.Paket;
import paket.VrstaPaketa;
import vozilo.Vozilo;

public class ConcreteVoziloCreator extends CsvReader<Vozilo> {
    @Override
    protected Vozilo createObject() {
        return new Vozilo(null, null, 0, 0, 0, 0, new ArrayList<Paket>(), 0, false, null, 0);
    }
}