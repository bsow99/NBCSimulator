package simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//import java.util.Timer;


public class RunNBC {

    public Variable v;

    public Main main;

    public int valueUltra;

    public int valueLightL;
    public int valueLightR;

    public boolean stopRun;

    public Sound sound;

    public LabelSet labelSet;
    public InstructionSet instructionSet;

    public int regelRunning;


    //public Label[] listOfLabel;
    List<Label> listOfLabel;
    public RunNBC(Main main) {
        this.main = main;


        labelSet = new LabelSet(Simulator.code.getSize());
        instructionSet = new InstructionSet();

        stopRun = false;

        v = new Variable();
        listOfLabel = new ArrayList<>();
        valueUltra = GraphicsPanel.gd.ultraDistance;
        valueLightL = GraphicsPanel.gd.lightL;
        valueLightR = GraphicsPanel.gd.lightR;

        regelRunning = -1;


        add_instruction(0, Simulator.code.getSize()-1);

    }


    /**
     * @author Babacar
     * @effects allow us to add a list of instructions to a stack(instructionSet)
     * where wue push them
     *
     * @param start : numero de l'instruction de départ
     * @param end : numero de l'instruction de fin
     * @param instructionSet1 : stack dans laquelle, l'on doit charger les instructions
     */
    public void add_instruction2(int start, int end, InstructionSet instructionSet1){
        System.out.println("add the line :("+start+","+end+")");
        for(int i = start; i <= end; i++){
            String lineI = Simulator.code.get(i);
            Instruction instruction = new Instruction(lineI,i);
            instructionSet1.push(instruction);
        }
        System.out.println(instructionSet1.instructions.size());
    }

    /**
     * Cette fonction perment d'ajouter une liste d'instruction dans la stack d'instruction principal
     * a partir du code nbc fournie
     * @param start : numero de l'instruction de départ
     * @param end : numero de l'instruction de fin
     */
    public void add_instruction(int start, int end){
        System.out.println("add the line :("+start+","+end+")");
        for(int i = start; i <= end; i++){
            String lineI = Simulator.code.get(i);
            Instruction instruction = new Instruction(lineI,i);
            instructionSet.push(instruction);
            //main.simulator.outputPanel.setMidden("Running line "+i+": "+lineI+"\n");
        }
        System.out.println(instructionSet.instructions.size());
    }

    /**
     * cette fonction execute une ligne d'instruction
     * @param lineI : ligne d'instruction
     * @param numLine : numero de la ligne d'instruction
     * @throws InterruptedException
     */
    public void executeLine(String lineI, int numLine) throws InterruptedException {
        String usedMethod;

        if(lineI.contains("endt") || lineI.contains("exit")){
            usedMethod = "verify end";

        }

        else if (lineI.contains("#define")){
            usedMethod ="define";

            String[] rule = lineI.trim().split("\\s+");
            assert rule.length <= 3;
            //[#define, SPEED, 80]
            try {
                String var = rule[1];
                String val = rule[2];
                v.add(var, val);
            }catch (Exception e) {e.printStackTrace();}
        }

        else if (lineI.contains("dseg")){

            if (lineI.contains("segment")){
                usedMethod = "dseg segment";
            }
            else if (lineI.contains("ends")){
                usedMethod = "dseg ends";
            }

        }

        else if(lineI.contains("byte") || lineI.contains("sword") || lineI.contains("word")){
            usedMethod = "declaration";
            String[] command = lineI.trim().split("\\s+");

            assert command.length >= 2;
            assert command.length <= 3;

            try {
                if (command.length == 2){
                    String op1 = command[0]; //Speed
                    //String op2 = command[1]; //type
                    v.add(op1,"0"); // 0 default value

                }else if(command.length == 3){
                    String op1 = command[0]; //Speed
                    //String op2 = command[1]; //type
                    String op3 = command[2]; //val
                    v.add(op1,op3);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            v.print();
        }

        else if (lineI.contains("thread")){
            if(lineI.contains("main")){
                usedMethod = "thread_main";
            }else{
                usedMethod = "thread_simple";
            }
        }

        else if(lineI.contains("Off")){
            usedMethod = "off_operation";
            off(lineI);
        }

        else if (lineI.contains("subroutine")){
            usedMethod = "subroutine";

        }

        else if(lineI.contains(":")){
            usedMethod = "label : "+lineI;
            String name = lineI.replace(":","").trim();
            Label l = new Label(name, numLine);
            //labelSet.push(l);
            listOfLabel.add(l);


        }

        else if(lineI.contains("jmp")){
            usedMethod = "jump_to";
            String line = lineI;

            jump_toLabel(line,instructionSet);



        }

        else if(lineI.contains("tst")){
            //tst GT, ccc, bbb

            String line = lineI;
            line += ", 0";
            //tst GT, ccc, bbb, 0
            Boolean ok = compare_simple(line);
            System.out.println(ok);

        }

        else if(lineI.contains("cmp")){
            //cmp GT, ccc, bbb, 53
            String line = lineI;
            Boolean ok = compare_simple(line);
            System.out.println(ok);

        }

        else if(lineI.contains("set")){
            usedMethod = "set_operation";
            String[] command = lineI.trim().split("\\s+");

            assert command.length == 3;

            try {
                String op1 = command[1]; //Speed
                String op2 = command[2]; //SPEED
                int op2_val = getValue(op2);
                set_function(op1,op2_val);
                v.print();
            }catch (Exception e){
                e.printStackTrace();
            }


        }

        else if(lineI.contains("mov")){
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
        }
        else if(lineI.contains("mul")){
            usedMethod = "add_operation";

            //sub Speed, 5, 10
            String ligne = lineI.replace(",","");// supprimer les virgules à la fin de speed, et SPEED,
            String[] command = ligne.trim().split("\\s+");
            assert command.length == 4;
            try {
                String var = command[1].trim(); //Speed
                String op1 = command[2].trim(); //5
                String op2 = command[3].trim(); //10

                int op1_val = getValue(op1);
                int op2_val = getValue(op2);
                int result = op1_val * op2_val;
                System.out.println(result);
                set_function(var,result);
                v.print();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        else if(lineI.contains("div")){
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
                int result = op1_val / op2_val;
                System.out.println(result);
                set_function(var,result);
                v.print();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        else if(lineI.contains("sub")){
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
        }

        else if(lineI.contains("OnFwd")){
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
        }

        else if (lineI.contains("SetSensorLight")){
            usedMethod = "SetSensorLight";
            //SetSensorLight(IN_3)
            try {
                String line = lineI;
                line = line.replace("SetSensorLight(IN_", "");
                line = line.replace(")", "");
                String sensorNum = line;
                setSensorLight(sensorNum);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        else if (lineI.contains("ReadSensor")){
            //ReadSensor(IN_3,Level)
            usedMethod = "ReadSensor";
            try {
                String line = lineI;
                line = line.replace("ReadSensor(IN_", "");
                line = line.replace(")", "");
                String[] command = line.trim().split(",");
                String sensor = command[0].trim(); //sensor
                String level = command[1].trim(); //level
                int newValue = getValueSensor(sensor);
                main.simulator.outputPanel.print("Sensor value is :" + newValue);
                set_function(level,newValue);
            }catch (Exception e){
                e.printStackTrace();
            }




        }

        else if (lineI.contains("SetSensorTouch")){
            usedMethod ="SetSensorTouch";
            //SetSensorTouch(IN_1)
            try {
                String line = lineI;
                line = line.replace("SetSensorTouch(IN_", "");
                line = line.replace(")", "");
                String sensorNum = line;
                setSensorTouch(sensorNum);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        else if(lineI.contains("OnRev")){
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

        }

        else if(lineI.contains("Off")){
            usedMethod = "off_operation";
            off(lineI);

        }
        else if(lineI.contains("PlayTone")){
            usedMethod = "music";
            sound = new Sound();
            sound.play();
        }
        else if (lineI.contains("RotateMotor")){
            usedMethod = "rotate motor";
            rotateMotor(lineI);
        }
        else{
            usedMethod = "unknown";
        }
    }


    //-----------------------------------------------------------------------------------------------//
    //                           fonctions pour executer une intruction particuliere                 //
    //-----------------------------------------------------------------------------------------------//

    /**
     * cette fonction assigne à une variable une valeur
     * @param op1 : variable
     * @param op2 : valeur
     */
    private void set_function(String op1, int op2){
        v.setPower(op1, op2);
    }


    /**
     * cette fonction activer les moteurs du robot
     * @param moteur : les moteurs à activer
     * @param vitesse : la vitesse assigner aux moteurs
     */
    private void activer_motor(String moteur, int vitesse){
        //moteur -->AC		vitesse=10
        System.out.println("activer_motor("+moteur+","+vitesse+")");
        for(int i=0; i<moteur.length(); i++){
            char c = moteur.charAt(i);

            Init.robot.setMotor(c, vitesse);
        }
    }

    /**
     *
     */
    public void rotateMotor(String line){
        line = line.replace("RotateMotor(OUT_","");
        line = line.replace(")","");
        String[] command = line.trim().split(",");
        //[A,100,90]
        assert command.length == 3;
        char motor = command[0].charAt(0);
        Init.robot.setMotor(motor,Integer.parseInt(command[1]));
    }

    /**
     * cette fonction deplacer le robot
     * @param motor : les moteurs à deplacer
     * @param vitesse : la vitesse assigner aux moteurs
     */
    private void move_function(String motor,int vitesse){
        activer_motor(motor,vitesse);
    }


    /**
     * Cette fonction pert de couper la vitesse la vitesse des moteurs
     * @param line : la ligne des intructions
     */
    private void off(String line){
        line = line.trim();
        //line = Coast(OUT_BC)
        line = line.replace("Off(OUT_", "");
        line = line.replace(")", "");
        String[] command = line.trim().split(",");
        //[BC]
        assert command.length == 1;
        String motor = command[0].trim(); //motor
        for(int temp = 0; temp<motor.length(); temp++) {
            Init.robot.setMotor(motor.charAt(temp),0);
        }
    }


    /**
     * Cette fonction permet d'activer les senseurs lumineux souhaités
     * @param sensorNum : numero du senseur (1, 2 ou 3)
     */
    private  void setSensorLight(String sensorNum){
        Init.robot.open(sensorNum);
        Simulator.outputPanel.print("Sensor :"+sensorNum+ " is connected...");
    }

    /**
     * Cette fonction permet d'activer le senseurs de touché (ultrasonic) souhaité
     * @param sensorNum : numero du senseur (1, 2 ou 3)
     */
    private void setSensorTouch(String sensorNum){
        //SetSensorTouch(IN_1)
        assert Integer.parseInt(sensorNum) < 5 && Integer.parseInt(sensorNum) >0;

        Init.robot.open(sensorNum);
        Simulator.outputPanel.print("The touch Sensor "+sensorNum+" is connected with a Ultrasonic sensor.\n");

    }

    /**
     * cette fonction renvoie la valeur d'un senseur (lumineux ou ultrasonic)
     * @param sensorNum : numero du senseur
     * @return : la valeur du senseur
     */
    private int getValueSensor(String sensorNum){
        //ReadSensor(IN_3,Level)

        assert Integer.parseInt(sensorNum) < 5 && Integer.parseInt(sensorNum) >0;

        if(Init.robot.getSensor(sensorNum.charAt(0)).getType().equals("LightL")){
            return GraphicsPanel.gd.lightL;
        }else if(Init.robot.getSensor(sensorNum.charAt(0)).getType().equals("LightR")){
            return GraphicsPanel.gd.lightR;
        }else{
            return GraphicsPanel.gd.ultraDistance;
        }

    }

    /**
     * cette fonction compare deux valeurs et assigne le resultat à une variable
     * 1 si la comparaison est vrai et 0 dans le cas contraire
     * @param line : la ligne d'instruction : cmp ou tst
     * @return true ou false
     */
    public Boolean compare_simple(String line){
        //tst GT, ccc, bbb, 0
        String ligne = line.replace(",","");// supprimer les virgules à la fin de speed, et SPEED,
        String[] command = ligne.trim().split("\\s+");
        //assert command.length == 4;
        Boolean ok =false;
        try {
            String comparaisonName = command[0].trim(); //cmp
            String comparaisonType = command[1].trim(); //LT
            String result = command[2].trim(); //result
            String val1 = command[3].trim(); //val1
            String val2 = command[4].trim(); //val1

            int op1_val = getValue(val1);
            System.out.println(op1_val);
            int op2_val = getValue(val2);
            System.out.println(op2_val);
            switch (comparaisonType) {
                case "LT":
                    ok = op1_val < op2_val;
                    break;
                case "GT":
                    ok = op1_val > op2_val;
                    break;
                case "EQ":
                    ok = op1_val == op2_val;
                    break;
                case "LTEQ":
                    ok = op1_val <= op2_val;
                    break;
                case "GTEQ":
                    ok = op1_val >= op2_val;
                    break;
                case "NEQ":
                    ok = op1_val != op2_val;
                    break;
                default:
                    assert false;
            }
            if (ok){
                set_function(result,1);
            }else {
                set_function(result,0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ok;
    }

    /**
     * cette fonction compare deux valeurs et jump vers un label (partie du code nbc)
     * si la comparaison est vraie
     * @param line : brcmp ou brtst
     * @param instructionSet1 : stack d'instructions
     * @return true or false
     */
    public Boolean comparaison_toLabel(String line,InstructionSet instructionSet1){
        //brcmp LT, CheckSensor, Level, THRESHOLD
        String ligne = line.replace(",","");
        String[] command = ligne.trim().split("\\s+");
        //assert command.length == 5;
        Boolean ok =false;
        try {
            String comparaisonName = command[0].trim(); //brcmp
            String comparaisonType = command[1].trim(); //LT
            String labelName = command[2].trim(); //Label
            String val1 = command[3].trim(); //val1
            String val2 = command[4].trim(); //val1

            int op1_val = getValue(val1);
            System.out.println(op1_val);
            int op2_val = getValue(val2);
            System.out.println(op2_val);
            switch (comparaisonType) {
                case "LT":
                    ok = op1_val < op2_val;
                    break;
                case "GT":
                    ok = op1_val > op2_val;
                    break;
                case "EQ":
                    ok = op1_val == op2_val;
                    break;
                case "LTEQ":
                    ok = op1_val <= op2_val;
                    break;
                case "GTEQ":
                    ok = op1_val >= op2_val;
                    break;
                case "NEQ":
                    ok = op1_val != op2_val;
                    break;
            }
            if (ok){
                for (Label l : listOfLabel){
                    if (l.getName().equals(labelName)){
                        Simulator.outputPanel.print("The line : \" "+line + "\" is true");
                                goToLabel(l,instructionSet1);
                                return ok;
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return ok;
    }

    /**
     * cette fonction permet d'execter le code à partir d'un label
     * @param a_label : label de destination
     * @param instructionSet1 :
     */
    public void goToLabel(Label a_label,InstructionSet instructionSet1) {
        int labelLine= a_label.getLineNum();
        add_instruction2(labelLine+1, Simulator.code.getSize()-1,instructionSet1);
        Simulator.outputPanel.print("Go to label"+a_label.getName());

    }

    /**
     * cette fonction permet d'execter la commande jump
     * @param line : ligne d'instruction jmp
     * @param instructionSet1 :
     * @throws InterruptedException
     */
    public void jump_toLabel(String line, InstructionSet instructionSet1) throws InterruptedException {
        String labelName = line.replace("jmp", "");
        labelName= labelName.trim();
        for (Label l : listOfLabel){
            if (l.getName().equals(labelName)){
                Simulator.outputPanel.print("The line : \" "+line + "\" is true");
                goToLabel(l,instructionSet1);
                return;
            }
        }
    }


    /**
     * cette fonction permet valeur (integer) d'une variable
     * et transforme une string ("1000") en entier 1000
     * @param deel : variable
     * @return valeur
     */
    public int getValue(String deel){

        if(containsAllDigits(deel)){
            return Integer.parseInt(deel);

        }else if(deel.contains("(") && deel.contains(")")){


            int openen = deel.indexOf('(');
            int sluiten = deel.indexOf(')');
            return getValue(deel.substring(openen+1, sluiten).trim());

        }else if(v.has(deel)){
            System.out.println("var trouvé");
            return Integer.parseInt(Variable.getValue(deel));

        }else{
            return -1;
        }




    }

    /**
     * cette fonction verifie qu'une chaine de contient des strings
     * @param line "12546" ou "654" ou ...
     * @return true or false
     */
    private boolean containsAllDigits(String line){

        for(int i=0; i<line.length(); i++){
            if(!Character.isDigit(line.charAt(i))){
                return false;
            }
        }

        return true;
    }


    public void end(){

        stopRun = false;

        v = new Variable();

        valueUltra = GraphicsPanel.gd.ultraDistance;
        valueLightL = GraphicsPanel.gd.lightL;
        valueLightR = GraphicsPanel.gd.lightR;

        regelRunning = -1;

    }


}

