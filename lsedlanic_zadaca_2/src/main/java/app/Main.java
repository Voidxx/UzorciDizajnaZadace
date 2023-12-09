package app;

import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import citaci.CitacTxt;
import citaci.CsvLoader;
import citaci.CsvLoaderFactory;
import objekti.DioPodrucja;
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
import stanjaVozila.NeaktivnoVozilo;
import stanjaVozila.NeispravnoVozilo;
import stanjePaketa.Pošiljatelj;
import stanjePaketa.Primatelj;
import stanjePaketa.Subject;
import tvrtka.Tvrtka;
import tvrtka.UredZaDostavu;
import tvrtka.UredZaPrijem;
import visitori.Visitor;
import visitori.VisitorImpl;
import voznja.SegmentVoznje;
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
        ucitajPodatke();

        VirtualnoVrijeme.getInstance();
        VirtualnoVrijeme virtualnoVrijeme = VirtualnoVrijeme.getInstance();
		virtualnoVrijeme.inicijalizirajVirtualniSat(vs);
        
        postaviStatusVozila();   
		   
		
		
        Scanner scanner = new Scanner(System.in);
        String unos;
        while (true) {
            System.out.println("Unesite komandu:");
            unos = scanner.nextLine();
            if ("Q".equals(unos)) {
                System.out.println("Izlazim iz programa...");
                break;
            } else if (Pattern.matches("^IP$", unos)) {
                hendlajIPKomandu();
            } else if (Pattern.matches("^VR\\s\\d+$", unos)) {
                hendlajVRKomandu(unos);        
            }
              else if (Pattern.matches("^VV\\s[A-Ža-ž0-9]+$", unos)){
            	  HendlajVVKomandu(unos);
            }
              else if (Pattern.matches("^SV$", unos)) {
            	  HendlajSVKomandu();
              }
              else if (Pattern.matches("^VS\\s[A-Ža-ž0-9]+\\s\\d+$", unos)) {
            	  HendlajVSKomandu(unos);
              }
              else if (Pattern.matches("^PP$", unos)) {
            	  HendlajPPKomandu();
              }
              else if (Pattern.matches("^PS\\s[A-Ža-ž0-9]+\\s(A|NI|NA)$", unos)) {
            	  hendlajPSKomandu(unos);
            }
              else if (Pattern.matches("^PO\\s'.*\\s.*'\\s[A-Ža-ž0-9]+\\s(D|N)$", unos)) {
            	  HendlajPOKomandu(unos);
              }
              else if (Pattern.matches("^SS$", unos)) {
            	  hendlajSSKomandu();
              }
              else if (Pattern.matches("^LS$", unos)) {
            	  hendlajLSKomandu();
              }
              else {
                System.out.println("Nevažeća komanda");
            }
        }
        scanner.close();
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
	
	
	private static void hendlajSSKomandu() {
		   SystemState systemState = new SystemState(UredZaDostavu.getInstance(), UredZaPrijem.getInstance(), Tvrtka.getInstance(), PogreskeBrojac.getInstance(), VirtualnoVrijeme.getInstance());
		   systemState.saveState("stanjeSustava.ser");
		}

		private static void hendlajLSKomandu() {
		   SystemStateProxy systemStateProxy = new SystemStateProxy();
		   SystemState systemState = systemStateProxy.loadState("stanjeSustava.ser");
		   if (systemState != null) {
		       UredZaDostavu.setInstance(systemState.getUredZaDostavu());
		       UredZaPrijem.setInstance(systemState.getUredZaPrijem());
		       Tvrtka.setInstance(systemState.getTvrtka());
		       PogreskeBrojac.setInstance(systemState.getPogreskeBrojac());
			   VirtualnoVrijeme virtualnoVrijeme = VirtualnoVrijeme.getInstance();
		       virtualnoVrijeme.setInstance(systemState.getVirtualnoVrijeme());
		   }
		}

    private static void HendlajPOKomandu(String unos) {
    	String[] dijelovi = unos.split("'");
    	String[] drugidijelovi = dijelovi[2].trim().split(" ");
    	System.out.println(dijelovi[1] + drugidijelovi[0] +  drugidijelovi[1]);
    	String posiljateljIliPrimatelj = dijelovi[1];
    	String oznakaPaketa = drugidijelovi[0];
    	String status = drugidijelovi[1];
    	
    	List<Paket> listaPaketaZaProvjeru = new ArrayList<Paket>();
    	for(Paket paket : UredZaPrijem.getInstance().dobaviListuPaketaZaDostavu()) {
    		if(paket.vratiPosiljatelja() != null)
    		listaPaketaZaProvjeru.add(paket);
    	}
    	for(Paket paket : UredZaPrijem.getInstance().dobaviListuOcekivanihPaketa()) {
    	for(Vozilo vozilo : UredZaDostavu.getInstance().dohvatiListuVozila()) {
    		if(vozilo.getUkrcani_paketi().contains(paket) && paket.vratiPosiljatelja() != null) {
    			listaPaketaZaProvjeru.add(paket);
    		}
    	}
    	}
    	
    	
    	for(Paket paket : listaPaketaZaProvjeru) {
    		Subject subject = paket.getOvajPaket();
    		if(status.equals("N")) {
	    		if(paket.vratiPosiljatelja().getOsoba().equals(posiljateljIliPrimatelj) && oznakaPaketa.equals(paket.getOznaka())) {
	    			Pošiljatelj posiljatelj = paket.getPosiljateljObserver();
	    			subject.detach(posiljatelj);
	    		}
	    		if(paket.vratiPrimatelja().getOsoba().equals(posiljateljIliPrimatelj) && oznakaPaketa.equals(paket.getOznaka())) {
	    			Primatelj primatelj = paket.getPrimateljObserver();
	    			subject.detach(primatelj);
	    		}
    		}
    		else if(status.equals("D")) {
        		if(paket.vratiPosiljatelja().getOsoba().equals(posiljateljIliPrimatelj) && oznakaPaketa.equals(paket.getOznaka())) {
        			Pošiljatelj posiljatelj = paket.getPosiljateljObserver();
        			subject.attach(posiljatelj);
        		}
        		if(paket.vratiPrimatelja().getOsoba().equals(posiljateljIliPrimatelj) && oznakaPaketa.equals(paket.getOznaka())) {
        			Primatelj primatelj = paket.getPrimateljObserver();
        			subject.attach(primatelj);
        		}
    		}
    	}
    	
    	
    	

    	}
    
    
    private static void HendlajPPKomandu() {
    	  for (Podrucje podrucje : Tvrtka.getInstance().getPodrucja()) {
    	      System.out.println(podrucje.getId());
    	      Set<Mjesto> printedMjesta = new HashSet<>();
    	      for (DioPodrucja dioPodrucja : podrucje.getChildren()) {
    	          if (dioPodrucja instanceof Mjesto) {
    	              Mjesto mjesto = (Mjesto) dioPodrucja;
    	              if (!printedMjesta.contains(mjesto) && mjesto.getNaziv() != null) {
    	                System.out.println("    " + mjesto.getNaziv());
    	                printedMjesta.add(mjesto);
    	              }
    	          } else if(dioPodrucja instanceof Ulica) {
    	              Ulica ulica = (Ulica) dioPodrucja;
    	              System.out.println("          " + ulica.getNaziv());
    	          }
    	      }
    	  }
    	}

	private static void HendlajSVKomandu() {
		   System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", "Oznaka vozila" , "Status", "Ukupno odvoženo km", "Broj hitnih paketa", "Broj običnih paketa", "Broj isporučenih paketa", "% zauzeća prostora", "% zauzeća težine,", "broj vožnji");

		   Visitor visitor = new VisitorImpl();
		   for (Vozilo vozilo : UredZaDostavu.getInstance().dohvatiListuVozila()) {
		       vozilo.accept(visitor);
		   }
	}
	

	
	private static void HendlajVSKomandu(String unos) {
		   String[] parts = unos.split(" ");
		   if (parts.length > 1) {
			   System.out.println(parts[0] + " " +  parts[1] + " " + parts[2]);
		       String voziloRegistracija = parts[1];
		       int brojVoznje = Integer.parseInt(parts[2]);
		       Vozilo vozilo = UredZaDostavu.getInstance().dohvatiVozilo(voziloRegistracija);
			   System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", 
				       "Vrijeme početka", 
				       "Vrijeme kraja", 
				       "Udaljenost (km)", 
				       "Trajanje vožnje", 
				       "Trajanje isporuke", 
				       "Ukupno trajanje segmenta", 
				       "Paket za dostaviti");
		       if (vozilo != null) {
				   Visitor visitor = new VisitorImpl();
		           if(vozilo.getObavljeneVoznje().get(brojVoznje - 1) != null)
		           for (SegmentVoznje segmentVoznje : vozilo.getObavljeneVoznje().get(brojVoznje - 1).getSegmentiVoznje()) {
		        	   
		               segmentVoznje.accept(visitor);
		           }
		       } else {
		           System.out.println("Vozilo s registracijom " + voziloRegistracija + " ne postoji.");
		       }
		   } else {
		       System.out.println("Nevažeća komanda.");
		   }
		}



	private static void provjeriStatusLagera() {
		   if (!Tvrtka.getInstance().isProvjeriStatusLageraCalled()) {
		       for(Paket paket : UredZaPrijem.getInstance().dobaviListuOcekivanihPaketa()) {
		           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
		           LocalDateTime dateTime = LocalDateTime.parse(paket.getVrijeme_prijema(), formatter);
		           LocalDateTime pocetakRada = LocalDateTime.parse(vs, formatter);
		           if(dateTime.isBefore(pocetakRada)) {
		               UredZaPrijem.getInstance().dodajPaketUSpremneZaDostavu(paket);
		               paket.getOvajPaket().notifyObservers("zaprimljen");
		           }
		       }
		       Tvrtka.getInstance().setProvjeriStatusLageraCalled(true);
		   }
	}



	private static void hendlajVRKomandu(String unos) {
		System.out.println("VR komanda dobivena");
		String[] parts = unos.split(" ");
		VirtualnoVrijeme virtualnoVrijeme = VirtualnoVrijeme.getInstance();
		if (parts.length > 1) {
		    try {
		        int hours = Integer.parseInt(parts[1]);
		        for (int i = 0; i < (hours*60*60)/ms; i++) {
		            try {
		            	Thread.sleep(1000);
		            	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
		            	LocalTime otvorenje = LocalTime.parse(pr, formatter);
		            	LocalTime zatvaranje = LocalTime.parse(kr, formatter);
		            	
		                Instant prijasnjeVrijeme = virtualnoVrijeme.getVrijeme();
		                LocalDateTime localDateTime = LocalDateTime.ofInstant(prijasnjeVrijeme, ZoneId.systemDefault());
		                LocalTime lokalnoVirtualnoVrijeme = localDateTime.toLocalTime();
		                
		                if (lokalnoVirtualnoVrijeme.isBefore(otvorenje) || lokalnoVirtualnoVrijeme.isAfter(zatvaranje)) { 
		                	System.out.println("Nije radno vrijeme...");
		                	virtualnoVrijeme.nadodajVrijeme(ms);
		                	System.out.println("Trenutno vrijeme virtualnog sata: " + virtualnoVrijeme.getVrijemeDateTime());  
		                } else {
		                provjeriStatusLagera();
		                LocalDateTime vrijemePrije = LocalDateTime.ofInstant(prijasnjeVrijeme, ZoneId.systemDefault());
		                LocalDateTime vrijemeNakon = vrijemePrije.plusSeconds(ms);
		                
		                if (vrijemeNakon.getMinute() < vrijemePrije.getMinute()) {
		                	int sekundiDoPunogSata = 3600 - (vrijemePrije.getMinute() * 60);
		                	int preostaleSekundeNakonPunogSata = vrijemeNakon.getMinute() * 60;
		                	provjeriHoceLiBitiDostavljenPaket(sekundiDoPunogSata);
		                	provjeriPakete(sekundiDoPunogSata);
		                	provjeriHoceLiBitiDostavljenPaket(preostaleSekundeNakonPunogSata);
		                	provjeriPakete(preostaleSekundeNakonPunogSata);
		                	ukrcajPakete();
		                	provjeriTrebajuLiKrenutiVozila();
		                	provjeriJeLiPunoVozilo();  	
		                }
		                else {
		                	System.out.println("Trenutno vrijeme virtualnog sata: " + virtualnoVrijeme.getVrijemeDateTime());  
		                	virtualnoVrijeme.nadodajVrijeme(ms);
		                	provjeriHoceLiBitiDostavljenPaket(ms);
		                	provjeriPrijemPaketa(vrijemePrije, vrijemeNakon);   
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
				   Visitor visitor = new VisitorImpl();
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
		VirtualnoVrijeme virtualnoVrijeme = VirtualnoVrijeme.getInstance();
		LocalDateTime vrijemePrije;
		LocalDateTime vrijemeNakon;
		provjeriHoceLiBitiDostavljenPaket(sekundi);
		vrijemePrije = LocalDateTime.ofInstant(virtualnoVrijeme.getVrijeme(), ZoneId.systemDefault());
		vrijemeNakon = vrijemePrije.plusSeconds(sekundi);
		System.out.println("Trenutno vrijeme virtualnog sata: " + virtualnoVrijeme.getVrijemeDateTime());  
		virtualnoVrijeme.nadodajVrijeme(sekundi);
		provjeriPrijemPaketa(vrijemePrije, vrijemeNakon);;
	}

	private static void hendlajPSKomandu(String unos) {
			String[] parts = unos.split(" ");
			String vehicleId = parts[1];
			String status = parts[2];
			
			Vozilo vozilo = UredZaDostavu.getInstance().dohvatiVozilo(vehicleId);
			if (vozilo != null) {
			   if ("A".equals(status)) {
				   if(vozilo.getState().mozeSePonovoAktivirati())
					   vozilo.setState(vozilo.getAktivnoStanje());
				   else
					   System.out.println("Nemožete neispravno vozilo ponovno postaviti kao aktivno/neaktivno.");
			   } else if ("NI".equals(status)) {
				   if(vozilo.getNeispravnoStanje() == null) {
				   NeispravnoVozilo neispravnoVozilo = new NeispravnoVozilo();
			       vozilo.setState(neispravnoVozilo);
			       vozilo.setNeispravnoStanje(neispravnoVozilo);
				   }
				   else {
					   vozilo.setState(vozilo.getNeispravnoStanje());
				   }
			   } else if ("NA".equals(status)) {
				   if(vozilo.getNeaktivnoStanje() == null) {
					   NeaktivnoVozilo neaktivnoVozilo = new NeaktivnoVozilo();
					   vozilo.setState(neaktivnoVozilo);
					   vozilo.setNeaktivnoStanje(neaktivnoVozilo);
				   }
				   else {
					   vozilo.setState(vozilo.getNeaktivnoStanje());
				   }
			   }
			} else {
			   System.out.println("Nema vozila sa opisom  " + vehicleId + ".");
			}
		}

	private static void hendlajIPKomandu() {
		System.out.println("IP komanda dobivena");
		VirtualnoVrijeme virtualnoVrijeme = VirtualnoVrijeme.getInstance();

		System.out.printf("%-20s %-20s %-15s %-15s %-20s %-20s %-15s %-15s%n", "Oznaka paketa" ,"Vrijeme prijema", "Vrsta paketa", "Vrsta usluge", "Status isporuke", "Vrijeme preuzimanja", "Iznos dostave", "Iznos pouzeća");

		for (Paket paket : UredZaDostavu.getInstance().getDostavljeniPaketi()) {
			
		    System.out.printf("%-20s %-20s %-15s %-15s %-20s %-20s %-15s %-15s%n",paket.getOznaka(), paket.getVrijeme_prijema(), paket.getVrsta_paketa(), paket.getUsluga_dostave(), "Dostavljeno", paket.getVrijeme_preuzimanja(), paket.getIzracunati_iznos_dostave(), paket.getIznos_pouzeca());
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
			if((instant.isBefore(virtualnoVrijeme.getVrijeme()) && instant.isAfter(instantPocetka)) || instant.isBefore(virtualnoVrijeme.getVrijeme()))
				System.out.printf("%-20s %-20s %-15s %-15s %-20s %-20s %-15s %-15s%n", paket.getOznaka(), paket.getVrijeme_prijema(), paket.getVrsta_paketa(), paket.getUsluga_dostave(), "U preuzimanju", null,  paket.getIzracunati_iznos_dostave(), paket.getIznos_pouzeca());
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
    		if(vozilo.isTrenutno_vozi() == true && vozilo.getStatus().equals("A")) {
	                	vozilo.dostaviPakete();
    		}
    	}
    }



	private static void provjeriTrebajuLiKrenutiVozila() {
		VirtualnoVrijeme virtualnoVrijeme = VirtualnoVrijeme.getInstance();

		for(Vozilo vozilo : UredZaDostavu.getInstance().dohvatiListuVozila()) {
			if(!vozilo.isTrenutno_vozi() && vozilo.getStatus().equals("A")) {
				if((vozilo.getTrenutni_teret_tezina() >= vozilo.getKapacitet_kg() / 2) || (vozilo.getTrenutni_teret_volumen() >= vozilo.getKapacitet_m3() / 2) || vozilo.getUkrcani_paketi().stream().anyMatch(paket -> paket.getUsluga_dostave().equals("H")) && vozilo.getUkrcani_paketi() != null) {
					vozilo.setTrenutno_vozi(true);
					AktivnoVozilo aktivnoVozilo = (AktivnoVozilo) vozilo.getState();
					VoznjaBuilder builder = aktivnoVozilo.getBuilder();
					builder.postaviVrijemePocetka(virtualnoVrijeme.getVrijemeDateTime());
					builder.postaviPostotakZauzecaProstora(vozilo.getTrenutni_teret_volumen() / vozilo.getKapacitet_m3() * 100);
					builder.postaviPostotakZauzecaTezine(vozilo.getTrenutni_teret_volumen() / vozilo.getKapacitet_kg() * 100);
					System.out.println("Vozilo: " + vozilo.getOpis() + " Kreće u dostavu!");
					vozilo.setDostavaSat(virtualnoVrijeme.getSat());
				}
			}
		}
	}
	
	private static void provjeriJeLiPunoVozilo() {
		VirtualnoVrijeme virtualnoVrijeme = VirtualnoVrijeme.getInstance();

		for(Vozilo vozilo : UredZaDostavu.getInstance().dohvatiListuVozila()) {
			if(vozilo.getTrenutni_teret_tezina() == vozilo.getKapacitet_kg() || vozilo.getTrenutni_teret_volumen() == vozilo.getKapacitet_m3() && !vozilo.isTrenutno_vozi() && vozilo.getStatus().equals("A")) {
				vozilo.setTrenutno_vozi(true);
				AktivnoVozilo aktivnoVozilo = (AktivnoVozilo) vozilo.getState();
				VoznjaBuilder builder = aktivnoVozilo.getBuilder();
				builder.postaviVrijemePocetka(virtualnoVrijeme.getVrijemeDateTime());
				builder.postaviPostotakZauzecaProstora(vozilo.getTrenutni_teret_volumen() / vozilo.getKapacitet_m3() * 100);
				builder.postaviPostotakZauzecaTezine(vozilo.getTrenutni_teret_volumen() / vozilo.getKapacitet_kg() * 100);
				System.out.println("Vozilo: " + vozilo.getOpis() + " Kreće u dostavu!");
				vozilo.setDostavaSat(virtualnoVrijeme.getSat());
			}
		}
		
	}


	private static void provjeriPrijemPaketa(LocalDateTime prije, LocalDateTime poslije) {
		for (Paket paket : UredZaPrijem.getInstance().dobaviListuOcekivanihPaketa()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
    		LocalDateTime dateTime = LocalDateTime.parse(paket.getVrijeme_prijema(), formatter);
    		if(dateTime.isBefore(poslije) && dateTime.isAfter(prije)) {
    			UredZaPrijem.getInstance().dodajPaketUSpremneZaDostavu(paket);

                paket.getOvajPaket().notifyObservers("zaprimljen");
    		}
		}
	}


    
	
	private static void ukrcajPakete() {
	    Collections.sort(UredZaPrijem.getInstance().dobaviListuPaketaZaDostavu(), new Comparator<Paket>() {
	        @Override
	        public int compare(Paket p1, Paket p2) {
	            if (p1.getUsluga_dostave().equals("H") && !p2.getUsluga_dostave().equals("H")) {
	                return -1;
	            } else if (!p1.getUsluga_dostave().equals("H") && p2.getUsluga_dostave().equals("H")) {
	                return 1;
	            } else {
	                return 0;
	            }
	        }
	    });

		   PaketIterator iterator = new PaketIteratorImpl(UredZaPrijem.getInstance().dobaviListuPaketaZaDostavu());
		   while(iterator.hasNext()) {
		       Paket paket = iterator.next();
		       Vozilo bestVozilo = null;
		       int bestRank = 4;
		       for (Vozilo vozilo : UredZaDostavu.getInstance().dohvatiListuVozila()) {
		           if(paket.vratiPrimatelja() != null && paket.vratiPrimatelja().dobaviPodrucje() != null) {
		               if (vozilo.getPodrucjaPoRangu().contains(paket.vratiPrimatelja().dobaviPodrucje().getId())) {
		                  int rank = vozilo.getPodrucjaPoRangu().indexOf(paket.vratiPrimatelja().dobaviPodrucje().getId()) + 1;
		                  if (bestVozilo == null || bestRank > rank) {
		                      bestVozilo = vozilo;
		                      bestRank = rank;
		                  }
		               }
		           }
		       }
		       if (bestVozilo != null && !bestVozilo.isTrenutno_vozi() && bestVozilo.getStatus().equals("A")) {
		           bestVozilo.ukrcajPakete(paket);
		           iterator.remove();
		       } else {
		           iterator.remove();
		       }
		   }
		}
}


