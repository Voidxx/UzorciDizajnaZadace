package citaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class CitacTxt {
    public Properties ucitajParametre(String putanjaDoDatoteke) throws IOException {
        Properties parametri = new Properties();
        BufferedReader reader = new BufferedReader(new FileReader(putanjaDoDatoteke));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("=", 2);
            if (parts.length == 2) {
               parametri.setProperty(parts[0].trim(), parts[1].trim());
            }
        }
        reader.close();
        return parametri;
    }
}
