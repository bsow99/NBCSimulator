package simulator;

import java.util.Scanner;


public class RunNBC {

    public Variable v;

    public Main main;

    public int valueUltra;

    public int valueLightL;
    public int valueLightR;

    public final int TAB = 4;

    public boolean stopRun;

    public LoopSet loopSet;

    public double pause;
    public int pauseLine, pauseEnd;
    public boolean pauseFound;

    public Vector vector;

    public int regelRunning;

    public boolean breakFound;
    public int breakLine;


    public RunNBC(Main main){
        this.main = main;

        vector = new Vector();

        loopSet = new LoopSet(main.simulator.code.getSize());

        stopRun = false;

        v = new Variable();

        valueUltra = GraphicsPanel.gd.ultraDistance;
        valueLightL = GraphicsPanel.gd.lightL;
        valueLightR = GraphicsPanel.gd.lightR;

        pause = 0.0;
        pauseLine = -1;
        pauseEnd = -1;
        pauseFound = false;

        regelRunning = -1;

        breakFound = false;
        breakLine = -1;

        runCode();

    }

    public void runCode(){

        read(0, Simulator.code.getSize()-1);
    }

    public void read(int start, int end){
        System.out.println("read("+start+","+end+")");

        for(int i = start; i <= end; i++){
            String lineI = Simulator.code.get(i);
            Simulator.outputPanel.setMidden("Running line "+i+": "+lineI+"\n");


            //System.out.println("for(i="+i+", end="+end+", i<=end == "+(i<=end)+")");


            String usedMethod = "";

            //verwijder ; of % (comment)
            if(lineI.length()>0){
                if(lineI.contains("/n")){
                    lineI = lineI.substring(0, lineI.indexOf("/n"));
                }/*else if(lineI.contains("%")){
					lineI = lineI.substring(0, lineI.indexOf("%"));
				}*/
            }

            //space
            if(this.main.simulator.code.isSpace(i)){
                usedMethod = "isSpace";

                //verify begin or end
            }else if(lineI.contains("endt") || lineI.contains("exit")){
                usedMethod = "verify end";

                // define
            }else if (lineI.contains("#define")){
                //String[] rule=
                String var; int value;
                if (lineI.contains("SPEED")){
                    var = "SPEED";
                    //value = ...
                }else if (lineI.contains("MOVE_TIME")){
                    var = "MOVE_TIME";
                    //value = ...
                }else if (lineI.contains("DECREMENT")){
                    var = "DECREMENT";
                    //value = ...
                }

            }else if (lineI.contains("dseg")){

                if (lineI.contains("segment")){
                    usedMethod = "dseg segment";
                    //var
                    //type = ...
                }else if (lineI.contains("ends")){
                    usedMethod = "dseg ends";
                }

            }else if(lineI.contains("byte") || lineI.contains("sword") ) {
                usedMethod = "declaration";
                String[] command = lineI.trim().split("\\s+");

                assert command.length >= 2;
                assert command.length <= 3;
                try {
                    if (command.length == 2) {
                        String op1 = command[0]; //Speed
                        String op2 = command[1]; //type
                        v.add(op1, "0"); // 0 default value

                    } else if (command.length == 3) {
                        String op1 = command[0]; //Speed
                        String op2 = command[1]; //type
                        String op3 = command[2]; //val
                        v.add(op1, op3);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                v.print();
            }else if (lineI.contains("thread")){
                if(lineI.contains("main")){
                    usedMethod = "thread_main";
                }else{
                    usedMethod = "thread_simple";
                }

            }else if (lineI.contains("subroutine")){
                usedMethod = "subroutine";
            }else if(lineI.contains(":")){
                usedMethod = "fonction : "+lineI;

            }else if(lineI.contains("jmp")){
                usedMethod = "jump_to";

            }else if(lineI.contains("brtst")){
                if(lineI.contains("EQ")){
                    usedMethod = "brtst_if_equality";
                }else if(lineI.contains("GT")){
                    usedMethod = "brtst_if_greater_than";
                }else if(lineI.contains("LT")){
                    usedMethod = "brtst_if_less_than";
                }else if(lineI.contains("LTEQ")){
                    usedMethod = "brtst_if_less_equal_than";
                }else if(lineI.contains("GTEQ")){
                    usedMethod = "brtst_if_greater_equal_than";
                }

            }else if(lineI.contains("brcmp")){
                if(lineI.contains("EQ")){
                    usedMethod = "brcmp_if_equality";
                }else if(lineI.contains("GT")){
                    usedMethod = "brcmp_if_greater_than";
                }else if(lineI.contains("LT")){
                    usedMethod = "brcmp_if_less_than";
                }else if(lineI.contains("LTEQ")){
                    usedMethod = "brcmp_if_less_equal_than";
                }else if(lineI.contains("GTEQ")){
                    usedMethod = "brcmp_if_greater_equal_than";
                }


                //unknown
            }else if(lineI.contains("set")){
                usedMethod = "set_operation";

                String[] command = lineI.trim().split("\\s+");

                assert command.length == 3;
                try {
                    String op1 = command[1]; //Speed
                    String op2 = command[2]; //SPEED
                    //System.out.println("ivi"+command[0]+command[1]+command[2]);
                    int op2_val = getValue(op2);
                    set_function(op1,op2_val);
                    v.print();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else if(lineI.contains("mov")){
                usedMethod = "mov_operation";

            }else if(lineI.contains("add")){
                usedMethod = "add_operation";

                //add Speed, 5, 10
                String ligne = lineI.replace(",","");// supprimer les virgules à la fin de speed, et SPEED,
                String[] command = ligne.trim().split("\\s+");
                assert command.length == 4;
                try {
                    String var = command[1].trim(); //Speed
                    String op1 = command[2].trim(); //5
                    String op2 = command[3].trim(); //10

                    int op1_val = getValue(op1);
                    int op2_val = getValue(op2);
                    int result = op1_val + op2_val;
                    System.out.println(result);
                    set_function(var,result);
                    v.print();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else if(lineI.contains("sub")){
                usedMethod = "sub_operation";

                //sub Speed, 5, 10
                String ligne = lineI.replace(",","");// supprimer les virgules à la fin de speed, et SPEED,
                String[] command = ligne.trim().split("\\s+");
                assert command.length == 4;
                try {
                    String var = command[1].trim(); //Speed
                    String op1 = command[2].trim(); //speed
                    String op2 = command[3].trim(); //10
                    //System.out.println(command.);
                    v.print();
                    System.out.println(v.getValue("Speed"));
                    int op1_val = getValue(op1);
                    int op2_val = getValue(op2);
                    int result = op1_val - op2_val;
                    set_function(var,result);
                    v.print();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }else if(lineI.contains("OnFwd")){
                usedMethod = "OnFwd_operation";

                //OnFwd(OUT_C,100)
                try {
                    String line = lineI;
                    line = line.replace("OnFwd(OUT_", "");
                    line = line.replace(")", "");
                    String[] command = line.trim().split(",");
                    String motor = command[0].trim(); //motor
                    String vitesse = command[1].trim(); //vitesse
                    move_function(motor, getValue(vitesse));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else if(lineI.contains("OnRev")){
                usedMethod = "OnRev_operation";

                //OnRev(OUT_C,100)
                try {
                    String line = lineI;
                    line = line.replace("OnRev(OUT_", "");
                    line = line.replace(")", "");
                    String[] command = line.trim().split(",");
                    String motor = command[0].trim(); //motor
                    String vitesse = command[1].trim(); //vitesse
                    move_function(motor, -getValue(vitesse));
                }catch (Exception e){
                    e.printStackTrace();
                }


            }else if(lineI.contains("wait")){
                usedMethod = "wait_operation";
                wait(lineI);

            }else if(lineI.contains("Off")) {
                usedMethod = "off_operation";

            }else if (lineI.contains("Random")){
                usedMethod = "random";

            }else{
                usedMethod = "unknown";
            }

            if(i == main.simulator.code.getSize()-1){
                System.out.println("stopRun");
                stopRun = true;
            }

            System.out.println("\t" + i + " >"+lineI + "\n\tmethod: "+usedMethod);
        }


    }


    private void set_function(String op1, int op2){
        v.setPower(op1, op2);
    }


    public boolean isNumeric(Scanner s){
        s.useDelimiter("");

        while(s.hasNext()){

            char c = s.next().charAt(0);
            if(!Character.isDigit(c)){
                return false;
            }
        }

        return true;

    }



    private void wait(String line){
        //wait(5000)

        String waarde = line.substring(line.indexOf(('('))+1, line.indexOf(')'));
        double value = Double.parseDouble(waarde);
        //pause = 0.5

        pause = value;
        //pauseLine = i;
        //pauseEnd = j;
        pauseFound = true;

    }


    private void switchLamp(String line){
        //line = "SwitchLamp(Motor_A, ‘on’);

        int lageStreepje = line.indexOf('_');
        char c = line.charAt(lageStreepje+1);
        //c = 'A'
        System.out.println(c);

        if(main.init.getRobot().getActuator((char)(c+32))==null){
            return;
        }

        if(line.contains("on")){
            main.init.robot.open(c+"");


        }else{
            main.init.robot.close(c+"");
        }



    }


    private int ifStatement(int i, String line){
        boolean statement = berekenBoolean(line);

        if(statement){
            return i++;
        }else{
            return main.simulator.code.endOfLine(i, main.simulator.code.numberOfSpace(i));

        }

    }


    private void move_function(String motor,int vitesse){
        activer_motor(motor,vitesse);
    }

    private void activer_motor(String moteur, int vitesse) {
        //moteur -->AC		vitesse=10
        System.out.println("activer_motor(" + moteur + "," + vitesse + ")");
        for (int i = 0; i < moteur.length(); i++) {
            char c = moteur.charAt(i);

            this.main.init.robot.setMotor(c, vitesse);
        }
    }

   /* public void sendToNXT(String s){
        s.trim();
        //m.SendToNXT();
        Scanner scanner = new Scanner(s);
        scanner.useDelimiter("");

        String naam = "";
        String letter = "";
        do{
            if(!letter.equals("") && Character.isLetter(letter.charAt(0))){
                naam += letter;
            }

            letter = scanner.next();

        }while(!letter.equals("."));
        //naam = "m"

        scanner.close();


        //waarde = 			"NXTMotor('AC', 'Power', 60);"
        String waarde = v.getValue(naam);
        onFwd(waarde);

    }
*/
    private void stopBrake(String line){
        line = line.trim();
        //line = "mA.Stop('brake');"

        Scanner s = new Scanner(line);
        s.useDelimiter("");

        String character = "";
        String var = "";

        do{
            var+=character;
            character = s.next();
        }while(!character.equals("."));

        //var = "mA"

        s.close();

        //sensor = 		"NXTMotor('A', 'Power', 20);"
        String sensor = v.getValue(var);
        int index = sensor.indexOf("'");



        //scanner heeft 		"A', 'Power', 20);"
        Scanner scanner = new Scanner(sensor.substring(index+1));
        scanner.useDelimiter("");



        String temp = "";
        String sensoren = "";

        do{
            temp = scanner.next();
            if(temp.equals("A") || temp.equals("B") || temp.equals("C")){
                sensoren += temp;
            }

        }while(!temp.equals("'"));

        //sensoren = "AB"
        s.close();


        for(int i=0; i<sensoren.length(); i++){
            main.init.robot.setMotor(sensoren.charAt(i), 0);
        }
    }


    private void stopOff(String line){
        line = line.trim();
        //line = "mA.Stop('off')"

        Scanner s = new Scanner(line);
        s.useDelimiter("");

        String character = "";
        String var = "";

        do{
            var+=character;
            character = s.next();
        }while(!character.equals("."));

        //var = "mA"

        s.close();

        //sensor = 		"NXTMotor('A', 'Power', 20);"
        String sensor = v.getValue(var);
        int index = sensor.indexOf("'");



        //scanner heeft 		"A', 'Power', 20);"
        Scanner scanner = new Scanner(sensor.substring(index+1));
        scanner.useDelimiter("");



        String temp = "";
        String sensoren = "";

        do{
            temp = scanner.next();
            if(temp.equals("A") || temp.equals("B") || temp.equals("C")){
                sensoren += temp;
            }

        }while(!temp.equals("'"));

        //sensoren = "AB"
        s.close();


        for(int i=0; i<sensoren.length(); i++){
            main.init.robot.stopMotor(sensoren.charAt(i));
        }


    }

    /*private void stop(String line){
        line = line.trim();
        //line = "mA.Stop('off')"

        Scanner s = new Scanner(line);
        s.useDelimiter("");

        String character = "";
        String var = "";

        do{
            var+=character;
            character = s.next();
        }while(!character.equals("."));

        //var = "mA"


        s.close();


        //var = "mA"

        v.setPower(var, 0);

        //sensor = 		"NXTMotor('A', 'Power', 20);"
        String sensor = v.getValue(var);
        int index = sensor.indexOf("'");



        //scanner heeft 		"A', 'Power', 20);"
        Scanner scanner = new Scanner(sensor.substring(index+1));
        scanner.useDelimiter("");



        String temp = "";
        String sensoren = "";

        do{
            temp = scanner.next();
            if(temp.equals("A") || temp.equals("B") || temp.equals("C")){
                sensoren += temp;
            }

        }while(!temp.equals("'"));

        //sensoren = "AB"
        s.close();

        activeerMotoren(sensoren, 0);



    }*/


	/*
	private void stop(String line){
		//#stop
		line = line.trim();
		//line = "mA.Stop('off')"

		Scanner s = new Scanner(line);
		s.useDelimiter("");

		String character = "";
		String var = "";

		do{
			var+=character;
			character = s.next();
		}while(!character.equals("."));

		//var = "mA"

		s.close();

		//sensor = 		"NXTMotor('A', 'Power', 20);"
		String sensor = v.getWaarde(var);
		int index = sensor.indexOf("'");



		//scanner heeft 		"A', 'Power', 20);"
		Scanner scanner = new Scanner(sensor.substring(index+1));
		scanner.useDelimiter("");



		String temp = "";
		String sensoren = "";

		do{
			temp = scanner.next();
			if(temp.equals("A") || temp.equals("B") || temp.equals("C")){
				sensoren += temp;
			}

		}while(!temp.equals("'"));

		//sensoren = "AB"
		s.close();

		activeerMotoren(sensoren, 0);

		////////////for loop moet in commentaar
		for(int i=0; i<sensoren.length(); i++){
			System.out.println("main.init.robot.setMotor("+sensoren.charAt(i)+", 0);");

			main.init.robot.setMotor(sensoren.charAt(i), 0);
			main.init.robot.stopMotor(sensoren.charAt(i));
		}


	}

	*/




	/*
	private Object getObject(String s){
		//NXTMotor('AC', 'Power', 60);
		if(s.contains("NXTMotor")){

			Scanner scanner = new Scanner(s.substring(9));
			scanner.useDelimiter(",");

			String[] deel = new String[3];
			for(int i = 0; i < deel.length; i++){
				deel[i] = scanner.next();
			}

			//'AC'
			Scanner s1 = new Scanner(deel[0]);
			s1.useDelimiter("");
			deel[0] = "";

			while(s1.hasNext()){
				char c = s1.next().charAt(0);
				if(c == '\''){

				}else{
					deel[0] += c;
				}
			}


			//'Power'
			Scanner s2 = new Scanner(deel[1]);

			//System.out.println("\t*****\tdeel[2]=" + deel[2] + "\t*****");

			//60 met misschien ')'
			Scanner s3 = new Scanner(deel[2]);
			s1.useDelimiter("");
			deel[2] = "";

			while(s3.hasNext()){
				char c = s3.next().charAt(0);
				//60
				if(Character.isDigit(c)){
					deel[2] += c;
				}else{

				}
			}
			int speed = Integer.parseInt(deel[2]);


			Scanner last = new Scanner(deel[0]);
			last.useDelimiter("");

			Variabele temp = new Variabele(3);
			while(last.hasNext()){
				String ABC = last.next();
				if(ABC.charAt(0) == 'A'){
					temp.add("A", new Motor(speed));

				}else if(ABC.charAt(0) == 'B'){
					temp.add("B", new Motor(speed));

				}else if(ABC.charAt(0) == 'C'){
					temp.add("C", new Motor(speed));
				}

			}


			return temp;

		}else{
			return null;
		}




	}
	*/

    private void openLight(String line, int lineNumber){
        //OpenLight(SENSOR_4, ‘active’);		or ‘ACTIVE’
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter("");

        String cijfer;

        do{
            cijfer = scanner.next();
        }while(!Character.isDigit(cijfer.charAt(0)));

        scanner.close();

        this.main.init.robot.open(cijfer);
        if(!(cijfer.equals("1") || cijfer.equals("2") || cijfer.equals("3") || cijfer.equals("4"))){
            main.simulator.outputPanel.print("Line "+lineNumber+": Sensor "+cijfer+" is not connected with a Light sensor.\n");
        }


    }

    private int getLight(String line){
        //GetLight(SENSOR_4))

        Scanner scanner = new Scanner(line);
        scanner.useDelimiter("");

        String cijfer;
        do{
            cijfer = scanner.next();
        }while(!Character.isDigit(cijfer.charAt(0)));

        scanner.close();




        if(this.main.init.robot.getSensor(cijfer.charAt(0)).getType().equals("LightL")){
            return valueLightL;
        }else{
            return valueLightR;
        }




    }



    private void closeSensor(String line, int lineNumber){
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter("");

        String cijfer;

        do{
            cijfer = scanner.next();
        }while(!Character.isDigit(cijfer.charAt(0)));

        scanner.close();

        this.main.init.robot.close(cijfer);
        if(!(cijfer.equals("1") || cijfer.equals("2") || cijfer.equals("3") || cijfer.equals("4"))){
            main.simulator.outputPanel.print("Line "+lineNumber+": Sensor "+cijfer+" is not connected with a Light sensor.\n");
        }


    }

    private void openUltrasonic(String line, int lineNumber){
        //OpenUltrasonic(SENSOR_3);
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter("");

        String cijfer;

        do{
            cijfer = scanner.next();
        }while(!Character.isDigit(cijfer.charAt(0)));

        scanner.close();


        this.main.init.robot.open(cijfer);
        if(!(cijfer.equals("1") || cijfer.equals("2") || cijfer.equals("3") || cijfer.equals("4"))){
            main.simulator.outputPanel.print("Line "+lineNumber+": Sensor "+cijfer+" is not connected with a Ultrasonic sensor.\n");
        }

    }

    private int getUltrasonic(String line){
        //return main.simulator.graphicsPanel.gd.ultraDistance;
        return valueUltra;
    }



    private int getTellerStop(String line){
        //String line = "for i=1:10";
        int dubbelePunt = line.indexOf(':');
        int eind = line.length();
        return Integer.parseInt(line.substring(dubbelePunt+1, eind).trim());
    }

    private int getTellerStart(String line){
        //String line = "for i=1:10";
        int is = line.indexOf('=');
        int dubbelePunt = line.indexOf(':');
        return Integer.parseInt(line.substring(is+1, dubbelePunt).trim());
    }

    private String getTeller(String line){
        //String line = "for i=1:10";
        int r = line.indexOf('r');
        int is = line.indexOf('=');
        return line.substring(r+1, is).trim();
    }

    public int getWaardeTeller(String teller, int tellerStart){
        //teller = "i"

        if(v.has(teller)){
            v.increaseValue(teller);
            return Integer.parseInt(v.getValue(teller));
        }else{
            v.add(teller, tellerStart+"");
            return tellerStart;
        }

    }




    public boolean forLoop(Loop loop){
        //start = 0, end = 2, line = "for i=1:10";
        //teller = "i", tellerStart = 1, tellerStop = 10

        String line = loop.line;
        String teller = getTeller(line);
        int tellerStart = getTellerStart(line);
        int tellerStop = getTellerStop(line);

        //als i nieuw is dan i=1		als i al bestaat dan i=2
        int i = getWaardeTeller(teller, tellerStart);
        System.out.println(i+"<="+tellerStop);
        if(i<=tellerStop){
            System.out.println("for is true");
            read(loop.start+1, loop.end-1);
            System.out.println();
            if(breakFound){
                loopSet.verwijderLoops(breakLine);
                breakFound = false;
                breakLine = -1;
            }else{
                loopSet.push(loop);
            }

            return true;
        }else{
            System.out.println("for is false");
            read(loop.end+1, main.simulator.code.getSize()-1);
            return false;
        }
    }


    public boolean whileLoop(Loop loop){
        if(berekenBoolean(loop.line)){
            System.out.println("while is true");
            read(loop.start+1, loop.end-1);

            if(breakFound){
                loopSet.verwijderLoops(breakLine);
                breakFound = false;
                breakLine = -1;
            }else{
                loopSet.push(loop);
            }


            return true;
        }else{
            System.out.println("while is false");
            read(loop.end+1, main.simulator.code.getSize()-1);
            return false;
        }
    }

    private boolean berekenBoolean(String line){
        //line = "while(GetUltrasonic(SENSOR_1) > minDistance)"		or		"if (val1 > 450 && val2 > 400)"

        String expressie = line.substring(line.indexOf("(") + 1, (line.length()-1));
        //expressie = "GetUltrasonic(SENSOR_1) > minDistance"

        //true or false
        if(expressie.contains("true")){
            return true;
        }else if(expressie.contains("false")){
            return false;
        }

        if(line.contains("&&")){
            String[] delen = expressie.split("&&");
            String deel1 = delen[0].trim();
            String deel2 = delen[1].trim();

            boolean d1 = berekenBoolean(deel1);
            boolean d2 = berekenBoolean(deel2);
            //System.out.println("#9. d1="+d1+"\td2="+d2);

            return d1 && d2;
        }



        //< or = or >
        if(expressie.contains("<")){
            String[] delen = expressie.split("<");
            String deel1 = delen[0].trim();
            String deel2 = delen[1].trim();

            return expressie1(deel1, deel2);

        }else if(expressie.contains(">")){
            String[] delen = expressie.split(">");
            String deel1 = delen[0].trim();
            String deel2 = delen[1].trim();

            return expressie2(deel1, deel2);

        }else if(expressie.contains("==")){
            String[] delen = expressie.split("==");
            String deel1 = delen[0].trim();
            String deel2 = delen[1].trim();

            return expressie3(deel1, deel2);
        }

        //fout
        return false;

    }


    private boolean expressie1(String deel1, String deel2){

        int d1 = getValue(deel1);
        int d2 = getValue(deel2);
        return d1 < d2;

    }

    private boolean expressie2(String deel1, String deel2){
        //deel1 = "val1", deel2 = "450"

        int d1 = getValue(deel1);
        int d2 = getValue(deel2);

        return d1 > d2;
    }


    private boolean expressie3(String deel1, String deel2){
        int d1 = getValue(deel1);
        int d2 = getValue(deel2);

        return d1 == d2;
    }

    private void display(String line){
        //display(GetUltrasonic(SENSOR_1));			display(vector(j))
        int openen = line.indexOf('(');
        int sluiten = line.lastIndexOf(')');
        String var = line.substring(openen+1, sluiten);

        main.simulator.print(getValue(var)+"\n");
    }

    private int getValue(String deel){
        //System.out.println("# jaaaa: "+deel);
        //deel = "minDistance", "GetUltrasonic(SENSOR_1)"
        //deel = "val1"
        if(deel.contains("GetLight")){
            return getLight(deel);

        }else if(deel.contains("GetUltrasonic")){
            return getUltrasonic(deel);

        }else if(containsAllDigits(deel)){
            return Integer.parseInt(deel);

        }else if(deel.contains("(") && deel.contains(")")){

            //deel = "light_vec(i)"
            int openen = deel.indexOf('(');
            int sluiten = deel.indexOf(')');
            String naam = deel.substring(0, openen);
            //System.out.println("# foooo: "+deel.substring(openen+1, sluiten).trim());
            return getValue(deel.substring(openen+1, sluiten).trim());
            //int index = Integer.parseInt(deel.substring(openen+1, sluiten).trim());
            //return vector.getWaarde(naam, index);

        }else if(v.has(deel)){

            //v.getWaarde(deel) = "GetLight(SENSOR_1)"
            return getValue(v.getValue(deel));


            //return Integer.parseInt(v.getWaarde(deel));
        }else if(loopSet.get(deel) != -1){
            return loopSet.get(deel);

        }else{
            return -1;
        }
    }

    private boolean containsAllDigits(String line){

        for(int i=0; i<line.length(); i++){
            if(!Character.isDigit(line.charAt(i))){
                return false;
            }
        }
        return true;
    }

    private void setPower(String deel1, String deel2){
        //#power
        //deel1 ="mA.Power"		deel2 = "20"

        Scanner s = new Scanner(deel1);
        s.useDelimiter("");

        String character = "";
        String var = "";

        do{
            var+=character;
            character = s.next();
        }while(!character.equals("."));

        s.close();

        //var = "mA"

        v.setPower(var, Integer.parseInt(deel2));

    }




    public void ververs(){
        vector = new Vector();

        loopSet = new LoopSet(main.simulator.code.getSize());

        stopRun = false;

        v = new Variable();

        valueUltra = main.simulator.graphicsPanel.gd.ultraDistance;
        valueLightL = main.simulator.graphicsPanel.gd.lightL;
        valueLightR = main.simulator.graphicsPanel.gd.lightR;

        pause = 0.0;
        pauseLine = -1;
        pauseEnd = -1;
        pauseFound = false;

        regelRunning = -1;

        breakFound = false;
        breakLine = -1;
    }


}
