package app;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.regex.Pattern;

import objekti.Paket;
import objekti.PaketIterator;
import objekti.PaketIteratorImpl;
import objekti.Vozilo;
import stanjaVozila.AktivnoVozilo;
import tvrtka.Tvrtka;
import tvrtka.UredZaDostavu;
import tvrtka.UredZaPrijem;
import voznja.VoznjaBuilder;

public class VRHandler implements CommandHandler{
	 private CommandHandler nextHandler;
	 
	@Override
	public void handleCommand(String command) {
		System.out.println("VR komanda dobivena");
		String[] parts = command.split(" ");
		VirtualnoVrijeme virtualnoVrijeme = VirtualnoVrijeme.getInstance();
		if (parts.length > 1) {
		    try {
		        int hours = Integer.parseInt(parts[1]);
		        for (int i = 0; i < (hours*60*60)/Tvrtka.getInstance().getMs(); i++) {
		            try {
		            	Thread.sleep(1000);
		            	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
		            	LocalTime otvorenje = LocalTime.parse(Tvrtka.getInstance().getPr(), formatter);
		            	LocalTime zatvaranje = LocalTime.parse(Tvrtka.getInstance().getKr(), formatter);
		            	
		                Instant prijasnjeVrijeme = virtualnoVrijeme.getVrijeme();
		                LocalDateTime localDateTime = LocalDateTime.ofInstant(prijasnjeVrijeme, ZoneId.systemDefault());
		                LocalTime lokalnoVirtualnoVrijeme = localDateTime.toLocalTime();
		                
		                if (lokalnoVirtualnoVrijeme.isBefore(otvorenje) || lokalnoVirtualnoVrijeme.isAfter(zatvaranje)) { 
		                	System.out.println("Nije radno vrijeme...");
		                	virtualnoVrijeme.nadodajVrijeme(Tvrtka.getInstance().getMs());
		                	System.out.println("Trenutno vrijeme virtualnog sata: " + virtualnoVrijeme.getVrijemeDateTime());  
		                } else {
		                provjeriStatusLagera();
		                LocalDateTime vrijemePrije = LocalDateTime.ofInstant(prijasnjeVrijeme, ZoneId.systemDefault());
		                LocalDateTime vrijemeNakon = vrijemePrije.plusSeconds(Tvrtka.getInstance().getMs());
		                
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
		                	virtualnoVrijeme.nadodajVrijeme(Tvrtka.getInstance().getMs());
		                	provjeriHoceLiBitiDostavljenPaket(Tvrtka.getInstance().getMs());
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
	
   private static void provjeriHoceLiBitiDostavljenPaket(int sekunde) {
    	for(Vozilo vozilo : UredZaDostavu.getInstance().dohvatiListuVozila()) {
    		if(vozilo.isTrenutno_vozi() == true && vozilo.getStatus().equals("A")) {
	                	vozilo.dostaviPakete();
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

	
	private static void provjeriStatusLagera() {
		   if (!Tvrtka.getInstance().isProvjeriStatusLageraCalled()) {
		       for(Paket paket : UredZaPrijem.getInstance().dobaviListuOcekivanihPaketa()) {
		           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
		           LocalDateTime dateTime = LocalDateTime.parse(paket.getVrijeme_prijema(), formatter);
		           LocalDateTime pocetakRada = LocalDateTime.parse(Tvrtka.getInstance().getVs(), formatter);
		           if(dateTime.isBefore(pocetakRada)) {
		               UredZaPrijem.getInstance().dodajPaketUSpremneZaDostavu(paket);
		               paket.getOvajPaket().notifyObservers("zaprimljen");
		           }
		       }
		       Tvrtka.getInstance().setProvjeriStatusLageraCalled(true);
		   }
	}

	@Override
	public void setNext(CommandHandler nextHandler) {
		this.nextHandler = nextHandler;
		
	}

	@Override
	public void provjeriKomandu(String command) {
		if (Pattern.matches("^VR\\s\\d+$", command)) {
			this.handleCommand(command);
		}
		else {
			nextHandler.provjeriKomandu(command);
		}
	}

}
