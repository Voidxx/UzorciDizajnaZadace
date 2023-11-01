package citaci;


import paket.Paket;

public class ConcretePaketCreator extends CsvReader<Paket> {
    @Override
    protected Paket createObject() {
        return new Paket(null, null, null, null, null, 0, 0, 0, 0, null, 0, 0, null);
    }
}