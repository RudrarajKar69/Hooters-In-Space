package elements;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends GameObject {

	int coins;
	int bulletN, S_H, levels = 1;
	Event_Handler ed;
	String GUN = "Pistol";
	public boolean shot = false, shoot = true;

	public Player(int x, int y, int width, int height, ArrayList<Image> image, boolean alive, ID id, int coins,
			int bulletn, int S_H) {
		super(x, y, width, height, image, alive, id);
		setCoins(coins);
		setBulletN(bulletn);
		this.S_H = S_H;
	}

	@Override
	public void draw(Graphics g) {

		if (getXspeed() == 0)
			g.drawImage(getImage().get(0), getX(), getY(), getWidth(), getHeight(), null);
		if (getXspeed() > 0) {
			BufferedImage rotatedImage = new BufferedImage(getImage().get(0).getWidth(null),
					getImage().get(0).getWidth(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g2dRotated = rotatedImage.createGraphics();
			g2dRotated.rotate(Math.PI / 2, getImage().get(0).getWidth(null) / 2, getImage().get(0).getHeight(null) / 2); // Rotate
																															// by
																															// 180
																															// degrees
			g2dRotated.drawImage(getImage().get(0), 0, 0, null);
			g2dRotated.dispose();
			g.drawImage(rotatedImage, getX(), getY(), getWidth(), getHeight(), null);
		}
		if (getXspeed() < 0) {
			BufferedImage rotatedImage = new BufferedImage(getImage().get(0).getWidth(null),
					getImage().get(0).getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g2dRotated = rotatedImage.createGraphics();
			g2dRotated.rotate(-Math.PI / 2, getImage().get(0).getWidth(null) / 2,
					getImage().get(0).getHeight(null) / 2); // Rotate by 180 degrees
			g2dRotated.drawImage(getImage().get(0), 0, 0, null);
			g2dRotated.dispose();
			g.drawImage(rotatedImage, getX(), getY(), getWidth(), getHeight(), null);
		}

	}

	@Override
	public void update(KeyEvent k, MouseEvent m) {

//		if (getLevels() > rnd.nextInt(5)+5)
//			setGUN("Machine Gun");

		setXspeed(0);
		ed = new Event_Handler(k, m);

		if (ed.Right)
			setXspeed(20);

		if (ed.Left)
			setXspeed(-20);

		if (getX() < 0)
			setX(S_H);
		if (getX() > S_H)
			setX(0);

		setX(getX() + getXspeed());

		shooting();
	}

	void shooting() {
		switch (GUN) {
		case "Pistol":
			if (!shoot)
				shot = false;

			if (ed.Shot && shoot) {
				shot = true;
				shoot = false;
			}
			if (!ed.Shot) {
				shot = false;
				shoot = true;
			}
			break;
		case "Machine Gun":
			shot = ed.Shot;
			break;
		}
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public int getBulletN() {
		return bulletN;
	}

	public void setBulletN(int bulletN) {
		this.bulletN = bulletN;
	}

	public String getGUN() {
		return GUN;
	}

	public void setGUN(String gUN) {
		GUN = gUN;
	}

	public int getLevels() {
		return levels;
	}

	public void setLevels(int levels) {
		this.levels = levels;
	}

	@Override
	public void update() {
	}
}
