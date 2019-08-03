import java.io.File;

public class Card {
	public static enum Suits {
		CLUBS,
		HEARTS,
		DIAMONDS,
		SPADES,
		NONE;
	}

	public static enum Ranks {
		ACE,
		TWO,
		THREE,
		FOUR,
		FIVE,
		SIX,
		SEVEN,
		EIGHT,
		NINE,
		TEN,
		JACK,
		QUEEN,
		KING,
		JOKER;
	}

	Suits suit;
	Ranks rank;
	String value;
	File img;

	public Card() {

		suit = Suits.NONE;
		rank = Ranks.JOKER;
	}

	public Card( Suits s, Ranks r ) {
		setSuit( s );
		setValue( r );
	}

	public void setSuit( Suits s ) {
		suit = s;
	}

	public void setValue( Ranks r ) {
		rank = r;
	}

	public Ranks getRank() {
		return rank;
	}

	public String toString() {
		return suit + "  " + rank;
	}
}
