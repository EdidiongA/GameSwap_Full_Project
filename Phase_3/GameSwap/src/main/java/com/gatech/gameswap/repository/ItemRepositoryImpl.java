package com.gatech.gameswap.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

//import com.gatech.gameswap.commons.Condition;
//import com.gatech.gameswap.commons.GameType;
import com.gatech.gameswap.model.GameTypeMetadata;
import com.gatech.gameswap.model.GameTypeMetadata.ComputerGamePlatform;
import com.gatech.gameswap.model.GameTypeMetadata.VideoGameMedia;
import com.gatech.gameswap.model.Item;
//import com.gatech.gameswap.model.Item.Condition;
//import com.gatech.gameswap.model.Item.GameType;
import com.gatech.gameswap.model.Item.ItemSearchKey;
import com.gatech.gameswap.model.ItemOwner;
import com.gatech.gameswap.model.ItemSearchResult;
import com.gatech.gameswap.model.Location;
import com.gatech.gameswap.model.OwnedItemsSummary;
import com.gatech.gameswap.model.User;
import com.gatech.gameswap.model.Phone;
import com.gatech.gameswap.model.Phone.PhoneNumberType;
import com.gatech.gameswap.model.Location;

@Repository
public class ItemRepositoryImpl implements ItemRepository{

	@Autowired
	private DataSource dataSource;

	Logger logger = Logger.getLogger(ItemRepositoryImpl.class);

	@Override
	public Long addItem(String email, Item item) {
		logger.info("Entered addItem()");
		Long itemId = null;
		try(Connection conn = dataSource.getConnection()) {

			try(PreparedStatement pstmt = conn.prepareStatement("insert into item(email, name, description, `condition`, type) values (?, ?, ?, ?, ?)")) {
				pstmt.setString(1, email);
				pstmt.setString(2, item.getName()); 
				pstmt.setString(3, item.getDescription());
				pstmt.setString(4, item.getCondition());
				pstmt.setString(5, item.getGameType());

				logger.info("Inserting item");
				if (pstmt.executeUpdate() == 0) {
					throw new Exception("Failed to insert item");
				}
			}

			try(PreparedStatement pstmt = conn.prepareStatement("select LAST_INSERT_ID()")) {
				logger.info("Getting Item ID for new item");
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) {
						itemId = rs.getLong(1);
					}
				}

				if (itemId <= 0) {
					throw new Exception("Error while getting auto generated Item ID for new item");
				}
			}
			
			if(item.getGameType().equals("Video Game")) {
				logger.info("Inserting VideoGame metadata");
				try(PreparedStatement pstmt = conn.prepareStatement("insert into VideoGame(item_id, platform_name, media) values (?, ?, ?)")) {
					pstmt.setLong(1, itemId); 
					pstmt.setString(2, item.getGameTypeMetadata().getVideoGamePlatform().toString());
					pstmt.setString(3, item.getGameTypeMetadata().getVideoGameMedia().toString());

					if (pstmt.executeUpdate() == 0) {
						throw new Exception("Failed to insert video game");
					}
				}
			} else if(item.getGameType().equals("Computer Game")) {
				logger.info("Inserting ComputerGame metadata");
				try(PreparedStatement pstmt = conn.prepareStatement("insert into ComputerGame(item_id, platform) values (?, ?)")) {
					pstmt.setLong(1, itemId); 
					pstmt.setString(2, item.getGameTypeMetadata().getComputerGamePlatform().toString());

					if (pstmt.executeUpdate() == 0) {
						throw new Exception("Failed to insert computer game");
					}
				}
			} else if(item.getGameType().equals("Jigsaw Puzzle")) {
				logger.info("Inserting JigsawPuzzle metadata");
				try(PreparedStatement pstmt = conn.prepareStatement("insert into JigsawPuzzle(item_id, piece_count) values (?, ?)")) {
					pstmt.setLong(1, itemId); 
					pstmt.setInt(2, item.getGameTypeMetadata().getJigsawPuzzlePieceCount());

					if (pstmt.executeUpdate() == 0) {
						throw new Exception("Failed to insert jigsaw puzzle");
					}
				}
			} else if(item.getGameType().equals("Board Game")) {
				logger.info("Inserting BoardGame metadata");
				try(PreparedStatement pstmt = conn.prepareStatement("insert into BoardGame(item_id) values (?)")) {
					pstmt.setLong(1, itemId);
					if (pstmt.executeUpdate() == 0) {
						throw new Exception("Failed to insert BoardGame");
					}
				}
			} else if(item.getGameType().equals("Card Game")) {
				logger.info("Inserting CardGame metadata");
				try(PreparedStatement pstmt = conn.prepareStatement("insert into CardGame(item_id) values (?)")) {
					pstmt.setLong(1, itemId); 


					if (pstmt.executeUpdate() == 0) {
						throw new Exception("Failed to insert CardGame");
					}

				}
			}else {
				throw new Exception("Unknown Game type");
			}
		}catch (Exception e) {
			itemId = null;
			logger.error("Error in addItem()", e);
		}
		logger.info("Completed addItem()");
		return itemId;
	}

	@Override
	public List<Item> getOwnedItems(String email) {
		List<Item> items = new ArrayList<>();
		try(Connection conn = dataSource.getConnection()){
			try(PreparedStatement pstmt = conn.prepareStatement("with items_available as ( select * from Item i where i.item_id not in ( select s.proposed_item_id from Swap s where s.proposer_email=? and (s.swap_id not in (select swap_id from AcknowledgedSwap) or s.swap_id in (select swap_id from AcknowledgedSwap a where a.status='ACCEPTED')) UNION select s.counterparty_item_id from Swap s where s.counterparty_email=? and (s.swap_id not in (select swap_id from AcknowledgedSwap) or s.swap_id in (select swap_id from AcknowledgedSwap a where a.status='ACCEPTED')) ) ) select i.item_id,i.name, case when length(i.description)>100 then concat(left(i.description, 100),'...') else i.description end description, i.description as fullDescription, i.`condition`, i.type from items_available i where i.email=? order by i.item_id")){
				pstmt.setString(1, email);
				pstmt.setString(2, email);
				pstmt.setString(3, email);
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) {
						Item item = new Item();
						item.setId(rs.getLong("item_id"));
						item.setName(rs.getString("name"));
						item.setDescription(rs.getString("description"));
						item.setCondition(rs.getString("condition"));
						item.setGameType(rs.getString("type"));
				
						/*if(item.getGameType().equals(GameType.VideoGame)) {
							GameTypeMetadata gameTypeMetadata = new GameTypeMetadata();
							gameTypeMetadata.setVideoGameMedia(VideoGameMedia.valueOf(rs.getString("vg_media")));
							gameTypeMetadata.setVideoGamePlatform(VideoGamePlatform.valueOf(rs.getString("vg_platform")));
							item.setGameTypeMetadata(gameTypeMetadata);
						} else if(item.getGameType().equals(GameType.ComputerGame)) {
							GameTypeMetadata gameTypeMetadata = new GameTypeMetadata();
							gameTypeMetadata.setComputerGamePlatform(ComputerGamePlatform.valueOf(rs.getString("cg_platform")));
							item.setGameTypeMetadata(gameTypeMetadata);
						} else if(item.getGameType().equals(GameType.JigsawPuzzle)) {
							GameTypeMetadata gameTypeMetadata = new GameTypeMetadata();
							gameTypeMetadata.setJigsawPuzzlePieceCount(rs.getInt("jp_piece_count"));
							item.setGameTypeMetadata(gameTypeMetadata);
						}*/
						items.add(item);
					}
				}
			}
		}catch (Exception e) {
			items = null;
			logger.error("Error in getItems()", e);
		}
		return items;
	}

	@Override
	public Item getItem(Long itemId, String email) {
		Item item = null;
		try(Connection conn = dataSource.getConnection()){
			try(PreparedStatement pstmt = conn.prepareStatement("with source_user as ( select u.email, l.latitude, l.longitude from User u, Location l where u.email = ? and u.postal_code = l.postal_code ) select i.email owner_email, i.name, case when length(i.description)>100 then concat(left(i.description, 100),'...') else i.description end description, i.description as fullDescription, i.`condition`, i.type, vg.platform_name vg_platform, vg.media vg_media, cg.platform cg_platform, jp.piece_count jp_piece_count, u.nickname, u.first_name, u.last_name, p.phone_number, p.share_phone_number, p.type as ptype, l.city, l.state, l.postal_code, case when (select 1 from RatedSwap rs where rs.email=i.email limit 1) is null then null else (select avg(rating) from RatedSwap rs where rs.email=i.email) end rating, 3958.75 * 2 * ATAN2(SQRT(POWER(SIN((radians(l.latitude-s.latitude))/2), 2) + COS(radians(s.latitude)) * COS(radians(l.latitude)) * POWER(SIN((radians(l.longitude)-radians(s.longitude))/2), 2)), SQRT(1-(POWER(SIN((radians(l.latitude-s.latitude))/2), 2) + COS(radians(s.latitude)) * COS(radians(l.latitude)) * POWER(SIN((radians(l.longitude)-radians(s.longitude))/2), 2)))) distance from source_user s inner join user u inner join location l on u.postal_code=l.postal_code inner join (select * from item where item_id=?) i on u.email=i.email left outer join phoneNumber p on u.email = p.email left outer join computergame cg on cg.item_id=i.item_id left outer join videogame vg on vg.item_id=i.item_id left outer join jigsawpuzzle jp on jp.item_id=i.item_id")){
				pstmt.setString(1, email);
				pstmt.setLong(2, itemId);
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) {
						item = new Item();
						item.setId(itemId);
						item.setName(rs.getString("name"));
						item.setDescription(rs.getString("description"));
						item.setFullDescription(rs.getString("fullDescription"));
						item.setCondition(rs.getString("condition"));
						item.setGameType(rs.getString("type"));

						if(item.getGameType().equals("Video Game")) {
							GameTypeMetadata gameTypeMetadata = new GameTypeMetadata();
							gameTypeMetadata.setVideoGameMedia(rs.getString("vg_media"));
							gameTypeMetadata.setVideoGamePlatform(rs.getString("vg_platform"));
							item.setGameTypeMetadata(gameTypeMetadata);
						} else if(item.getGameType().equals("Computer Game")) {
							GameTypeMetadata gameTypeMetadata = new GameTypeMetadata();
							gameTypeMetadata.setComputerGamePlatform(rs.getString("cg_platform"));
							item.setGameTypeMetadata(gameTypeMetadata);
						} else if(item.getGameType().equals("Jigsaw Puzzle")) {
							GameTypeMetadata gameTypeMetadata = new GameTypeMetadata();
							gameTypeMetadata.setJigsawPuzzlePieceCount(rs.getInt("jp_piece_count"));
							item.setGameTypeMetadata(gameTypeMetadata);
						}

						ItemOwner itemOwner = new ItemOwner();
						itemOwner.setDistance(Math.round(rs.getDouble("distance")*10)/10d);
						if(rs.getObject("rating")!=null) {
							itemOwner.setRating(Math.round(rs.getDouble("rating")*100)/100d);
						}
						User user = new User();
						user.setEmail(rs.getString("owner_email"));
						
						user.setNickName(rs.getString("nickName"));
						user.setFirstName(rs.getString("first_name"));
						user.setLastName(rs.getString("last_name"));
					
						 Phone phone = new Phone(); 
						 phone.setNumber(rs.getString("phone_number"));
						 phone.setShare(rs.getBoolean("share_phone_number")); 
						 //phone.setType(rs.getString("type"));
						 //System.out.println(rs.getString("ptype"));
						 if(rs.getString("ptype")!= null)
							 phone.setType(Enum.valueOf(PhoneNumberType.class,rs.getString("ptype")));
						 user.setPhone(phone);
						 

						Location location = new Location();
						location.setCity(rs.getString("city"));
						location.setState(rs.getString("state"));
						location.setPostalCode(rs.getString("postal_code"));
						user.setLocation(location);
						itemOwner.setUser(user);
						item.setItemOwner(itemOwner);
					}
				}
			}
		}catch (Exception e) {
			item = null;
			logger.error("Error in getItem()", e);
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public List<ItemSearchResult> searchItems(String email, ItemSearchKey searchKey, String searchFor) {
		List<ItemSearchResult> results = new ArrayList<>();
		try(Connection conn = dataSource.getConnection()){
			if(searchKey.equals(ItemSearchKey.Keyword)) {
				try(PreparedStatement pstmt = conn.prepareStatement("with source_user as ( select u.email, l.latitude, l.longitude from User u, Location l where u.email = ? and u.postal_code = l.postal_code ), items_available as ( select * from Item i where i.item_id not in ( select s.proposed_item_id from Swap s where s.swap_id not in (select swap_id from AcknowledgedSwap) or s.swap_id in (select swap_id from AcknowledgedSwap a where a.status='ACCEPTED') UNION select s.counterparty_item_id from Swap s where s.swap_id not in (select swap_id from AcknowledgedSwap) or s.swap_id in (select swap_id from AcknowledgedSwap a where a.status='ACCEPTED') ) and i.email <> ? ), items_distance as ( select i.item_id, 3958.75 * 2 * ATAN2(SQRT(POWER(SIN((radians(l.latitude-s.latitude))/2), 2) + COS(radians(s.latitude)) * COS(radians(l.latitude)) * POWER(SIN((radians(l.longitude)-radians(s.longitude))/2), 2)), SQRT(1-(POWER(SIN((radians(l.latitude-s.latitude))/2), 2) + COS(radians(s.latitude)) * COS(radians(l.latitude)) * POWER(SIN((radians(l.longitude)-radians(s.longitude))/2), 2)))) distance from items_available i, user u, location l, source_user s where i.email=u.email and u.postal_code=l.postal_code ) select * from (select i.item_id,i.name, case when length(i.description)>100 then concat(left(i.description, 100),'...') else i.description end description, i.description as fulldescription, i.`condition`, i.type, d.distance, case when lower(i.name) like concat('%', lower(?), '%') then 'true' else null end name_matched, case when lower(i.description) like concat('%', lower(?), '%') then 'true' else null end description_matched from items_available i, items_distance d where i.item_id=d.item_id) i where name_matched is not null or description_matched is not null order by distance, item_id")){
					pstmt.setString(1, email);
					pstmt.setString(2, email);
					pstmt.setString(3, searchFor);
					pstmt.setString(4, searchFor);

					try(ResultSet rs = pstmt.executeQuery()){
						while(rs.next()) {
							ItemSearchResult itemSearchResult = new ItemSearchResult();

							Item item = new Item();
							item.setId(rs.getLong("item_id"));
							item.setName(rs.getString("name"));
							item.setDescription(rs.getString("description"));
							item.setCondition(rs.getString("condition"));
							item.setGameType(rs.getString("type"));
							itemSearchResult.setItem(item);

							itemSearchResult.setDistance(Math.round(rs.getDouble("distance")*10)/10d);
							itemSearchResult.setIsNameMatched(rs.getBoolean("name_matched"));
							itemSearchResult.setIsDescriptionMatched(rs.getBoolean("description_matched"));

							results.add(itemSearchResult);
						}
					}
				}
			} else if(searchKey.equals(ItemSearchKey.Miles)) {
				try(PreparedStatement pstmt = conn.prepareStatement("with source_user as ( select u.email, l.latitude, l.longitude from User u, Location l where u.email = ? and u.postal_code = l.postal_code ), items_available as ( select * from Item i where i.item_id not in ( select s.proposed_item_id from Swap s where s.swap_id not in (select swap_id from AcknowledgedSwap) or s.swap_id in (select swap_id from AcknowledgedSwap a where a.status='ACCEPTED') UNION select s.counterparty_item_id from Swap s where s.swap_id not in (select swap_id from AcknowledgedSwap) or s.swap_id in (select swap_id from AcknowledgedSwap a where a.status='ACCEPTED') ) and i.email <> ? ), items_distance as ( select i.item_id, 3958.75 * 2 * ATAN2(SQRT(POWER(SIN((radians(l.latitude-s.latitude))/2), 2) + COS(radians(s.latitude)) * COS(radians(l.latitude)) * POWER(SIN((radians(l.longitude)-radians(s.longitude))/2), 2)), SQRT(1-(POWER(SIN((radians(l.latitude-s.latitude))/2), 2) + COS(radians(s.latitude)) * COS(radians(l.latitude)) * POWER(SIN((radians(l.longitude)-radians(s.longitude))/2), 2)))) distance from items_available i, user u, location l, source_user s where i.email=u.email and u.postal_code=l.postal_code ) select i.item_id,i.name, case when length(i.description)>100 then concat(left(i.description, 100),'...') else i.description end description, i.`condition`, i.type, d.distance from items_available i, items_distance d where i.item_id=d.item_id and d.distance<=? order by d.distance, i.item_id")){
					pstmt.setString(1, email);
					pstmt.setString(2, email);
					pstmt.setDouble(3, Double.parseDouble(searchFor));

					try(ResultSet rs = pstmt.executeQuery()){
						while(rs.next()) {
							ItemSearchResult itemSearchResult = new ItemSearchResult();

							Item item = new Item();
							item.setId(rs.getLong("item_id"));
							item.setName(rs.getString("name"));
							item.setDescription(rs.getString("description"));
							item.setCondition(rs.getString("condition"));
							item.setGameType(rs.getString("type"));
							itemSearchResult.setItem(item);
							itemSearchResult.setDistance(Math.round(rs.getDouble("distance")*10)/10d);

							results.add(itemSearchResult);
						}
					}
				}
			} else if(searchKey.equals(ItemSearchKey.MyPostalCode)) {
				try(PreparedStatement pstmt = conn.prepareStatement("with source_user as ( select u.email, l.postal_code, l.latitude, l.longitude from User u, Location l where u.email = ? and u.postal_code = l.postal_code ), items_available as ( select * from Item i where i.item_id not in ( select s.proposed_item_id from Swap s where s.swap_id not in (select swap_id from AcknowledgedSwap) or s.swap_id in (select swap_id from AcknowledgedSwap a where a.status='ACCEPTED') UNION select s.counterparty_item_id from Swap s where s.swap_id not in (select swap_id from AcknowledgedSwap) or s.swap_id in (select swap_id from AcknowledgedSwap a where a.status='ACCEPTED') ) and i.email <> ? ), items_distance as ( select i.item_id, 3958.75 * 2 * ATAN2(SQRT(POWER(SIN((radians(l.latitude-s.latitude))/2), 2) + COS(radians(s.latitude)) * COS(radians(l.latitude)) * POWER(SIN((radians(l.longitude)-radians(s.longitude))/2), 2)), SQRT(1-(POWER(SIN((radians(l.latitude-s.latitude))/2), 2) + COS(radians(s.latitude)) * COS(radians(l.latitude)) * POWER(SIN((radians(l.longitude)-radians(s.longitude))/2), 2)))) distance from items_available i, user u, location l, source_user s where i.email=u.email and u.postal_code=l.postal_code and u.postal_code=s.postal_code ) select i.item_id,i.name, case when length(i.description)>100 then concat(left(i.description, 100),'...') else i.description end description, i.description as fullDescription, i.`condition`, i.type, d.distance from items_available i, items_distance d where i.item_id=d.item_id order by d.distance, i.item_id")){
					pstmt.setString(1, email);
					pstmt.setString(2, email);

					try(ResultSet rs = pstmt.executeQuery()){
						while(rs.next()) {
							ItemSearchResult itemSearchResult = new ItemSearchResult();

							Item item = new Item();
							item.setId(rs.getLong("item_id"));
							item.setName(rs.getString("name"));
							item.setDescription(rs.getString("description"));
							item.setCondition(rs.getString("condition"));
							item.setGameType(rs.getString("type"));
							itemSearchResult.setItem(item);
							itemSearchResult.setDistance(Math.round(rs.getDouble("distance")*10)/10d);

							results.add(itemSearchResult);
						}
					}
				}
			} else if(searchKey.equals(ItemSearchKey.PostalCode)) {
				try(PreparedStatement pstmt = conn.prepareStatement("with source_user as ( select u.email, l.latitude, l.longitude from User u, Location l where u.email = ? and u.postal_code = l.postal_code ), items_available as ( select * from Item i where i.item_id not in ( select s.proposed_item_id from Swap s where s.swap_id not in (select swap_id from AcknowledgedSwap) or s.swap_id in (select swap_id from AcknowledgedSwap a where a.status='ACCEPTED') UNION select s.counterparty_item_id from Swap s where s.swap_id not in (select swap_id from AcknowledgedSwap) or s.swap_id in (select swap_id from AcknowledgedSwap a where a.status='ACCEPTED') ) and i.email <> ? ), items_distance as ( select i.item_id, 3958.75 * 2 * ATAN2(SQRT(POWER(SIN((radians(l.latitude-s.latitude))/2), 2) + COS(radians(s.latitude)) * COS(radians(l.latitude)) * POWER(SIN((radians(l.longitude)-radians(s.longitude))/2), 2)), SQRT(1-(POWER(SIN((radians(l.latitude-s.latitude))/2), 2) + COS(radians(s.latitude)) * COS(radians(l.latitude)) * POWER(SIN((radians(l.longitude)-radians(s.longitude))/2), 2)))) distance from items_available i, user u, location l, source_user s where i.email=u.email and u.postal_code=l.postal_code and u.postal_code=? ) select i.item_id,i.name, case when length(i.description)>100 then concat(left(i.description, 100),'...') else i.description end description, i.description as fullDescription, i.`condition`, i.type, d.distance from items_available i, items_distance d where i.item_id=d.item_id order by d.distance, i.item_id")){

					pstmt.setString(1, email);	
					pstmt.setString(2, email);
					pstmt.setString(3, searchFor);

					try(ResultSet rs = pstmt.executeQuery()){
						while(rs.next()) {
							ItemSearchResult itemSearchResult = new ItemSearchResult();

							Item item = new Item();
							item.setId(rs.getLong("item_id"));
							item.setName(rs.getString("name"));
							item.setDescription(rs.getString("description"));
							item.setCondition(rs.getString("condition"));
							item.setGameType(rs.getString("type"));
							itemSearchResult.setItem(item);
							itemSearchResult.setDistance(Math.round(rs.getDouble("distance")*10)/10d);

							results.add(itemSearchResult);
						}
					}
				}
			}
		}catch (Exception e) {
			results = null;
			logger.error("Error in searchItems()", e);
		}
		return results;
	}

	@Override
	public OwnedItemsSummary getOwnedItemsSummary(String email) {
		OwnedItemsSummary ownedItemsSummary = null;
		try(Connection conn = dataSource.getConnection()){
			try(PreparedStatement pstmt = conn.prepareStatement("with items_available as ( select * from Item i where i.item_id not in ( select s.proposed_item_id from Swap s where s.proposer_email=? and (s.swap_id not in (select swap_id from AcknowledgedSwap) or s.swap_id in (select swap_id from AcknowledgedSwap a where a.status='ACCEPTED')) UNION select s.counterparty_item_id from Swap s where s.counterparty_email=? and (s.swap_id not in (select swap_id from AcknowledgedSwap) or s.swap_id in (select swap_id from AcknowledgedSwap a where a.status='ACCEPTED')) ) ) select count(case when type='Board Game' then 1 else null end) board_games_count, count(case when type='Card Game' then 1 else null end) card_games_count, count(case when type='Computer Game' then 1 else null end) computer_games_count, count(case when type='Jigsaw Puzzle' then 1 else null end) jigsaw_puzzles_count, count(case when type='Video Game' then 1 else null end) video_games_count, count(1) total from items_available i where i.email=?")){
				pstmt.setString(1, email);
				pstmt.setString(2, email);
				pstmt.setString(3, email);
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) {
						ownedItemsSummary = new OwnedItemsSummary();
						ownedItemsSummary.setBoardGamesCount(rs.getInt("board_games_count"));
						ownedItemsSummary.setCardGamesCount(rs.getInt("card_games_count"));
						ownedItemsSummary.setVideoGamesCount(rs.getInt("video_games_count"));
						ownedItemsSummary.setComputerGamesCount(rs.getInt("computer_games_count"));
						ownedItemsSummary.setJigsawPuzzlesCount(rs.getInt("jigsaw_puzzles_count"));
						ownedItemsSummary.setTotalCount(rs.getInt("total"));
					}
				}
			}
		}catch (Exception e) {
			ownedItemsSummary = null;
			logger.error("Error in getOwnedItemsSummary()", e);
		}
		return ownedItemsSummary;
	}

	@Override
	public List<String> getVideoGamePlatforms() {
		List<String> videoGamePlatforms = new ArrayList<>();
		try(Connection conn = dataSource.getConnection()){
			try(PreparedStatement pstmt = conn.prepareStatement("select platform_name from platform")){
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) {
						videoGamePlatforms.add(rs.getString("platform_name"));
					}
				}
			}
		}catch (Exception e) {
			videoGamePlatforms = null;
			logger.error("Error in getVideoGamePlatforms()", e);
		}
		return videoGamePlatforms;
	}


}




