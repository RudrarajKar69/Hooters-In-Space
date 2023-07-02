package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import elements.Event_Handler;
import elements.Player;

@SuppressWarnings("serial")
public class Shop extends JLabel implements ActionListener {

	// Timer for animation
	Timer timer;
	// Window object for key event handling
	Window k;
	// command variable to keep track of user input
	int x = 0, command = 0;

	Scene next;
	Player p;

	// Constructor
	Shop(Window k, Scene s, Player p) {
		// Initializing instance variables
		next = s;
		this.k = k;
		this.p = p;

		this.setBackground(Color.black);
		// Starting timer
		timer = new Timer(200, this);
		timer.start();
	}

	public static void playSound(String filePath) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Shop.class.getResource(filePath));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);

			// Add a listener to handle the sound completion event
			clip.addLineListener(event -> {
				if (event.getType() == LineEvent.Type.STOP) {
					clip.close(); // Close the clip when it finishes playing
				}
			});

			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Overriding paintComponent method to draw on JPanel
	protected void paintComponent(Graphics g) {
		draw(g);
	}

	// Custom draw method to draw graphics on the JPanel
	void draw(Graphics g) {
		// Filling the background with a purple color
		g.setColor(new Color(255, 0, 255));
		g.fillRect(0, 0, Scene.SCREEN_WIDTH, Scene.SCREEN_HEIGHT);

		// Drawing the main text "Scene!" in bold font with size 96
		g.setFont(g.getFont().deriveFont(Font.BOLD, 96F));
		String text = "Shop";
		int x = getXforCenteredText(text, g), y = (Scene.SCREEN_HEIGHT) / 4;

		// Drawing the shadow of the main text in gray color with an offset of 5 pixels
		// in both x and y direction
		g.setColor(Color.gray);
		g.drawString(text, x + 5, y + 5);

		// Drawing the main text in white color
		g.setColor(Color.white);
		g.drawString(text, x, y);

		// Drawing an image of the player character in the center of the screen
		x = Scene.SCREEN_WIDTH / 2 - (Scene.TILE_SIZE / 2);
		y += (Scene.SCREEN_HEIGHT) / 4 - (Scene.TILE_SIZE / 2);
//		g.drawImage(new ImageIcon(getClass().getClassLoader().getResource("PLAYER.png")).getImage(), x, y, null);

		// Drawing the "Restart" and "Quit" options in bold font with size 40
		g.setFont(g.getFont().deriveFont(Font.BOLD, 30F));
		text = "Start";
		x = getXforCenteredText(text, g);
		y += (Scene.SCREEN_HEIGHT) / 8;
		g.drawString(text, x, y);
		if (command == 0)
			g.drawString(">", x - 50, y);

		text = "Buy Bullets(10/-)";
		x = getXforCenteredText(text, g);
		y += (Scene.SCREEN_HEIGHT) / 8;
		g.drawString(text, x, y);
		if (command == 1)
			g.drawString(">", x - 50, y);

		if (next.music == true)
			text = "Stop music";
		else if (next.music == false)
			text = "Start music";
		x = getXforCenteredText(text, g);
		y += (Scene.SCREEN_HEIGHT) / 8;
		g.drawString(text, x, y);
		if (command == 2)
			g.drawString(">", x - 50, y);

		text = "Quit";
		x = getXforCenteredText(text, g);
		y += (Scene.SCREEN_HEIGHT) / 8;
		g.drawString(text, x, y);
		if (command == 3)
			g.drawString(">", x - 50, y);

	}

	// Helper method to get x coordinate for centered text
	private int getXforCenteredText(String text, Graphics g2) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = Scene.SCREEN_WIDTH / 2 - length / 2;
		return x;
	}

	// Overriding actionPerformed method to handle
	@Override
	public void actionPerformed(ActionEvent e) {
		// Get the key event from the window
		Event_Handler e1 = new Event_Handler(k.getKey(), k.getMouse());
		// Check if a key was pressed
		if (e1 != null) {
			// Check which key was pressed
			if (e1.Up) { // Move the selection to the "Restart" option
				playSound("/Blip_Select.wav");
				if (command == 0)
					command = 3;
				else if (command == 1)
					command = 0;
				else if (command == 2)
					command = 1;
				else if (command == 3)
					command = 2;
			}
			if (e1.Down) {
				playSound("/Blip_Select.wav");
				// Move the selection to the "Quit" option
				if (command == 3)
					command = 0;
				else if (command == 0)
					command = 1;
				else if (command == 1)
					command = 2;
				else if (command == 2)
					command = 3;

			}
			if (e1.Enter || e1.Shot) { // Check which option is currently selected
				playSound("/Blip_Select.wav");
				if (command == 3)
					// If "Quit" is selected, exit the program
					System.exit(0);
				else {
					// If "Start" is selected, go back to the difficulty screen
					if (command == 1) {
						if (p.getCoins() > 10) {
							p.setCoins(p.getCoins() - 10);
							p.setBulletN(p.getBulletN() + 10);
						} else
							System.out.println("Not sufficient money");
					}
					if (command == 2) {
						if (next.music == false) {
							next.playMusic();
							next.music = true;
						} else if (next.music == true) {
							next.stopMusic();
							next.music = false;
						}

					}
					next.timer.start();
					k.add(next);
					// Update the window's UI to show the new screen
					SwingUtilities.updateComponentTreeUI(k);
					// Stop the timer
					timer.stop();
					// Remove this screen from the window
					k.remove(this);
				}
			}
		}
		// Redraw the screen
		repaint();
	}
}
