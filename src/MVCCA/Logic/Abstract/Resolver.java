package MVCCA.Logic.Abstract;

/**
 * Resolver interface to handle custom rulesets.
 * Both methods take integer representing number of neighbours and decide what to do
 * with that, and then return a number representing cell's new state.
 */
public interface Resolver {
    public abstract int ifDead(int n);
    public abstract int ifAlive(int n);
}
