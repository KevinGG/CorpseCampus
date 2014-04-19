package com.game.screens;

import java.awt.Color;

import org.loon.framework.javase.game.action.avg.AVGDialog;
import org.loon.framework.javase.game.action.avg.AVGScreen;
import org.loon.framework.javase.game.action.avg.command.Command;
import org.loon.framework.javase.game.core.graphics.component.LButton;
import org.loon.framework.javase.game.core.graphics.component.LMessage;
import org.loon.framework.javase.game.core.graphics.component.LPaper;
import org.loon.framework.javase.game.core.graphics.component.LSelect;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.timer.LTimerContext;



public class BillBoard extends AVGScreen {

	LPaper roleName;
	String path = "";
	
	// 自定义命令（有些自定义命令为了突出写成了中文，实际不推荐�?
	String flag = "自定义命�?";

	String[] selects = { "鹏凌三千帅不帅？" };

	int type;

	public BillBoard() {
		super("assets/script/s1.txt", AVGDialog.getRMXPDialog("assets/images/w6.png",100, 100));
	}

	public void onLoading() {
		roleName = new LPaper("assets/images/"+path, 0, 0);
		leftOn(roleName);
		roleName.setLocation(0, 0);
		add(roleName);
	}

	public void drawScreen(LGraphics g) {
		switch (type) {
		case 1:
			g.setAntiAlias(true);
			g.drawSixStart(Color.yellow, 130, 100, 100);
			g.setAntiAlias(false);
			break;

		default:
			break;
		}
	}


	public void initCommandConfig(Command command) {
		// 初始化时预设变量
		command.setVariable("p", "assets/images/p.png");
		command.setVariable("sel0", selects[0]);
	}

	public void initMessageConfig(LMessage message) {

	}

	public void initSelectConfig(LSelect select) {
	}

	public boolean nextScript(String mes) {

		// 自定义命令（有些自定义命令为了突出写成了中文，实际不推荐�?
		if (roleName != null) {
			if ("noname".equalsIgnoreCase(mes)) {
				roleName.setVisible(false);
			} else if ("name0".equalsIgnoreCase(mes)) {
				roleName.setVisible(true);
				roleName.setBackground("assets/images/name0.png");
				roleName.setLocation(5, 15);
			} else if ("name1".equalsIgnoreCase(mes)) {
				roleName.setVisible(true);
				roleName.setBackground("assets/images/name1.png");
				roleName.setLocation(getWidth() - roleName.getWidth() - 5, 15);
			}
		}
		if ((flag + "星星").equalsIgnoreCase(mes)) {
			// 添加脚本事件标记（需要点击后执行�?
			setScrFlag(true);
			type = 1;
			return false;
		} else if ((flag + "去死吧，星星").equalsIgnoreCase(mes)) {
			type = 0;
		} else if ((flag + "关于天才").equalsIgnoreCase(mes)) {
			message.setVisible(false);
			setScrFlag(true);
			// 强行锁定脚本
			setLocked(true);
			LButton yes = new LButton("assets/images/dialog_yes.png", 112, 33) {
				public void doClick() {
					// 解除锁定
					setLocked(false);
					// 触发事件
					// click();
					// 删除当前按钮
					remove(this);
				}
			};
			centerOn(yes);
			add(yes);
			return false;
		}
		return true;
	}

	public void onExit() {
		// 重新返回标题画面
		setScreen(new MenuScreen());
	}

	public void onKeyDown(LKey e) {
		
		int keyCode = e.getKeyCode();
		
		switch(keyCode) {	
				case 32:
					if(path != "GameOver1.png" && path != "WIN.png")
						runIndexScreen(2);
					else
						runIndexScreen(0);
					break;
		
		}	
	}
	public void onSelect(String message, int type) {
		if (selects[0].equalsIgnoreCase(message)) {
			command.setVariable("sel0", String.valueOf(type));
		}
	}

	public void alter(LTimerContext timer) {
	
	}
	
	public void setPath(String p) {
		path = p;
	}

}
