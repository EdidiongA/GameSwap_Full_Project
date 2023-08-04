package com.gatech.gameswap.service;

import java.util.List;

import com.gatech.gameswap.model.Item;
import com.gatech.gameswap.model.ItemSearchResult;
import com.gatech.gameswap.model.OwnedItemsSummary;

public interface ItemService {

	Long addItem(String email, Item item);
	List<Item> getOwnedItems(String email);
	OwnedItemsSummary getOwnedItemsSummary(String email);
	Item getItem(Long itemId, String email);
	List<ItemSearchResult> searchItems(String email, Item.ItemSearchKey searchKey, String searchFor);
	List<String> getVideoGamePlatforms();
}
