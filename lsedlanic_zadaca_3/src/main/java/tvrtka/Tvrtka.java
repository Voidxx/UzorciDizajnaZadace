package tvrtka;

import java.util.ArrayList;
import java.util.List;

import objekti.Mjesto;
import objekti.Osoba;
import objekti.Podrucje;
import objekti.Ulica;

public class Tvrtka{
	private static Tvrtka instance = null;
	String vs = null;
	int ms = 0;
	String pr = null;
	String kr = null;
	int mt = 0;
	int vi = 0;
	String pv = null;
	String vp = null;
	String pp = null;
	String pu = null;
	String pm = null;
	String po = null;
	String pmu = null;
	String gps = null;
	int isporuka = 0;
	private List<Ulica> ulice = new ArrayList<Ulica>();
	private List<Osoba> osobe = new ArrayList<Osoba>();
	private List<Podrucje> podrucja = new ArrayList<Podrucje>();
	private List<Mjesto> mjesta = new ArrayList<Mjesto>();
	boolean isProvjeriStatusLageraCalled = false;
	
	

    public boolean isProvjeriStatusLageraCalled() {
		return isProvjeriStatusLageraCalled;
	}

	public void setProvjeriStatusLageraCalled(boolean isProvjeriStatusLageraCalled) {
		this.isProvjeriStatusLageraCalled = isProvjeriStatusLageraCalled;
	}

	public String getPmu() {
		return pmu;
	}

	public void setPmu(String pmu) {
		this.pmu = pmu;
	}

	public List<Ulica> getUlice() {
		return ulice;
	}

	public void setUlice(List<Ulica> ulice) {
		this.ulice = ulice;
	}

	public List<Osoba> getOsobe() {
		return this.osobe;
	}

	public void setOsobe(List<Osoba> osobe) {
		this.osobe = osobe;
	}

	public List<Podrucje> getPodrucja() {
		return podrucja;
	}

	public void setPodrucja(List<Podrucje> podrucja) {
		this.podrucja = podrucja;
	}

	public List<Mjesto> getMjesta() {
		return mjesta;
	}

	public void setMjesta(List<Mjesto> mjesta) {
		this.mjesta = mjesta;
	}

	public String getPu() {
		return pu;
	}

	public void setPu(String pu) {
		this.pu = pu;
	}

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public String getPo() {
		return po;
	}

	public void setPo(String po) {
		this.po = po;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public int getIsporuka() {
		return isporuka;
	}

	public void setIsporuka(int isporuka) {
		this.isporuka = isporuka;
	}

	public static void setInstance(Tvrtka instance) {
		Tvrtka.instance = instance;
	}

	public String getVs() {
		return vs;
	}

	public void setVs(String vs) {
		this.vs = vs;
	}

	public int getMs() {
		return ms;
	}

	public void setMs(int ms) {
		this.ms = ms;
	}

	public String getPr() {
		return pr;
	}

	public void setPr(String pr) {
		this.pr = pr;
	}

	public String getKr() {
		return kr;
	}

	public void setKr(String kr) {
		this.kr = kr;
	}

	public int getMt() {
		return mt;
	}

	public void setMt(int mt) {
		this.mt = mt;
	}

	public int getVi() {
		return vi;
	}

	public void setVi(int vi) {
		this.vi = vi;
	}

	public String getPp() {
		return pp;
	}

	public void setPp(String pp) {
		this.pp = pp;
	}

	public String getPv() {
		return pv;
	}

	public void setPv(String pv) {
		this.pv = pv;
	}

	public String getVp() {
		return vp;
	}

	public void setVp(String vp) {
		this.vp = vp;
	}

	private Tvrtka() {

    }

    public static Tvrtka getInstance() {
        if (instance == null) {
            instance = new Tvrtka();
        }
        return instance;
    }

}