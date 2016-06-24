package com.xv.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.List;

import java.awt.*;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ExplicitGroup;





public class Tank {
	public static final int STEP = 3; //坦克移动一步走过的像素距离
	int x , y;      //坦克位置坐标
	int preX ,preY;
	int SIZE_X = 40;
	int SIZE_Y = 40;
	int blood = 48; //设置坦克的生命值
	TankFrame tf;
	enum Direction {L, LU, U, RU, R, RD, D, LD,STOP}; //设置8个枚举类型，作为坦克移动方向的标记
	protected Direction dir = Direction.STOP;
	protected Direction predir = Direction.U;
	private boolean left=false;
	private boolean up=false;
	private boolean right=false;
	private boolean down=false;   //设置4个布尔量记录按键状态
	public static Toolkit tk = Toolkit.getDefaultToolkit();
	public static Image[] images= {
		tk.getImage(Tank.class.getClassLoader().getResource("image/tankD.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("image/tankL.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("image/tankLD.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("image/tankLU.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("image/tankR.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("image/tankRD.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("image/tankRU.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("image/tankU.gif"))
		
	};
	public Tank (){};
	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Tank (int x, int y, TankFrame tf) {
		this(x, y);
		this.tf = tf;
	}
	
	public void draw (Graphics g) {
		/*Color c = g.getColor();
		g.setColor(Color.white);
		if(blood > 0) {
			g.fillOval(x, y, SIZE_X, SIZE_Y);
		}
		//g.drawLine(x1, y1, x2, y2);
		if(blood>0) {
			this.drawLine(g);  //如果血量为0，就取消炮筒的显示
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
		
		g.fillRect(this.x, this.y-20, blood, 10);
	}
	
	protected void drawLine(Graphics g) {
		switch (predir) {
		case L: g.drawLine(x-38, y+20, x+20, y+20);
		break;
		case LU: g.drawLine(x-20, y-20,x+20, y+20);
		break;
		case U:g.drawLine(x+20, y-38,x+20, y+20);
		break;
		case RU:g.drawLine(x+60, y-20, x+20, y+20);
		break;
		case R:g.drawLine(x+78, y+20,x+20, y+20);
		break;
		case RD:g.drawLine(x+60, y+60,x+20, y+20);
		break;
		case D:g.drawLine(x+20, y+78,x+20, y+20);
		break;
		case LD:g.drawLine(x-20, y+60,x+20, y+20);
		break;
		default:
		break;
		}
	}
	public void hit(List<Bullet> mybullets) {   //这个检测坦克与子弹碰撞的方法是用坦克绘制线程调用的
		if(mybullets.size()!=0&&this.blood>=0) {
			for(int i = 0;i<mybullets.size(); i++) {
				if((Math.pow(((this.x+15)-mybullets.get(i).x),2)+Math.pow(((this.y+15)-mybullets.get(i).y),2))<700&&(mybullets.get(i).ifFriend==false)) {
					this.blood -= 5;
					mybullets.remove(i);
				}
			}
			
		} 


	}
	public void crash (List<EnemyTank> enemyTanks) {   //检测我方坦克与地方坦克的碰撞，如果碰撞，敌方坦克就死亡。
		if(enemyTanks.size()!=0&&this.blood>=0) {
			for(int i = 0;i<enemyTanks.size(); i++) {
				if((Math.pow(((this.x+15)-enemyTanks.get(i).x),2)+Math.pow(((this.y+15)-enemyTanks.get(i).y),2))<2200) {
					this.blood -= 10;
					enemyTanks.get(i).blood = 0;
					enemyTanks.remove(i);
				}
			}
		}
	}
	
	public Rectangle getRectangle() {    //返回坦克的碰撞模型实体
		return new Rectangle(x,y,SIZE_X,SIZE_Y);
	}
	
	public void move() {
		switch (dir) {
			case L: 
				if(x>0) {
					preX = x;
					x-=STEP;
				}
			break;
				
			case LU: 
				if(x>0&&y>20) {
					preX = x;
					preY = y;
					x-=STEP;y-=STEP;
				}
			break;
			case U:
				if(y>20) {
					preY = y;
					y-=STEP;
				}
			break;
			case RU:
				if(x<760&&y>20) {
					preX = x;
					preY = y;
					x+=STEP;y-=STEP;
				}
			break;
			case R:
				if(x<760) {
					preX = x;
					x+=STEP;
				}
			break;
			case RD:
				if(x<760&&y<560) {
					preX = x;
					preY = y;
					x+=STEP;y+=STEP;
				}
			break;
			case D:
				if(y<560) {
					preY = y;
					y+=STEP;
				}
			break;
			case LD:
				if(x>0&&y<560) {
					preX = x;
					preY = y;
					x-=STEP;y+=STEP;
				}
			break;
			case STOP:x+=0;
			break;
			default:
			break;
		}
	}
	public void keyPressed(KeyEvent e) {
		
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ENTER:
			if(this.blood>0) {    //限制弹夹的容量
			tf.mybullets.add(new Bullet(x+15, y+15, predir,true));
			}
			//tf.mybBullet=fire();
			break;
		case KeyEvent.VK_CONTROL:
			tf.mybullets.add(new Bullet(x+15, y+15, Direction.D,true));
			tf.mybullets.add(new Bullet(x+15, y+15, Direction.U,true));
			tf.mybullets.add(new Bullet(x+15, y+15, Direction.L,true));
			tf.mybullets.add(new Bullet(x+15, y+15, Direction.R,true));
			tf.mybullets.add(new Bullet(x+15, y+15, Direction.RD,true));
			tf.mybullets.add(new Bullet(x+15, y+15, Direction.RU,true));
			tf.mybullets.add(new Bullet(x+15, y+15, Direction.LD,true));
			tf.mybullets.add(new Bullet(x+15, y+15, Direction.LU,true));
			break;
		case KeyEvent.VK_LEFT:
			left=true;
			break;
		case KeyEvent.VK_UP:
			up=true;
			break;
		case KeyEvent.VK_RIGHT:
			right=true;
			break;
		case KeyEvent.VK_DOWN:
			down=true;
			break;
		default:
			break;
		}
		this.updateDirection_Press();
	}
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		
			
		case KeyEvent.VK_LEFT:
			left=false;
			break;
		case KeyEvent.VK_UP:
			up=false;
			break;
		case KeyEvent.VK_RIGHT:
			right=false;
			break;
		case KeyEvent.VK_DOWN:
			down=false;
			break;
		default:
			break;
		}
		this.updateDirection_Release();
	}
	
	private void updateDirection_Press() {
		if(left==true&&up==false&&right==false&&down==false) {
			this.dir=Direction.L;
			this.predir=Direction.L;
		}
		else if (left==true&&up==true&&right==false&&down==false) {
			this.dir=Direction.LU;
			this.predir=Direction.LU;
		}
		else if (left==false&&up==true&&right==false&&down==false) {
			this.dir=Direction.U;
			this.predir=Direction.U;
		}
		else if (left==false&&up==true&&right==true&&down==false) {
			this.predir=Direction.RU;
			this.dir=Direction.RU;
		}
		else if (left==false&&up==false&&right==true&&down==false) {
			this.predir=Direction.R;
			this.dir=Direction.R;
		}
		else if (left==false&&up==false&&right==true&&down==true) {
			this.predir=Direction.RD;
			this.dir=Direction.RD;
		}
		else if (left==false&&up==false&&right==false&&down==true) {
			this.predir=Direction.D;
			this.dir=Direction.D;
		}
		else if (left==true&&up==false&&right==false&&down==true) {
			this.predir=Direction.LD;
			this.dir=Direction.LD;
		}
		else if (left==false&&up==false&&right==false&&down==false) {
			this.dir=Direction.STOP;
		}
	}
	private void updateDirection_Release() {
		if(left==true&&up==false&&right==false&&down==false) {
			this.dir=Direction.L;
		}
		else if (left==true&&up==true&&right==false&&down==false) {
			this.dir=Direction.LU;
		}
		else if (left==false&&up==true&&right==false&&down==false) {
			this.dir=Direction.U;
		}
		else if (left==false&&up==true&&right==true&&down==false) {
			this.dir=Direction.RU;
		}
		else if (left==false&&up==false&&right==true&&down==false) {
			this.dir=Direction.R;
		}
		else if (left==false&&up==false&&right==true&&down==true) {
			this.dir=Direction.RD;
		}
		else if (left==false&&up==false&&right==false&&down==true) {
			this.dir=Direction.D;
		}
		else if (left==true&&up==false&&right==false&&down==true) {

			this.dir=Direction.LD;
		}
		else if (left==false&&up==false&&right==false&&down==false) {
			this.dir=Direction.STOP;
		}
	}
	
	/*public Bullet fire() {
		Bullet bullet = new Bullet(this.x+15, this.y+15,this.predir);
		return bullet;
		
	}
	*/
}
