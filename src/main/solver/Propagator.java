package solver;

import definition.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;

class Propagator {
    private Csp csp;
    private Variable currentNode;
    private Domain[] savedDomains;
    private Constraint currentConstraint;
    private boolean[] currentFilter;

    /**
     * Vaut true si l'un des domaines a été vidé.
     */
    private boolean arcsAreConsistant;

    /**
     * Un tableau de nbVariables boolean. L'élément i vaut true ssi son domaine a
     * changé lors de la propagation.
     */
    private IterableBitSet changedDomains;

    /**
     * Contient les contraintes que la propagation du filtrage va activer.
     * On active une contrainte quand le domaine d'une de ses variables change.
     */
    private Queue<Constraint> activeConstraints;


    Propagator(Csp csp, Variable currentNode) {
        this.csp = csp;
        this.currentNode = currentNode;
        savedDomains = csp.cloneDomains();
        activeConstraints = new LinkedList<>();
        arcsAreConsistant = true;
        changedDomains = new IterableBitSet();
    }


    boolean areArcsConsistent() {
        return arcsAreConsistant;
    }

    IterableBitSet changedDomains() {
        return changedDomains;
    }

    private boolean canStillPropagate() {
        return !activeConstraints.isEmpty();
    }

    private void setCurrentConstraint(Constraint cons) {
        currentConstraint = cons;
    }

    private void setCurrentFilter(boolean[] filter) {
        currentFilter = filter;
    }

    /**
     * Restore les domaines à leur état d'avant le filtrage.
     */
    void restoreDomains() {
        changedDomains.forEach(i -> csp.getVars()[i].setDomain(savedDomains[i]));
    }

    /**
     * Lance le filtrage du Csp avec pour point de départ la variable var. Et effectue la
     * propagation à travers les contraintes qui concernet les variables dont le domaine
     * a été réduit par un filtrage.
     */
    void propagateFromCurrentNode() {
        csp.relatedConstraints(currentNode).forEach(activeConstraints::add);

        while (canStillPropagate() && arcsAreConsistant)
            startPropagation();
    }

    private void startPropagation() {
        setCurrentConstraint(activeConstraints.poll());
        setCurrentFilter(currentConstraint.filter());

        if (currentFilter[0]) arcsAreConsistant = false;
        else propagate();
    }

    private void propagate() {
        IntStream.range(0, currentFilter.length)
            .filter(i -> currentFilter[i])
            .mapToObj(this::getAssociatedVariable)
            .forEach(this::addToPropagationQueue);
    }

    private Variable getAssociatedVariable(int i) {
        return currentConstraint.getVars()[i - 1];
    }

    private void addToPropagationQueue(Variable var) {
        changedDomains.addValue(var.getInd());
        addActivatedConstraints(var);
    }

    private void addActivatedConstraints(Variable modifiedVariable) {
        csp.relatedConstraints(modifiedVariable)
            .filter(this::hasJustBeenModified)
            .forEach(activeConstraints::add);
    }

    private boolean hasJustBeenModified(Constraint c) {
        return !c.equals(currentConstraint) && !activeConstraints.contains(c);
    }
}
