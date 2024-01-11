package app;

import java.io.File;
import java.util.Properties;

public class ProvjeraParametara {
    private final ProvjeraArgumenata prvaProvjera = new ProvjeraArgumenata();
    public void provjeriParametre(Properties parametri) {
        prvaProvjera.provjeriArgumente(parametri.stringPropertyNames().toArray(new String[0]));
        validateParameters(parametri);
    }
    private void validateParameters(Properties parametri) {


    	   String gps = parametri.getProperty("gps");
    	   if (gps != null) {
    	       String[] parts = gps.split(", ");
    	       if (parts.length == 2) {
    	           try {
    	               Double.parseDouble(parts[0].trim());
    	               Double.parseDouble(parts[1].trim());
    	           } catch (NumberFormatException e) {
    	               System.out.println("Invalid GPS coordinate for parameter 'gps': " + gps);
    	           }
    	       } else {
    	           System.out.println("Invalid GPS coordinate for parameter 'gps': " + gps);
    	       }
    	   }

    	   String isporuka = parametri.getProperty("isporuka");
    	   if (isporuka != null) {
    	       try {
    	           Integer.parseInt(isporuka);
    	       } catch (NumberFormatException e) {
    	           System.out.println("Invalid integer for parameter 'isporuka': " + isporuka);
    	       }
    	   }
    	   
           String po = parametri.getProperty("po");
           if (po != null) {
               File file = new File(po);
               if (!file.exists()) {
                   System.out.println("File not found for parameter 'po': " + po);
               }
           }
           String pm = parametri.getProperty("pm");
           if (pm != null) {
               File file = new File(pm);
               if (!file.exists()) {
                   System.out.println("File not found for parameter 'po': " + po);
               }
           }
           String pp = parametri.getProperty("pp");
           if (pp != null) {
               File file = new File(pp);
               if (!file.exists()) {
                   System.out.println("File not found for parameter 'po': " + po);
               }
           }
           String pu = parametri.getProperty("pu");
           if (pu != null) {
               File file = new File(pu);
               if (!file.exists()) {
                   System.out.println("File not found for parameter 'po': " + po);
               }
           }
    	}
}