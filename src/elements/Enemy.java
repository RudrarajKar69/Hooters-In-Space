package elements;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Enemy extends GameObject {

	double Health;
	public boolean shoot = false;
	int score;

	public Enemy(int x, int y, int width, int height, ArrayList<Image> image, boolean alive, ID id, int health,
			int Score) {
		super(x, y, width, height, image, alive, id);
		Health = health;
		score = Score;
	}

	@Override
	public void draw(Graphics g) {

		g.drawImage(getImage().get(0), getX(), getY(), getWidth(), getHeight(), null);

	}

	@Override
	public void update() {

		setY(getY() + getYspeed());

		// Shoot bullets
		if (rnd.nextInt(200) + 1 == 1)
			shoot = true;
	}

	@Override
	public void update(KeyEvent k, MouseEvent m) {

	}

}
