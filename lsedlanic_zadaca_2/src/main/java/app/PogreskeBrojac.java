package app;

import java.io.Serializable;

public class PogreskeBrojac implements Serializable{
	private static final long serialVersionUID = 5235288177029674801L;
	private static PogreskeBrojac instance = null;
	private int brojac;

	
    private PogreskeBrojac() {
    		brojac = 0;
    }

    public static PogreskeBrojac getInstance() {
        if (instance == null) {
            instance = new PogreskeBrojac();
        }
        return instance;
    }
    
	public static void setInstance(PogreskeBrojac instance) {
		PogreskeBrojac.instance = instance;
	}
    
    public void dodajPogresku(String opis, String sadrzaj) {
    	System.out.println("Pogreška " + brojac + ": \nRedak " + sadrzaj + "\nnije važeći jer " + opis);
    	brojac++;
    }
    
}
