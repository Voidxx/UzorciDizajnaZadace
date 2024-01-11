package objekti;

import java.util.List;

public interface DioPodrucja {
	   void add(DioPodrucja component);
	   void remove(DioPodrucja component);
	   DioPodrucja getChild(int index);
	   int getNumChildren();
	   List<DioPodrucja> getChildren();
	   int getIndentationLevel();
	}
