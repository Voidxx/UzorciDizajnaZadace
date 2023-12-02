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
import java.util.stream.Collectors;

import citaci.CitacTxt;
import citaci.CsvLoader;
import citaci.CsvLoaderFactory;
import objekti.Mjesto;
import objekti.Paket;
import objekti.Vozilo;
import objekti.VrstaPaketa;
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
        CsvLoader<Mjesto> mjestaLoader = (CsvLoader<Mjesto>) CsvLoaderFactory.getLoader("mjesta");
        List<Mjesto> mjesta = mjestaLoader.loadCsv(pm);
        Tvrtka.getInstance().setMjesta(mjesta);
        System.out.println(mjesta);

        VirtualnoVrijeme.getInstance();
        VirtualnoVrijeme.inicijalizirajVirtualniSat(vs);
        
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
            } else {
                System.out.println("Nevažeća komanda");
            }
        }
        scanner.close();
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
		                	provjeriTrebajuLiKrenutiVozila();
		                	provjeriPakete(preostaleSekundeNakonPunogSata);
		                    
		                } else {
		                	System.out.println("Trenutno vrijeme virtualnog sata: " + VirtualnoVrijeme.getVrijemeDateTime());  
		                	VirtualnoVrijeme.nadodajVrijeme(ms);
		                	provjeriHoceLiBitiDostavljenPaket(ms);
		                	provjeriDaLiJePotrebnoUkrcatiPakete(vrijemePrije, vrijemeNakon);
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



	private static void provjeriPakete(int sekundi) {
		LocalDateTime vrijemePrije;
		LocalDateTime vrijemeNakon;
		provjeriHoceLiBitiDostavljenPaket(sekundi);
		vrijemePrije = LocalDateTime.ofInstant(VirtualnoVrijeme.getVrijeme(), ZoneId.systemDefault());
		vrijemeNakon = vrijemePrije.plusSeconds(sekundi);
		System.out.println("Trenutno vrijeme virtualnog sata: " + VirtualnoVrijeme.getVrijemeDateTime());  
		VirtualnoVrijeme.nadodajVrijeme(sekundi);
		provjeriDaLiJePotrebnoUkrcatiPakete(vrijemePrije, vrijemeNakon);
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
	                	vozilo.azurirajDostavu(vi);
	            }
	        }
    	}
	}



	private static void provjeriTrebajuLiKrenutiVozila() {
		for(Vozilo vozilo : UredZaDostavu.getInstance().dohvatiListuVozila()) {
			if(!vozilo.isTrenutno_vozi()) {
				if((vozilo.getTrenutni_teret_tezina() >= vozilo.getKapacitet_kg() / 2) || (vozilo.getTrenutni_teret_volumen() >= vozilo.getKapacitet_m3() / 2)  && vozilo.getUkrcani_paketi() != null) {
					if(vozilo.getUkrcani_paketi().stream().anyMatch(paket -> paket.getUsluga_dostave().equals("H"))) {
					vozilo.setTrenutno_vozi(true);
					System.out.println("Vozilo: " + vozilo.getOpis() + " Kreće u dostavu!");
					vozilo.setDostavaSat(VirtualnoVrijeme.getSat());
					}
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



	private static void provjeriDaLiJePotrebnoUkrcatiPakete(LocalDateTime prije, LocalDateTime poslije) {	      
    	provjeriPrijemPaketa(prije, poslije);    	
    	ukrcajPakete();   	
    }



	private static void provjeriPrijemPaketa(LocalDateTime prije, LocalDateTime poslije) {
		for (Paket paket : UredZaPrijem.getInstance().dobaviListuOcekivanihPaketa()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
    		LocalDateTime dateTime = LocalDateTime.parse(paket.getVrijeme_prijema(), formatter);
    		if(dateTime.isBefore(poslije) && dateTime.isAfter(prije)) {
    			UredZaPrijem.getInstance().dodajPaketUSpremneZaDostavu(paket);
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

	    List<Vozilo> sortiranaVozila = UredZaDostavu.getInstance().dohvatiListuVozila().stream()
	            .sorted(Comparator.comparing(Vozilo::getRedoslijed))
	            .collect(Collectors.toList());
	    for (Vozilo vozilo : sortiranaVozila) {
	        if(vozilo.isTrenutno_vozi() == false) {
	            List<Paket> paketiZaDostavu = new ArrayList<>(UredZaPrijem.getInstance().dobaviListuPaketaZaDostavu());
	            for (Paket paket : paketiZaDostavu) {
	                double tezinaPaketa = paket.getTezina();
	                double volumenPaketa = paket.getVisina() * paket.getSirina() * paket.getDuzina();
	                if ((tezinaPaketa + vozilo.getTrenutni_teret_tezina() <= vozilo.getKapacitet_kg() && volumenPaketa + vozilo.getTrenutni_teret_volumen() <= vozilo.getKapacitet_m3())) {
	                	vozilo.setTrenutni_teret_tezina(vozilo.getTrenutni_teret_tezina() + tezinaPaketa);
	                	vozilo.setTrenutni_teret_volumen(vozilo.getTrenutni_teret_volumen() + volumenPaketa);
	                	vozilo.dodajPaketUVozilo(paket);
	                    UredZaPrijem.getInstance().dobaviListuPaketaZaDostavu().remove(paket);
	                    System.out.println("Dodan paket: " + paket.getOznaka() + " u vozilo: " + vozilo.getOpis() + " na vrijeme: " + VirtualnoVrijeme.getVrijemeDateTime() + " - " + "Trenutni teret: KG " + vozilo.getTrenutni_teret_tezina() + " Volumen " + vozilo.getTrenutni_teret_volumen());
	                    
	                }
	            }
	        }
	    }
	}
    
    
    
}


