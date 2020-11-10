package br.com.appletpong.enums;

public enum State {
	GAME_MENU(0), GAME_RUNNING(1), GAME_OVER(2);

	private int value;

	private State(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
