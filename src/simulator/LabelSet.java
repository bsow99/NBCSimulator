package simulator;

public class LabelSet {
    public Label[] labels;
    public int aantalElementen;
    public int maxElementen;

    public LabelSet(int max){
        maxElementen = max;
        labels = new Label[max];
        aantalElementen = 0;

    }


    public void push(Label l){
        System.out.println("push("+l.getName()+")");
        labels[aantalElementen] = l;
        aantalElementen++;
    }

    public Label pop(){
        System.out.println("pop");
        aantalElementen--;
        return labels[aantalElementen];
    }

    public boolean isEmpty(){
        return aantalElementen == 0;
    }

    public int get(String naam){
        for(int i=0; i<aantalElementen; i++){
            Label label = labels[i];
            if(label.getName().equals(naam)){
                return label.getLineNum();
            }

        }
        return -1;


    }


   /* public void verwijderLoops(int regelBreak){
        System.out.println("verwijderLoops("+regelBreak+") aantalElementen = "+aantalElementen);
        while(!isEmpty()){

            Label label = pop();
            System.out.println("pop() ==>"+label.getName());
            System.out.println("label.end("+label.end+") >=" + "regelBreak("+regelBreak+")");
            if(label.end >= regelBreak){
                System.out.println("\tpush");
                push(label);
                return;
            }

        }



    }*/

}
