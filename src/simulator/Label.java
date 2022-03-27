package simulator;

public class Label {
    private String name;
    private int lineNum;

    public Label(String name, int num){
        this.name = name;
        this.lineNum = num;
    }

    public int getLineNum() {
        return lineNum;
    }

    public String getName() {
        return name;
    }

}
