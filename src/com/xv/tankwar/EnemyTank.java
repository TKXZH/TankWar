package com.xv.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class EnemyTank extends Tank{
	
	public static List<Direction> dirList = new ArrayList<Direction>();  //因为所有敌方坦克共用一个随机方向产生表，因此用static
	public static void Initia() {
		dirList.add(Direction.U);
		dirList.add(Direction.L);
		dirList.add(Direction.R);
		dirList.add(Direction.D);
		dirList.add(Direction.LD);
		dirList.add(Direction.LU);
		dirList.add(Direction.RD);
		dirList.add(Direction.RU);
		
	}
	public EnemyTank() {   //重写构造方法，实例化敌方坦克时，使用随机数来定位坦克的位置。
	this.x = (new Random().nextInt(700)%600)+50;
	this.y = (new Random().nextInt(550)%500)+50;
	}
	
	@Override
	public void draw(Graphics g) {
		/*Color c = g.getColor();
		g.setColor(Color.red);
		if(blood > 0) {
			g.fillOval(x, y, SIZE_X, SIZE_Y);
		}
		//g.drawLine(x1, y1, x2, y2);
		if(blood>0) {
			super.drawLine(g);  //如果血量为0，就取消炮筒的显示
		}
		g.fillRect(this.x, this.y-40, blood, 10);
		g.setColor(c);*/
		if(blood > 0) {
			switch (this.predir) {
				case D:g.drawImage(images[0], this.x, this.y, null);
					break;
				case L:g.drawImage(images[1], this.x, this.y, null);
				break;
				case LD:g.drawImage(images[2], this.x, this.y, null);
				break;
				case LU:g.drawImage(images[3], this.x, this.y, null);
				break;
				case R:g.drawImage(images[4], this.x, this.y, null);
				break;
				case RD:g.drawImage(images[5], this.x, this.y, null);
				break;
				case RU:g.drawImage(images[6], this.x, this.y, null);
				break;
				case U:g.drawImage(images[7], this.x, this.y, null);
				break;
				

			default:
				break;
			}
			
		}
		g.setColor(Color.red);
		g.fillRect(this.x, this.y-20, blood, 10);
	
	}
	public Direction randomDirection() {    //产生随机的Direction枚举类型，使
		int i;
		i = (new Random().nextInt(7));
		return dirList.get(i);
	}
	@Override
	public void hit(List<Bullet> mybullets) {
		if(mybullets.size()!=0&&this.blood>=0) {
			for(int i = 0;i<mybullets.size(); i++) {
				if((Math.pow(((this.x+15)-mybullets.get(i).x),2)+Math.pow(((this.y+15)-mybullets.get(i).y),2))<700&&(mybullets.get(i).ifFriend==true)) {
					this.blood -= 5;
					mybullets.remove(i);
				}
			}
			
		} 
	}
	
	public Bullet shoot () {
		Bullet enemyBullet = new Bullet(x+15, y+15, predir, false);
		return enemyBullet;
	}
	
	
}

	

