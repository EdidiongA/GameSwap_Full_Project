package com.gatech.gameswap.model;

import com.fasterxml.jackson.annotation.JsonInclude;
//import com.gatech.gameswap.commons.Condition;
//import com.gatech.gameswap.commons.GameType;


public class Item {

	public enum ItemSearchKey {
		  Keyword, MyPostalCode, Miles, PostalCode
	}
	

	
	
	private Long id;
	private String name;
	private String description;
	private String fullDescription;
	private String condition;
	private String gameType;
	
	
	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private GameTypeMetadata gameTypeMetadata;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ItemOwner itemOwner;
	
	public ItemOwner getItemOwner() {
		return itemOwner;
	}

	public void setItemOwner(ItemOwner itemOwner) {
		this.itemOwner = itemOwner;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", description=" + description + ", condition=" + condition
				+ ", gameType=" + gameType + ", gameTypeMetadata=" + gameTypeMetadata + "]";
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

	public GameTypeMetadata getGameTypeMetadata() {
		return gameTypeMetadata;
	}
	public void setGameTypeMetadata(GameTypeMetadata gameTypeMetadata) {
		this.gameTypeMetadata = gameTypeMetadata;
	}
		

}
