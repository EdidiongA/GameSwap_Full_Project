package com.gatech.gameswap.model;

public class ItemSearchResult {

	private Item item;
	private Double distance;
	private Boolean isNameMatched;
	@Override
	public String toString() {
		return "ItemSearchResult [item=" + item + ", distance=" + distance + ", isNameMatched=" + isNameMatched
				+ ", isDescriptionMatched=" + isDescriptionMatched + "]";
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public Boolean getIsNameMatched() {
		return isNameMatched;
	}
	public void setIsNameMatched(Boolean isNameMatched) {
		this.isNameMatched = isNameMatched;
	}
	public Boolean getIsDescriptionMatched() {
		return isDescriptionMatched;
	}
	public void setIsDescriptionMatched(Boolean isDescriptionMatched) {
		this.isDescriptionMatched = isDescriptionMatched;
	}
	private Boolean isDescriptionMatched;

}
