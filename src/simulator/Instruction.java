package simulator;

public class Instruction {
    private String line;
    private int numberLine;
    public Instruction(String line,int num){

        this.line = line;
        this.numberLine = num;

    }

    public String getLine() {
        return line;
    }



    public int getNumberLine() {
        return numberLine;
    }
}
