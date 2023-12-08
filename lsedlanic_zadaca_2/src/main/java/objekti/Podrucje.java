package objekti;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import citaci.CsvObjekt;
import tvrtka.Tvrtka;

public class Podrucje implements CsvObjekt, DioPodrucja{
	  private int id;
	  private List<DioPodrucja> children = new ArrayList<>();


	  @Override
	    public int getIndentationLevel() {
	        return 0; 
	    }

	  public int getId() {
		return id;
	  }

	  public void setId(int id) {
		  this.id = id;
	  }

	@Override
	  public void add(DioPodrucja component) {
	      this.children.add(component);
	  }

	  @Override
	  public void remove(DioPodrucja component) {
	      this.children.remove(component);
	  }

	  @Override
	  public DioPodrucja getChild(int index) {
	      return this.children.get(index);
	  }

	  @Override
	  public int getNumChildren() {
	      return this.children.size();
	  }
	  @Override
	  public List<DioPodrucja> getChildren() {
	      return this.children;
	  }
	  
	  

	  @Override
	  public void process(String linija) throws ParseException {
	    linija = linija.replaceAll("\\s", "");
	    if (linija.trim().isEmpty()) {
	        return;
	    }

	    String[] parts = linija.split(";");
	    String id = parts[0];
	    String[] gradUlicaPairs = parts[1].split(",");

	    this.id = Integer.parseInt(id);

	    for (String gradUlicaPair : gradUlicaPairs) {
	        String[] gradUlica = gradUlicaPair.split(":");
	        Mjesto mjesto = new Mjesto();
	        for(Mjesto mjesto2 : Tvrtka.getInstance().getMjesta()) {
	        	if(mjesto2.getId() == Integer.parseInt(gradUlica[0]))
	        		mjesto = mjesto2;
	        		this.add(mjesto);
	        }


	        if ("*".equals(gradUlica[1])) {
	            // Add all Ulica objects from the Mjesto object to the Podrucje object
	            for (Ulica ulica : mjesto.getUlice()) {
	                this.add(ulica);
	            }
	        } else {
	            // Add the corresponding Ulica object from the Mjesto object to the Podrucje object
	            Ulica ulica = mjesto.dobaviUlicu(Integer.parseInt(gradUlica[1]));
	            if (ulica != null) {
	                this.add(ulica);
	            }
	        }
	    }
	  }
	  
	  

	@Override
	public boolean imaVrijednosti() {
		return this.id != 0;
	}


}



