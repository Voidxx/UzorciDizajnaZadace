package app;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import objekti.Paket;
import tvrtka.Tvrtka;
import tvrtka.UredZaDostavu;
import tvrtka.UredZaPrijem;

public class IPHandler implements CommandHandler{
	 private CommandHandler nextHandler;

	@Override
	public void handleCommand(String command) {
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
			LocalDateTime dateTimePocetka = LocalDateTime.parse(Tvrtka.getInstance().getVs(), formatter);
			Instant instantPocetka = dateTimePocetka.atZone(ZoneId.systemDefault()).toInstant();
			if((instant.isBefore(virtualnoVrijeme.getVrijeme()) && instant.isAfter(instantPocetka)) || instant.isBefore(virtualnoVrijeme.getVrijeme()))
				System.out.printf("%-20s %-20s %-15s %-15s %-20s %-20s %-15s %-15s%n", paket.getOznaka(), paket.getVrijeme_prijema(), paket.getVrsta_paketa(), paket.getUsluga_dostave(), "U preuzimanju", null,  paket.getIzracunati_iznos_dostave(), paket.getIznos_pouzeca());
		}
	}

	@Override
	public void setNext(CommandHandler nextHandler) {
		this.nextHandler = nextHandler;
		
	}

	@Override
	public void provjeriKomandu(String command) {
		if (Pattern.matches("^IP$", command)) {
			this.handleCommand(command);
		}
		else {
			nextHandler.provjeriKomandu(command);
		}
	}

}
