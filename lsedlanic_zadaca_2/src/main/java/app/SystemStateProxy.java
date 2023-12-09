package app;

public class SystemStateProxy implements SystemStateInterface {
	   private SystemState systemState;

	   @Override
	   public void saveState(String filename) {
	       if (systemState == null) {
	           systemState = new SystemState(null, null, null, null, null);
	       }
	       systemState.saveState(filename);
	   }

	   @Override
	   public SystemState loadState(String filename) {
	       if (systemState == null) {
	           systemState = new SystemState(null, null, null, null, null);
	       }
	       return systemState.loadState(filename);
	   }
	   

	}