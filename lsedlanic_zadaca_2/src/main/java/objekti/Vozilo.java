package objekti;

import java.io.Serializable;
import java.text.ParseException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.PogreskeBrojac;
import citaci.CsvObjekt;
import stanjaVozila.AktivnoVozilo;
import stanjaVozila.NeaktivnoVozilo;
import stanjaVozila.NeispravnoVozilo;
import stanjaVozila.Stanje;
import tvrtka.Tvrtka;
import visitori.Host;
import visitori.Visitor;
import voznja.Voznja;

public class Vozilo implements CsvObjekt, Serializable, Host {
    private static final long serialVersionUID = 7209462325314152693L;
	private String registracija;
    private String opis;
    private double kapacitet_kg;
    private double kapacitet_m3;
    private double trenutni_teret_tezina;
    private double trenutni_teret_volumen;
    private List<Paket> ukrcani_paketi;
    private int redoslijed;
    private boolean trenutno_vozi;
    private Clock dostavaSat;
    private double prikupljeniNovac;
    private int prosjecnaBrzina;
    private String status;
    private List<Integer> podrucjaPoRangu;
    private Stanje state;
    private List<Voznja> obavljeneVoznje = new ArrayList<Voznja>();
    private double trenutniLon;
    private double trenutniLat;
    private int brojIsporucenihPaketa;
    private double ukupnoKmPrijedeno;
    private AktivnoVozilo aktivnoStanje;
    private NeaktivnoVozilo neaktivnoStanje;
    private NeispravnoVozilo neispravnoStanje;

	
	
	public Vozilo(String registracija, String opis, double kapacitet_kg, double kapacitet_m3,
			double trenutni_teret_tezina, double trenutni_teret_volumen, List<Paket> ukrcani_paketi, int redoslijed,
			boolean trenutno_vozi, Clock dostavaSat, double prikupljeniNovac, int prosjecnaBrzina, String status,
			List<Integer> podrucjaPoRangu) {
		super();
		this.registracija = registracija;
		this.opis = opis;
		this.kapacitet_kg = kapacitet_kg;
		this.kapacitet_m3 = kapacitet_m3;
		this.trenutni_teret_tezina = trenutni_teret_tezina;
		this.trenutni_teret_volumen = trenutni_teret_volumen;
		this.ukrcani_paketi = ukrcani_paketi;
		this.redoslijed = redoslijed;
		this.trenutno_vozi = trenutno_vozi;
		this.dostavaSat = dostavaSat;
		this.prikupljeniNovac = prikupljeniNovac;
		this.prosjecnaBrzina = prosjecnaBrzina;
		this.status = status;
		this.podrucjaPoRangu = podrucjaPoRangu;
		this.ukupnoKmPrijedeno = 0;
	}

	
	
	
	public AktivnoVozilo getAktivnoStanje() {
		return aktivnoStanje;
	}




	public void setAktivnoStanje(AktivnoVozilo aktivnoStanje) {
		this.aktivnoStanje = aktivnoStanje;
	}




	public NeaktivnoVozilo getNeaktivnoStanje() {
		return neaktivnoStanje;
	}




	public void setNeaktivnoStanje(NeaktivnoVozilo neaktivnoStanje) {
		this.neaktivnoStanje = neaktivnoStanje;
	}




	public NeispravnoVozilo getNeispravnoStanje() {
		return neispravnoStanje;
	}




	public void setNeispravnoStanje(NeispravnoVozilo neispravnoStanje) {
		this.neispravnoStanje = neispravnoStanje;
	}




	public double getUkupnoKmPrijedeno() {
		return ukupnoKmPrijedeno;
	}




	public void setUkupnoKmPrijedeno(double ukupnoKmPrijedeno) {
		this.ukupnoKmPrijedeno = ukupnoKmPrijedeno;
	}

	public void dodajKmNaUkupno(double km) {
		this.ukupnoKmPrijedeno = this.ukupnoKmPrijedeno + km;
	}



	public int getBrojIsporucenihPaketa() {
		return brojIsporucenihPaketa;
	}

	@Override
	public void accept(Visitor visitor) {
		   visitor.visitVozilo(this);
		}


	public void setBrojIsporucenihPaketa(int brojIsporucenihPaketa) {
		this.brojIsporucenihPaketa = brojIsporucenihPaketa;
	}




	public void dodajPaketUIsporucene() {
		brojIsporucenihPaketa++;
	}

	
    public double getTrenutniLon() {
		return trenutniLon;
	}




	public void setTrenutniLon(double trenutniLon) {
		this.trenutniLon = trenutniLon;
	}




	public double getTrenutniLat() {
		return trenutniLat;
	}




	public void setTrenutniLat(double trenutniLat) {
		this.trenutniLat = trenutniLat;
	}




	public List<Voznja> getObavljeneVoznje() {
		return obavljeneVoznje;
	}


	public void setObavljeneVoznje(List<Voznja> obavljeneVoznje) {
		this.obavljeneVoznje = obavljeneVoznje;
	}
	
	public void dodajObavljenuVoznju(Voznja voznja) {
		this.obavljeneVoznje.add(voznja);
	}


	public void setState(Stanje state) {
        this.state = state;
    }
	
	public Stanje getState() {
		return this.state;
	}

    public void ukrcajPakete(Paket paket) {
        state.ukrcajPakete(this, paket);
    }

    public void dostaviPakete() {
        state.dostaviPakete(this, Tvrtka.getInstance().getVi());
    }

    public boolean vratiSeUUred() {
       return state.vratiSeUUred(this);
    }
	public int getProsjecnaBrzina() {
		return prosjecnaBrzina;
	}


	public void setProsjecnaBrzina(int prosjecnaBrzina) {
		this.prosjecnaBrzina = prosjecnaBrzina;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}




	public List<Integer> getPodrucjaPoRangu() {
		return podrucjaPoRangu;
	}



	public void setPodrucjaPoRangu(List<Integer> podrucjaPoRangu) {
		this.podrucjaPoRangu = podrucjaPoRangu;
	}

	public void dodajUPodrucjaPoRangu(Integer podrucje) {
		this.podrucjaPoRangu.add(podrucje);
	}


	public String getRegistracija() {
		return registracija;
	}
	public void setRegistracija(String registracija) {
		this.registracija = registracija;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	public double getKapacitet_kg() {
		return kapacitet_kg;
	}
	public void setKapacitet_kg(double d) {
		this.kapacitet_kg = d;
	}
	public double getKapacitet_m3() {
		return kapacitet_m3;
	}
	public void setKapacitet_m3(double d) {
		this.kapacitet_m3 = d;
	}
	public int getRedoslijed() {
		return redoslijed;
	}
	public void setRedoslijed(int redoslijed) {
		this.redoslijed = redoslijed;
	}

	@Override
	public void process(String linija) throws ParseException {
		//dodat validacije
		 try {
		     validate(linija);
		 } catch (ParseException e) {
		     PogreskeBrojac.getInstance().dodajPogresku(e.getMessage(), linija);
		     return;
		 }
		 String[] vrijednosti = linija.split(";");
         this.setRegistracija(vrijednosti[0]);
         this.setOpis(vrijednosti[1]);
         this.setKapacitet_kg(Double.parseDouble(vrijednosti[2].replace(",", ".")));
         this.setKapacitet_m3(Double.parseDouble(vrijednosti[3].replace(",", ".")));
         this.setRedoslijed(Integer.parseInt(vrijednosti[4]));
         this.setProsjecnaBrzina(Integer.parseInt(vrijednosti[5]));
         String[] podrucjaString = vrijednosti[6].split(",");
         for (String s: podrucjaString) {
             this.podrucjaPoRangu.add(Integer.parseInt(s));
         }
         this.setStatus(vrijednosti[7]);
         if(this.getStatus() == "A")
        	 this.setState(new AktivnoVozilo());
         else if(this.getStatus() == "NA")
        	 this.setState(new NeaktivnoVozilo());
         else if(this.getStatus() == "NI")
        	 this.setState(new NeispravnoVozilo());
         
         
         
		
	}
	
	public boolean imaVrijednosti() {
		   return registracija != null;
		}
	
	
	private void validate(String linija) throws ParseException {
		   String[] vrijednosti = linija.split(";");

		   if (vrijednosti.length != 8) {
		       throw new ParseException("Redak sadrži " + vrijednosti.length + " vrijednosti, ali se očekuje 8 vrijednosti.", 0);
		   }

		   for (int i = 2; i <= 3; i++) {
		       try {
		           Double.parseDouble(vrijednosti[i].replace(",", "."));
		       } catch (NumberFormatException e) {
		           throw new ParseException("Nevažeča vrijednost broja na indeksu:  " + i + ": " + e.getMessage(), 0);
		       }
		   }

		   for (int i = 0; i < 2; i++) {
		       if (vrijednosti[i].trim().isEmpty()) {
		           throw new ParseException("Nepostojeće polje na indeksu:  " + i, 0);
		       }
		   }

		   try {
		       Integer.parseInt(vrijednosti[4]);
		   } catch (NumberFormatException e) {
		       throw new ParseException("Nevažeći integer na indeksu 4: " + e.getMessage(), 0);
		   }
		}

	public double getTrenutni_teret_volumen() {
		return trenutni_teret_volumen;
	}

	public void setTrenutni_teret_volumen(double trenutni_teret_volumen) {
		this.trenutni_teret_volumen = trenutni_teret_volumen;
	}

	public double getTrenutni_teret_tezina() {
		return trenutni_teret_tezina;
	}

	public void setTrenutni_teret_tezina(double trenutni_teret_tezina) {
		this.trenutni_teret_tezina = trenutni_teret_tezina;
	}

	public List<Paket> getUkrcani_paketi() {
		return ukrcani_paketi;
	}

	public void setUkrcani_paketi(List<Paket> ukrcani_paketi) {
		this.ukrcani_paketi = ukrcani_paketi;
	}
	
	public void dodajPaketUVozilo(Paket paket) {
		this.ukrcani_paketi.add(paket);
	}
	
	public void makniPaketIzVozila(Paket paket) {
		this.ukrcani_paketi.remove(paket);
	}

	public boolean isTrenutno_vozi() {
		return trenutno_vozi;
	}

	public void setTrenutno_vozi(boolean trenutno_vozi) {
		this.trenutno_vozi = trenutno_vozi;
	}


	public Clock getDostavaSat() {
		return dostavaSat;
	}


	public void setDostavaSat(Clock dostavaSat) {
		this.dostavaSat = dostavaSat;
		
	}
	
    public Instant getVrijeme() {
        Instant sada = this.dostavaSat.instant();
        return sada;
    }
	
    public String getVrijemeDostaveDateTime() {
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", Locale.ENGLISH);
    	Instant sada = dostavaSat.instant();

    	ZonedDateTime zonedDateTime = sada.atZone(ZoneId.systemDefault());
    	LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();

    	String formatiraniDateTime = localDateTime.format(formatter);
        return formatiraniDateTime;
    }
    

	public double getPrikupljeniNovac() {
		return prikupljeniNovac;
	}

	public void setPrikupljeniNovac(double prikupljeniNovac) {
		this.prikupljeniNovac = prikupljeniNovac;
	}
	
	public void dodajNovac(double novac) {
		this.prikupljeniNovac = this.prikupljeniNovac + novac;
	}



}
