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
 * �˰汾����̹����ͼ����дDraw����
 * 1.��Image����洢̹��ͼƬ
 * 2.��ClassLoader�����ҵ�ͼƬ��URL
 * 3.ͨ���ж�preDirection��ȷ����ʾ��������ͼƬ
 * 4.�Ż���ԭ�����۸��ӵ��̣߳��������߳�ͬ���������漰�Լ�����Ԫ�������ı�Ĳ�����Ҫ������ʣ�
 *   ��ֹ�±����Խ���쳣��
 * Designed By XZH
 */
public class TankFrame extends Frame{
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;   //�����Ϊ��̬����������ά��
	Tank myTank = new Tank(400, 300, this);
	EnemyTank enTank = null;
	EnemyTank et = new EnemyTank();
	List<Bullet> mybullets = new ArrayList<Bullet>(1);//����һ���������Ա���෢�ڵ���
	List<EnemyTank> enemyTanks = new ArrayList<EnemyTank>();//����һ���������Ա������з�̹�ˡ�
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
		EnemyTank.Initia(); //��ʼ���з�̹�����й��õ�Direction��
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.exit(0);//��д���ര�ڹرշ���
			}
		});//ʹ�������ڲ���ʵ�ּ�����
			
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
			
		new Thread(new PaintThread()).start();//�����ػ��߳�
		new Thread(new TankThread()).start();//����̹���ƶ��߳�
		new Thread(new EnemyTankSpeedThread()).start();
		new Thread(new EnemyTankThread()).start();
		new Thread(new EnemyBulletThread()).start();
		new Thread(new EnergyThread()).start();	
		
			
	}
	
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		myWall.draw(g);
		myWall.hit(myTank);
		myTank.draw(g);//����̹�˵��Ի淽��
		myTank.hit(mybullets);
		myTank.crash(enemyTanks);
		if(mybullets.size()!=0) {
			for(int i = 0;i<mybullets.size(); i++) {
				mybBullet = mybullets.get(i);
				mybBullet.draw(g);
				myWall.hitByBullet(mybBullet);
				if(mybBullet.x>900||mybBullet.y>900||mybBullet.x<0||mybBullet.y<0) {
					mybullets.remove(i);   //���������ĳ���ӵ�ʵ�������귶Χ������Ļ�����䶪����
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
	} //˫����������Ӱ
	
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
		
	}   //�����ػ��߳�
	public class TankThread implements Runnable {
		public void run() {
			while(true) {
					myTank.move();
					myTank.hit(yourbullets);
				//System.out.println(mybullets.size()); //���������е��ӵ�����
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}  //̹��λ��ά���̣߳����ⰴ�°�����ʶ��Ϊ����֮��Ŀհ�����ʹ̹���ڰ��°�����һ˲��Ϳ�ʼ����ǰ��(Ҳ�ɽ�ҵ�����Ž�paint������)
	
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
		super("̹�˴�ս");
		this.setBackground(color);
		this.setLayout(null);
		this.setBounds(x, y, width, height);
		this.setVisible(true);
		this.setResizable(false);
		
		
	}//����������
	

}
