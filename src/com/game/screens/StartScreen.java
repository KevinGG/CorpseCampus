package com.game.screens;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.loon.framework.javase.game.action.sprite.AnimationHelper;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.timer.LTimer;
import org.loon.framework.javase.game.core.timer.LTimerContext;
import org.loon.framework.javase.game.srpg.SRPGScreen;
import org.loon.framework.javase.game.srpg.ability.SRPGAbilityFactory;
import org.loon.framework.javase.game.srpg.ability.SRPGDamageData;
import org.loon.framework.javase.game.srpg.actor.SRPGActor;
import org.loon.framework.javase.game.srpg.actor.SRPGActorFactory;
import org.loon.framework.javase.game.srpg.actor.SRPGActors;
import org.loon.framework.javase.game.srpg.field.SRPGField;
import org.loon.framework.javase.game.srpg.field.SRPGFieldElement;
import org.loon.framework.javase.game.srpg.field.SRPGFieldElements;
import org.loon.framework.javase.game.srpg.field.SRPGTeams;

public class StartScreen extends SRPGScreen {
	//static int positionX=8;
	//static int positionY=9;
	private final int stepWidth = 20;
	private final int wSteps = 40, hSteps = 30;
	private final int zombieNum = 3;
	private char mapVisit[][] = new char[hSteps][wSteps];
	private SRPGActor p=null, q=null;
	private List<SRPGActor> zombies = new ArrayList<SRPGActor>(), zombiesTemp = new ArrayList<SRPGActor>();
	public static  int key=0;
	private String[] data=null;
	public static int keyPosX=0,keyPosY=0;
	LTimer randomTimer = new LTimer(2000);
	LTimer zombieTimer = new LTimer(5000);
	List<LTimer> zMoveTimer = new ArrayList<LTimer>();
	
	public StartScreen() {
		super("assets/images/classroom.txt", null, 20, 20);
		if(!readMapVisitFile("assets/images/classroom.txt", mapVisit, 30))
			MainScreen.scene.close();  //最好提示	
		SRPGAbilityFactory.makeDefAbilitys();
		SRPGActorFactory.makeDefActorStatus();
		p = new SRPGActor(SRPGActorFactory.makeActorStatus("p", 10, 5, 0), AnimationHelper.makeObject("assets/images/maleffx.png",  4, 4, 20, 60), 20, 20);
		q = new SRPGActor(SRPGActorFactory.makeActorStatus("q", 10, 5, 1), AnimationHelper.makeRMXPObject("assets/images/male5.png"), 20, 20);
		for(int i = 0; i < zombieNum; i++) {
			LTimer t = new LTimer(500);
			t.stop();
			zMoveTimer.add(t);
		}
		p.setPos(10, 10);
		q.setPos(20, 9);
		for(int i = 0; i < zombieNum; i++) {
			SRPGActor actor = new SRPGActor(SRPGActorFactory.makeActorStatus("p", 10, 5, 0), AnimationHelper.makeObject("assets/images/monster.png",  4, 4, 20, 60), 20, 20);
			actor.setPos(-10000, -10000);
			zombiesTemp.add(actor);
		}
		while(mapVisit[keyPosY][keyPosX] != '0')
			produceRandomKey();
		System.out.println(keyPosX+"   "+keyPosY);
	}
	
	static public boolean readMapVisitFile(String fileName, char[][] m, int row){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			int i = 0;
			String strLine = null;
			while((strLine = reader.readLine()) != null && i<row) {
				strLine = strLine.replaceAll(",", "");
				m[i++] = strLine.toCharArray();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void setLoad(String[] d){
		p.setPos(Integer.parseInt(d[0]),Integer.parseInt(d[1]));
		for(int i = 0; i < zombieNum; i++) {
		zombiesTemp.get(i).setPos(Integer.parseInt(d[2*i+2]),Integer.parseInt(d[2*i+3]));
		}
		key=Integer.parseInt(d[8]);
	}
	
	static synchronized public boolean checkHit(SRPGActor actor, char[][] mapVisit, int stepWidth) {
		int direction = actor.getDirection();		
		int actorHeight = (actor.getActionImage().getHeight(null)+(stepWidth>>1))/stepWidth;
		int actorWidth = (actor.getActionImage().getWidth(null)+(stepWidth>>1))/stepWidth;
		int x = actor.getPosX() - ((actorWidth-1)>>1);
		//int y = actor.getPosY() - ((actorHeight-1)>>1);
		int y = actor.getPosY() + (actorHeight>>1);
		//System.out.println("direction:"+direction+" X:"+x+" Y:"+ y + "	"+actorHeight+"::"+actorWidth);
		switch(direction) {
		case MOVE_UP:
			if((y-1)<0)
				return true;
			return intersection(x, y-1, actorWidth, 1, mapVisit);
		case MOVE_DOWN: 
			if((y+1)>29)
				return true;
			return intersection(x, y+1, actorWidth, 1, mapVisit);
		case MOVE_LEFT: 
			if((x-1)<0)
				return true;
			return intersection(x-1, y, 1, /*actorHeight*/1, mapVisit);
		case MOVE_RIGHT:
			if((x+1)>39)
				return true;
			return intersection(x+1, y, 1, /*actorHeight*/1, mapVisit);
		default:  break;
		}
		return false;
	}
	
	static public boolean intersection(int x, int y, int w, int h, char[][] mapVisit) {
		for(int i=0; i<h; i++) {			
			for(int j=0; j<w; j++) {
				//System.out.println("Y:"+y+" X:"+(x+j) + "	Visit:"+mapVisit[y][x+j]);
				if(mapVisit[y][x+j] == '1')
					return true;
			}
			y ++;
		}
		return false;
	}

	@Override
	protected void initFieldElementConfig(SRPGFieldElements elements) {
		// TODO Auto-generated method stub
		//elements.putBattleElement(0, ELEMENT_PALACE, "皇宫");
		//elements.putBattleElement(1, ELEMENT_WALL, "雕塑");
	}

	@Override
	protected void initMapConfig(SRPGField field) {
		// TODO Auto-generated method stub
		field.setBigImageMap("assets/images/classroom.png");
	}
	
	boolean bCreate = false;
	@Override
	protected void initActorConfig(SRPGActors actors) {
		actors.putActor(0, p);
		actors.putActor(1, q);
		for(int i = 0; i < zombieNum; i++)
			actors.putActor(2+i, zombiesTemp.get(i));
	}

	@Override
	protected void initTeamConfig(SRPGTeams team) {
		// TODO Auto-generated method stub
		team.setTeams(new String[]{"教室1-C", "教室1-C"});
	}

	@Override
	protected boolean startProcess() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean endProcess() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void processChangePhaseBefore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processChangePhaseAfter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processDeadActorBefore(int index, SRPGActor actor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processDeadActorAfter(int index, SRPGActor actor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processAttackBefore(int index, SRPGActor actor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processAttackAfter(int index, SRPGActor actor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processLevelUpBefore(int index, SRPGActor actor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processLevelUpAfter(int index, SRPGActor actor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processDamageInputAfter(SRPGDamageData damagedata, int atk,
			int def) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processDamageInputBefore(SRPGDamageData damagedata, int atk,
			int def) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClickActor(SRPGActor actor, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClickField(SRPGFieldElement element, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDown(LTouch e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUp(LTouch e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMove(LTouch e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoading() {
		//randomTimer.start();
		zombieTimer.start();
	}
	
	private int pos[] = new int[2];
	private Random random = new Random();
	
	public void produceRandomPos() {
		pos[0] = random.nextInt() % wSteps;
		pos[1] = random.nextInt() % hSteps;
		if(pos[0] < 0)
			pos[0] += wSteps;
		if(pos[1] < 0)
			pos[1] += hSteps;
	}
	
	public void produceRandomKey() {
		keyPosX = random.nextInt() % wSteps;
		keyPosY = random.nextInt() % hSteps;
		if(keyPosX < 0)
			keyPosX += wSteps;
		if(keyPosY < 0)
			keyPosY += hSteps;
	}
	
	private void produceZombie() {
		if(zombies.size() >= zombieNum) {
			zombieTimer.stop();
			return ;
		}
		produceRandomPos();
		while(mapVisit[pos[1]][pos[0]] != '0')
			produceRandomPos();
		SRPGActor a = this.getSRPGActors().find(zombies.size()+2);
		a.setPos(pos[0], pos[1]);
		zMoveTimer.get(zombies.size()).start();
		zombies.add(a);
	}
	
	private void zombieAction(int index) {
		int target[] = p.getPos();
		SRPGActor a = zombies.get(index);
		int own[] = a.getPos();
		target[0] -= own[0];
		target[1] -= own[1];
		int absx = Math.abs(target[0]), absy = Math.abs(target[1]);
		int unitx = target[0] > 0 ? 1 : -1;
		int unity = target[1] > 0 ? 1 : -1;
//		if(absx > absy) {
//			own[1] += target[1] > 0 ? 1 : -1;
//			own[0] += absy==0 ? 1 : target[0] > 0 ? (absx/absy+0.5f) : -(absx/absy+0.5f);
//		}
//		else {
//			own[0] += target[0] > 0 ? 1 : -1;
//			own[1] += absy==0 ? 1 : target[1] > 0 ? (absx/absy+0.5f) : -(absx/absy+0.5f);
//		}
//		a.setPos(own);
//		if(absx > absy)
//			if(target[0] > 0)
//				a.setDirection(MOVE_RIGHT);
//			else
//				a.setDirection(MOVE_LEFT);
//		else
//			if(target[1] > 0)
//				a.setDirection(MOVE_DOWN);
//			else
//				a.setDirection(MOVE_UP);
		int der = MOVE_UP;
		if(absx > absy) {
			if(mapVisit[own[1]][own[0]+unitx] == '0') {
				own[0] = own[0]+unitx;
				der = unitx > 0 ? MOVE_RIGHT : MOVE_LEFT;
			}
			else if(mapVisit[own[1]+unity][own[0]] == '0') {
				own[1] = own[1] + unity;
				der = unity > 0 ? MOVE_DOWN : MOVE_UP;
			}
			else if(mapVisit[own[1]-unity][own[0]] == '0') {
				own[1] = own[1] - unity;
				der = unity > 0 ? MOVE_UP : MOVE_DOWN;
			}
			else if(mapVisit[own[1]][own[0]-unitx] == '0') {
				own[0] = own[0]-unitx;
				der = unitx < 0 ? MOVE_RIGHT : MOVE_LEFT;
			}
		}
		else {
			if(mapVisit[own[1]+unity][own[0]] == '0') {
				own[1] = own[1] + unity;
				der = unity > 0 ? MOVE_DOWN : MOVE_UP;
			}
			else if(mapVisit[own[1]][own[0]+unitx] == '0') {
				own[0] = own[0]+unitx;
				der = unitx > 0 ? MOVE_RIGHT : MOVE_LEFT;
			}
			else if(mapVisit[own[1]][own[0]-unitx] == '0') {
				own[0] = own[0]-unitx;
				der = unitx < 0 ? MOVE_RIGHT : MOVE_LEFT;
			}
			else if(mapVisit[own[1]-unity][own[0]] == '0') {
				own[1] = own[1] - unity;
				der = unity > 0 ? MOVE_UP : MOVE_DOWN;
			}
		}
		a.setPos(own);
		a.setDirection(der);
		target = p.getPos();
		if(own[0] == target[0] && own[1] == target[1])
			gameOver();	
	}

	@Override
	public void alter(LTimerContext timer) {
		if(randomTimer.action(timer.getTimeSinceLastUpdate())) {
			
		}
		if(zombieTimer.action(timer.getTimeSinceLastUpdate())) {
			produceZombie();
		}
		for(int i=0; i < zombieNum; i++) {
			if(zMoveTimer.get(i).action(timer.getTimeSinceLastUpdate()))
				zombieAction(i);
		}
	}
	
	private boolean ranomDie(int posx, int posy) {
		produceRandomPos();
		//int distance = (posx-pos[0])*(posx-pos[0]) + (posy-pos[1])*(posy-pos[1]);
		System.out.println("randomDie: posx:"+posx+" posy:"+posy+" pos:"+pos[0]+" "+pos[1]);
		if(posx==pos[0] && pos[1]==posy)
			return true;
		return false;
	}
	
	private void stopTimer() {
		randomTimer.stop();
		zombieTimer.stop();
		for(int i = 0; i < zMoveTimer.size(); i++)
			zMoveTimer.get(i).stop();
	}
	
	private void gameOver() {	
		//stopTimer();
		playSound("assets/audios/Scream.wav");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//setEndView(true);
		((BillBoard)getScreens().get(3)).setPath("GameOver1.png");
		runIndexScreen(3);
	}
		
	@Override
	public void onKeyDown(LKey e) {
		SRPGActor currentSRPGActor = p;
		int keyCode = e.getKeyCode();
		int posX = currentSRPGActor.getPosX();
		int posY = currentSRPGActor.getPosY();
		if(ranomDie(posX, posY)) {
			//LImage bloodypalm = LImage.createImage("assets/images/RandomDie.png");	
			gameOver();
			return;
		}
		setCenterActor(0);
		switch(keyCode) {
		case 37: //left
			currentSRPGActor.setDirection(MOVE_LEFT);
			if(!checkHit(currentSRPGActor, mapVisit, stepWidth))
				currentSRPGActor.setPosX(posX-1);
			break;  
		case 38: //up
			currentSRPGActor.setDirection(MOVE_UP);
			if(!checkHit(currentSRPGActor, mapVisit, stepWidth))
				currentSRPGActor.setPosY(posY-1);
			break;  
		case 39: //right
			currentSRPGActor.setDirection(MOVE_RIGHT);
			if(!checkHit(currentSRPGActor, mapVisit, stepWidth))
				currentSRPGActor.setPosX(posX+1);
			break;  
		case 40: //down
			currentSRPGActor.setDirection(MOVE_DOWN);
			if(!checkHit(currentSRPGActor, mapVisit, stepWidth))
				currentSRPGActor.setPosY(posY+1);
			break;  
		case 32: //space
			if(currentSRPGActor.getPosY()+1==8 && 2*currentSRPGActor.getPosX()+1==9 && currentSRPGActor.getDirection()==MOVE_UP){
				((BillBoard)getScreens().get(3)).setPath("notice1.png");
				runIndexScreen(3);
			}
			if((posX >=16 && posX <= 24) && (posY>=5 && posY<=6) && currentSRPGActor.getDirection()==MOVE_UP){
				((BillBoard)getScreens().get(3)).setPath("BlackBoard_Class1.png");
				runIndexScreen(3);
			}
			if((posX==keyPosX && posY==keyPosY)){
				key=1;
			}
			if((posX==36 && (posY==8 || posY==9 || posY==27 || posY==28)) && key==1){
				((BillBoard)getScreens().get(3)).setPath("WIN.png");
				runIndexScreen(3);
			}
			System.out.println(posX+"   "+posY);
			//if(currentSRPGActor.getPosY()+1==  && 2*currentSRPGActor.getPosX()+1==  && currentSRPGActor.getDirection()==MOVE_UP){
			//	runIndex
			//}
			break;  
		case 16: //shift  菜单
			runIndexScreen(1);
			break;
		case 17:
			File file = new File("save.dat");
			try {
				BufferedWriter save = new BufferedWriter(new FileWriter(file));
				save.write(p.getPosX()+","+p.getPosY()+","+zombiesTemp.get(0).getPosX()+","+zombiesTemp.get(0).getPosY()+","+zombiesTemp.get(1).getPosX()+","+zombiesTemp.get(1).getPosY()+","+zombiesTemp.get(2).getPosX()+","+zombiesTemp.get(2).getPosY()+","+key);
				save.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		default:
		}
		//System.out.println("Row: "+(currentSRPGActor.getPosY()+1)+"  col: "+(2*currentSRPGActor.getPosX()+1));
	}
}
