package app;

public interface SystemStateInterface {
	   void saveState(String filename);
	   SystemState loadState(String filename);
	}