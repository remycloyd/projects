
// PlayerSprite.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Represents a player as a subclass of TiledSprite.

   A player can pick things up. When it has picked up 
   everything, it has 'won'.

   If a player is 'hit' by an alien, it loses a life.
   When all of its lives are gone, the palyer has 'lost'.

   The player communicates with the WorldDisplay object 
   to find out about the world (e.g. if there is something
   to pick up on the current tile).

   Sound effects are attached to:
     * picking something up
     * failing to pick something up
     * being hit by an alien
     * hitting a no-go tile or block
*/

import image.ImagesLoader;

import java.awt.Point;

import sound.ClipsLoader;


public class PlayerSprite extends TiledSprite
{
  // max number of times it can be hit before the game ends
  private final static int MAX_HITS = 3;

  private ClipsLoader clipsLoader;   // for playing audio effects
  private AlienTilesPanel atPanel;

  private int hitCount = 0;


  public PlayerSprite(int x, int y, int w, int h, 
                                ClipsLoader clipsLd, ImagesLoader imsLd, 
                                WorldDisplay wd, AlienTilesPanel atp)
  { super(x, y, w, h, imsLd, "still", wd);
    clipsLoader = clipsLd;
    atPanel = atp;
  } // end of PlayerSprite()


  public boolean tryPickup()
  /* The user requests that the player sprite tries to pick up
     something from its current tile position.
  */
  {
    String pickupName;
    // System.out.println("pickup: " + getTileLoc() );
    if ((pickupName = world.overPickup( getTileLoc())) == null) {
      clipsLoader.play("noPickup", false);     // nothing to pickup
      return false;
    }
    else {     // found a pickup
      clipsLoader.play("gotPickup", false);
      world.removePickup(pickupName);    // tell WorldDisplay
      return true;
    }
  }


  // ----------------- alien hit related methods ---------------


  public void hitByAlien()
  /* WorldDisplay tells the player that it's been hit.
     If the player's been hit enough times, tell the AlienTilesPanel
     that the game is over. */
  { clipsLoader.play("hit", false);
    hitCount++;
    if (hitCount == MAX_HITS)    // player is dead
      atPanel.gameOver();
  } // end of hitByAlien()


  public String getHitStatus()
  // AlienTilesPanel uses this method to report on the player's status
  {  
    int livesLeft = MAX_HITS-hitCount;
    if (livesLeft <= 0)
      return "You're D*E*A*D";
    else if (livesLeft == 1)
      return "1 life left";
    else
      return  "" + livesLeft + " lives left";  
  } // end of getHitStatus()



  // ----------------- movement methods ---------------------------

  public void move(int quad)
  /*  Try to move in the specified quadrant direction.
      If it's not possible then stop moving (and be slapped).
      If the move is possible, then update the sprite's tile
      location, its image, and tell WorldDisplay that it
      has move.
  */
  {
    Point newPt = tryMove(quad);
    if (newPt == null) {   // move not possible
      clipsLoader.play("slap", false);
      standStill();
    }
    else {    // move is possible
      setTileLoc(newPt);    // update the sprite's tile location
      if (quad == NE)
        setImage("ne");
      else if (quad == SE)
        setImage("se");
      else if (quad == SW)
        setImage("sw");
      else // quad == NW
        setImage("nw");
      world.playerHasMoved(newPt, quad);
    }
  }  // end of move()


  public void standStill()
  {  setImage("still");    }

}  // end of PlayerSprite class