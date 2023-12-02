package objekti;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import citaci.CsvObjekt;

public class Podrucje implements CsvObjekt, DioPodrucja{
	  private int id;
	  private List<DioPodrucja> children = new ArrayList<>();



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
	  public void process(String linija) throws ParseException {
	      // potrebna validacija
	      String[] vrijednosti = linija.split("; ");
	      this.setId(Integer.parseInt(vrijednosti[0]));
	      String[] paroviSaDvotockom = vrijednosti[1].split(",");

	      // add streets to the area
	      for (String par : paroviSaDvotockom) {
	          String[] parovi = par.split(":");
	          int cityId = Integer.parseInt(parovi[0]);
	          String streetIds = parovi[1];

	          if (streetIds.equals("*")) {
	              // add all streets of the city to the area
	              for (DioPodrucja child : this.children) {
	                 if (child instanceof Ulica) {
	                     Ulica ulica = (Ulica) child;
	                     if (ulica.getId() == cityId) {
	                         this.add(ulica);
	                     }
	                 }
	              }
	          } else {
	              // add only the specified streets to the area
	              for (String id : streetIds.split(",")) {
	                 for (DioPodrucja child : this.children) {
	                     if (child instanceof Ulica) {
	                         Ulica ulica = (Ulica) child;
	                         if (ulica.getId() == Integer.parseInt(id)) {
	                             this.add(ulica);
	                         }
	                     }
	                 }
	              }
	          }
	      }
	  }
	  
	  

	@Override
	public boolean imaVrijednosti() {
		// TODO Auto-generated method stub
		return false;
	}


}



