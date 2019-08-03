import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;

public class Army {
	private Base[][] board = new Base[4][4];
	private List<Base> orderRanged, orderMelee;
	boolean defeated;
	  
	public Army() {
		initBoard();
		defeated = false;
	}
	  
	public void initBoard() {
		for( int i = 0; i < 4; i++ ) {
			for( int j = 0; j < 4; j++ ) {
				board[i][j] = new Base();
			}
		}
	}
	  
	public boolean addBase( Base b ) {
		if( b.getType().equalsIgnoreCase("Melee") ) {
			for( int i = 0; i < 2; i++ ) {
				for( int j = 0; j < 4; j++ ) {
					if( board[i][j].getName().equals("empty") ) {
						board[i][j] = b;
						board[i][j].setBoardPosition( i, j );
						return true;
					}
				}
			}
			return false;
		} else {
			for( int i = 2; i < 4; i++ ) {
				for( int j = 0; j < 4; j++ ) {
					if( board[i][j].getName().equals("empty") ) {
						board[i][j] = b;
						return true;
					}
				}
			}
			return false;
		}
	}

	public int getTotalHits( User user, String type, int init ) {
		List<Base> minions = new ArrayList<Base>();
		Card temp;
		int hits = 0, flipvalue = 0;

		minions = getMinions( type, init );

		if( minions.size() == 0 ) return 0;

		for( Base pb : minions ) {
			user.setCurrentCardType( pb );
			for( int k = 0; k < pb.getFlips(); k++ ) {
				temp = user.draw();
				if( temp.getRank() == Card.Ranks.KING && user.getType() == Conquest.Player.HUMAN ) {
					//System.out.println( "Flipped KING - > CHEAT" );
					user.drawCheat(1);
				}
				user.addFlipToCard( pb, temp );
			}
		}

		//System.out.println( "SIZE -> " + user.getCurrentHand().size() );
		//System.out.println( "Play Cheat cards" );
		if( user.getType() == Conquest.Player.HUMAN ) {
			user.useCheats();
		}


		for( CurrentHand ch : user.getCurrentHand() ) {
			Base pb = ch.getBase();
			for( Card c : ch.getCards() ) {
				flipvalue = c.getRank().ordinal()+1;
				if( flipvalue >= pb.getHit() && flipvalue <= 10 ) {
					System.out.println( "hit -> " + flipvalue + " > " + pb.getHit() );
					hits++;

					//doesn't need to be only HUMAN players
					//Change this when it works
				} else if( c.getRank() == Card.Ranks.JACK && user.getType() == Conquest.Player.HUMAN ) {
					Special special = pb.checkSpecials( c );
					if( special != null ) {
						pb.addSpecialHit( special );
					}
				} else if( c.getRank() == Card.Ranks.QUEEN && user.getType() == Conquest.Player.HUMAN ) {
					System.out.println( "QUEEN" );
				} else {
					//System.out.println( "miss -> " + flipvalue + " > " + pb.getHit() );
				}
			}
		}
		return hits;
	}

	private ArrayList<Base> getMinions( String type, int init ) {
		ArrayList<Base> group = new ArrayList<Base>();
		int start = 0, end = 2;

		if( type.equals( "Ranged" ) ) {
			start = 2;
			end = 4;
		}

		for( int i = start; i < end; i++ ) {
			for( int j = 0; j < 4; j++ ) {
				if( !board[i][j].getName().equals("empty") && board[i][j].getInitiative() == init ) {
					group.add(board[i][j]);
				}
			}
		}

		Collections.sort(group, new Comparator<Base>() {
			@Override 
			public int compare(Base init1, Base init2) {
				return init1.getInitiative() - init2.getInitiative(); // Ascending
			}
		});

		return group;
	}

	public void choosePlayerHits( int hits ) {
		Scanner input = new Scanner( System.in );
		int response, i, j;

		for( int h = 0; h < hits; h++ ) {

			printBoard();
			response = input.nextInt();
			if( response > 12 ) {
				i = 3;
				j = response - 12 -1;
			} else if( response > 8 ) {
				i = 2;
				j = response - 8 - 1;
			} else if( response > 4 ) {
				i = 1;
				j = response -4 -1;
			} else {
				i = 0;
				j = response - 1;
			}

			//player.getArmy(0).removeUnit(i,j);
			wound(i,j);
			printBoard();
		}
		input.close();
	}

	public void assignCallistoHits( String type, int hits ) {
		List<Base> order;

		if( type.equals("Ranged" ) ) {
			createOrderRanged();

			for( int i = 0; i < hits; i++ ) {
				order = getOrderRanged();
				Base b = order.get(0);
				b.setWounded();
				nextWoundedRanged();
			}
		} else if( type.equals( "Melee") ) {
			createOrderMelee();

			for( int i = 0; i < hits; i++ ) {
				order = getOrderMelee();
				Base b = order.get(0);
				b.setWounded();
				nextWoundedMelee();
			}
		}
	}

	public void wound( int i, int j ) {
		board[i][j].setWounded();
	}

	private void createOrderRanged() {
		List<Base> temp, temp2;
		orderRanged = new ArrayList<Base>();
		temp = new ArrayList<Base>();
		temp2 = new ArrayList<Base>();

		for( int i = 0; i < 4; i++ ) {
			for(int j = 0; j < 4; j++ ) {
				if( !board[i][j].getName().equals("empty") && board[i][j].getHP() > 0 ) {
					temp.add(board[i][j]);
				}
			}
		}

		for( Base b : temp ) {
			if( b.getName().equals("Ogre") && !b.isWounded() )
				orderRanged.add(b);
			else
				temp2.add(b);
		}

		temp = new ArrayList<Base>(temp2);
		temp2.clear();

		for( Base b : temp ) {
			if( b.getName().equals("Skeletal Archer") )
				orderRanged.add(b);
			else
				temp2.add(b);
		}

		temp = new ArrayList<Base>(temp2);
		temp2.clear();

		for( Base b : temp ) {
			if( b.getName().equals("Goblin Grenader") )
				orderRanged.add(b);
			else
				temp2.add(b);
		}

		temp = new ArrayList<Base>(temp2);
		temp2.clear();

		for( Base b : temp ) {
			if( b.getName().equals("Ogre") )
				orderRanged.add(b);
			else
				temp2.add(b);
		}

		temp = new ArrayList<Base>(temp2);
		temp2.clear();

		for( Base b : temp ) {
			if( b.getName().equals("Wolf-rider") )
				orderRanged.add(b);
			else
				temp2.add(b);
		}
	}

	private void createOrderMelee() {
		List<Base> temp, temp2;
		orderMelee = new ArrayList<Base>();
		temp = new ArrayList<Base>();
		temp2 = new ArrayList<Base>();

		for( int i = 0; i < 4; i++ ) {
			for(int j = 0; j < 4; j++ ) {
				if( !board[i][j].getName().equals("empty") && board[i][j].getHP() > 0 ) {
					temp.add(board[i][j]);
				}
			}
		}

		for( Base b : temp ) {
			if( b.getName().equals("Ogre") && !b.isWounded() )
				orderMelee.add(b);
			else
				temp2.add(b);
		}

		temp = new ArrayList<Base>(temp2);
		temp2.clear();

		for( Base b : temp ) {
			if( b.getName().equals("Wolf-rider") )
				orderMelee.add(b);
			else
				temp2.add(b);
		}

		temp = new ArrayList<Base>(temp2);
		temp2.clear();

		for( Base b : temp ) {
			if( b.getName().equals("Skeletal Archer") )
				orderMelee.add(b);
			else
				temp2.add(b);
		}

		temp = new ArrayList<Base>(temp2);
		temp2.clear();

		for( Base b : temp ) {
			if( b.getName().equals("Goblin Grenader") )
				orderMelee.add(b);
			else
				temp2.add(b);
		}

		temp = new ArrayList<Base>(temp2);
		temp2.clear();

		for( Base b : temp ) {
			if( b.getName().equals("Ogre") )
				orderMelee.add(b);
			else
				temp2.add(b);
		}
	}


	private List<Base> getOrderRanged() {
		return orderRanged;
	}

	private List<Base> getOrderMelee() {
		return orderMelee;
	}

	private void nextWoundedRanged() {
		Base b = orderRanged.get(0);
		orderRanged.remove(0);
		orderRanged.add(b);
	}

	private void nextWoundedMelee() {
		Base b = orderMelee.get(0);
		orderMelee.remove(0);
		orderMelee.add(b);
	}

	public void checkDeath() {
		int count = 0;
		for( int i = 0; i < 4; i++ ) {
			for(int j = 0; j < 4; j++ ) {
				if( !(board[i][j].getName()).equals("empty") && board[i][j].getHP() == 0 ) {
					board[i][j] = new Base();
					count++;
				} else if( board[i][j].getName().equals("empty") ) {
					count++;
				}
			}
		}
		if( count == 16 ) {
			defeated = true;
		}
	}

	public void removeWounded() {
		for( int i = 0; i < 4; i++ ) {
			for( int j = 0; j < 4; j++ ) {
				if( board[i][j].isWounded() ) {
					System.out.println( board[i][j].getName() + " is wounded. HP = " + board[i][j].getHP() );
					board[i][j] = new Base();
				}
			}
		}
	}

	public void doSpecialHits( User user ) {
		for( int i = 0; i < 4; i++ ) {
			for( int j = 0; j < 4; j++ ) {
				if( !board[i][j].getName().equals("empty") ) {
					List<Special> specials = board[i][j].getSpecialHits();
					System.out.println( "SPECIAL SIZE -> " + specials.size() );
					if( specials.size() > 0 ) {
						System.out.println( board[i][j].getName() + " has SPECIAL hit => " + specials.get(0).getName() );
						switch( specials.get(0).getName() ) {
						case "Sniper":
							//do sniper
							System.out.format( "%20s%n", "SNIPER" );
							sniper( user.getArmy(0) );
							break;
						case "Parry":
							//do parry
							System.out.println( "PARRY" );
							break;
						default:
							System.out.println( "Not implemented yet");
						}
					}
				}
			}
		}
	}

	private void sniper( Army army ) {    
		Scanner input = new Scanner( System.in );
		int response, i, j;

		army.printBoard();

		response = input.nextInt();
		if( response > 12 ) {
			i = 3;
			j = response - 12 -1;
		} else if( response > 8 ) {
			i = 2;
			j = response - 8 - 1;
		} else if( response > 4 ) {
			i = 1;
			j = response -4 -1;
		} else {
			i = 0;
			j = response - 1;
		}

		//player.getArmy(0).removeUnit(i,j);
		army.wound(i,j);
		army.printBoard();
		input.close();
	}

	public void clearSpecialHits() {
		for( int i = 0; i < 4; i++ ) {
			for( int j = 0; j < 4; j++ ) {
				if( !board[i][j].getName().equals( "empty" ) ) {
					board[i][j].clearSpecialHits();
				}
			}
		}
	}




	public boolean isDefeated() {
		//System.out.println( "Defeated -> " + defeated );
		return defeated;
	}

	public void printBoard() {
		for( int i = 0; i < 4; i++ ) {
			for( int j = 0; j < 3; j++ ) {
				System.out.format( "%18s%1d |", board[i][j].getName(), board[i][j].getHP() );
				//System.out.print( board[i][j].getName() + " | ");
			}
			System.out.format( "%18s%1d%n", board[i][3].getName(), board[i][3].getHP() );
			//System.out.println( board[i][3].getName() );
		}
		System.out.println();
	}

}
