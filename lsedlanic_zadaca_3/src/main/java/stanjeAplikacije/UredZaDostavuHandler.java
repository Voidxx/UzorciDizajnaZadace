package stanjeAplikacije;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tvrtka.UredZaDostavu;

public class UredZaDostavuHandler implements Handler {
	  private Handler next;


	  @Override
	  public void handleSave(Map<String, Object> state) {
	   Field[] fields = UredZaDostavu.getInstance().getClass().getDeclaredFields();
	   for (Field field : fields) {
	      try {
	          field.setAccessible(true);
	          String name = field.getName();
	          Object value = field.get(UredZaDostavu.getInstance());
	          if (value instanceof List) {
	        	   List<?> listValue = (List<?>) value;
	        	   List<Object> clonedList = new ArrayList<>();
	        	   for (Object obj : listValue) {
	        	       if (obj instanceof Cloneable) {
	        	           try {
	        	               Method cloneMethod = obj.getClass().getMethod("clone");
	        	               obj = cloneMethod.invoke(obj);
	        	           } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
	        	               e.printStackTrace();
	        	           }
	        	       }
	        	       clonedList.add(obj);
	        	   }
	        	   state.put(name, clonedList);
	        	} else {
	        	   state.put(name, value);
	        	}
	      } catch (IllegalAccessException e) {
	          e.printStackTrace();
	      }
	   }
	  }

	  @Override
	  public void handleLoad(Map<String, Object> state) {
	   Field[] fields = UredZaDostavu.getInstance().getClass().getDeclaredFields();
	   for (Field field : fields) {
	      try {
	          if (Modifier.isStatic(field.getModifiers())) continue;
	          field.setAccessible(true);
	          String name = field.getName();
	          Object value = state.get(name);
	          if (value != null) {
	        	  if (value instanceof List) {
	        		   List<Object> listValue = (List<Object>) value;
	        		   for (int i = 0; i < listValue.size(); i++) {
	        		       Object obj = listValue.get(i);
	        		       if (obj instanceof Cloneable) {
	        		           try {
	        		               Method cloneMethod = obj.getClass().getMethod("clone");
	        		               obj = cloneMethod.invoke(obj);
	        		           } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
	        		               e.printStackTrace();
	        		           }
	        		       }
	        		       listValue.set(i, obj);
	        		   }
	        		   field.set(UredZaDostavu.getInstance(), listValue);
	        		} else {
	        		   field.set(UredZaDostavu.getInstance(), value);
	        		}
	          }
	      } catch (IllegalAccessException e) {
	          e.printStackTrace();
	      }
	   }
	  }

	  @Override
	  public Handler setNext(Handler handler) {
	      this.next = handler;
	      return handler;
	  }
	}
