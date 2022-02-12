package simulator;


public class Variable {
	
	private int numberOfElements;
	
	private String[] name;
	private String[] value;
	
	public Variable(){
		numberOfElements = 0;
		name = new String[10];
		value = new String[10];
		
	}
	
	/*public Variable(int i){
		numberOfElements = 0;
		name = new String[i];
		value = new String[i];
	}*/
	
	public void add(String s, String o){
		if(!has(s)){
			name[numberOfElements] = s;
			value[numberOfElements] = o;
			numberOfElements++;
		}
	}
	
	
	public boolean has(String s){
		//System.out.println("has("+s+")");
		for(int i = 0; i < numberOfElements; i++){
			//System.out.println("name["+i+"].equals("+s+") = " +name[i].equals(s));
			if(name[i].equals(s)){
				return true;
			}
		}
		//System.out.println("Variabele:\tThere is no variabele with name = \"" + s + "\"");
		return false;
		
	}
	
	public String getValue(String s){
		for(int i = 0; i < numberOfElements; i++){
			if(name[i].equals(s)){
				return value[i];
			}
		}
		System.out.println("Variabele:\tThere is no variabele with name = \"" + s + "\"");
		return null;
	}
	
	public void setValue(String name, String value){
		
		for(int i=0; i<numberOfElements; i++){
			if(this.name[i].equals(name)){
				this.value[i] = value;
				return;
			}
		}
		
	}
	
	
	public void increaseValue(String s){
		int value = Integer.parseInt(getValue(s));
		value++;
		setValue(s, value+"");
		
	}
	
	public void print(){
		System.out.println("Variable:\tThese are all the variables:");
		for(int i = 0; i < numberOfElements; i++){
			System.out.println(name[i] + "\t=\t" + value[i]);
		}
	}
	
	
	public void setPower(String var, int speed){
		//var = "mA"			speed = 20
		//value = "NXTMotor('A', 'Power', 20);"
		String value = getValue(var);
		//22
		int laatsteKomma = (value.lastIndexOf(",")+1);
		//" 20)"
		String voor = value.substring(0, laatsteKomma);
		String na = ""+speed+")";
		
		setValue(var, voor+na);

	}
}
