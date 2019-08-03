import java.util.ArrayList;
import java.util.List;

public class Base {
	private int hit, flips, hp, initiative;
	private int i, j;
	private String name;
	private String type;
	private List<Special> abilities;
	private List<Special> specialhits;
	private List<Card> cards;
	private boolean wounded;
  
	public Base() {
		name = "empty";
	}
	
	public Base( String n, String t, int f, int h ) {
	    name = n;
	    type = t;
	    hit = h;
	    flips = f;
	    abilities = new ArrayList<Special>();
	    specialhits = new ArrayList<Special>();
	    cards = new ArrayList<Card>();
	    wounded = false;
	    hp = 1;
	    initiative = 5;
	}
	
	public void addCard( Card c ) {
		cards.add(c);
	}
	
	public void clearCards() {
		cards = new ArrayList<Card>();
	}
	  
	public void slow() {
		initiative = 7;
	}
  
	public void setBoardPosition( int x, int y ) {
		i = x;
		j = y;
	}
  
	public int getI() {
		return i;
	}
  
	public int getJ() {
		return j;
	}
  
	public void firstStrike() {
		initiative = 3;
	}
  
	public void setHP( int h ) {
		hp = h;
	}
  
	public void hit() {
		if( hp > 0 )
			hp--;
	}
  
	public boolean isWounded() {
		return wounded;
	}
  
	public void setWounded() {
		hit();
		wounded = true;
	}
  
	public void heal() {
		wounded = false;
		hp++;
	}
  
	public int getHP() {
		return hp;
	}
  
	public String getName() { return name; }
  
	public String getType() { return type; }
  
	public void updateHit( int h ) {
		hit = h;
	}
  
	public int getHit() { return hit; }
  
	public void updateFlips( int f ) {
		flips = f;
	}
  
	public int getFlips() { return flips; }
  
	public void addAbility( Special s ) {
		abilities.add( s );
	}
  
	public int getInitiative() { return initiative; }
  
	public void setInitiative( int n ) {
		initiative = n;
	}
  
	public Special checkSpecials( Card card ) {
		System.out.println( "CHECK SPECIALS" );

		if( abilities.size() == 0 )
			return null;
		
		for( Special special : abilities ) {
			if( special.isBothRequired() ) {
				System.out.println( "Special requires both rank and suit" );
			}
			else if( special.isRankRequired() ) {
				System.out.println( "Rank required -> " + special.getRank() );
				System.out.println( special.toString() );
			//check flips vs special
				if( special.equals( card.getRank() ) ) {
					System.out.println( "USE Special -> " + special.toString() );
					return special;
				}
			}
		}
		return null;
  }
  
	public void addSpecialHit( Special s ) {
		specialhits.add(s);
	}
  
	public void clearSpecialHits() {
		specialhits.clear();
	}
  
	public List<Special> getSpecialHits() {
		return specialhits;
	}
  

	public String toString() {
    /*	String s = name + ", " + type + ", " + hit + ", " + flips + ": ";
		for( Special spec : abilities ) {
  		s += spec.toString() + ", ";
	}*/
    
    String s = name;
    
    return s;
  }
  
}
