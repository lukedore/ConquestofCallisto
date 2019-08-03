import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class TokenBag {
	private List<String> bag;
	  
	public TokenBag() {
		bag = new ArrayList<String>();
		fillBag();
		shuffle();
	}
	  
	private void fillBag() {
		for( int i = 0; i < 10; i++ ) {
			bag.add("r");
			bag.add("g");
			bag.add("b");
			bag.add("w");
		}
	}
  
	private void shuffle() {
		Collections.shuffle( bag );
	}
  
	public String draw() {
		return bag.remove(0);
	}
  
	public void replace( String s ) {
		for( int i = 0; i < 10; i++ ) {
			bag.add( s );
		}
		shuffle();
	}
  
	public int count() {
		return bag.size();
	}
}
