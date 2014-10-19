package MVCCA.Logic;

/**
 * Abstract class for automaton logic
 */
public abstract class Logic {
    private String logicName;

    public String getName(){
        return logicName;
    }

    public void setLogicName(String s){
        logicName = s;
    }
}
