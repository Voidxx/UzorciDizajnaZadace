package app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import tvrtka.Tvrtka;
import tvrtka.UredZaDostavu;
import tvrtka.UredZaPrijem;

public class SystemState implements Serializable, SystemStateInterface {
	   private static final long serialVersionUID = -3264959896283747800L;
	private UredZaDostavu uredZaDostavu;
	   private UredZaPrijem uredZaPrijem;
	   private Tvrtka tvrtka;
	   private PogreskeBrojac pogreskeBrojac;
	   private VirtualnoVrijeme virtualnoVrijeme;

	   public SystemState(UredZaDostavu uredZaDostavu, UredZaPrijem uredZaPrijem, Tvrtka tvrtka, PogreskeBrojac pogreskeBrojac, VirtualnoVrijeme virtualnoVrijeme) {
	       this.uredZaDostavu = uredZaDostavu;
	       this.uredZaPrijem = uredZaPrijem;
	       this.tvrtka = tvrtka;
	       this.pogreskeBrojac = pogreskeBrojac;
	       this.virtualnoVrijeme = virtualnoVrijeme;
	   }

	   @Override
	   public void saveState(String filename) {
	       try {
	           FileOutputStream fileOut = new FileOutputStream(filename);
	           ObjectOutputStream out = new ObjectOutputStream(fileOut);
	           out.writeObject(this);
	           out.close();
	           fileOut.close();
	           System.out.println("System state saved to " + filename);
	       } catch (IOException i) {
	           i.printStackTrace();
	       }
	   }

	   @Override
	   public SystemState loadState(String filename) {
	       try {
	           FileInputStream fileIn = new FileInputStream(filename);
	           ObjectInputStream in = new ObjectInputStream(fileIn);
	           SystemState systemState = (SystemState) in.readObject();
	           in.close();
	           fileIn.close();
	           System.out.println("System state loaded from " + filename);
	           return systemState;
	       } catch (IOException i) {
	           i.printStackTrace();
	       } catch (ClassNotFoundException c) {
	           System.out.println("SystemState class not found");
	           c.printStackTrace();
	       }
	       return null;
	   }

	public UredZaDostavu getUredZaDostavu() {
		return uredZaDostavu;
	}

	public void setUredZaDostavu(UredZaDostavu uredZaDostavu) {
		this.uredZaDostavu = uredZaDostavu;
	}

	public UredZaPrijem getUredZaPrijem() {
		return uredZaPrijem;
	}

	public void setUredZaPrijem(UredZaPrijem uredZaPrijem) {
		this.uredZaPrijem = uredZaPrijem;
	}

	public Tvrtka getTvrtka() {
		return tvrtka;
	}

	public void setTvrtka(Tvrtka tvrtka) {
		this.tvrtka = tvrtka;
	}

	public PogreskeBrojac getPogreskeBrojac() {
		return pogreskeBrojac;
	}

	public void setPogreskeBrojac(PogreskeBrojac pogreskeBrojac) {
		this.pogreskeBrojac = pogreskeBrojac;
	}

	public VirtualnoVrijeme getVirtualnoVrijeme() {
		return virtualnoVrijeme;
	}

	public void setVirtualnoVrijeme(VirtualnoVrijeme virtualnoVrijeme) {
		this.virtualnoVrijeme = virtualnoVrijeme;
	}
	   
	   
	   
	   
	}