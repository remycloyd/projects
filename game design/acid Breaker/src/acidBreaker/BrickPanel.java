package acidBreaker;import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import javax.swing.JPanel;

public class BrickPanel extends JPanel implements MouseListener, MouseMotionListener, Runnable, ActionListener 
{

	private static final long serialVersionUID = -6327156264100888124L;
	private boolean gameOver, victory;
	private Paddle paddle;
	private int mouselocale;
	Thread thread;
	private Ball ball;
	private List<Brick> brix;
	private long startTime, stopTime;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width =  (int) screenSize.getWidth();
	int height = (int) screenSize.getHeight();
	private Timer timer;
	private int delay = 3;
	private ClipLoader ClipLoader;
	
	// off-screen rendering
    //private Graphics screen; 
    private Image screenImage = null;
	  
    // image and clip loader information files
    private static final String IMS_INFO = "imsInfo.txt";
    private static final String SNDS_FILE = "clipsinfo.txt";
    
    // holds the background image
    private BufferedImage bgImage = null;
	
	
	public BrickPanel() {
		super(null, true);
		
		this.setPreferredSize(new Dimension(width, height)); // enables JFrame to utilize frame size	
		System.out.println("Detected Screen with ideal play dimensions: "+ "\n" + width + " X " + height);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		timer = new Timer (delay, this);
		timer.start();
		gameOver = false;
		victory = false;
		ball = new Ball();
		paddle = new Paddle(ball);
		mouselocale = 0;
		this.createbrix();
		thread = new Thread(this);
		thread.start();
		// load the background image
	    imageLoader imsLoader = new imageLoader(IMS_INFO);
	    bgImage = imsLoader.getImage("background");
	    // initialise the clip loader
	    ClipLoader = new ClipLoader(SNDS_FILE);
	}// end of BrickPanel
	
	
	public void createbrix() {
		brix = new ArrayList<Brick>();
		double temp = 40;
		for (int i = 0; i < 8; i++) {
			brix.add(new Brick(ball, (int) temp));
			temp += Brick.width + 40;
		}
	}

	
	public void paint(Graphics screen) 
	{
		
		  if (screenImage == null){
			  screenImage = createImage(width, height);
		      if (screenImage == null) {
		        System.out.println("screenImage is null");
		        return;
		      }
		      else
		        screen = screenImage.getGraphics();
		    }

		// draw the bg: use the image or a black color
		if (bgImage==null) {
		screen.setColor(Color.black);
		screen.fillRect(0, 0, width, height); 
		}
		else {
			
			screen.drawImage(bgImage.getScaledInstance(width, height, 1), 0, 0,  this);}

		if (!gameOver) {
			// draw objects in game panel		
			paddle.draw(screen);
			ball.draw(screen);
			for (Brick b : brix) {
				b.draw(screen);
			}
		} else if (gameOver && !victory) {
			screen.setColor(Color.red);
			screen.setFont(new Font("Verdana", Font.BOLD, 32));
			screen.drawString("GAME OVER", (width/2)-200, height/3);
			screen.drawString("Click Here to Play Again", (width/2)-200, height/2);
		} 
		else 
		{
			ClipLoader.play("gameOver", true);
			screen.setColor(Color.white);
			double time = Math.round(stopTime - startTime) / 1000.0;
			screen.setFont(new Font("Verdana", Font.BOLD, 32));
			screen.drawString("YOU WON in " + time + " seconds.", width/2, height/3);			
			screen.drawString("Click Here to Play Again", (width/2)-200, height/2);
		}

	}
	
	

	public void run() {
		while (true) {
			paddle.move(mouselocale);
			ball.move();
			paddle.paddleballCollider(ClipLoader);
			for (int i = 0; i < brix.size(); i++) {
				if (brix.get(i).brickballCollider(ClipLoader)) {
					brix.remove(i);
					i--;
				}
			}
			checkGameOver();
			repaint();
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void checkGameOver() {
		if (!gameOver) {
			if (ball.getY() > 900) {
				gameOver = true;
				ball.sethztlSpeed(0);
				ball.setvertSpeed(0);
				stopTime = System.currentTimeMillis();
			}
			if (brix.isEmpty()) {
				stopTime = System.currentTimeMillis();
				victory = true;
				gameOver = true;
			}
		}

	}

	public void mouseClicked(MouseEvent mouse) {
		if (!ball.isMoving()) {
			ball.startMoving();
			startTime = System.currentTimeMillis();
		}
		if (gameOver) {
			ball = new Ball();
			paddle = new Paddle(ball);
			createbrix();
			gameOver = false;
			victory = false;
		}
	}

	
	public void mouseEntered(MouseEvent mouse) {

	}

	
	public void mouseExited(MouseEvent arg0) {
	}

	
	public void mousePressed(MouseEvent arg0) {

	}

	
	public void mouseReleased(MouseEvent arg0) {

	}

	
	public void mouseDragged(MouseEvent arg0) {
			
			

	}

	
	public void mouseMoved(MouseEvent mouse) {
		mouselocale = mouse.getX();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
} // end of BrickPanel Class