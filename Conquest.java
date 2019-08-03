//import java.util.List;
//import java.util.ArrayList;
//import java.util.Scanner;

public class Conquest {

	public static enum Player {
		HUMAN,
	    CALLISTO
	}
	
	public static void main(String[] args) {
	    //int callistoLevel = 4;
	    User player = new User( Player.HUMAN );
	    User callisto = new User( Player.CALLISTO );
	    
	    player.addArmy( new Army() );
	    callisto.addArmy( new Army() );
	    
	    buildBoard( callisto );
	    
	    buildHeroBoard( player );
	    
	    //battle( player, callisto );
	    while( !player.getArmy(0).isDefeated() && !callisto.getArmy(0).isDefeated() ) {
	    	round( player, callisto );
	    }
	    player.battleEnd();
	}
	
	/* per round/turn ranged/melee battle system */
	public static void round( User player, User callisto ) {
		int phits, chits;

		callisto.getArmy(0).printBoard();

		if( !player.hasCheatCards() ) {
			player.drawCheat(4);
			player.setCheatCardsDrawn();
		}
		/*
		 * need to check different initiatvie levels
		 * for first strike and bard, etc
		 * */
		//each initiatve round for Ranged
		for( int k = 1; k < 10; k++ ) {
			//System.out.println( "Ranged Round " + k );
			phits = player.getArmy(0).getTotalHits( player, "Ranged", k );
			chits = callisto.getArmy(0).getTotalHits( callisto, "Ranged", k );
			if( phits != 0 || chits != 0 ) {
				System.out.format( "%15s | %15s%n", "Player", "Callisto" );
				System.out.format( "%10d hits | %10d hits%n", phits, chits );
			}

			//do special hits
			System.out.println( "DO SPECIAL HITS" );
			player.getArmy(0).doSpecialHits( callisto );

			player.getArmy(0).clearSpecialHits();

			if( chits != 0 ) player.getArmy(0).choosePlayerHits( chits );

			if( phits != 0 ) {
				//callisto.getArmy(0).printBoard();
				callisto.getArmy(0).assignCallistoHits( "Ranged", phits );
				callisto.getArmy(0).printBoard();
			}
			player.discardCurrentHand();
			callisto.discardCurrentHand();

			//System.out.println( "\nCHECK Death Method Ranged" );
			player.getArmy(0).checkDeath();
			callisto.getArmy(0).checkDeath();
			if( player.getArmy(0).isDefeated() || callisto.getArmy(0).isDefeated() ) {
				System.out.println("End of Battle Ranged " + k );
				return;
			}
		}

		//each initiative round for Melee
		for( int k = 1; k < 10; k++ ) {
			//System.out.println( "Melee Round " + k );
			phits = player.getArmy(0).getTotalHits( player, "Melee", k );
			chits = callisto.getArmy(0).getTotalHits( callisto, "Melee", k );
			if( phits != 0 || chits != 0 ) {
				System.out.format( "%15s | %15s%n", "Player", "Callisto" );
				System.out.format( "%10d hits | %10d hits%n", phits, chits );
			}
			if( chits != 0 ) player.getArmy(0).choosePlayerHits( chits );

			if( phits != 0 ) {
				//callisto.getArmy(0).printBoard();
				System.out.println("Player hits -> "+phits);
				callisto.getArmy(0).assignCallistoHits( "Melee", phits );
				callisto.getArmy(0).printBoard();
			}
			player.discardCurrentHand();
			callisto.discardCurrentHand();

			//System.out.println( "\nCHECK Death Method -> MELEE" );
			player.getArmy(0).checkDeath();
			callisto.getArmy(0).checkDeath();
			if( player.getArmy(0).isDefeated() || callisto.getArmy(0).isDefeated() ) {
				System.out.println("End of Battle Melee " + k );
				return;
			}
		}


		System.out.println();
		player.getArmy(0).printBoard();
		System.out.println();
		callisto.getArmy(0).printBoard();
		//NOTE: implement player healing here
		//      ask Russel about blocking
		//player.getArmy(0).checkDeath();
		//callisto.getArmy(0).checkDeath();

	}
	
	public static void buildBoard( User callisto ) {
		String token;

		for(int i = 0; i < (callisto.getCurrentLevel()+4); i++ ) {
			token = callisto.getTokenBag().draw();
			if( token.equals("r") ) callisto.addBase( 0, makeWolfRider() );
			else if( token.equals("g") ) callisto.addBase( 0, makeGoblinGrenader() );
			else if( token.equals("w") ) callisto.addBase( 0, makeSkeletalArcher() );
			else if( token.equals("b") ) callisto.addBase( 0, makeOgre() );
		}

		/*for(int i = 0; i < (callisto.getCurrentLevel()+4); i++ ) {
	      token = callisto.getTokenBag().draw();
	      if( token.equals("r") ) callisto.addBase( 0, makeOgre() );
	      else if( token.equals("g") ) callisto.addBase( 0, makeOgre() );
	      else if( token.equals("w") ) callisto.addBase( 0, makeOgre() );
	      else if( token.equals("b") ) callisto.addBase( 0, makeOgre() );
	    }*/
	}

	public static void buildHeroBoard( User p ) {

		for( int i = 0; i < 3; i++ ) {
			p.getArmy(0).addBase( makeSwordsman() );
		}

		for( int i = 0; i < 2; i++ ) {
			p.getArmy(0).addBase( makeArcher() );
		}

		Base fastArcher = new Base( "fast Archer", "Ranged", 1, 7 );
		fastArcher.addAbility( new Special( Card.Ranks.JACK, Card.Suits.NONE, "Sniper", "rank" ) );
		fastArcher.setInitiative(3);
		p.getArmy(0).addBase(fastArcher);
	}
	
	public static Base makeSkeletalArcher() {
		Base skeleton = new Base( "Skeletal Archer", "Ranged", 1, 7 );
		skeleton.addAbility( new Special(  Card.Ranks.JACK, Card.Suits.NONE, "Sniper", "rank" ) );
		return skeleton;
	}

	public static Base makeGoblinGrenader() {
		Base goblin = new Base( "Goblin Grenader", "Ranged", 1, 6 );
		goblin.addAbility( new Special( Card.Ranks.JACK, Card.Suits.NONE, "Explode", "rank" ) );
		return goblin;
	}

	public static Base makeWolfRider() {
		Base wolf = new Base( "Wolf-rider", "Melee", 1, 7 );
		//wolf.addAbility( new Special( Card.Ranks.JOKER, Card.Suits.NONE, "First Strike" ) );
		wolf.firstStrike();

		return wolf;
	}

	public static Base makeOgre() {
		Base ogre = new Base( "Ogre", "Melee", 1, 6 );
		ogre.setHP(2);
		ogre.addAbility( new Special( Card.Ranks.QUEEN, Card.Suits.NONE, "Double Damage", "rank") );
		ogre.addAbility( new Special( Card.Ranks.JOKER, Card.Suits.NONE, "Slow", "rank") );
		ogre.setInitiative(7);

		return ogre;
	}

	public static Base makeArcher() {
		Base archer = new Base( "Archer", "Ranged", 1, 7 );
		archer.addAbility( new Special( Card.Ranks.JACK, Card.Suits.NONE, "Sniper", "rank" ) );
		return archer;
	}

	public static Base makeSwordsman() {
		Base swordsman = new Base( "Swordsman", "Melee", 1, 6 );
		swordsman.addAbility( new Special( Card.Ranks.JACK, Card.Suits.NONE, "Parry", "rank" ) );
		swordsman.setHP( 2 );

		return swordsman;
	}
}
