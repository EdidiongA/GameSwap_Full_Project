package com.gatech.gameswap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gatech.gameswap.model.History;
import com.gatech.gameswap.model.Item;
import com.gatech.gameswap.model.Swap;
import com.gatech.gameswap.model.SwapAck;
import com.gatech.gameswap.model.SwapDetail;
import com.gatech.gameswap.model.SwapRating;
import com.gatech.gameswap.service.SwapService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/swap")
public class SwapController {
	
	@Autowired
	SwapService swapService;
	
	
	@GetMapping(path= "/proposeswap")
	@ApiOperation("list the given users items which is allowed for swwap")
	public ResponseEntity<List<Item>> proposeSwap(@RequestParam String userID, @RequestParam Long itemID){
	
		 List<Item> userItemList = null;
		try{
			userItemList = swapService.proposeSwap(userID,itemID);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<Item>>(userItemList, userItemList != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path= "/acceptrejectswap")
	@ApiOperation("List the unaccepted swap list for given user")
	public ResponseEntity<List<SwapAck>> swapAck(@RequestParam String userID){
	
		List<SwapAck> aclList = null;
		try{
			aclList = swapService.ackPage(userID);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<SwapAck>>(aclList, aclList != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path= "/unratedswap")
	@ApiOperation("List the unrated swap list for given user")
	public ResponseEntity<List<SwapRating>> unRatedSwapList(@RequestParam String userID){
	
		List<SwapRating> unRatedList = null;
		try{
			unRatedList = swapService.unRatedSwap(userID);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<SwapRating>>(unRatedList, unRatedList != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path= "/swaphistory")
	@ApiOperation("List all the swap performed by given user")
	public ResponseEntity<History> viewSwapHistory(@RequestParam String userID){
	
		History swapHistoryList = null;
		try{
			swapHistoryList = swapService.swapHistory(userID);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<History>(swapHistoryList, swapHistoryList != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path= "/swapdetails")
	@ApiOperation("Display swap information of selected swap")
	public ResponseEntity<SwapDetail> viewSwapDetails(@RequestParam String userID,@RequestParam Long swapID){
	
		SwapDetail swapdetail = null;
		try{
			swapdetail = swapService.swapDetail(userID,swapID);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<SwapDetail>(swapdetail, swapdetail != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping(path= "/confirmswap")
	@ApiOperation("Add selected user item to swap for desired item in swap table")
	public ResponseEntity<Integer> confirmSwap(@RequestBody Swap swap){
	
		
		//Boolean success = false;
		try{
			Boolean swapStatus = swapService.swapRequest(swap);
			
			if(swapStatus)
				return new ResponseEntity<Integer>(HttpStatus.CREATED);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
	}

	
	@PostMapping(path= "/accept")
	@ApiOperation("Insert to Acknowledgedswap table once the swap is accpted")
	public ResponseEntity<Integer> acceptSwap(@RequestParam Long proposerItemID, @RequestParam Long counterPartyItemID){
	
		try{
			Boolean acceptStatus = swapService.swapAccept(proposerItemID,counterPartyItemID);
			
			if(acceptStatus)
				return new ResponseEntity<Integer>(HttpStatus.ACCEPTED);
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping(path= "/reject")
	@ApiOperation("Insert to Acknowledgedswap table once the swap is rejected")
	public ResponseEntity<Integer> rejectSwap(@RequestParam Long proposerItemID, @RequestParam Long counterPartyItemID){
	
		try{
			Boolean acceptStatus = swapService.swapReject(proposerItemID,counterPartyItemID);
			
			if(acceptStatus)
				return new ResponseEntity<Integer>(HttpStatus.ACCEPTED);
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping(path= "/ratingupdate")
	@ApiOperation("Insert to ratedswap table once the swap is rated")
	public ResponseEntity<Integer> updateRting(@RequestParam Long swapID, @RequestParam String userID, @RequestParam int rating){
	
		try{
			Boolean acceptStatus = swapService.upadteSwapRating(swapID,userID,rating);
			
			if(acceptStatus)
				return new ResponseEntity<Integer>(HttpStatus.ACCEPTED);
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
	}
	
}
