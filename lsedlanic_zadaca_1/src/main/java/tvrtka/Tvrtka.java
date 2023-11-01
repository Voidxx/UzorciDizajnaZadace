package tvrtka;

public class Tvrtka {
    private static Tvrtka instance = null;
    private UredZaPrijem uredZaPrijem;
    private UredZaDostavu uredZaDostavu;
	static String vs = null;
	static int ms = 0;
	static String pr = null;
	static String kr = null;
	static int mt = 0;
	static int vi = 0;
	static String pp = null;
	static String pv = null;
	static String vp = null;
	
	

    public static String getVs() {
		return vs;
	}

	public static void setVs(String vs) {
		Tvrtka.vs = vs;
	}

	public static int getMs() {
		return ms;
	}

	public static void setMs(int ms) {
		Tvrtka.ms = ms;
	}

	public static String getPr() {
		return pr;
	}

	public static void setPr(String pr) {
		Tvrtka.pr = pr;
	}

	public static String getKr() {
		return kr;
	}

	public static void setKr(String kr) {
		Tvrtka.kr = kr;
	}

	public static int getMt() {
		return mt;
	}

	public static void setMt(int mt) {
		Tvrtka.mt = mt;
	}

	public static int getVi() {
		return vi;
	}

	public static void setVi(int vi) {
		Tvrtka.vi = vi;
	}

	public static String getPp() {
		return pp;
	}

	public static void setPp(String pp) {
		Tvrtka.pp = pp;
	}

	public static String getPv() {
		return pv;
	}

	public static void setPv(String pv) {
		Tvrtka.pv = pv;
	}

	public static String getVp() {
		return vp;
	}

	public static void setVp(String vp) {
		Tvrtka.vp = vp;
	}

	private Tvrtka() {
        uredZaPrijem = UredZaPrijem.getInstance();
        uredZaDostavu = UredZaDostavu.getInstance();
    }

    public static Tvrtka getInstance() {
        if (instance == null) {
            instance = new Tvrtka();
        }
        return instance;
    }

    public UredZaPrijem getUredZaPrijem() {
        return uredZaPrijem;
    }

    public UredZaDostavu getUredZaDostavu() {
        return uredZaDostavu;
    }
}