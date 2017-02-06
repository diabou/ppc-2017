package definition;

import search.Solution;
import search.Tools;

import java.util.ArrayList;


public class Csp {

	private Variable[] vars; // l'ensemble des variables du CSP. Note: les domaines sont connus au travers des variables
	private Constraint[] cons; // l'ensemble des contraintes du CSP

	public Csp(Variable[] vars, Constraint[] cons) {
		this.vars = vars;
		this.cons = cons;
	}

    public Csp(Variable[] vars) {
        this(vars, new Constraint[]{});
    }

	public Variable[] getVars() {
		return vars;
	}

	public int getNbVars() {
		return getVars().length;
	}

	public Constraint[] getConstraints() {
		return this.cons;
	}

	// retourne la premiere variable non instanciee du csp
	public Variable randomVar() {
		for (Variable v : this.vars) if (!v.isInstantiated()) return v;
		return null;
	}

    public Variable smallestVar() {
        Variable smallest = new Variable("biggest", -1, 0, 99999);
        for (Variable v : this.vars)
            if (!v.isInstantiated()
                    && v.getDomainSize() < smallest.getDomainSize())
                smallest = v;
        return smallest;
    }


    public Variable smallestRatio() {
        Variable smallest = new Variable("biggest", -1, 0, 99999);
        for (Variable v : this.vars)
            if (!v.isInstantiated()
                    && ratio(v) < ratio(smallest))
                smallest = v;
        return smallest;
    }

    public double ratio(Variable var) {
        return var.getDomainSize() / ((double) getNbConstraints(var));
    }


    public Domain[] cloneDomains() {
        int nbVars = getNbVars();
        Domain[] rep = new Domain[nbVars];
        for (int i = 0; i < nbVars; i++) rep[i] = getVars()[i].getDomain().clone();
        return rep;
    }

	/**
	 * @return true ssi toutes les variables sont instanciees.
	 */
	public boolean allInstanciated() {
		for (Variable v : this.vars) if (!v.isInstantiated()) return false;
		return true;
	}

	/**
	 * @return true vrai l'ensemble des contraintes du CSP est verifi�.
	 */
	public boolean hasSolution() {
		for (Constraint c : getConstraints()) if (!c.isSatisfied()) return false;
		return allInstanciated();
	}

	public Variable[] instanciated() {
		ArrayList<Variable> vars = new ArrayList<Variable>();
		for (Variable v : vars) if (v.isInstantiated()) vars.add(v);
		Variable[] rep = (Variable[]) vars.toArray();
		return rep;
	}

    public int getNbConstraints(Variable var) {
        return getConstraintsAsArray(var).length;
    }

	/**
	 * @param var
     * @return un array contenant les constraintes du Csp concernées par var.
     */
    public Constraint[] getConstraintsAsArray(Variable var) {
        return Tools.toArray(getConstraintsAsArrayList(var));
    }

    public ArrayList<Constraint> getConstraintsAsArrayList(Variable var) {
        ArrayList<Constraint> rep = new ArrayList<Constraint>();
		for (Constraint c : getConstraints()) {
			Variable[] vars = c.getVars();
			for (Variable v : vars) if (v.equals(var)) rep.add(c);
		}
		return rep;
	}

	public boolean satisfied(Constraint[] cons) {
		for (Constraint c : cons)
			if (c.areInstanciated() && !c.isSatisfied())
				return false;
		return true;
	}

	public boolean necessary(Constraint[] cons) {
		for (Constraint c : cons)
			if (!c.isNecessary())
				return false;
		return true;
	}

	public String toString() {
		String s = "---Variables : \n";
		for (Variable v : this.vars) s += v + "\n";
		s += "---Contraintes \n";
		for (Constraint c : this.cons) s += c + "\n";
		return s;
	}

	public Solution solution() {
		return new Solution(this.vars);
	}
}