package app;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
      tipoviVrijednosti.put("pp", File.class);
      tipoviVrijednosti.put("pv", File.class);
      tipoviVrijednosti.put("vp", File.class);
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
                    System.out.println("Nevažeća vrijednost za argument '" + kljuc + "': " + vrijednost);
                }
                if (tip == File.class) {
                    File file = new File(vrijednost);
                    if (!file.exists()) {
                        System.out.println("Datoteka nije pronadena za argument '" + kljuc + "': " + vrijednost);
                    }
                } else if (tip == String.class) {
                    if (kljuc.equals("vs")) {
                        DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
                        sdf.setLenient(false);
                        try {
                            sdf.parse(vrijednost);
                        } catch (ParseException e) {
                            System.out.println("Nevažeći datum vrijeme za argument '" + kljuc + "': " + vrijednost);
                        }
                    } else if (vrijednost.isEmpty()) {
                        System.out.println("Nevažeći string za argument '" + kljuc + "': " + vrijednost);
                    }
                } else if (tip == Integer.class) {
                    try {
                        Integer.parseInt(vrijednost);
                    } catch (NumberFormatException e) {
                        System.out.println("Nevažeći integer za argument '" + kljuc + "': " + vrijednost);
                    }
                }
              }
          }
      }
  }
}