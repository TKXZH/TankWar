package com.xv.tankwar;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.Rectangle;

import com.xv.tankwar.Tank.Direction;


public class Wall {
	int x;
	int y;
	int width;
	int height;
	
	public Wall(int i, int j, int k, int l) {
		x = i;
		y = j;
		width = k;
		height = l;
		
	}

	public void draw (Graphics g) {
		g.setColor(Color.green);
		g.fillRect(x, y, width, height);
	}
	
	public Rectangle getRectangle () {
		return new Rectangle(x,y,width,height);
	}
	
	public void hit(Tank tank) {
		if(tank.blood>0&&this.getRectangle().intersects(tank.getRectangle())) {
			tank.dir = Direction.STOP;
			tank.x = tank.preX;
			tank.y = tank.preY;
		}
	}
	public void hitByBullet(Bullet bullet) {
		if(this.getRectangle().intersects(bullet.getRectangle())) {
			bullet.x = 1000;
			bullet.y = 1000;   //先用出界的方式移除这个子弹，交给其他线程remove之。
		}
	}
}
