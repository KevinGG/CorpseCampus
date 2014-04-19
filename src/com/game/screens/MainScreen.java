package com.game.screens;

import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.loon.framework.javase.game.GameDeploy;
import org.loon.framework.javase.game.GameScene;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.Screen;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.timer.LTimerContext;

public class MainScreen extends Screen {
	
	private LImage background = LImage.createImage("assets/images/OP.jpg");
	private LImage bloodypalm = LImage.createImage("assets/images/palm.png");
	int selected = 0;
	public static GameScene scene = null;
	//public static Screen currentScreen = null;

	public MainScreen() {
		this.playSound("assets/audios/1.wav");
	}

	@Override
	public void alter(LTimerContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(LGraphics g) {			
		g.drawImage(background, 0, 0);
		int y = 0;
		switch(selected) {
		case 0: y = 363; break;
		case 1: y = 408; break;
		case 2: y = 453; break;
		}
		g.drawImage(bloodypalm, 300, y);	
		
		/*g.setColor(new LColor(255,0,0));
		g.drawRect(336, 372, 110, 28);
		g.drawRect(336, 417, 90, 28);
		g.drawRect(336, 462, 90, 28);*/
	}
	
	public static void main(String [] args) {
		scene = new GameScene("Campus", 800, 600);
		GameDeploy deploy = scene.getDeploy();
		deploy.addScreen(new MainScreen());
		deploy.addScreen(new MenuScreen());
		deploy.addScreen(new StartScreen());
		deploy.addScreen(new BillBoard());
		deploy.runFirstScreen();
		deploy.mainLoop();
		deploy.setShowFPS(true);
		deploy.setShowMemory(true);
		scene.showFrame(); 
	}

	@Override
	public void rightClick(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTouchDown(LTouch e) {
		int x = getTouchX();
		int y = getTouchY();
		if(x>=336 && x<=446 && y>=372 && y<=490) {
			if(y <= 400) {
				selected = 0;
				getScreens().set(2, new StartScreen());
				runIndexScreen(2);
			}
			else if(y >= 417 && y <= 435 && x <= 426) {
				selected = 1;
				String[] data = null;
				try{
					 File file = new File("save.dat");
					 BufferedReader reader = new BufferedReader(new FileReader(file));
					 String text=null;
					 while ((text = reader.readLine()) != null){
						 data=text.split(",");
					 }
					 reader.close();
					 ((StartScreen)getScreens().get(2)).setLoad(data);
					}catch (IOException ex) {
					}
				//	for(int i=0;i<9;i++){
				//		System.out.println(data[i]);
				//	}
					
	                 runIndexScreen(2);
				//load;
			}
			else if(y >= 462 && x <= 426) {
				selected = 2;
				scene.close();
			}
		}
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
		int t = e.getKeyCode();
		switch(t) {
		case 40: selected = (++selected)%3; break;
		case 38: selected = (--selected)<0 ? selected+3 : selected%3; break;
		case 10: switch(selected){
			case 0: /*setScreen(new StartScreen());*/
				getScreens().set(2, new StartScreen());
				runIndexScreen(2);
				break;
			case 1: break;
			case 2: scene.close();break;
		}
		break;
		default :
		}
	}
	
}

