package objekti;

public interface DioPodrucja {
	   void add(DioPodrucja component);
	   void remove(DioPodrucja component);
	   DioPodrucja getChild(int index);
	   int getNumChildren();
	}
