package com.gatech.gameswap.model;

public class OwnedItemsSummary {

	private Integer boardGamesCount;
	private Integer cardGamesCount;
	private Integer computerGamesCount;
	private Integer jigsawPuzzlesCount;
	private Integer videoGamesCount;
	private Integer totalCount;
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	@Override
	public String toString() {
		return "OwnedItemsSummary [boardGamesCount=" + boardGamesCount + ", cardGamesCount=" + cardGamesCount
				+ ", computerGamesCount=" + computerGamesCount + ", jigsawPuzzlesCount=" + jigsawPuzzlesCount
				+ ", videoGamesCount=" + videoGamesCount + ", totalCount=" + totalCount + "]";
	}
	public Integer getBoardGamesCount() {
		return boardGamesCount;
	}
	public void setBoardGamesCount(Integer boardGamesCount) {
		this.boardGamesCount = boardGamesCount;
	}
	public Integer getCardGamesCount() {
		return cardGamesCount;
	}
	public void setCardGamesCount(Integer cardGamesCount) {
		this.cardGamesCount = cardGamesCount;
	}
	public Integer getComputerGamesCount() {
		return computerGamesCount;
	}
	public void setComputerGamesCount(Integer computerGamesCount) {
		this.computerGamesCount = computerGamesCount;
	}
	public Integer getJigsawPuzzlesCount() {
		return jigsawPuzzlesCount;
	}
	public void setJigsawPuzzlesCount(Integer jigsawPuzzlesCount) {
		this.jigsawPuzzlesCount = jigsawPuzzlesCount;
	}
	public Integer getVideoGamesCount() {
		return videoGamesCount;
	}
	public void setVideoGamesCount(Integer videoGamesCount) {
		this.videoGamesCount = videoGamesCount;
	}
}
