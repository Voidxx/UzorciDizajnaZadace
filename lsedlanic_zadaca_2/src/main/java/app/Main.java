package app;

import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import citaci.CitacTxt;
import citaci.CsvLoader;
import citaci.CsvLoaderFactory;
import objekti.Mjesto;
import objekti.Osoba;
import objekti.Paket;
import objekti.PaketIterator;
import objekti.PaketIteratorImpl;
import objekti.Podrucje;
import objekti.Ulica;
import objekti.Vozilo;
import objekti.VrstaPaketa;
import stanjaVozila.AktivnoVozilo;
import stanjePaketa.Pošiljatelj;
import stanjePaketa.Primatelj;
import stanjePaketa.Subject;
import tvrtka.Tvrtka;
import tvrtka.UredZaDostavu;
import tvrtka.UredZaPrijem;
import visitori.VoznjaVisitor;
import visitori.VoznjaVisitorImpl;
import voznja.Voznja;
import voznja.VoznjaBuilder;



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

        VirtualnoVrijeme.getInstance();
        VirtualnoVrijeme.inicijalizirajVirtualniSat(vs);
           String[] gpsKoordinateUredaString = gps.trim().split(",");
           double latitudaUreda = Double.parseDouble(gpsKoordinateUredaString[0]);
           double longitudaUreda = Double.parseDouble(gpsKoordinateUredaString[1]);
           
		   for (Vozilo vozilo : UredZaDostavu.getInstance().dohvatiListuVozila()) {
			   vozilo.setTrenutniLon(longitudaUreda);
			   vozilo.setTrenutniLat(latitudaUreda);
			   if(vozilo.isTrenutno_vozi() == false && vozilo.getStatus().equals("A")) {
				   vozilo.setState(new AktivnoVozilo());
			   } 
		   }   
		   
		provjeriStatusLagera();
        Scanner scanner = new Scanner(System.in);
        String unos;
        while (true) {
            System.out.println("Unesite komandu:");
            unos = scanner.nextLine().toUpperCase();
            if ("Q".equals(unos)) {
                System.out.println("Izlazim iz programa...");
                break;
            } else if (unos.startsWith("IP")) {
                hendlajIPKomandu();
            } else if (unos.startsWith("VR")) {
                hendlajVRKomandu(unos);        
            }
              else if (unos.startsWith("VV")){
            	  HendlajVVKomandu(unos);
            }
              else {
                System.out.println("Nevažeća komanda");
            }
        }
        scanner.close();
    }



	private static void provjeriStatusLagera() {
		for(Paket paket : UredZaPrijem.getInstance().dobaviListuOcekivanihPaketa()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
    		LocalDateTime dateTime = LocalDateTime.parse(paket.getVrijeme_prijema(), formatter);
    		LocalDateTime pocetakRada = LocalDateTime.parse(vs, formatter);
    		if(dateTime.isBefore(pocetakRada)) {
    			UredZaPrijem.getInstance().dodajPaketUSpremneZaDostavu(paket);
    			System.out.println("Paket iz lagera: " + paket.getOznaka() + " je spreman za dostavu.");
    		}
		}
	}



	private static void hendlajVRKomandu(String unos) {
		System.out.println("VR komanda dobivena");
		String[] parts = unos.split(" ");
		if (parts.length > 1) {
		    try {
		        int hours = Integer.parseInt(parts[1]);
		        for (int i = 0; i < (hours*60*60)/ms; i++) {
		            try {
		            	Thread.sleep(1000);
		            	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
		            	LocalTime otvorenje = LocalTime.parse(pr, formatter);
		            	LocalTime zatvaranje = LocalTime.parse(kr, formatter);

		                Instant prijasnjeVrijeme = VirtualnoVrijeme.getVrijeme();
		                LocalDateTime localDateTime = LocalDateTime.ofInstant(prijasnjeVrijeme, ZoneId.systemDefault());
		                LocalTime lokalnoVirtualnoVrijeme = localDateTime.toLocalTime();
		                
		                if (lokalnoVirtualnoVrijeme.isBefore(otvorenje) || lokalnoVirtualnoVrijeme.isAfter(zatvaranje)) { 
		                	System.out.println("Nije radno vrijeme...");
		                	return;
		                } else {
		                
		                LocalDateTime vrijemePrije = LocalDateTime.ofInstant(prijasnjeVrijeme, ZoneId.systemDefault());
		                LocalDateTime vrijemeNakon = vrijemePrije.plusSeconds(ms);
		                
		                if (vrijemeNakon.getMinute() < vrijemePrije.getMinute()) {
		                	int sekundiDoPunogSata = 3600 - (vrijemePrije.getMinute() * 60);
		                	int preostaleSekundeNakonPunogSata = vrijemeNakon.getMinute() * 60;
		                	
		                	provjeriPakete(sekundiDoPunogSata);	
		                	ukrcajPakete();
		                	provjeriTrebajuLiKrenutiVozila();
		                	provjeriPakete(preostaleSekundeNakonPunogSata);
		                    
		                } else {
		                	System.out.println("Trenutno vrijeme virtualnog sata: " + VirtualnoVrijeme.getVrijemeDateTime());  
		                	VirtualnoVrijeme.nadodajVrijeme(ms);
		                	provjeriHoceLiBitiDostavljenPaket(ms);
		                	provjeriPrijemPaketa(vrijemePrije, vrijemeNakon);   
		                	provjeriJeLiPunoVozilo();  	
		                }
		                }
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }
		        }
		    } catch (NumberFormatException e) {
		        System.out.println("Nevažeći broj sati.");
		    }
		}
	}

	private static void HendlajVVKomandu(String unos) {
		   String[] parts = unos.split(" ");
		   if (parts.length > 1) {
		       String registracija = parts[1];
		       Vozilo vozilo = UredZaDostavu.getInstance().dohvatiVozilo(registracija);
		       if (vozilo != null) {
		           VoznjaVisitor visitor = new VoznjaVisitorImpl();
		           for (Voznja voznja : vozilo.getObavljeneVoznje()) {
		               voznja.accept(visitor);
		           }
		       } else {
		           System.out.println("Vozilo s registracijom " + registracija + " ne postoji.");
		       }
		   } else {
		       System.out.println("Nevažeća komanda.");
		   }
	}


	private static void provjeriPakete(int sekundi) {
		LocalDateTime vrijemePrije;
		LocalDateTime vrijemeNakon;
		provjeriHoceLiBitiDostavljenPaket(sekundi);
		vrijemePrije = LocalDateTime.ofInstant(VirtualnoVrijeme.getVrijeme(), ZoneId.systemDefault());
		vrijemeNakon = vrijemePrije.plusSeconds(sekundi);
		System.out.println("Trenutno vrijeme virtualnog sata: " + VirtualnoVrijeme.getVrijemeDateTime());  
		VirtualnoVrijeme.nadodajVrijeme(sekundi);
		provjeriPrijemPaketa(vrijemePrije, vrijemeNakon);;
	}



	private static void hendlajIPKomandu() {
		System.out.println("IP komanda dobivena");

		System.out.printf("%-20s %-15s %-15s %-20s %-20s %-15s %-15s%n", "Vrijeme prijema", "Vrsta paketa", "Vrsta usluge", "Status isporuke", "Vrijeme preuzimanja", "Iznos dostave", "Iznos pouzeća");

		for (Paket paket : UredZaDostavu.getInstance().getDostavljeniPaketi()) {
			
		    System.out.printf("%-20s %-15s %-15s %-20s %-20s %-15s %-15s%n", paket.getVrijeme_prijema(), paket.getVrsta_paketa(), paket.getUsluga_dostave(), "Dostavljeno", paket.getVrijeme_preuzimanja(), paket.getIzracunati_iznos_dostave(), paket.getIznos_pouzeca());
		}
		Set<Paket> dostavljeniPaketiSet = new HashSet<>(UredZaDostavu.getInstance().getDostavljeniPaketi());
		List<Paket> ocekivaniPaketi = UredZaPrijem.getInstance().dobaviListuOcekivanihPaketa();
		ocekivaniPaketi.removeIf(dostavljeniPaketiSet::contains);

		for (Paket paket : ocekivaniPaketi) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
			LocalDateTime dateTime = LocalDateTime.parse(paket.getVrijeme_prijema(), formatter);
			Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
			LocalDateTime dateTimePocetka = LocalDateTime.parse(vs, formatter);
			Instant instantPocetka = dateTimePocetka.atZone(ZoneId.systemDefault()).toInstant();
			if(instant.isBefore(VirtualnoVrijeme.getVrijeme()) && instant.isAfter(instantPocetka))
				System.out.printf("%-20s %-15s %-15s %-20s %-20s %-15s %-15s%n", paket.getVrijeme_prijema(), paket.getVrsta_paketa(), paket.getUsluga_dostave(), "U preuzimanju", null,  paket.getIzracunati_iznos_dostave(), paket.getIznos_pouzeca());
		}
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
    

    
    private static void provjeriHoceLiBitiDostavljenPaket(int sekunde) {
    	for(Vozilo vozilo : UredZaDostavu.getInstance().dohvatiListuVozila()) {
    		if(vozilo.isTrenutno_vozi() == true) {
	    			Instant prijasnjeVrijeme = vozilo.getVrijeme();
	                Instant vrijemeDostave = prijasnjeVrijeme.plusSeconds(vi*60);
	                if(vrijemeDostave.isBefore(VirtualnoVrijeme.getVrijeme()) || vrijemeDostave.equals(VirtualnoVrijeme.getVrijeme())) {
	                	vozilo.dostaviPakete();
	            }
	        }
    	}
	}



	private static void provjeriTrebajuLiKrenutiVozila() {
		for(Vozilo vozilo : UredZaDostavu.getInstance().dohvatiListuVozila()) {
			if(!vozilo.isTrenutno_vozi() && vozilo.getStatus().equals("A")) {
				if((vozilo.getTrenutni_teret_tezina() >= vozilo.getKapacitet_kg() / 2) || (vozilo.getTrenutni_teret_volumen() >= vozilo.getKapacitet_m3() / 2) || vozilo.getUkrcani_paketi().stream().anyMatch(paket -> paket.getUsluga_dostave().equals("H")) && vozilo.getUkrcani_paketi() != null) {
					vozilo.setTrenutno_vozi(true);
					AktivnoVozilo aktivnoVozilo = (AktivnoVozilo) vozilo.getState();
					VoznjaBuilder builder = aktivnoVozilo.getBuilder();
					builder.postaviVrijemePocetka(VirtualnoVrijeme.getVrijemeDateTime());
					builder.postaviPostotakZauzecaProstora(vozilo.getTrenutni_teret_volumen() / vozilo.getKapacitet_m3() * 100);
					builder.postaviPostotakZauzecaTezine(vozilo.getTrenutni_teret_volumen() / vozilo.getKapacitet_kg() * 100);
					System.out.println("Vozilo: " + vozilo.getOpis() + " Kreće u dostavu!");
					vozilo.setDostavaSat(VirtualnoVrijeme.getSat());
				}
			}
		}
	}
	
	private static void provjeriJeLiPunoVozilo() {
		for(Vozilo vozilo : UredZaDostavu.getInstance().dohvatiListuVozila()) {
			if(vozilo.getTrenutni_teret_tezina() == vozilo.getKapacitet_kg() || vozilo.getTrenutni_teret_volumen() == vozilo.getKapacitet_m3() && !vozilo.isTrenutno_vozi()) {
				vozilo.setTrenutno_vozi(true);
				System.out.println("Vozilo: " + vozilo.getOpis() + " Kreće u dostavu!");
				vozilo.setDostavaSat(VirtualnoVrijeme.getSat());
			}
		}
		
	}


	private static void provjeriPrijemPaketa(LocalDateTime prije, LocalDateTime poslije) {
		for (Paket paket : UredZaPrijem.getInstance().dobaviListuOcekivanihPaketa()) {
			Subject subject = new Subject();
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
    		LocalDateTime dateTime = LocalDateTime.parse(paket.getVrijeme_prijema(), formatter);
    		if(dateTime.isBefore(poslije) && dateTime.isAfter(prije)) {
    			UredZaPrijem.getInstance().dodajPaketUSpremneZaDostavu(paket);
    			System.out.println("Paket: " + paket.getOznaka() + " je zaprimljen.");
    			
                //obavijestavanje
                Pošiljatelj posiljatelj = new Pošiljatelj(paket.getPosiljatelj());
                Primatelj primatelj = new Primatelj(paket.getPrimatelj());
                
                subject.attach(primatelj);
                subject.attach(posiljatelj);
                
                subject.setPaket(paket);
    		}
		}
	}


    
//	private static void ukrcajPakete() {
//		   PaketIterator iterator = new PaketIteratorImpl(UredZaPrijem.getInstance().dobaviListuPaketaZaDostavu());
//	       for (Vozilo vozilo : UredZaDostavu.getInstance().dohvatiListuVozila()) {
//		   while(iterator.hasNext()) {
//		       Paket paket = iterator.next();
//		           if(vozilo.isTrenutno_vozi() == false && vozilo.getStatus().equals("A")) {
//		               vozilo.setState(new AktivnoVozilo());
//		               vozilo.ukrcajPakete(paket);
//		               iterator.remove();
//		               break;
//		           }
//		       }
//		   }
//		}
	
	
	private static void ukrcajPakete() {
		   // Sort the list of packages by area id
		   Collections.sort(UredZaPrijem.getInstance().dobaviListuPaketaZaDostavu(), new Comparator<Paket>() {
		       @Override
		       public int compare(Paket p1, Paket p2) {
		    	   if(p1.vratiPrimatelja() == null || p2.vratiPrimatelja() == null)
		    		   return 0;

		    		Osoba primatelj1 = p1.vratiPrimatelja();
		    		Osoba primatelj2 = p2.vratiPrimatelja();

		    		if(primatelj1.dobaviPodrucje() == null || primatelj2.dobaviPodrucje() == null)
		    		   return 0;

		    		return Integer.compare(primatelj1.dobaviPodrucje().getId(), primatelj2.dobaviPodrucje().getId());
		       }
		   });

  
		   // Iterate over the sorted list of packages
		   PaketIterator iterator = new PaketIteratorImpl(UredZaPrijem.getInstance().dobaviListuPaketaZaDostavu());
		   while(iterator.hasNext()) {
		       Paket paket = iterator.next();
		       // Find the first vehicle that has the same area id in its getPodrucjaPoRangu() method
		       for (Vozilo vozilo : UredZaDostavu.getInstance().dohvatiListuVozila()) {
		    	if(paket.vratiPrimatelja() != null) {
		    	if(paket.vratiPrimatelja().dobaviPodrucje() != null) {
		    		
		           if (vozilo.getPodrucjaPoRangu().contains(paket.vratiPrimatelja().dobaviPodrucje().getId())) {
		               // Load the package into the vehicle
		               vozilo.ukrcajPakete(paket);
		               iterator.remove();
		               break;
		           }
		       }else {
		    	   iterator.remove();
		    	   break;
		       }
		    	
		   }else {
			   iterator.remove();
			   break;
		   }
		}
	}
}

}


