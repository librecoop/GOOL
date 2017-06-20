public class Complex {
	
	private double re;
	private double im;
	
	public Complex(double re, double im){
		this.re = re;
		this.im = im;
	}
	
	//Accessors without mutator
	public double realPart(){return re;}
	public double imaginaryPart(){return im;}
	
	//Arithmetic operators
	public Complex add(Complex c){
		return new Complex(re + c.re, im + c.im);
	}
	
	public Complex subtract(Complex c){
		return new Complex(re - c.re, im - c.im);
	}
	
	public Complex multiply(Complex c){
		return new Complex(re * c.re - im * c.im, re * c.im + im * c.re);
	}
	
	public Complex divide(Complex c){
		double tmp = c.re * c.re + c.im * c.im;
		return new Complex((re * c.re + im * c.im) / tmp,
				(im * c.re - re * c.im) / tmp);
	}

	//Conjugate
	public Complex conj(){
		return new Complex(re,-im);
	}

	public static void main(String[] args) {
		Complex c1 = new Complex(2.0,4.0);
		Complex c2 = new Complex(2.0,0.0);
		Complex c3 = c1.divide(c2);
		System.out.print("Division : ");
		System.out.print(c3.realPart());
		System.out.print(" + ");
		System.out.print(c3.imaginaryPart());
		System.out.println("j");
		if (c3.realPart() > 0.0){
		  System.out.println("real part is positive");
		}
		else{
		  System.out.println("real part is negative or null");
		}
	}

}
