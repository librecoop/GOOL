class Test {public static void main(String[] args){CinemaT p = new CinemaT("Nef","numSalle"); p.display();}}
class Cinema {protected String nomCine; public Cinema(String nomC){this.nomCine = nomC;} public void display(){System.out.println(this.nomCine);}}
class CinemaT extends Cinema {protected String numS; public CinemaT(String nomC, String num){super(nomC); this.numS = num;}}
