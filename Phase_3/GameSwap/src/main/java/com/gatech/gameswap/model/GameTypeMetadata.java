package com.gatech.gameswap.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class GameTypeMetadata {
	
	public enum VideoGameMedia {
		OpticalDisc, GameCard, Cartridge
	}
	
	public enum ComputerGamePlatform {
		Linux, Macos, Windows
	}
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String videoGamePlatform;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String videoGameMedia;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String computerGamePlatform;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer jigsawPuzzlePieceCount;
	
	public String getVideoGamePlatform() {
		return videoGamePlatform;
	}
	public void setVideoGamePlatform(String videoGamePlatform) {
		this.videoGamePlatform = videoGamePlatform;
	}
	public String getVideoGameMedia() {
		return videoGameMedia;
	}
	public void setVideoGameMedia(String videoGameMedia) {
		this.videoGameMedia = videoGameMedia;
	}
	public String getComputerGamePlatform() {
		return computerGamePlatform;
	}
	public void setComputerGamePlatform(String computerGamePlatform) {
		this.computerGamePlatform = computerGamePlatform;
	}
	public Integer getJigsawPuzzlePieceCount() {
		return jigsawPuzzlePieceCount;
	}
	public void setJigsawPuzzlePieceCount(Integer jigsawPuzzlePieceCount) {
		this.jigsawPuzzlePieceCount = jigsawPuzzlePieceCount;
	}
	
}
