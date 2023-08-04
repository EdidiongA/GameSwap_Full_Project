package com.gatech.gameswap.service;

import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gatech.gameswap.model.History;
import com.gatech.gameswap.model.Item;
import com.gatech.gameswap.model.Swap;
import com.gatech.gameswap.model.SwapAck;
import com.gatech.gameswap.model.SwapDetail;
import com.gatech.gameswap.model.SwapRating;
import com.gatech.gameswap.repository.SwapRepository;

@Service
public class SwapServiceImpl implements SwapService{
	
	@Autowired
	SwapRepository swapRepository;
	
	@Override
	public Boolean swapRequest(Swap swap) throws SQLException{
		return swapRepository.swapRequest(swap);
		
	}

	@Override
	public Boolean swapAccept(Long proposerItemID, Long counterPartyItemID) throws SQLException {
		return swapRepository.swapAccept(proposerItemID, counterPartyItemID);
	}

	@Override
	public Boolean swapReject(Long proposerItemID, Long counterPartyItemID) throws SQLException {
		return swapRepository.swapReject(proposerItemID, counterPartyItemID);
	}

	@Override
	public Boolean upadteSwapRating(Long swapID, String userID, int rating) throws SQLException {
		return swapRepository.upadteSwapRating(swapID, userID, rating);
	}

	@Override
	public List<Item> proposeSwap(String userID,Long itemID) throws SQLException {
		return swapRepository.proposeSwap(userID,itemID);
	}

	@Override
	public List<SwapAck> ackPage(String userID) throws SQLException {
		return swapRepository.ackPage(userID);
	}

	@Override
	public List<SwapRating> unRatedSwap(String userID) throws SQLException {
		return swapRepository.unRatedSwap(userID);
	}

	@Override
	public History swapHistory(String userID) throws SQLException {
		return swapRepository.swapHistory(userID);
	}

	@Override
	public SwapDetail swapDetail(String userID, Long swapID) throws SQLException {
		return swapRepository.swapDetail(userID, swapID);
	}
}
