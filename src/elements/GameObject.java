package elements;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public abstract class GameObject {

	int x = 0, y = 0, xspeed = 0, yspeed = 0, height = 0, width = 0;
	ArrayList<Image> image;
	boolean alive;
	ID id;
	Random rnd = new Random();

	GameObject(int x, int y, int width, int height, ArrayList<Image> image, boolean alive, ID id) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setImage(image);
		setAlive(alive);
		setId(id);
	}

	public abstract void draw(Graphics g);

	public abstract void update();

	public abstract void update(KeyEvent k, MouseEvent m);
	
	public ArrayList<Image> getImage() {
		return image;
	}

	public void setImage(ArrayList<Image> image2) {
		this.image = image2;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getXspeed() {
		return xspeed;
	}

	public void setXspeed(int xspeed) {
		this.xspeed = xspeed;
	}

	public int getYspeed() {
		return yspeed;
	}

	public void setYspeed(int yspeed) {
		this.yspeed = yspeed;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
