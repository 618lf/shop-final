package com.tmt.wechat.bean.menu;

import java.io.Serializable;

public class Menu implements Serializable {

	private static final long serialVersionUID = -3198768807105682259L;

	private MenuButtons menu;

	public MenuButtons getMenu() {
		return menu;
	}

	public void setMenu(MenuButtons menu) {
		this.menu = menu;
	}

}
