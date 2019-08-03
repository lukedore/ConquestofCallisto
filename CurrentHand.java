import java.util.List;
import java.util.ArrayList;

public class CurrentHand {
	private Base base;
	private List<Card> cards;

	public CurrentHand( Base b ) {
		base = b;
		cards = new ArrayList<Card>();
	}

	public void addCard( Card c ) {
		cards.add( c );
	}

	public Base getBase() {
		return base;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void replaceCard( Card oldcard, Card newcard ) {
		if( cards.remove( oldcard ) ) {
			System.out.println( "Removed" );
		}
		cards.add( newcard );
	}

	public String toString() {
		String s = String.format( "%-15s%n", base.toString() );
		for( Card c : cards ) {
			s += String.format( "%25s%n", c.toString() );
		}
		return s;
	}

}