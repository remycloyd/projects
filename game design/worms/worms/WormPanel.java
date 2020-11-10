package worms;

/* The game's drawing surface. It shows:
 - the moving worm
 - the obstacles (blue boxes)
 - the current average FPS and UPS
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.text.DecimalFormat;

public class WormPanel extends GamePanel {

	private static final long serialVersionUID = 1825086766957972644L;

	private DecimalFormat df = new DecimalFormat("0.##"); // 2 dp

	private WormChase wcTop;
	private Worm fred; // the worm
	private Obstacles obs; // the obstacles

	// used at game termination
	private int score = 0;
	private Font font;
	private FontMetrics metrics;

	public WormPanel(WormChase wc, long period) {
		super(period);
		wcTop = wc;
	} // end of WormPanel()

	protected void mousePress(int x, int y)
	// is (x,y) near the head or should an obstacle be added?
	{
		if (!isPaused && !gameOver) {
			if (fred.nearHead(x, y)) { // was mouse press near the head?
				gameOver = true;
				score = (40 - timeSpentInGame) + (40 - obs.getNumObstacles());
				// hack together a score
			} else { // add an obstacle if possible
				if (!fred.touchedAt(x, y)) // was the worm's body untouched?
					obs.add(x, y);
				wcTop.setBoxNumber(obs.getNumObstacles()); // report new number
															// of boxes

			}
		}
	} // end of testPress()

	protected void simpleUpdate() {
		fred.move();
		wcTop.setTimeSpent(timeSpentInGame);

	} // end of gameUpdate()

	protected void simpleInitialize() {
		// create game components
		obs = new Obstacles();
		fred = new Worm(PWIDTH, PHEIGHT, obs);

		// set up message font
		font = new Font("SansSerif", Font.BOLD, 24);
		metrics = this.getFontMetrics(font);

	}

	protected void simpleRender(Graphics dbg) {
		dbg.setColor(Color.blue);
		dbg.setFont(font);

		// report frame count & average FPS and UPS at top left
		// dbg.drawString("Frame Count " + frameCount, 10, 25);
		dbg.drawString("Average FPS/UPS: " + df.format(averageFPS) + ", "
				+ df.format(averageUPS), 20, 25); // was (10,55)

		dbg.setColor(Color.black);

		// draw game elements: the obstacles and the worm
		obs.draw(dbg);
		fred.draw(dbg);

	}

	protected void gameOverMessage(Graphics g)
	// center the game-over message in the panel
	{
		String msg = "Game Over. Your Score: " + score;
		int x = (PWIDTH - metrics.stringWidth(msg)) / 2;
		int y = (PHEIGHT - metrics.getHeight()) / 2;
		g.setColor(Color.red);
		g.setFont(font);
		g.drawString(msg, x, y);
	} // end of gameOverMessage()

} // end of WormPanel class
