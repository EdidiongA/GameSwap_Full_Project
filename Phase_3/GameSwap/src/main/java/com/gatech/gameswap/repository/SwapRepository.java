package com.gatech.gameswap.repository;

import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONObject;

import com.gatech.gameswap.model.History;
import com.gatech.gameswap.model.Item;
import com.gatech.gameswap.model.Swap;
import com.gatech.gameswap.model.SwapAck;
import com.gatech.gameswap.model.SwapDetail;
import com.gatech.gameswap.model.SwapRating;

public interface SwapRepository {
	Boolean swapRequest(Swap swap) throws SQLException;
	Boolean swapAccept(Long proposerItemID, Long counterPartyItemID) throws SQLException;
	Boolean swapReject(Long proposerItemID, Long counterPartyItemID) throws SQLException;
	Boolean upadteSwapRating(Long swapID, String userID, int rating) throws SQLException;
	List<Item> proposeSwap(String userID,Long itemID) throws SQLException;
	List<SwapAck> ackPage(String userID) throws SQLException;
	List<SwapRating> unRatedSwap(String userID) throws SQLException;
	History swapHistory(String userID) throws SQLException;
	SwapDetail swapDetail(String userID, Long swapID) throws SQLException;
}
