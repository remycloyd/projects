package br.com.appletpong.utils;

import java.applet.AudioClip;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class GameUtils {
	private static final String IMAGE_PATH = "/";
	private static final String SOUND_PATH = "file:///";

	public static Image getImage(String fileName) {
		Image image = null;
		try {
			image = ImageIO.read(new File(IMAGE_PATH + fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	public static URL getSoundURL() {
		URL url = null;
		try {
			url = new java.net.URL(SOUND_PATH);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}

}
