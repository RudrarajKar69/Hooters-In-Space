package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import elements.Event_Handler;

@SuppressWarnings("serial")
public class Starting extends JLabel implements ActionListener {

	// Timer for animation
	Timer timer;
	// Window object for key event handling
	Window k;
	// command variable to keep track of user input
	int x = 0, command = 0;

	JLabel next;

	// Constructor
	Starting(Window k) {
		// Initializing instance variables
		this.k = k;
		this.setBackground(Color.black);
		// Starting timer
		timer = new Timer(200, this);
		timer.start();
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

	// Overriding paintComponent method to draw on JPanel
	protected void paintComponent(Graphics g) {
		draw(g);
	}

	// Custom draw method to draw graphics on the JPanel
	void draw(Graphics g) {
		// Filling the background with a purple color
		g.setColor(new Color(0, 255, 0));
		g.fillRect(0, 0, Scene.SCREEN_WIDTH, Scene.SCREEN_HEIGHT);

		// Drawing the main text "Scene!" in bold font with size 96
		g.setFont(g.getFont().deriveFont(Font.BOLD, 60F));
		String text = "Star Wars(Rudra Edition)";
		int x = getXforCenteredText(text, g), y = (Scene.SCREEN_HEIGHT) / 4;

		// Drawing the shadow of the main text in gray color with an offset of 5 pixels
		// in both x and y direction
		g.setColor(Color.gray);
		g.drawString(text, x + 5, y + 5);

		// Drawing the main text in white color
		g.setColor(Color.white);
		g.drawString(text, x, y);

		//Image
		Image temp = new ImageIcon(getClass().getClassLoader().getResource("player/PLAYER.png")).getImage();
		x=Scene.SCREEN_WIDTH/2-Scene.TILE_SIZE;
		y+=Scene.TILE_SIZE;
		g.drawImage(temp, x, y, Scene.TILE_SIZE*2, Scene.TILE_SIZE*2, null);
		
		// Drawing the "Start" and "Quit" options in bold font with size 40
		g.setFont(g.getFont().deriveFont(Font.BOLD, 40F));
		text = "Start";
		x = getXforCenteredText(text, g);
		y += (Scene.SCREEN_HEIGHT) / 3;
		g.drawString(text, x, y);
		if (command == 0)
			g.drawString(">", x - (Scene.SCREEN_HEIGHT) / 2, y);

		text = "Quit";
		x = getXforCenteredText(text, g);
		y += (Scene.SCREEN_HEIGHT) / 6;
		g.drawString(text, x, y);
		if (command == 1)
			g.drawString(">", x - (Scene.SCREEN_HEIGHT) / 2, y);

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
				command = 0;
				playSound("/Blip_Select.wav");
			}
			if (e1.Down) {
				// Move the selection to the "Quit" option
				command = 1;
				playSound("/Blip_Select.wav");
			}
			if (e1.Enter || e1.Shot) { // Check which option is currently selected
				playSound("/Blip_Select.wav");
				if (command == 1)
					// If "Quit" is selected, exit the program
					System.exit(0);
				else if (command == 0) {
					// If "Restart" is selected, go back to the difficulty screen
					next = new Scene(k);
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
