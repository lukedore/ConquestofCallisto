import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	List<Card> cards = new ArrayList<Card>();

	public Deck( Conquest.Player p ) {
		buildDeck();

		if( p == Conquest.Player.HUMAN ) 
			addJoker();

		shuffle();
	}

	public void shuffle() {
		Collections.shuffle(this.cards);
	}

	public String toString() {
		return cards.toString();
	}

	public Card draw() {
		return cards.remove(0);
	}

	private void buildDeck() {
		for( Card.Suits s : Card.Suits.values() ) {
			for( Card.Ranks r : Card.Ranks.values() ) {
				if( s == Card.Suits.NONE || r == Card.Ranks.JOKER ) {
					continue;
				}
				Card c = new Card( s, r);
				cards.add( c );
			}
		}
	}

	public void addCard( Card c ) {
		cards.add( c );
	}

	private void addJoker() {
		cards.add( new Card( Card.Suits.NONE, Card.Ranks.JOKER ) );
	}

}