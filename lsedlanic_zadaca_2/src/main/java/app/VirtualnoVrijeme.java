package app;

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class VirtualnoVrijeme implements Serializable{
	

	private static final long serialVersionUID = -2703718095521748531L;
	private static VirtualnoVrijeme instance = null;
    private  Clock virtualniSat = null;
	
    private VirtualnoVrijeme() {
    	
    }

    public static VirtualnoVrijeme getInstance() {
        if (instance == null) {
            instance = new VirtualnoVrijeme();
        }
        return instance;
    }
    
	public void setInstance(VirtualnoVrijeme instance) {
		VirtualnoVrijeme.instance = instance;
	}
	
    public void inicijalizirajVirtualniSat(String vs) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
		LocalDateTime dateTime = LocalDateTime.parse(vs, formatter);
		Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
		virtualniSat = Clock.fixed(instant, ZoneId.systemDefault());
    }

    public void nadodajVrijeme(int ms) {
        Instant sada = virtualniSat.instant();
        Instant kasnije = sada.plusSeconds(ms);
        virtualniSat = Clock.fixed(kasnije, ZoneId.systemDefault());
    }
    
    public Instant getVrijeme() {
        Instant sada = virtualniSat.instant();
        return sada;
    }
    
    public String getVrijemeDateTime() {
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
    	Instant sada = virtualniSat.instant();

    	ZonedDateTime zonedDateTime = sada.atZone(ZoneId.systemDefault());
    	LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();

    	String formatiraniDateTime = localDateTime.format(formatter);
        return formatiraniDateTime;
    }
    
    public Clock getSat() {
        return virtualniSat;
    }
    
}
