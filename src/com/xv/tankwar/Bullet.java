package com.xv.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.xv.tankwar.Tank.Direction;

public class Bullet {
	boolean ifFriend;
	int x, y;//̹��λ����Ϣ
	int counter=1;
	Direction dir = null;
	public static final int STEP = 2;  //��̬�����������ӵ��ٶ�
	public Bullet(int x, int y, Direction dir,boolean ifFriend) {    //����̹�˵�λ�ù����ӵ�λ��
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.ifFriend = ifFriend;
	}
	public Rectangle getRectangle() {
		return new Rectangle(x,y,10,10);
	}
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.white);
		g.fillOval(x, y, 10, 10);
		g.setColor(c);
		move(); 
		
	}
	
	public void move() {
		switch (dir) {
			
			case L: x-=STEP;
			break;
			case LU: {x-=STEP;y-=STEP;}
			break;
			case U:y-=STEP;
			break;
			case RU:{x+=STEP;y-=STEP;}
			break;
			case R:x+=STEP;
			break;
			case RD:{x+=STEP;y+=STEP;}
			break;
			case D:y+=STEP;
			break;
			case LD:{x-=STEP;y+=STEP;}
			break;
			case STOP:x+=0;
			break;
			default:
			break;
		}
	}

}
