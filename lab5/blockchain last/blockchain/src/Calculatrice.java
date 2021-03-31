

public class Calculatrice implements java.io.Serializable  {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public double a;
	public double b;
	public int op;

	public Calculatrice(double a, double b,int op) {
		this.a = a;
		this.b = b;
		this.op = op;
	}

	
	public double mul(double a,double b){
		return a*b;
	}

	public double div(double a,double b) {
		if (b==0) {
			throw new IllegalArgumentException("Cant divide by 0");
		}
	return a/b;
	}

	public double add(double a,double b){
		return a+b;
	}

	public double sub(double a,double b){
		return a-b;
	}
}
