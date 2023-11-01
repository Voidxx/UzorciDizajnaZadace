package citaci;

import java.text.ParseException;

import paket.VrstaPaketa;

public class ConcreteVrstaPaketaCreator extends CsvReader<VrstaPaketa> {
    @Override
    protected VrstaPaketa createObject() {
        return new VrstaPaketa(null, null, 0, 0, 0, 0, 0, 0, 0, 0);
    }
}