package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import citaci.ConcreteVrstaPaketaCreator;
import citaci.ConcretePaketCreator;
import citaci.ConcreteVoziloCreator;
import citaci.CsvReader;

import java.text.NumberFormat;

import paket.Paket;
import paket.VrstaPaketa;
import tvrtka.Tvrtka;
import tvrtka.UredZaDostavu;
import tvrtka.UredZaPrijem;
import vozilo.Vozilo;



public class Main {
	static String regex = "^(?=.*(--vs [0-9]{2}\\.[0-9]{2}\\.[0-9]{4}\\. [0-9]{2}:[0-9]{2}:[0-9]{2}))(?=.*(--ms [0-9]+))(?=.*(--pr [0-9]{2}:[0-9]{2}))(?=.*(--kr [0-9]{2}:[0-9]{2}))(?=.*(--mt [0-9]+))(?=.*(--vi [0-9]+))(?=.*(--pp \\w+\\.csv))(?=.*(--pv \\w+\\.csv))(?=.*(--vp \\w+\\.csv)).*$";
	static String vs = null;
	static int ms = 0;
	static String pr = null;
	static String kr = null;
	static int mt = 0;
	static int vi = 0;
	static String pp = null;
	static String pv = null;
	static String vp = null;
	
    public static void main(String[] args) throws ParseException, IOException {
    	String input = String.join(" ", args);
    	if (!input.matches(regex)) {
    	    System.out.println("Invalid command line arguments");
    	    return;
    	}
    	String[] argumenti = input.split("--");
        odrediUlazneVarijable(argumenti);
    

        List<VrstaPaketa> vrstePaketa = new ConcreteVrstaPaketaCreator().readCsv(vp);
        List<Vozilo> vozila = new ConcreteVoziloCreator().readCsv(pv);
        List<Paket> paketi = new ConcretePaketCreator().readCsv(pp);
        
        Tvrtka tvrtka = Tvrtka.getInstance();
        UredZaPrijem uredZaPrijem = tvrtka.getUredZaPrijem();
        UredZaDostavu uredZaDostavu = tvrtka.getUredZaDostavu();
        
        uredZaDostavu.postaviVozila(vozila);
        uredZaPrijem.postaviVrstePaketa(vrstePaketa);
        uredZaPrijem.postaviOcekivanePakete(paketi);


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
                hendlajIPKomandu(uredZaPrijem, uredZaDostavu);
            } else if (unos.startsWith("VR")) {
                hendlajVRKomandu(uredZaDostavu, unos);        
            } else {
                System.out.println("Nevažeća komanda");
            }
        }
        scanner.close();
    }



	private static void hendlajVRKomandu(UredZaDostavu uredZaDostavu, String unos) {
		System.out.println("VR komanda dobivena");
		// Handle VR command
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
		                    VirtualnoVrijeme.nadodajVrijeme(ms);
		                    System.out.println("Trenutno vrijeme virtualnog sata: " + VirtualnoVrijeme.getVrijemeDateTime()); 
		                	System.out.println("Nije radno vrijeme... spavam.");
		                    Thread.sleep(1000);
		                } else {
		                
		                LocalDateTime vrijemePrije = LocalDateTime.ofInstant(prijasnjeVrijeme, ZoneId.systemDefault());
		                LocalDateTime vrijemeNakon = vrijemePrije.plusSeconds(ms);
		                
		                if (vrijemeNakon.getMinute() < vrijemePrije.getMinute()) {
		                	int sekundiDoPunogSata = 3600 - (vrijemePrije.getMinute() * 60);
		                	int preostaleSekundeNakonPunogSata = vrijemeNakon.getMinute() * 60;
		                	
		                	provjeriHoceLiBitiDostavljenPaket(uredZaDostavu, sekundiDoPunogSata);
		                	vrijemePrije = LocalDateTime.ofInstant(VirtualnoVrijeme.getVrijeme(), ZoneId.systemDefault());
		                	vrijemeNakon = vrijemePrije.plusSeconds(sekundiDoPunogSata);
		                	System.out.println("Trenutno vrijeme virtualnog sata: " + VirtualnoVrijeme.getVrijemeDateTime());  
		                	provjeriDaLiJePotrebnoUkrcatiPakete(vrijemePrije, vrijemeNakon);
		                	VirtualnoVrijeme.nadodajVrijeme(sekundiDoPunogSata);
		                	provjeriTrebajuLiKrenutiVozila(uredZaDostavu);
		                	
		                	  
		                	provjeriHoceLiBitiDostavljenPaket(uredZaDostavu, preostaleSekundeNakonPunogSata);
		                	vrijemePrije = LocalDateTime.ofInstant(VirtualnoVrijeme.getVrijeme(), ZoneId.systemDefault());
		                	vrijemeNakon = vrijemePrije.plusSeconds(preostaleSekundeNakonPunogSata);
		                	System.out.println("Trenutno vrijeme virtualnog sata: " + VirtualnoVrijeme.getVrijemeDateTime());  
		                	provjeriDaLiJePotrebnoUkrcatiPakete(vrijemePrije, vrijemeNakon);
		                    VirtualnoVrijeme.nadodajVrijeme(preostaleSekundeNakonPunogSata);
		                    
		                } else {
		                	provjeriHoceLiBitiDostavljenPaket(uredZaDostavu, ms);
		                	System.out.println("Trenutno vrijeme virtualnog sata: " + VirtualnoVrijeme.getVrijemeDateTime());  
		                	provjeriDaLiJePotrebnoUkrcatiPakete(vrijemePrije, vrijemeNakon);
		                	VirtualnoVrijeme.nadodajVrijeme(ms);
		                }
//		                
//		                if (vrijemeNakon.getMinute() < vrijemePrije.getMinute()) {
//		                	int sekundiDoPunogSata = 3600 - (vrijemePrije.getMinute() * 60);
//		                	provjeriDaLiJePotrebnoUkrcatiPakete(vrijemePrije, vrijemeNakon);
//		                	provjeriHoceLiBitiDostavljenPaket(uredZaDostavu, sekundiDoPunogSata);
//		                	
//		                	VirtualnoVrijeme.nadodajVrijeme(sekundiDoPunogSata);
//		                	provjeriTrebajuLiKrenutiVozila(uredZaDostavu);
//		                	
//		                    int preostaleSekundeNakonPunogSata = vrijemeNakon.getMinute() * 60;
//		                    provjeriHoceLiBitiDostavljenPaket(uredZaDostavu, preostaleSekundeNakonPunogSata);
//		                    VirtualnoVrijeme.nadodajVrijeme(preostaleSekundeNakonPunogSata);
//		                    
//		                } else {
//		                	VirtualnoVrijeme.nadodajVrijeme(ms);
//		                	provjeriHoceLiBitiDostavljenPaket(uredZaDostavu, ms);
//		                }
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



	private static void hendlajIPKomandu(UredZaPrijem uredZaPrijem, UredZaDostavu uredZaDostavu) {
		System.out.println("IP komanda dobivena");

		System.out.printf("%-20s %-15s %-15s %-20s %-20s %-15s %-15s%n", "Vrijeme prijema", "Vrsta paketa", "Vrsta usluge", "Status isporuke", "Vrijeme preuzimanja", "Iznos dostave", "Iznos pouzeća");

		for (Paket paket : uredZaDostavu.getDostavljeniPaketi()) {
			
		    System.out.printf("%-20s %-15s %-15s %-20s %-20s %-15s %-15s%n", paket.getVrijeme_prijema(), paket.getVrsta_paketa(), paket.getUsluga_dostave(), "Dostavljeno", paket.getVrijeme_preuzimanja(), paket.getIzracunati_iznos_dostave(), paket.getIznos_pouzeca());
		}
		Set<Paket> dostavljeniPaketiSet = new HashSet<>(uredZaDostavu.getDostavljeniPaketi());
		List<Paket> ocekivaniPaketi = uredZaPrijem.dobaviListuOcekivanihPaketa();
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



	private static void odrediUlazneVarijable(String[] arguments) {
		for (String argument : arguments) {
            if (argument.trim().isEmpty()) {
                continue;
            }
            String[] dijelovi = argument.trim().split(" ", 2);
            if (dijelovi.length == 2) {
                String key = dijelovi[0].trim();
                String vrijednost = dijelovi[1].trim();
                switch (key) {
    	            case "vs":
    	                vs = vrijednost.substring(0, vrijednost.length());
    	                Tvrtka.setVs(vs);
    	                break;
    	            case "ms":
    	                ms = Integer.parseInt(vrijednost);
    	                Tvrtka.setMs(ms);
    	                break;
    	            case "pr":
    	                pr = vrijednost.substring(0, vrijednost.length());
    	                Tvrtka.setPr(pr);
    	                break;
    	            case "kr":
    	                kr = vrijednost.substring(0, vrijednost.length());
    	                Tvrtka.setKr(kr);
    	                break;
    	            case "mt":
    	                mt = Integer.parseInt(vrijednost);
    	                Tvrtka.setMt(mt);
    	                break;
    	            case "vi":
    	                vi = Integer.parseInt(vrijednost);
    	                Tvrtka.setVi(vi);
    	                break;
    	            case "pp":
    	                pp = vrijednost.substring(0, vrijednost.length());
    	                Tvrtka.setPp(pp);
    	                break;
    	            case "pv":
    	                pv = vrijednost.substring(0, vrijednost.length());
    	                Tvrtka.setPv(pv);
    	                break;
    	            case "vp":
    	                vp = vrijednost.substring(0, vrijednost.length());
    	                Tvrtka.setVp(vp);
    	                break;
    	        }
    	    }
    	}
	}
    

    
    private static void provjeriHoceLiBitiDostavljenPaket(UredZaDostavu uredZaDostavu, int sekunde) {
    	for(Vozilo vozilo : uredZaDostavu.dohvatiListuVozila()) {
    		if(vozilo.isTrenutno_vozi() == true) {

	    			Instant prijasnjeVrijeme = vozilo.getVrijeme();
	    			
	                Instant vrijemeDostave = prijasnjeVrijeme.plusSeconds(vi*60);
	                Instant vrijemeNakon = prijasnjeVrijeme.plusSeconds(sekunde);
	                if(vrijemeDostave.isBefore(vrijemeNakon) || vrijemeDostave.equals(vrijemeNakon)) {
	                	vozilo.azurirajDostavu(vi);
	                }
	            }
    	}
	}



	private static void provjeriTrebajuLiKrenutiVozila(UredZaDostavu uredZaDostavu) {
		for(Vozilo vozilo : uredZaDostavu.dohvatiListuVozila()) {
			if((vozilo.getTrenutni_teret_tezina() >= vozilo.getKapacitet_kg() / 2) || (vozilo.getTrenutni_teret_volumen() >= vozilo.getKapacitet_m3() / 2) || vozilo.getUkrcani_paketi().stream().anyMatch(paket -> paket.getUsluga_dostave().equals("H") && !vozilo.isTrenutno_vozi())) {
				vozilo.setTrenutno_vozi(true);
				System.out.println("Vozilo: " + vozilo.getOpis() + " Kreće u dostavu!");
				vozilo.setDostavaSat(VirtualnoVrijeme.getSat());
			}
		}
	}



	private static void provjeriDaLiJePotrebnoUkrcatiPakete(LocalDateTime prije, LocalDateTime poslije) {	
    	
        Tvrtka tvrtka = Tvrtka.getInstance();
        UredZaPrijem uredZaPrijem = tvrtka.getUredZaPrijem();
        UredZaDostavu uredZaDostavu = tvrtka.getUredZaDostavu();
        
    	provjeriPrijemPaketa(prije, poslije, uredZaPrijem);    	
    	ukrcajPakete(uredZaPrijem, uredZaDostavu);   	
    }



	private static void provjeriPrijemPaketa(LocalDateTime prije, LocalDateTime poslije, UredZaPrijem uredZaPrijem) {
		for (Paket paket : uredZaPrijem.dobaviListuOcekivanihPaketa()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
    		LocalDateTime dateTime = LocalDateTime.parse(paket.getVrijeme_prijema(), formatter);
    		if(dateTime.isBefore(poslije) && dateTime.isAfter(prije)) {
    			uredZaPrijem.dodajPaketUSpremneZaDostavu(paket);
    		}
		}
	}



	private static void ukrcajPakete(UredZaPrijem uredZaPrijem, UredZaDostavu uredZaDostavu) {
	    Collections.sort(uredZaPrijem.dobaviListuPaketaZaDostavu(), new Comparator<Paket>() {
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

	    List<Vozilo> sortiranaVozila = uredZaDostavu.dohvatiListuVozila().stream()
	            .sorted(Comparator.comparing(Vozilo::getRedoslijed))
	            .collect(Collectors.toList());
	    for (Vozilo vozilo : sortiranaVozila) {
	        if(vozilo.isTrenutno_vozi() == false) {
	            List<Paket> paketiZaDostavu = new ArrayList<>(uredZaPrijem.dobaviListuPaketaZaDostavu());
	            for (Paket paket : paketiZaDostavu) {
	                double tezinaPaketa = paket.getTezina();
	                double volumenPaketa = paket.getVisina() * paket.getSirina() * paket.getDuzina();
	                if ((tezinaPaketa + vozilo.getTrenutni_teret_tezina() <= vozilo.getKapacitet_kg() && volumenPaketa + vozilo.getKapacitet_m3() <= vozilo.getKapacitet_m3())) {
	                    vozilo.dodajPaketUVozilo(paket);
	                    uredZaPrijem.dobaviListuPaketaZaDostavu().remove(paket);
	                    System.out.println("Dodan paket: " + paket.getOznaka() + " u vozilo: " + vozilo.getOpis() + " na vrijeme: " + paket.getVrijeme_prijema());
	                }
	            }
	        }
	    }
	}
    
    
    
}


