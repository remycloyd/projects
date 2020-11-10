package acidBreaker;
import javax.swing.JFrame;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;


public class acidBrickGame extends JFrame
	{
		private static final long serialVersionUID = -1644365438665562583L;
		private BrickPanel panel;
		private MidisLoader midisLoader;
		static GraphicsDevice device = GraphicsEnvironment
		        .getLocalGraphicsEnvironment().getScreenDevices()[0];
		

		public acidBrickGame() 
		{	
			// load the background MIDI sequence
		    midisLoader = new MidisLoader();
		    midisLoader.load("br", "alice.mid");
		    midisLoader.play("br", true);   // repeatedly play it
			panel = new BrickPanel();
			this.setUndecorated(true);
			device.isFullScreenSupported();
			device.setFullScreenWindow(this);
			this.add(panel);
			this.setResizable(false);
			this.setTitle("Acid Brick Destroyer");
			this.add(panel);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(true);	
			this.pack(); // creates size based upon components
		}

		
		@SuppressWarnings("unused")
		public static void main(String[] args) 
		{
			 acidBrickGame ABG = new acidBrickGame();
			 
						  
			 
		}
	}


