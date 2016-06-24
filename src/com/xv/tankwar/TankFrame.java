package com.xv.tankwar;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;



import com.xv.tankwar.Tank.Direction;
/*
 * 此版本加入坦克贴图，重写Draw方法
 * 1.用Image数组存储坦克图片
 * 2.用ClassLoader反射找到图片的URL
 * 3.通过判定preDirection来确定显示具体哪张图片
 * 4.优化了原本错综复杂的线程，并加入线程同步锁，对涉及对集合中元素数量改变的操作需要互斥访问，
 *   防止下标访问越界异常。
 * Designed By XZH
 */
public class TankFrame extends Frame{
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;   //定义成为静态常量，方便维护
	Tank myTank = new Tank(400, 300, this);
	EnemyTank enTank = null;
	EnemyTank et = new EnemyTank();
	List<Bullet> mybullets = new ArrayList<Bullet>(1);//声明一个容器，以保存多发炮弹。
	List<EnemyTank> enemyTanks = new ArrayList<EnemyTank>();//声明一个容器，以保存多个敌方坦克。
	java.awt.Image offscreenImage = null;
	Bullet mybBullet = null;
	List<Bullet> yourbullets=new ArrayList<Bullet>(1);
	List<Energy> energys=new ArrayList<Energy>(1);
	Wall myWall = new Wall(380,155,10,320);
	int enemyTankCounter;
	
	
		public static void main(String[] args) {
		TankFrame TF = new TankFrame(50,50,GAME_WIDTH,GAME_HEIGHT,Color.darkGray);
		TF.launchFrame();
	}
	
	public void launchFrame() {
		//new Properties.load(TankFrame.class.getClassLoader().getResourceAsStream("config/propertise"));
		EnemyTank.Initia(); //初始化敌方坦克类中公用的Direction表
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.exit(0);//重写父类窗口关闭方法
			}
		});//使用匿名内部类实现监听器
			
		this.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				myTank.keyPressed(e);
			}
				
		});
		this.addKeyListener(new KeyAdapter() {
			
			public void keyReleased(KeyEvent e) {
				myTank.keyReleased(e);
			}
			
		});
			
		new Thread(new PaintThread()).start();//启动重画线程
		new Thread(new TankThread()).start();//启动坦克移动线程
		new Thread(new EnemyTankSpeedThread()).start();
		new Thread(new EnemyTankThread()).start();
		new Thread(new EnemyBulletThread()).start();
		new Thread(new EnergyThread()).start();	
		
			
	}
	
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		myWall.draw(g);
		myWall.hit(myTank);
		myTank.draw(g);//调用坦克的自绘方法
		myTank.hit(mybullets);
		myTank.crash(enemyTanks);
		if(mybullets.size()!=0) {
			for(int i = 0;i<mybullets.size(); i++) {
				mybBullet = mybullets.get(i);
				mybBullet.draw(g);
				myWall.hitByBullet(mybBullet);
				if(mybBullet.x>900||mybBullet.y>900||mybBullet.x<0||mybBullet.y<0) {
					mybullets.remove(i);   //如果容器中某个子弹实例的坐标范围超出屏幕，则将其丢弃。
				}
			}
		}
		if(enemyTanks.size()!=0) {
			for(int i = 0;i<enemyTanks.size(); i++) {
				enTank = enemyTanks.get(i);
				enTank.draw(g);
				enTank.hit(mybullets);
				myWall.hit(enTank);
				
			}
		}	
		if(energys.size()!=0) {
			for(int i = 0;i<energys.size(); i++) {
				energys.get(i).hit(myTank);
				energys.get(i).draw(g);
				if(energys.get(i).tag==false) {
					energys.remove(i);
				}
			}
		}
	}
	
	public void update(Graphics g) {
		if(offscreenImage == null) {
			offscreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);	
		}
		Graphics gOffScreen = offscreenImage.getGraphics();
		gOffScreen.setColor(Color.BLACK);
		gOffScreen.fillRect(0, 0, 800, 600);
		paint(gOffScreen);
		g.drawImage(offscreenImage, 0, 0, null);
	} //双缓冲消除重影
	
	public class PaintThread implements Runnable {
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}   //界面重画线程
	public class TankThread implements Runnable {
		public void run() {
			while(true) {
					myTank.move();
					myTank.hit(yourbullets);
				//System.out.println(mybullets.size()); //测试容器中的子弹数量
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}  //坦克位置维护线程，避免按下按键到识别为长按之间的空白区，使坦克在按下按键的一瞬间就开始匀速前进(也可将业务代码放进paint方法中)
	
	public class EnemyTankThread implements Runnable {
		int i = 0;
		public void run() {
			while(true) {
				
					
				
					Direction dir = et.randomDirection();
					synchronized (enemyTanks) {
						
					
						enemyTanks.get(i).dir = dir;
						enemyTanks.get(i).predir = dir;
						++i;
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					synchronized (enemyTanks) {
					if(i== enemyTanks.size()) {
						i=0;
					}
					}

			
				}
			
		}
	}  
	public class EnemyTankSpeedThread implements Runnable {
		public void run() {
			while(true) {
					if(enemyTanks.size()<7) {
						
							
						synchronized (enemyTanks) {
							enemyTanks.add(new EnemyTank());
						}
						
						
					}
					for(int i = 0;i<enemyTanks.size(); i++) {
						if(enemyTanks.get(i).blood<=0) {
								synchronized (enemyTanks) {
									enemyTanks.remove(i);  
								}
								
							
							 
						}
						
						
					}
					for(int i = 0;i<enemyTanks.size(); i++) {
						
							enemyTanks.get(i).move();
						
						
					}
					
				try {
					Thread.sleep(35);
					//System.out.println(enTank.dir);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}  
	
	public class EnemyBulletThread implements Runnable {
		public void run() {
			while(true) {
				if(enemyTanks.size()!=0) {
					for(int i = 0;i<enemyTanks.size(); i++) {
						mybullets.add(enemyTanks.get(i).shoot());
						
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public class EnergyThread implements Runnable {
		public void run() {
			while(true) {
					if(energys.size()<=1) {
						energys.add(new Energy(new Random().nextInt(700)+50,new Random().nextInt(500)+50));
					}
				try {
					Thread.sleep(8000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	} 
	 
	TankFrame(int x, int y, int width, int height, Color color) {
		super("坦克大战");
		this.setBackground(color);
		this.setLayout(null);
		this.setBounds(x, y, width, height);
		this.setVisible(true);
		this.setResizable(false);
		
		
	}//构造主场景
	

}
