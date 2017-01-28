package definition;

public abstract class Constraint {
	private Variable[] variables = new Variable[2];
	
	public Constraint (Variable[] vars){
		variables = vars;
	}
	
	/**
	 * @return les variables de la contrainte.
	 */
	public Variable[] getVars() {
		return variables;
	}
	
	/**
	 * 
	 * @return vrai si toutes les variables de la contrainte sont instanci�es. 
	 */
	public boolean areInstanciated(){
		for (Variable v : getVars()) 
			if (!v.isInstantiated()) return false;
		return true;
	}

	/**
	 * @return vrai ssi toutes les variables de la contrainte sont 
	 * instanciees et la contrainte est verifiee
	 */
	public abstract boolean isSatisfied();
	
	/**
	 * Une condition necessaire a la satisfaction de la contrainte
	 * @return vrai ssi il existe encore un tuple satisfaisant la contrainte
	 */
	public abstract boolean isNecessary();
	
	/**
	 * Modifie les domaines des variables de sorte � enlever les valeurs ne pouvant satisfaire
	 * la contrainte
	 * @return Un array de trois boolean. Le premier vaut true si un domaine a �t� vid�,
	 * le deuxi�me vaut true si le domaine de la premi�re variable a chang�, le troisi�me 
	 * vaut true si le domaine de la seconde variable a chang�. 
	 */
	public abstract boolean[] filter();
	
	public abstract String toString();

}