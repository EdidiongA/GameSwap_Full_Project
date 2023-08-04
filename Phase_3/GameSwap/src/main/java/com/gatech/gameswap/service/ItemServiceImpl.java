package com.gatech.gameswap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gatech.gameswap.model.Item;
import com.gatech.gameswap.model.Item.ItemSearchKey;
import com.gatech.gameswap.model.ItemSearchResult;
import com.gatech.gameswap.model.OwnedItemsSummary;
import com.gatech.gameswap.repository.ItemRepository;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	ItemRepository itemRepository;
	
	@Override
	public Long addItem(String email, Item item) {
		return itemRepository.addItem(email, item);
	}

	@Override
	public List<ItemSearchResult> searchItems(String email, ItemSearchKey searchKey, String searchFor) {
		return itemRepository.searchItems(email, searchKey, searchFor);
	}

	@Override
	public List<Item> getOwnedItems(String email) {
		return itemRepository.getOwnedItems(email);
	}

	@Override
	public Item getItem(Long itemId, String email) {
		return itemRepository.getItem(itemId, email);
	}

	@Override
	public OwnedItemsSummary getOwnedItemsSummary(String email) {
		return itemRepository.getOwnedItemsSummary(email);
	}

	@Override
	public List<String> getVideoGamePlatforms() {
		return itemRepository.getVideoGamePlatforms();
	}

}
