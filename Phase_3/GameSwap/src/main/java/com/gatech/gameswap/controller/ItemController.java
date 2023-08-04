package com.gatech.gameswap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gatech.gameswap.model.Item;
import com.gatech.gameswap.model.ItemSearchResult;
import com.gatech.gameswap.model.OwnedItemsSummary;
import com.gatech.gameswap.service.ItemService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/item")
public class ItemController {

	@Autowired
	ItemService itemService;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("add item for user with given email")
	public ResponseEntity<Long> addItem(@RequestHeader String email, @RequestBody Item item) {

		Long itemId = null;
		try{
			itemId = itemService.addItem(email, item);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Long>(itemId, (itemId !=null && itemId > 0) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(path = "/owned", method = RequestMethod.GET)
	@ApiOperation("get owned items available for swapping for user with given email")
	public ResponseEntity<List<Item>> getOwnedItems(@RequestParam String email) {
		List<Item> items;
		try{
			items = itemService.getOwnedItems(email);
		}catch(Exception e) {
			e.printStackTrace();
			items = null;
		}
		return new ResponseEntity<List<Item>>(items, items != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(path = "/videoGamePlatforms", method = RequestMethod.GET)
	@ApiOperation("get list of available video game platforms")
	public ResponseEntity<List<String>> getVideoGamePlatforms() {
		List<String> videoGamePlatforms;
		try{
			videoGamePlatforms = itemService.getVideoGamePlatforms();
		}catch(Exception e) {
			e.printStackTrace();
			videoGamePlatforms = null;
		}
		return new ResponseEntity<List<String>>(videoGamePlatforms, videoGamePlatforms != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(path = "/owned/summary", method = RequestMethod.GET)
	@ApiOperation("get summary count of owned items available for swapping for user with given email")
	public ResponseEntity<OwnedItemsSummary> getOwnedItemsSummary(@RequestHeader String email) {
		OwnedItemsSummary ownedItemsSummary;
		try{
			ownedItemsSummary = itemService.getOwnedItemsSummary(email);
		}catch(Exception e) {
			e.printStackTrace();
			ownedItemsSummary = null;
		}
		return new ResponseEntity<OwnedItemsSummary>(ownedItemsSummary, ownedItemsSummary != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/{itemId}")
	@ApiOperation("search available items with respect to given user")
	public ResponseEntity<Item> getItem(@RequestHeader String email, @PathVariable("itemId") Long itemId) {
		Item item;
		try{
			item = itemService.getItem(itemId, email);
		}catch(Exception e) {
			e.printStackTrace();
			item = null;
		}
		return new ResponseEntity<Item>(item, item != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/search")
	@ApiOperation("search all available items globally with given search criteria")
	public ResponseEntity<List<ItemSearchResult>> searchItems(@RequestHeader String email, @RequestParam Item.ItemSearchKey searchKey, @RequestParam String searchValue) {
		List<ItemSearchResult> items;
		try{
			items = itemService.searchItems(email, searchKey, searchValue);
		}catch(Exception e) {
			e.printStackTrace();
			items = null;
		}
		return new ResponseEntity<List<ItemSearchResult>>(items, items != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
