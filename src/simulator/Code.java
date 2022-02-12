package simulator;

public class Code {

	private final String[] code;
	private int numberLines;

	public Code(int max){
		numberLines = 0;
		code = new String[max];
	}

	public void add(String line){

		code[numberLines] = line;
		numberLines++;
	}

	public String get(int index){

		return code[index];
	}

	public int getSize(){
		return numberLines;
	}



	public int numberOfSpace(int index){
		System.out.println("#  numberOfSpace("+index+")");
		if(code[index].length()==0){
			return -1;
		}

		int space = 0;
		int i = 0;

		System.out.println("   space = "+space+"\ti = "+i);

		while(i<code[index].length() && (code[index].charAt(i) == ' ' || code[index].charAt(i) == '\t')){
			//System.out.println("    code["+index+"].charAt("+i+") =" + code[index].charAt(i));

			if(code[index].charAt(i) == ' '){
				space++;
			}else{
				space+=4;
			}
			i++;
			//System.out.println("    space = "+space+"\ti = "+i);
		}
		return space;
	}


	public int endOfLine(int beginIndex, int numberSpaces){
		//System.out.println("# endOfLine("+beginIndex+","+numberSpaces+")");

		int i = beginIndex+1;

		while(i < numberLines){
			//System.out.println("  "+i+"<"+numberLines+"\tgeeft true");
			if(numberOfSpace(i) == numberSpaces){
				return i;
			}else{
				i++;
			}
		}
		//System.out.println(" i = "+i);
		return -1;
	}

	@Override
	public String toString(){
		String temp = "";

		for(int i=0; i<numberLines; i++){
			String whitespaces = "";
			//1 whitespaces
			if(i<10){
				whitespaces = " ";
			}
			temp += whitespaces+i+" "+code[i] + "\n";
		}

		if (temp.length() > 0){
			temp = temp.substring(0, temp.length()-1);
		}

		return temp;
	}


	public boolean isSpace(int i){
		String temp = String.copyValueOf(code[i].toCharArray());

		return (temp.trim().length() == 0 || code[i].length() == 0);
	}

	public int regelNummerEnd(int beginIndex){

		int numberSpaces = numberOfSpace(beginIndex);

		int i = beginIndex;

		while(i < numberLines){
			if(numberOfSpace(i) == numberSpaces && code[i].contains("end")){
				return i;
			}else{
				i++;
			}
		}
		return -1;
	}


	public int doBreak(int i){

		int teller = i;

		while(teller>=0){
			if(code[teller].contains("while") || code[teller].contains("for")){
				break;
			}
			teller--;
		}

		return regelNummerEnd(teller)+1;
	}

}
