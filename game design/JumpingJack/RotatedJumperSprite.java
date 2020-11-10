import image.ImagesLoader;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import framework.BricksManager;


public class RotatedJumperSprite extends JumperSprite {

	
	private GraphicsConfiguration gc;
	private int angle = 0;
	
	public RotatedJumperSprite(int w, int h, int brickMvSz, BricksManager bm,
			ImagesLoader imsLd, int p) {
		super(w, h, brickMvSz, bm, imsLd, p);

		// get the GraphicsConfiguration so images can be copied easily and
		// efficiently
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		gc = ge.getDefaultScreenDevice().getDefaultConfiguration();

	
	}

	/**
	 * Create a new BufferedImage which is the input image, rotated angle
	 * degrees clockwise.
	 * 
	 * An issue is edge clipping. The simplest solution is to design the image
	 * with plenty of (transparent) border.
	 */
	public BufferedImage getRotatedImage(BufferedImage src, int angle) {
		if (src == null) {
			System.out.println("getRotatedImage: input image is null");
			return null;
		}

		int transparency = src.getColorModel().getTransparency();
		BufferedImage dest = gc.createCompatibleImage(src.getWidth(), src
				.getHeight(), transparency);
		Graphics2D g2d = dest.createGraphics();

		AffineTransform origAT = g2d.getTransform(); // save original transform

		// rotate the coord. system of the dest. image around its center
		AffineTransform rot = new AffineTransform();
		rot.rotate(Math.toRadians(angle), src.getWidth() / 2,
				src.getHeight() / 2);
		g2d.transform(rot);

		g2d.drawImage(src, 0, 0, null); // copy in the image

		g2d.setTransform(origAT); // restore original transform
		g2d.dispose();

		return dest;
	} // end of getRotatedImage()

	  public void drawSprite(Graphics g) 
	  {
		  angle = angle +1 % 360;
		  if (isActive()) {
	      if (image == null) {   // the sprite has no image
	        g.setColor(Color.yellow);   // draw a yellow circle instead
	        g.fillOval(locx, locy, SIZE, SIZE);
	        g.setColor(Color.black);
	      }
	      else {
	        if (isLooping)
	          image = player.getCurrentImage();
	        BufferedImage foo = getRotatedImage(image, angle);
	        g.drawImage(foo, locx, locy, null);
	      }
	    }
	  } // end of drawSprite()

}
