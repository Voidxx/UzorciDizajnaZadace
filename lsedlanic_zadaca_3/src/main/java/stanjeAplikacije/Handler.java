package stanjeAplikacije;

import java.util.Map;

public interface Handler {
	  void handleSave(Map<String, Object> state);
	  void handleLoad(Map<String, Object> state);
	  Handler setNext(Handler handler);
	}