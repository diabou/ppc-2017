package definition;

import static definition.factories.ConstraintFactory.DIFF;

public class ConstraintDiff extends BinaryConstraint {

    public ConstraintDiff(Variable v1, Variable v2) {
        super(new Variable[]{v1, v2}, DIFF);
    }

    @Override
    public boolean isSatisfied() {
        return !areInstantiated() || getVars()[0].getValue() != getVars()[1].getValue();
    }

    @Override
    public boolean isNecessary() {
        return isSatisfied();
    }

    @Override
    public boolean[] filter() {
        boolean rep1 = false;
        boolean rep2 = false;
        Variable v1 = getVars()[0];
        Variable v2 = getVars()[1];
        Domain d1 = v1.getDomain();
        Domain d2 = v2.getDomain();
        if (v1.isInstantiated()) rep2 = d2.remove(v1.getValue());
        else if (v2.isInstantiated()) rep1 = d1.remove(v2.getValue());
        return new boolean[]{(d1.size() == 0 || d2.size() == 0), rep1, rep2};
    }
}
