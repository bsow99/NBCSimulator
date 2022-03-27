package simulator;

import java.util.ArrayList;
import java.util.List;

public class InstructionSet {
    public List<Instruction> instructions;


    public InstructionSet(){
        instructions = new ArrayList<>();
    }


    public void push(Instruction inst){
        System.out.println("push("+inst.getLine()+")");
        instructions.add(inst);
    }

    public Instruction pop(){
        System.out.println("pop ");
        Instruction first = instructions.get(0);
        System.out.println("pop "+first.getLine());
        instructions.remove(0);
        return first;
    }

    public boolean isEmpty(){
        return instructions.isEmpty();
    }

}
