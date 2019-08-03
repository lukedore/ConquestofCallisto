
public class Special {
	private Card.Ranks rank;
	private Card.Suits suit;
	private String name;
	private boolean suitrequired, rankrequired, bothrequired;


	//add enum of all special names

	public Special( Card.Ranks r, Card.Suits s, String n, String type ) {
		rank = r;
		suit = s;
		name = n;
		if( type.equals( "suit" ) ) {
			suitrequired = true;
		} else if( type.equals( "rank" ) ) {
			rankrequired = true;
		} else if( type.equals( "both" ) ) {
			bothrequired = true;
		} 
	}

	public void setRankRequired() {
		rankrequired = true;
	}

	public boolean isRankRequired() {
		return rankrequired;
	}

	public void setSuitRequired() {
		suitrequired = true;
	}

	public boolean isSuitRequired() {
		return suitrequired;
	}

	public boolean isBothRequired() {
		return bothrequired;
	}

	public Card.Ranks getRank() {
		return rank;
	}

	public boolean equals( Card.Ranks r ) {
		return rank == r;
	}

	public String getName() {
		return name;
	}


	public String toString() {
		return rank + "/" + suit + " --> " + name;
	}
}
