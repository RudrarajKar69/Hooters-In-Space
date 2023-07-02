package elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

import main.Scene;
import main.Starting;

public class Bullet extends GameObject {

	ArrayList<GameObject> enemies;
	public String posseser;
	Player p;

	public Bullet(int x, int y, int width, int height, ArrayList<Image> image, boolean alive, ID id, String posseser,
			Player p) {
		super(x, y, width, height, image, alive, id);
		this.posseser = posseser;
		this.p = p;
	}

	@Override
	public void draw(Graphics g) {

		if (posseser.equals("e")) {
			BufferedImage tintedImage = new BufferedImage(getImage().get(0).getWidth(null),
					getImage().get(0).getHeight(null), BufferedImage.TYPE_INT_ARGB);

			// Get the Graphics object for drawing on the tintedImage
			Graphics2D g2d = tintedImage.createGraphics();

			// Draw the original image onto the tintedImage
			g2d.drawImage(getImage().get(0), 0, 0, null);

			// Apply the green tint by manipulating the pixels
			int width = tintedImage.getWidth();
			int height = tintedImage.getHeight();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					// Get the pixel color at (x, y)
					int rgb = tintedImage.getRGB(x, y);
					Color color = new Color(rgb, true);

					// Apply the green tint by increasing the green component
					int red = Math.min(color.getRed() + 200, 255); // Increase green by 50 (adjust as desired)

					// Create a new color with the increased green component
					Color tintedColor = new Color(red, color.getGreen(), color.getBlue(), color.getAlpha());

					// Set the tinted color at (x, y)
					tintedImage.setRGB(x, y, tintedColor.getRGB());
				}
			}

			// Dispose the Graphics object
			g2d.dispose();

			BufferedImage rotatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2dRotated = rotatedImage.createGraphics();
			g2dRotated.rotate(Math.PI, width / 2, height / 2); // Rotate by 180 degrees
			g2dRotated.drawImage(tintedImage, 0, 0, null);
			g2dRotated.dispose();

			g.drawImage(rotatedImage, getX(), getY(), getWidth(), getHeight(), null);
		} else if (posseser.equals("p")) {
			if (getId() == ID.Pistol)
				g.drawImage(getImage().get(0), getX(), getY(), getWidth(), getHeight(), null);
			else if (getId() == ID.Machine_Gun)
				g.drawImage(getImage().get(1), getX(), getY(), getWidth(), getHeight(), null);
		}

	}

	@Override
	public void update() {
		if (posseser.equals("p")) {
			if (getY() > 0)
				setYspeed(-10);
			else if (getY() < 0)
				setAlive(false);
		} else if (posseser.equals("e")) {
			if (getY() < Scene.SCREEN_HEIGHT - getHeight())
				setYspeed(10);
			else if (getY() > Scene.SCREEN_HEIGHT - getHeight())
				setAlive(false);
		}

		setY(getY() + getYspeed());

		if (posseser.equals("p")) {
			for (GameObject x : getEnemies()) {
				if (AABB(this, x)) {
					this.setAlive(false);
					x.setAlive(false);
					playSound("/Hit_Hurt3.wav");
				}
			}
		}

		if (posseser.equals("e")) {
			if (AABB(this, p)) {
				this.setAlive(false);
				p.setAlive(false);
			}
		}

	}
	
	public static void playSound(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    Starting.class.getResource(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            
            // Add a listener to handle the sound completion event
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();  // Close the clip when it finishes playing
                }
            });
            
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	boolean AABB(Bullet b, GameObject e) {
		if (b.getX() < e.getX() + e.getWidth() && b.getX() + b.getWidth() > e.getX()
				&& b.getY() < e.getY() + e.getHeight() && b.getY() + b.getHeight() > e.getY())
			return true;
		return false;

	}

	@Override
	public void update(KeyEvent k, MouseEvent m) {
	}

	public ArrayList<GameObject> getEnemies() {
		return enemies;
	}

	public void setEnemies(ArrayList<GameObject> enemies) {
		this.enemies = enemies;
	}

}
