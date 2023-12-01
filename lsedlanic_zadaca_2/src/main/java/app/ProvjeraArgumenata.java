package app;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProvjeraArgumenata {
    private static final List<String> validniArgumenti = Arrays.asList("vs", "ms", "pr", "kr", "mt", "vi", "pp", "pv", "vp");
    private final Map<String, Class<?>> tipoviVrijednosti = new HashMap<>();
    
    public ProvjeraArgumenata() {
        tipoviVrijednosti.put("vs", String.class);
        tipoviVrijednosti.put("mt", Integer.class);
        tipoviVrijednosti.put("vi", Integer.class);
        tipoviVrijednosti.put("pr", String.class);
        tipoviVrijednosti.put("kr", String.class);
        tipoviVrijednosti.put("ms", Integer.class);
        tipoviVrijednosti.put("pp", String.class);
        tipoviVrijednosti.put("pv", String.class);
        tipoviVrijednosti.put("vp", String.class);
    }
    
    public void provjeriArgumente(String[] argumenti) {
        for (String argument : argumenti) {
            if (argument.trim().isEmpty()) {
                continue;
            }
            String[] dijelovi = argument.trim().split(" ", 2);
            if (dijelovi.length == 2) {
                String kljuc = dijelovi[0].trim();
                String vrijednost = dijelovi[1].trim();
                if (!validniArgumenti.contains(kljuc)) {
                   System.out.println("Nevažeći argument: " + kljuc);
                   return;
                }
                Class<?> tip = tipoviVrijednosti.get(kljuc);
                if (tip != null) {
                   try {
                       tip.getDeclaredMethod("valueOf", String.class).invoke(null, vrijednost);
                   } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                       System.out.println("Invalid value for argument '" + kljuc + "': " + vrijednost);
                   }
                }
                // continue with the next argument
            }
        }
    }
}