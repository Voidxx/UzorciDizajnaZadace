package app;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import citaci.CitacTxt;
import citaci.CsvLoader;
import citaci.CsvLoaderFactory;
import objekti.Mjesto;
import objekti.Osoba;
import objekti.Paket;
import objekti.Podrucje;
import objekti.Ulica;
import objekti.Vozilo;
import objekti.VrstaPaketa;
import stanjaVozila.AktivnoVozilo;
import tvrtka.Tvrtka;
import tvrtka.UredZaDostavu;
import tvrtka.UredZaPrijem;



public class Main {
	static String vs = null;
	static int ms = 0;
	static String pr = null;
	static String kr = null;
	static int mt = 0;
	static int vi = 0;
	static String pmu = null;
	static String pv = null;
	static String vp = null;
	static String pp = null;
	static String pu = null;
	static String pm = null;
	static String po = null;
	static String gps = null;
	static int isporuka = 0;



	
    public static void main(String[] args) throws ParseException, IOException {

        CitacTxt parametriDatoteke = new CitacTxt();
        Properties parametri = parametriDatoteke.ucitajParametre(args[0]);
        ProvjeraParametara provjeraParametara = new ProvjeraParametara();
        provjeraParametara.provjeriParametre(parametri);
        
        odrediUlazneVarijable(parametri);
        ucitajPodatke();

        VirtualnoVrijeme.getInstance();
        VirtualnoVrijeme virtualnoVrijeme = VirtualnoVrijeme.getInstance();
		virtualnoVrijeme.inicijalizirajVirtualniSat(vs);
        
        postaviStatusVozila();   
		   

        CommandHandler ipHandler = new SortirajPoTeziniDecorator(new IPHandler());
        CommandHandler vrHandler = new VRHandler();
        CommandHandler vvHandler = new VVHandler();
        CommandHandler svHandler = new SVHandler();
        CommandHandler vsHandler = new VSHandler();
        CommandHandler ppHandler = new PPHandler();
        CommandHandler psHandler = new PSHandler();
        CommandHandler poHandler = new POHandler();
        CommandHandler ssHandler = new SSHandler();
        CommandHandler lsHandler = new LSHandler();
        CommandHandler spvHandler = new SPVHandler();
        CommandHandler ppvHandler = new PPVHandler();
        CommandHandler qHandler = new QHandler();
        CommandHandler nepostojecaKomandaHandler = new NepostojecaKomandaHandler();

        ipHandler.setNext(vrHandler);
        vrHandler.setNext(vvHandler);
        vvHandler.setNext(svHandler);
        svHandler.setNext(vsHandler);
        vsHandler.setNext(ppHandler);
        ppHandler.setNext(psHandler);
        psHandler.setNext(poHandler);
        poHandler.setNext(ssHandler);
        ssHandler.setNext(lsHandler);
        lsHandler.setNext(spvHandler);
        spvHandler.setNext(ppvHandler);
        ppvHandler.setNext(qHandler);
        qHandler.setNext(nepostojecaKomandaHandler);
        
        Scanner scanner = new Scanner(System.in);
        String unos;
        while (true) {
            System.out.println("Unesite komandu:");
            unos = scanner.nextLine();
            try {
            	ipHandler.provjeriKomandu(unos);
            }
            catch(Exception e){
            	System.out.println(e.getMessage());
            	scanner.close();
            	break;
            }
        }
    }

	private static void postaviStatusVozila() {
		String[] gpsKoordinateUredaString = gps.trim().split(",");
           double latitudaUreda = Double.parseDouble(gpsKoordinateUredaString[0]);
           double longitudaUreda = Double.parseDouble(gpsKoordinateUredaString[1]);
           
		   for (Vozilo vozilo : UredZaDostavu.getInstance().dohvatiListuVozila()) {
			   vozilo.setTrenutniLon(longitudaUreda);
			   vozilo.setTrenutniLat(latitudaUreda);
			   if(vozilo.isTrenutno_vozi() == false && vozilo.getStatus().equals("A")) {
				   AktivnoVozilo aktivnoVozilo = new AktivnoVozilo();
				   vozilo.setState(aktivnoVozilo);
				   vozilo.setAktivnoStanje(aktivnoVozilo);
			   } 
		   }
	}

	private static void ucitajPodatke() throws IOException, ParseException {
		CsvLoader<VrstaPaketa> vrstePaketaLoader = (CsvLoader<VrstaPaketa>) CsvLoaderFactory.getLoader("vrstePaketa");
        List<VrstaPaketa> vrstePaketa = vrstePaketaLoader.loadCsv(vp);
        UredZaPrijem.getInstance().postaviVrstePaketa(vrstePaketa);
        CsvLoader<Vozilo> voziloLoader = (CsvLoader<Vozilo>) CsvLoaderFactory.getLoader("vozila");
        List<Vozilo> vozila = voziloLoader.loadCsv(pv);
        UredZaDostavu.getInstance().postaviVozila(vozila);
        CsvLoader<Paket> paketLoader = (CsvLoader<Paket>) CsvLoaderFactory.getLoader("paketi");
        List<Paket> paketi = paketLoader.loadCsv(pp);
        UredZaPrijem.getInstance().postaviOcekivanePakete(paketi);
 
        CsvLoader<Ulica> uliceLoader = (CsvLoader<Ulica>) CsvLoaderFactory.getLoader("ulice");
        List<Ulica> ulice = uliceLoader.loadCsv(pu);
        Tvrtka.getInstance().setUlice(ulice);
        CsvLoader<Mjesto> mjestaLoader = (CsvLoader<Mjesto>) CsvLoaderFactory.getLoader("mjesta");
        List<Mjesto> mjesta = mjestaLoader.loadCsv(pm);
        Tvrtka.getInstance().setMjesta(mjesta);
        CsvLoader<Osoba> osobeLoader = (CsvLoader<Osoba>) CsvLoaderFactory.getLoader("osobe");
        List<Osoba> osobe = osobeLoader.loadCsv(po);
        Tvrtka.getInstance().setOsobe(osobe);
        CsvLoader<Podrucje> podrucjaLoader = (CsvLoader<Podrucje>) CsvLoaderFactory.getLoader("podrucja");
        List<Podrucje> podrucja = podrucjaLoader.loadCsv(pmu);
        Tvrtka.getInstance().setPodrucja(podrucja);
	}
	


	   private static void odrediUlazneVarijable(Properties parametri) {
		   Tvrtka tvrtka = Tvrtka.getInstance();
	       vs = parametri.getProperty("vs");
	       tvrtka.setVs(vs);
	       ms = Integer.parseInt(parametri.getProperty("ms"));
	       tvrtka.setMs(ms);
	       pr = parametri.getProperty("pr");
	       tvrtka.setPr(pr);
	       kr = parametri.getProperty("kr");
	       tvrtka.setKr(kr);
	       mt = Integer.parseInt(parametri.getProperty("mt"));
	       tvrtka.setMt(mt);
	       vi = Integer.parseInt(parametri.getProperty("vi"));
	       tvrtka.setVi(vi);
	       pv = parametri.getProperty("pv");
	       tvrtka.setPv(pv);
	       vp = parametri.getProperty("vp");
	       tvrtka.setVp(vp);
	       pp = parametri.getProperty("pp");
	       tvrtka.setPp(pp);
	       pu = parametri.getProperty("pu");
	       tvrtka.setPu(pu);
	       pm = parametri.getProperty("pm");
	       tvrtka.setPm(pm);
	       po = parametri.getProperty("po");
	       tvrtka.setPo(po);
	       pmu = parametri.getProperty("pmu");
	       tvrtka.setPmu(pmu);
	       gps = parametri.getProperty("gps");
	       tvrtka.setGps(gps);
	       isporuka = Integer.parseInt(parametri.getProperty("isporuka"));
	       tvrtka.setIsporuka(isporuka);
	   }
    


}


