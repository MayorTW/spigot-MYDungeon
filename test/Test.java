import java.util.*;

class Test{
	public static void main (String[] args){
		List<Integer> a = new ArrayList<>();
		for(int i = 0; i<6; i++){
			a.add(i);
		}
		List<Integer> b = new ArrayList<>(a);
		System.out.println(a);
		System.out.println(b);
		
		a.add(6);
		System.out.println(a);
		System.out.println(b);
		
		b.remove(2);
		System.out.println(a);
		System.out.println(b);
	}
}