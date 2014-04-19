package com.game.screens;

import java.io.*;

import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.Screen;
import org.loon.framework.javase.game.core.graphics.component.LButton;
import org.loon.framework.javase.game.core.graphics.component.LPaper;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.timer.LTimerContext;
import org.loon.framework.javase.game.srpg.actor.SRPGStatus;

public class MenuScreen extends Screen {
	
	LButton start, end;
	 String[] data=new String[9];

	LPaper title;
	
	LImage palm = new LImage("assets/images/palm.png");
	LImage male1profile=new LImage("assets/images/male1_profile.png");
	int i = -1;
	
	SRPGStatus ps = new SRPGStatus();
    
	public MenuScreen() {

	}

	public void onLoad() {

		setBackground("assets/images/menuBackGround.jpg");
		
		start = new LButton("assets/images/playerState.png", 135, 40) {
			public void doClick() {
				i = 0;

			}
		};
		start.setLocation(70, 100);
		start.setEnabled(true);
		add(start);
		
		start = new LButton("assets/images/item.png", 74, 40) {
			public void doClick() {
				i = 1;

			}
		};
		start.setLocation(70, 160);
		start.setEnabled(true);
		add(start);
		
		
		start = new LButton("assets/images/load.png", 135, 40) {
			public void doClick() {
				//setScreen(new MyAVGScreen());
				try{
				 File file = new File("save.dat");
				 BufferedReader reader = new BufferedReader(new FileReader(file));
				 String text=null;
				 while ((text = reader.readLine()) != null){
					 data=text.split(",");
				 }
				 reader.close();
				 ((StartScreen)getScreens().get(2)).setLoad(data);
				}catch (IOException e) {
				}
			//	for(int i=0;i<9;i++){
			//		System.out.println(data[i]);
			//	}
				
                 i=2;
                 runIndexScreen(2);
			}
		};
		start.setLocation(70, 220);
		start.setEnabled(true);
		add(start);
		
		start = new LButton("assets/images/return.png", 169, 40) {
			public void doClick() {
				i=3;
				runFirstScreen();

			}
		};
		start.setLocation(68, 280);
		start.setEnabled(true);
		add(start);
		
		start = new LButton("assets/images/resume.png", 135, 40) {
			public void doClick() {
				runIndexScreen(2);
				//setScreen(MainScreen.currentScreen);

			}
		};
		start.setLocation(70, 340);
		start.setEnabled(true);
		add(start);

/*		// ����һ����¼��ȡ��ť�����տ�160����56�ֽⰴťͼ
		LButton btn2 = new LButton("assets/title_load.png", 160, 56);
		// �趨��ťλ��Ϊx=2,y=startλ������
		btn2.setLocation(2, start.getY() + start.getHeight() + 20);
		// �趨�˰�ť������
		btn2.setEnabled(false);
		// ��Ӱ�ť
		add(btn2);

		// ����һ���������ð�ť�����տ�215����57�ֽⰴťͼ
		LButton btn3 = new LButton("assets/title_option.png", 215, 57);
		// �趨��ťλ��Ϊx=2,y=btn2λ������
		btn3.setLocation(2, btn2.getY() + btn2.getHeight() + 20);
		// �趨�˰�ť������
		btn3.setEnabled(false);
		// ��Ӱ�ť
		add(btn3);

		// ����һ���˳���ť�����տ�142����57�ֽⰴťͼ�����趨��Click�¼�
		end = new LButton("assets/title_end.png", 142, 57) {
			public void doClick() {
				LSystem.exit();
			}
		};
		// �趨��ťλ��Ϊx=2,y=btn3λ������
		end.setLocation(2, btn3.getY() + btn3.getHeight() + 20);
		// �趨�˰�ť������
		end.setEnabled(false);
		// ��Ӱ�ť
		add(end);

		// ����һ������
		title = new LPaper("assets/title.png", -200, 0);
		// ��ӱ���
		add(title);*/
	}
	
	public void draw(LGraphics g) {
		if (i == 0) {
		    ps.name="С��";
			g.drawString(ps.name, 200 , 180);
			ps.name="С��Сʱ����Ϊ���ֻ����ˣ��Ӵ�����ʮ��������";
			g.drawString(ps.name, 200 , 200);
			ps.name="����תѧ��������XX��ѧ��ѧ��";
			g.drawString(ps.name, 200 , 220);
			g.drawImage(palm, 40, 100);
			g.drawImage(male1profile,550,80);
		}
		if (i == 1) {
			//g.drawString(Integer.toString(ps.ability[0]), 500 , 400);
			g.drawImage(palm, 40, 160);
			if(StartScreen.key==1){
				g.drawString(  "���ҵ���Կ��", 200, 180);
			}
		}
		if (i == 2) {
			//g.drawString(Integer.toString(ps.ability[0]), 500 , 400);
			g.drawImage(palm, 40, 220);
		}
		if (i == 3) {
			//g.drawString(Integer.toString(ps.ability[0]), 500 , 400);
			g.drawImage(palm, 40, 280);
		}
	}
	
	
	

	@Override
	public void alter(LTimerContext timer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTouchDown(LTouch e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTouchUp(LTouch e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTouchMove(LTouch e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onKeyDown(LKey e) {
		int keyCode = e.getKeyCode();
		switch(keyCode) {
		case 16: 
			runIndexScreen(2);
			
			//setScreen(MainScreen.currentScreen);
			break;
		}
	}

}
