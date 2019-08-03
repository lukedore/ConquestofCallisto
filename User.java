import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class User {
	private List<Army> armies;
	private List<Card> cheatHand, discardDeck;
	private List<CurrentHand> currenthand;
	private Deck playDeck;
	private TokenBag tb;
	private Conquest.Player type;
	private int callistoLevel = -1;
	private boolean drawncheatcards;

	public User( Conquest.Player gp ) {
		currenthand = new ArrayList<CurrentHand>();
		armies = new ArrayList<Army>();
		discardDeck = new ArrayList<Card>();
		type = gp;
		if( gp == Conquest.Player.HUMAN ) {
			cheatHand = new ArrayList<Card>();
			playDeck = new Deck( gp );
			tb = null;
			drawncheatcards = false;
		} else {
			cheatHand = null;
			playDeck = new Deck( gp );
			callistoLevel = 1;
			tb = new TokenBag();
		}
	}

	public void addDeck( Deck d ) {
		playDeck = d;
	}

	public Card draw() {
		Card temp = playDeck.draw();

		if( temp.getRank() == Card.Ranks.JOKER ) {
			System.out.println("JOKER");
			discardDeck.add(temp);
			reshuffle();
			temp = draw();
		} 
		return temp;
	}

	public void setCurrentCardType( Base b ) {
		currenthand.add( new CurrentHand( b ) );
		//System.out.println( "Adding Base -> " + b.toString() );
	}

	public void addFlipToCard( Base b, Card c ) {
		for( CurrentHand ch : currenthand ) {
			if( ch.getBase().equals(b) ) {
				//System.out.format("Found Base %s, adding card -> %s%n", b.toString(), c.toString() );
				ch.addCard(c);
			}
		}
	}

	public List<CurrentHand> getCurrentHand() {
		return currenthand;
	}

	public void drawCheat(int n) {
		Card temp;

		for(int i = 0; i< n; i++ ) {
			temp = playDeck.draw();
			if( temp.getRank() == Card.Ranks.JOKER ) {
				System.out.println("JOKER -> Reshuffle <-");
				discardDeck.add(temp);
				i--;
				reshuffle();
			} else {
				cheatHand.add(temp);
			}
		}
	}

	public void useCheats() {
		int response, cheatchoice, count = 1;
		Card cheat;

		while( !cheatHand.isEmpty() ) {
			Scanner in = new Scanner( System.in );
			count = 1;
			showCheatCards();
			showCurrentHand();
			System.out.println( "Choose a card to replace" );
			response = in.nextInt();

			if( response == 0 ) {
				in.close();
				return;
			}
				

			for( CurrentHand ch : currenthand ) {
				for( Card c : ch.getCards() ) {
					if( c.getRank() == Card.Ranks.KING && getType() == Conquest.Player.HUMAN ) {
						System.out.println( "Cannot cheat a played king" );
						count++;
						continue;
					}
					if( response == count ) {
						//System.out.println( "THIS CARD -> " + c.toString() );
						System.out.println("Which Cheat card: ");
						cheatchoice = in.nextInt();
						cheat = cheatHand.get(cheatchoice-1);
						//System.out.println("CHEAT CARD -> " + cheat.toString() );
						ch.replaceCard( c, cheat);
						if( cheat.getRank() == Card.Ranks.KING && getType() == Conquest.Player.HUMAN ) {
							System.out.println( "Cheat KING - > CHEAT" );
							drawCheat(1);
						} 
						cheatHand.remove(cheatchoice-1);
						response = 0;
					} else {
						count++;
					}
				}
			}
			in.close();
		}
		
	}

	public void showCurrentHand() {
		for( CurrentHand ch : currenthand ) {
			System.out.print( ch.toString() );
		}
	}

	public void reshuffle() {
		while( !discardDeck.isEmpty() ) {
			playDeck.addCard( discardDeck.remove(0) );
		}

		playDeck.shuffle();
	}

	public void addArmy( Army a ) {
		armies.add(a);
	}

	public Army getArmy( int n ) {
		return armies.get(0);
	}

	public void discardCurrentHand() {
		for( CurrentHand ch : currenthand ) {
			for( Card c : ch.getCards() ) {
				discardDeck.add(c);
			}
		}
		currenthand.clear();

	}
	public boolean addBase( int n, Base b ) {
		if( armies.get(n) == null ) return false;

		armies.get(n).addBase(b);

		return true;
	}

	public int getCurrentLevel() {
		return callistoLevel;
	}

	public TokenBag getTokenBag() {
		return tb;
	}

	public boolean hasCheatCards() {
		if( !drawncheatcards )
			return false;
		return true;
	}

	public void showCheatCards() {
		System.out.println( cheatHand.toString() );
	}

	public Conquest.Player getType() { return type; }

	public void setCheatCardsDrawn() {
		drawncheatcards = true;
	}

	public void battleEnd() {
		//remove wounded from army
		//unless cleric (not implemented)
		armies.get(0).removeWounded();
		armies.get(0).printBoard();
	}
}