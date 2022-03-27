package simulator;
import java.util.HashMap;

/**
 *
 */
public class Variable {
	
	private int numberOfElements;
	private static HashMap <String, String> var;

	/**
	 *
	 */

	public Variable(){
		numberOfElements = 0;
		var = new HashMap<>();
		
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static boolean has(String s){
		return var.containsKey(s);
	}

	/**
	 *
	 * @param s
	 * @param o
	 */
	public void add(String s, String o){
		if(!var.containsKey(s)){
			var.put(s,o);
			numberOfElements++;
		}
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static String getValue(String s){
		for(String value: var.keySet()){
			if(value.equals(s))
				return var.get(s);
		}
		System.out.println("Variable:\tThere is no variable with name = \"" + s + "\"");
		return null;
	}

	/**
	 *
	 * @param name
	 * @param value
	 */
	public void setValue(String name, String value){

		for(String val : var.keySet()){
			if(val.equals(name)){
				var.put(val,value.replace(")", ""));
			}
		}
	}

	/**
	 *
	 * @param s
	 */
	public void increaseValue(String s){
		int value = Integer.parseInt(getValue(s));
		value++;
		setValue(s, value+"");
		
	}

	/**
	 *
	 */
	public void print(){
		System.out.println("Variable:\tThese are all the variables:");
		var.forEach((key, value) -> System.out.println(key + "  =  " + value.replace(')',' ')));
	}

	/**
	 *
	 * @param var
	 * @param speed
	 */
	public void setPower(String var, int speed){

		int comma = (getValue(var).lastIndexOf(",")+1);
		String val = getValue(var).substring(0, comma);
		String vit = ""+speed+")";
		
		setValue(var, val+vit);

	}
}
