public class Test {
	public int z;

	public Test(int i){this.z=i+2;}

	public static void main(String[] args) {
		System.out.println(new Test(5).z);
	}
}
