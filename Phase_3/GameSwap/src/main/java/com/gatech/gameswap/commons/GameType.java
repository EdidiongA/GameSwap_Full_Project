package com.gatech.gameswap.commons;

public enum GameType {
	
	BoardGame("Board Game"), 
	CardGame("Card Game"), 
	VideoGame("Video Game"), 
	ComputerGame("Computer Game"), 
	JigsawPuzzle("Jigsaw Puzzle");
	
	private String gameType;
	
	GameType(String gameType) {
		this.gameType = gameType;
	}
	public String getGameType() {
        return this.gameType;
    } 
}
