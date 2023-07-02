package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import elements.Bullet;
import elements.Enemy;
import elements.GameObject;
import elements.ID;
import elements.Player;

@SuppressWarnings("serial")
public class Scene extends JLabel implements ActionListener, Runnable {

	public static final int ORIGINAL_TILE_SIZE = 16;
	public static final int SCALER = 3;
	public static final int TILE_SIZE = SCALER * ORIGINAL_TILE_SIZE;

	public static final int SCREEN_WIDTH = 720;
	public static final int SCREEN_HEIGHT = SCREEN_WIDTH / 12 * 9;

	Timer timer;
	Thread thread;

	boolean running;

	public boolean music = true;

	int SCORE = 0, c = 0;

	ArrayList<GameObject> objects;
	ArrayList<GameObject> enemies;
	Player p;
	Bullet b;
	Window w;
	JLabel next;
	Random rnd;
	ArrayList<Image> player, enemy, bullet;
	Clip s;

	Scene(Window w) {

		player = new ArrayList<Image>();
		enemy = new ArrayList<Image>();
		bullet = new ArrayList<Image>();
		player.add(new ImageIcon(getClass().getClassLoader().getResource("player/PLAYER.png")).getImage());
		enemy.add(new ImageIcon(getClass().getClassLoader().getResource("Enemy.png")).getImage());
		bullet.add(new ImageIcon(getClass().getClassLoader().getResource("bullet/BULLET-1.png")).getImage());
		bullet.add(new ImageIcon(getClass().getClassLoader().getResource("bullet/BULLET.png")).getImage());
		constructor(w);
		playMusic();
	}

	public static void playSound(String filePath) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Scene.class.getResource(filePath));
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

	public void playMusic() {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Scene.class.getResource("/MUSIC.wav"));
			s = AudioSystem.getClip();
			s.open(audioInputStream);
			// Add a listener to handle the sound completion event
			s.addLineListener(event -> {
				if (event.getType() == LineEvent.Type.STOP) {
					s.setFramePosition(0); // Reset the clip's position to the beginning
					s.start(); // Start playing the sound again
				}
			});

			s.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopMusic() {

		if (s != null && s.isRunning()) {
			s.stop();
			s.close();
		}
	}

	void constructor(Window w) {
		this.w = w;

		rnd = new Random();

		timer = new Timer(1000 / (1000 / 60), this);
		thread = new Thread(this);

		running = true;

		objects = new ArrayList<GameObject>();
		enemies = new ArrayList<GameObject>();
		p = new Player(0, SCREEN_HEIGHT - TILE_SIZE, TILE_SIZE, TILE_SIZE, player, true, ID.Player, 0, 10,
				SCREEN_WIDTH);

		p.setXspeed(30);
		p.setBulletN(10);
		objects.add(p);

		enemy_Spawner();

		timer.start();
		thread.start();
	}

	protected void paintComponent(Graphics g) {

		g.drawImage(new ImageIcon(getClass().getClassLoader().getResource("Floor1.png")).getImage(), 0, 0, SCREEN_WIDTH,
				SCREEN_HEIGHT, null);
		draw(g);
	}

	void enemy_Spawner() {

		SCORE++;

		// Gives bullets
		objects.clear();
		objects.add(p);

		if (p.getGUN() == "Pistol")
			p.setBulletN(p.getBulletN() + 5);
		else if (p.getGUN() == "Machine Gun")
			p.setBulletN(p.getBulletN() + 10);
		// Increases the level
		p.setLevels(p.getLevels() + 1);

		// Spawns enemies at random spots
		for (int i = 0; i < rnd.nextInt(3, 13); i++) {
			Enemy temp = new Enemy(rnd.nextInt(0, SCREEN_WIDTH - TILE_SIZE),
					rnd.nextInt(0, SCREEN_HEIGHT - (TILE_SIZE * 2)), TILE_SIZE+20, TILE_SIZE+20, enemy, true, ID.Enemy, 100,
					SCORE);
			temp.setYspeed(rnd.nextInt(1, 3)); // Sets its speed
			objects.add(temp);// Adds it to list objects
			if (enemies != null)
				enemies.add(temp);// Adds it to list enemies
		}
	}

	void draw(Graphics g) {

		if (enemies != null) {
			g.setColor(Color.white);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Bullets: " + p.getBulletN() + "||Coins: " + p.getCoins() + "||Enemies: " + enemies.size(),
					(SCREEN_WIDTH - metrics.stringWidth(
							"Bullets: " + p.getBulletN() + "||Coins: " + p.getCoins() + "||Enemies: " + enemies.size()))
							/ 2,
					g.getFont().getSize());

			g.drawString("Gun: " + p.getGUN(), (SCREEN_WIDTH - metrics.stringWidth("Gun: " + p.getGUN())) / 2,
					g.getFont().getSize() * 2);
		}

		for (GameObject x : objects) {
			x.draw(g);
		}
	}

	// Draws
	@Override
	public void run() {
		while (running) {
			repaint();
		}
	}

	// Updates
	@Override
	public void actionPerformed(ActionEvent e) {
		// Stores index of enemy and bullets in list objects to be removed
		int bi = 0, ei = 0;

		if (w.getKey() != null)
			if (w.getKey().getKeyCode() == KeyEvent.VK_Q) {
				timer.stop();
				next = new Shop(w, this, p);
				w.add(next);
				SwingUtilities.updateComponentTreeUI(w);
				w.remove(this);
			}

		// Updates every gameObject
		for (GameObject x : objects) {
			if (x.getId() == ID.Player) {
				Player temp = (Player) x;
				temp.update(w.ke, w.me);
				if (temp.shot && temp.getBulletN() > 0) {
					// Adds a bullet object to the list objects based on gun held by the player
					if (p.getGUN() == "Pistol") {
						b = (new Bullet(temp.getX() + temp.getWidth() / 2 - TILE_SIZE / 2,
								temp.getY() + temp.getHeight(), TILE_SIZE, TILE_SIZE, bullet, true, ID.Pistol, "p",
								temp));
						playSound("/Laser_Shoot7.wav");
					} else if (p.getGUN() == "Machine Gun") {
						b = (new Bullet(temp.getX() + temp.getWidth() / 2 - TILE_SIZE / 2,
								temp.getY() + temp.getHeight(), TILE_SIZE, TILE_SIZE, bullet, true, ID.Machine_Gun, "p",
								temp));
						playSound("/Laser_Shoot.wav");
					}
					temp.setBulletN(temp.getBulletN() - 1);
				}
			} else if (x.getId() == ID.Pistol || x.getId() == ID.Machine_Gun) {
				if (!x.isAlive()) {
					bi = objects.indexOf(x); // Stores value of the bullet to be removed
					p.setCoins(p.getCoins() + 1);
				} else if (x.isAlive())
					((Bullet) x).setEnemies(this.enemies);// Sets the list enemy in the bullet class equal to the list
															// enemy in this class
				try {
					x.update();
				} catch (Exception e1) {
				}
				this.enemies = ((Bullet) x).getEnemies();// Sets the list enemy in this class equal to the list enemy in
															// the bullet class
			}

			else if (x.getId() == ID.Enemy) {
				if (!x.isAlive())
					ei = objects.indexOf(x); // Stores index of enemy object to be removed
				else if (x.isAlive()) {
					if (x.getY() + x.getHeight() >= SCREEN_HEIGHT)
						p.setAlive(false); // Kills the player
					if (((Enemy) x).shoot && enemies != null) {
						b = (new Bullet(x.getX() + x.getWidth() / 2 - TILE_SIZE / 2, x.getY() + x.getHeight(),
								TILE_SIZE, TILE_SIZE, bullet, true, ID.Pistol, "e", p));
						playSound("/Laser_Shoot7.wav");
						((Enemy) x).shoot = false;
						enemies.add(b);
					}
					x.update();
				}
			} else
				x.update();
		}

		// Adds bullets
		if (b != null) {
			objects.add(b);
			b = null;
		}

		// Removes bullets
		if (bi != 0) {
			if (((Bullet) objects.get(bi)).posseser == "e" && enemies != null)
				enemies.remove(objects.get(bi));
			objects.remove(bi);
			bi = 0;
		}

		// Removes enemies
		if (ei != 0 && enemies != null) {
			enemies.remove(objects.get(ei));
			objects.remove(ei);
			ei = 0;
		}

		// Spawns new enemies
		if (enemies != null) {
			c = 0;
			for (int i = 0; i < enemies.size(); i++) {
				if (enemies.get(i).getId() == ID.Enemy)
					c++;
			}
		}
		if (c == 0)
			enemy_Spawner();

		// Player dies
		if (!p.isAlive()) {
			playSound("/Explosion.wav");
			stopMusic();
			running = false;
			timer.stop();
			next = new Endscreen(w, p.getCoins(), p.getBulletN(), SCORE); // Adds death/end screen
			w.add(next);
			SwingUtilities.updateComponentTreeUI(w);
			w.remove(this);
		}

	}
}
