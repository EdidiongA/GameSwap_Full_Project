package com.gatech.gameswap.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gatech.gameswap.model.History;
import com.gatech.gameswap.model.Item;
import com.gatech.gameswap.model.Phone;
import com.gatech.gameswap.model.Phone.PhoneNumberType;
//import com.gatech.gameswap.model.Item.Condition;
//import com.gatech.gameswap.model.Item.GameType;
import com.gatech.gameswap.model.Swap;
import com.gatech.gameswap.model.SwapAck;
import com.gatech.gameswap.model.SwapDetail;
import com.gatech.gameswap.model.SwapHistory;
import com.gatech.gameswap.model.SwapHistorySummary;
import com.gatech.gameswap.model.SwapRating;


@Repository
public class SwapRepositoryImpl implements SwapRepository{

	@Autowired
	private DataSource dataSource;
	
	@Override
	public Boolean swapRequest(Swap swap) throws SQLException {
		
		String sql = "INSERT into Swap (proposer_email, counterparty_email, proposed_item_id, counterparty_item_id, proposed_date) VALUES (?,?,?,?,CURRENT_DATE())";
		 try(Connection conn = dataSource.getConnection()) {
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setString(1, swap.getProposerID());
				statement.setString(2, swap.getCounterPartyID());
				statement.setLong(3, swap.getProposerItemID());
				statement.setLong(4, swap.getCounterPartyItemID());
			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
			    System.out.println("swap requested!");
			    return true;
			}
			conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
	}
	

	@Override
	public List<Item> proposeSwap(String userID,Long itemID) throws SQLException {
		List<Item> items = new ArrayList<Item>();
	    try(Connection conn = dataSource.getConnection())  {
					String sql = "SELECT item.item_id, item.type, item.name, item.condition FROM item WHERE item_id NOT IN(\n"
							+ "SELECT s.counterparty_item_id from swap as s \n"
							+ "where s.counterparty_email = ? and s.swap_id in (select swap_id from acknowledgedswap where status='ACCEPTED')\n"
							+ "UNION\n"
							+ "SELECT s.counterparty_item_id from swap as s \n"
							+ "where s.counterparty_email = ? and s.proposed_item_id = ? and s.swap_id in (select swap_id from acknowledgedswap where status='REJECTED')\n"
							+ "UNION\n"
							+ "SELECT s.counterparty_item_id from swap as s \n"
							+ "where s.counterparty_email = ? and  s.swap_id not in (select swap_id from acknowledgedswap) \n"
							+ "UNION\n"
							+ "SELECT s.proposed_item_id from swap as s \n"
							+ "where s.proposer_email = ? and  s.swap_id in (select swap_id from acknowledgedswap where status='ACCEPTED')\n"
							+ "UNION\n"
							+ "SELECT s.proposed_item_id from swap as s \n"
							+ "where s.proposer_email = ? and  s.counterparty_item_id = ? and s.swap_id in (select swap_id from acknowledgedswap where status='REJECTED')\n"
							+ "UNION\n"
							+ "SELECT s.proposed_item_id from swap as s \n"
							+ "where s.proposer_email = ? and  s.swap_id not in (select swap_id from acknowledgedswap)) AND email = ?";
				
					PreparedStatement statement = conn.prepareStatement(sql);
					statement.setString(1, userID);
					statement.setString(2, userID);
					statement.setLong(3, itemID);
					statement.setString(4, userID);
					statement.setString(5, userID);
					statement.setString(6, userID);
					statement.setLong(7, itemID);
					statement.setString(8, userID);
					statement.setString(9, userID);
					
					ResultSet result = statement.executeQuery();
					
					while(result.next()) {
						Item itm = new Item();
						itm.setId(result.getLong("item_id"));
						itm.setName(result.getString("name"));
						itm.setGameType(result.getString("type"));
						itm.setCondition(result.getString("condition"));
					items.add(itm);
					}

					conn.close();	
			}

	    catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return items;
	}
	
	@Override
	public List<SwapAck> ackPage(String userID) throws SQLException {
		List<SwapAck> ackList = new ArrayList<SwapAck>();
	    try(Connection conn = dataSource.getConnection()) {
		 String sql ="SELECT latitude, longitude FROM user JOIN Location on location.postal_code = user.postal_code where email =?";
		 PreparedStatement statement = conn.prepareStatement(sql);
		 statement.setString(1, userID);
		 ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				Double latitude = rs.getDouble("latitude");
				Double longitude = rs.getDouble("longitude");
				
				String sql1 = "SELECT Swap.proposed_date, User.nickname proposer, item1.name proposed_item ,item2.name desired_item,item1.item_id proposed_Item_id, item2.item_id counterparty_item_id,\n"
						+ "(SELECT AVG(rating) Rating FROM ratedswap WHERE email=proposer_email GROUP BY email) Rating,\n"
						+ "( 3958.75 * 2 * atan2(sqrt(sin((radians(? - (location.latitude))) / 2) * sin((radians(? - (location.latitude))) / 2) *\n"
						+ "cos(radians(?)) * cos(radians(?)) *\n"
						+ "sin((radians((?)- (location.longitude))) / 2) * sin((radians((?)- (location.longitude))) / 2)),\n"
						+ "sqrt(1-(sin((radians(? - (location.latitude))) / 2) * sin((radians(? - (location.latitude))) / 2) *\n"
						+ "cos(radians(?)) * cos(radians(?)) *\n"
						+ "sin((radians((?)- (location.longitude))) / 2) * sin((radians((?)- (location.longitude))) / 2))))) AS distance\n"
						+ "FROM User\n"
						+ "JOIN Swap ON (Swap.proposer_email = User.email)\n"
						+ "LEFT OUTER JOIN Item AS item1 ON (swap.proposed_item_id = item1.item_id)\n"
						+ "LEFT OUTER JOIN Item AS item2 ON (swap.counterparty_item_id = item2.item_id)\n"
						+ "LEFT OUTER JOIN Location ON (User.postal_code = Location.postal_code)\n"
						+ "WHERE swap.swap_id IN\n"
						+ "(SELECT swap_id from swap where counterparty_email = ? AND swap_id NOT IN (SELECT Acknowledgedswap.swap_id from swap JOIN AcknowledgedSwap \n"
						+ "ON swap.swap_id = Acknowledgedswap.swap_id WHERE counterparty_email = ?))";
				PreparedStatement stat = conn.prepareStatement(sql1);
				stat.setDouble(1, latitude);
				stat.setDouble(2, latitude);
				stat.setDouble(3, latitude);
				stat.setDouble(4, latitude);
				stat.setDouble(5, longitude);
				stat.setDouble(6, longitude);
				stat.setDouble(7, latitude);
				stat.setDouble(8, latitude);
				stat.setDouble(9, latitude);
				stat.setDouble(10, latitude);
				stat.setDouble(11, longitude);
				stat.setDouble(12, longitude);
				stat.setString(13, userID);
				stat.setString(14, userID);
				ResultSet resultSet = stat.executeQuery();
				while(resultSet.next()) {
					SwapAck ack = new SwapAck();
		
					ack.setProposed_date(resultSet.getString("proposed_date"));
					ack.setProposer(resultSet.getString("proposer"));
					ack.setProposed_item(resultSet.getString("proposed_item"));
					ack.setDesired_item(resultSet.getString("desired_item"));
					//ack.setDistance(Math.round(resultSet.getDouble("distance")*100)/10d);
					//ack.setRating(Math.round(resultSet.getDouble("rating")*100)/100d);
					Formatter formatter = new Formatter();
			        formatter.format("%.1f", resultSet.getDouble("distance"));
			        ack.setDistance(formatter.toString());
			        Formatter formatter1 = new Formatter();
			        formatter1.format("%.2f", resultSet.getDouble("rating"));
			        ack.setRating(formatter1.toString());
					ack.setProposed_item_id(resultSet.getInt("proposed_Item_id"));
					ack.setCounterparty_item_id(resultSet.getInt("counterparty_item_id"));
					ackList.add(ack);
				}
				conn.close();
			}
	    } catch (SQLException e) {
			e.printStackTrace();
		}
		return ackList;
	}

	@Override
	public List<SwapRating> unRatedSwap(String userID) throws SQLException {
		List<SwapRating> unratedList = new ArrayList<SwapRating>();
	    try(Connection conn = dataSource.getConnection()) {
	    	 String sql ="SELECT A.swap_id, A.acknowledged_date AS Acceptance_Date, \n"
	 		 		+ "(SELECT COUNT(*) FROM Swap where swap_id = A.swap_id AND proposer_email = ?) My_Role,\n"
	 		 		+ "(SELECT name FROM Item where Item_id = S.proposed_item_id) AS Proposed_Item,\n"
	 		 		+ "(SELECT name FROM Item where Item_id = S.counterparty_item_id) AS Desired_Item,\n"
	 		 		+ "(SELECT nickname FROM User where (email != ? \n"
	 		 		+ "AND (email = S.counterparty_email OR email = S.proposer_email))) AS Other_User,\n"
	 		 		+ "(SELECT email FROM User WHERE (email != ? \n"
	 		 		+ "AND (email = S.counterparty_email OR email = S.proposer_email))) AS Other_user_id\n"
	 		 		+ " FROM Swap AS S \n"
	 		 		+ " JOIN Acknowledgedswap AS A ON S.swap_id = A.swap_id\n"
	 		 		+ " WHERE S.Swap_id\n"
	 		 		+ "IN\n"
	 		 		+ "((SELECT Swap.swap_id from Swap\n"
	 		 		+ "JOIN Acknowledgedswap\n"
	 		 		+ "ON Swap.swap_id = Acknowledgedswap.swap_id \n"
	 		 		+ "WHERE Acknowledgedswap.status = 'ACCEPTED' \n"
	 		 		+ "AND (Swap.counterparty_email = ? OR  Swap.proposer_email = ?) \n"
	 		 		+ "AND Acknowledgedswap.swap_id NOT IN\n"
	 		 		+ "(SELECT swap_id from Ratedswap where email = ?)))\n"
	 		 		+ "ORDER BY acknowledged_date DESC;\n"
	 		 		+ "";
		 PreparedStatement stat = conn.prepareStatement(sql);
		 stat.setString(1, userID);
		 stat.setString(2, userID);
		 stat.setString(3, userID);
		 stat.setString(4, userID);
		 stat.setString(5, userID);
		 stat.setString(6, userID);
		
			ResultSet resultSet = stat.executeQuery();
			
			while(resultSet.next()) {
				SwapRating unrate = new SwapRating();
				unrate.setSwap_id(resultSet.getLong("swap_id"));
				unrate.setAccepted_date(resultSet.getString("Acceptance_Date"));
				if(resultSet.getInt("My_Role") == 0)
					unrate.setMyRole("Counterparty");
				else
					unrate.setMyRole("Proposer");
				
				unrate.setProposed_item(resultSet.getString("Proposed_Item"));
				unrate.setDesired_item(resultSet.getString("Desired_Item"));
				unrate.setOther_user(resultSet.getString("Other_User"));
				unrate.setOther_user_id(resultSet.getString("Other_user_id"));
				unratedList.add(unrate);
			}
			conn.close();
	    
	    } catch (SQLException e) {
			e.printStackTrace();
		}
		return unratedList;
	}
	
	@Override
	public History swapHistory(String userID) throws SQLException {
		History history = new History();

	    try(Connection conn = dataSource.getConnection()) {
	    	SwapHistorySummary summary = new SwapHistorySummary();
	    	summary.setMy_role("Proposer");
	    	//record.put("My_Role", "Proposer");
	    	int total = 0;
	    	int reject = 0;
	    	int total1 = 0;
	    	int reject1 = 0;
	    	 String sql ="SELECT COUNT(*) Total from AcknowledgedSwap \n"
	    	 		+ "WHERE swap_id\n"
	    	 		+ "IN (SELECT swap_id from Swap WHERE Swap.proposer_email = ?)";
	    	 PreparedStatement statement = conn.prepareStatement(sql);
	    	 statement.setString(1, userID);
			 ResultSet resultSet = statement.executeQuery();
				
			if(resultSet.next()) {
				total = resultSet.getInt("Total");
				summary.setTotal_count(resultSet.getInt("Total"));
			}
			
			String sql1 ="SELECT COUNT(*) Accepted from AcknowledgedSwap WHERE swap_id\n"
					+ "IN (SELECT swap_id from SWAP WHERE Swap.proposer_email = ?)\n"
					+ "AND status = 'ACCEPTED'";
			PreparedStatement statement1 = conn.prepareStatement(sql1);
	    	 statement1.setString(1, userID);
			 ResultSet rs = statement1.executeQuery();
			if(rs.next()) {
				summary.setAccepted_count(rs.getInt("Accepted"));
			}
			
			String sql2 ="SELECT COUNT(*) Rejected from AcknowledgedSwap WHERE swap_id\n"
					+ "IN (SELECT swap_id from SWAP WHERE Swap.proposer_email = ?)\n"
					+ "AND status = 'REJECTED';\n";
			PreparedStatement statement2 = conn.prepareStatement(sql2);
	    	 statement2.setString(1, userID);
			 ResultSet res = statement2.executeQuery();
			if(res.next()) {
				reject = res.getInt("Rejected");
				summary.setRejected_count(res.getInt("Rejected"));
			}
			double result =((double)(reject)/(double)(total))*100;
			summary.setRejected_percentage(String.valueOf(result));
			/*String sql3 ="SELECT count(*) * 100.0 / (SELECT COUNT(*) Rejected from Swap WHERE Swap.proposer_email = ?) Rejected_percentage\n"
					+ "FROM AcknowledgedSwap \n"
					+ "WHERE swap_id\n"
					+ "IN (SELECT swap_id from Swap WHERE Swap.proposer_email =?)\n"
					+ "AND status = 'REJECTED';\n";
			PreparedStatement statement3 = conn.prepareStatement(sql3);
	    	 statement3.setString(1, userID);
	    	 statement3.setString(2, userID);
			 ResultSet result = statement3.executeQuery();
			if(result.next()) {
				summary.setRejected_percentage(result.getString("Rejected_percentage"));
			}
*/
			history.setProposer_summary(summary);
			SwapHistorySummary summary1 = new SwapHistorySummary();
		    
			summary1.setMy_role("Counterparty");
	    	 String sql4 ="SELECT COUNT(*) Total from AcknowledgedSwap \n"
	    	 		+ "WHERE swap_id\n"
	    	 		+ "IN (SELECT swap_id from Swap WHERE Swap.counterparty_email = ?)";
	    	 PreparedStatement statement4 = conn.prepareStatement(sql4);
	    	 statement4.setString(1, userID);
			 ResultSet re = statement4.executeQuery();
				
			if(re.next()) {
				total1 =re.getInt("Total");
				summary1.setTotal_count(re.getInt("Total"));
			}
			
			String sql5 ="SELECT COUNT(*) Accepted from AcknowledgedSwap WHERE swap_id\n"
					+ "IN (SELECT swap_id from SWAP WHERE Swap.counterparty_email = ?)\n"
					+ "AND status = 'ACCEPTED'";
			PreparedStatement statement5 = conn.prepareStatement(sql5);
	    	 statement5.setString(1, userID);
			 ResultSet resul = statement5.executeQuery();
			if(resul.next()) {
				summary1.setAccepted_count(resul.getInt("Accepted"));
			}
			
			String sql6 ="SELECT COUNT(*) Rejected from AcknowledgedSwap WHERE swap_id\n"
					+ "IN (SELECT swap_id from SWAP WHERE Swap.counterparty_email = ?)\n"
					+ "AND status = 'REJECTED';\n";
			PreparedStatement statement6 = conn.prepareStatement(sql6);
	    	 statement6.setString(1, userID);
			 ResultSet resu = statement6.executeQuery();
			if(resu.next()) {
				reject1 = resu.getInt("Rejected");
				summary1.setRejected_count(resu.getInt("Rejected"));
			}
			
			double result1 =((double)(reject1)/(double)(total1))*100;
			summary1.setRejected_percentage(String.valueOf(result1));
			/*String sql7 ="SELECT count(*) * 100.0 / (SELECT COUNT(*)  Rejected from Swap WHERE Swap.counterparty_email = ?) Rejected_percentage\n"
					+ "FROM AcknowledgedSwap \n"
					+ "WHERE swap_id\n"
					+ "IN (SELECT swap_id from Swap WHERE Swap.counterparty_email = ?)\n"
					+ "AND status = 'REJECTED';\n";
			PreparedStatement statement7 = conn.prepareStatement(sql7);
	    	 statement7.setString(1, userID);
	    	 statement7.setString(2, userID);
			 ResultSet results = statement7.executeQuery();
			if(results.next()) {
				summary1.setRejected_percentage(results.getString("Rejected_percentage"));
			}*/
			history.setCounterparty_summary(summary1);
	    	
		 String sql8 ="SELECT S.swap_id, S.proposed_date, A.acknowledged_date AS Accept_Reject_Date, A.status,\n"
		 		+ "(SELECT name FROM Item WHERE Item_id = S.proposed_item_id) AS Proposed_Item,\n"
		 		+ "(SELECT name FROM Item WHERE Item_id = S.counterparty_item_id) AS Desired_Item,\n"
		 		+ "(SELECT COUNT(*) FROM Swap where swap_id = A.swap_id AND proposer_email = ?) My_Role,\n"
		 		+ "(SELECT nickname FROM User WHERE (email != ? \n"
		 		+ "AND (email = S.counterparty_email OR email = S.proposer_email))) AS Other_User,\n"
		 		+ "(SELECT email FROM User WHERE (email != ? \n"
		 		+ "AND (email = S.counterparty_email OR email = S.proposer_email))) AS Other_user_id ,\n"
		 		+ "(SELECT rating from ratedswap r WHERE (email = S.counterparty_email OR email = S.proposer_email) \n"
		 		+ "AND r.email = ? AND s.swap_id = r.swap_id) AS rating FROM Swap AS S\n"
		 		+ "JOIN AcknowledgedSwap AS A ON S.swap_id = A.swap_id\n"
		 		+ "LEFT OUTER JOIN RatedSwap AS R ON A.swap_id = R.swap_id AND R.email!=?\n"
		 		+ "WHERE (S.counterparty_email = ? OR  S.proposer_email = ?)\n"
		 		+ "ORDER BY A.acknowledged_date DESC, S.proposed_date ASC;\n";
		 
		 
		 
		 PreparedStatement statement8 = conn.prepareStatement(sql8);
    	 statement8.setString(1, userID);
    	 statement8.setString(2, userID);
    	 statement8.setString(3, userID);
    	 statement8.setString(4, userID);
    	 statement8.setString(5, userID);
    	 statement8.setString(6, userID);
    	 statement8.setString(7, userID);
		 ResultSet resultSe = statement8.executeQuery();
			List<SwapHistory> swapHistoryList = new ArrayList<SwapHistory>();
			while(resultSe.next()) {
				SwapHistory history2 = new SwapHistory();
				history2.setSwap_id(resultSe.getLong("swap_id"));
				history2.setProposed_Date(resultSe.getDate("proposed_date"));
				history2.setAcknowledged_date(resultSe.getDate("Accept_Reject_Date"));
				history2.setSwap_status(resultSe.getString("status"));
				if(resultSe.getInt("My_Role") == 0)
					history2.setMy_role("Counterparty");
				else
					history2.setMy_role("Proposer");
				history2.setProposed_item(resultSe.getString("Proposed_Item"));
				history2.setDesired_item(resultSe.getString("Desired_Item"));
				history2.setOther_user(resultSe.getString("Other_User"));
				history2.setOther_user_id(resultSe.getString("Other_user_id"));
				//history2.setRating(Math.round(resultSe.getDouble("rating")*100)/100d);
				Formatter formatter = new Formatter();
		        formatter.format("%.2f", resultSe.getDouble("rating"));
				history2.setRating(formatter.toString());
				swapHistoryList.add(history2);
			}
			history.setHistory(swapHistoryList);
			conn.close();
	    
	    } catch (SQLException e) {
			e.printStackTrace();
		}
		return history;
	}


	@Override
	public SwapDetail swapDetail(String userID,Long swapID) throws SQLException {
		SwapDetail details = new SwapDetail();
	    try(Connection conn = dataSource.getConnection()) {
	    	
	    //swap details
		String sql = "SELECT S.proposed_date, A.acknowledged_date, A.status, R.rating,\n"
				+ "(SELECT COUNT(*) FROM Swap where swap_id = A.swap_id AND proposer_email = ?) My_Role \n"
				+ "FROM Swap AS S\n"
				+ "JOIN AcknowledgedSwap AS A ON S.swap_id = A.swap_id\n"
				+ "LEFT OUTER JOIN RatedSwap AS R ON (R.swap_id = A.swap_id AND R.email =?)\n"
				+ "WHERE s.swap_id = ?";
		PreparedStatement statement = conn.prepareStatement(sql);
		 statement.setString(1, userID);
    	 statement.setString(2, userID);
    	 statement.setLong(3,swapID);
		ResultSet resultSet = statement.executeQuery();
		if(resultSet.next()) {
			details.setProposed_Date(resultSet.getDate("proposed_date"));
			details.setAcknowledged_date(resultSet.getDate("acknowledged_date"));
			details.setSwap_status(resultSet.getString("status"));
			//details.setRating(Math.round(resultSet.getDouble("rating")*100)/100d);
			Formatter formatter = new Formatter();
	        formatter.format("%.2f", resultSet.getDouble("rating"));
	        details.setRating(formatter.toString());
			if(resultSet.getInt("My_Role") == 1)
				details.setMy_role("Proposer");
			else
				details.setMy_role("Counterparty");

		}
			//user details
		    
		    String sql2 ="SELECT latitude, longitude FROM user JOIN Location on location.postal_code = user.postal_code where email =?";
		    PreparedStatement stmt = conn.prepareStatement(sql2);
		    stmt.setString(1, userID);
			 ResultSet rs = stmt.executeQuery();
			 
			 
			 
			 if(rs.next()) {
				 Double latitude = rs.getDouble("latitude");
				 Double longitude = rs.getDouble("longitude");
				 String sql1 = "SELECT User.first_name, User.nickname, User.email email, P.phone_number, P.type, P.share_phone_number, ( 3958.75 * 2 * atan2(sqrt(sin((radians(? - (location.latitude))) / 2) * sin((radians(? - (location.latitude))) / 2) *\n"
							+ "cos(radians(?)) * cos(radians(?)) *\n"
							+ "sin((radians((?)- (location.longitude))) / 2) * sin((radians((?)- (location.longitude))) / 2)),\n"
							+ "sqrt((1-sin((radians(? - (location.latitude))) / 2) * sin((radians(? - (location.latitude))) / 2) *\n"
							+ "cos(radians(?)) * cos(radians(?)) *\n"
							+ "sin((radians((?)- (location.longitude))) / 2) * sin((radians((?)- (location.longitude))) / 2))))) AS distance FROM User\n"
							+ "JOIN Swap ON (Swap.counterparty_email = User.email \n"
							+ "OR Swap.proposer_email = User.email)\n"
							+ "LEFT OUTER JOIN PhoneNumber AS P ON P.email = User.email\n"
							+ "LEFT OUTER JOIN Location ON (User.postal_code = Location.postal_code)\n"
							+ "WHERE user.email NOT IN (?) and Swap.swap_id = ?";

				 PreparedStatement stat = conn.prepareStatement(sql1);
					stat.setDouble(1, latitude);
					stat.setDouble(2, latitude);
					stat.setDouble(3, latitude);
					stat.setDouble(4, latitude);
					stat.setDouble(5, longitude);
					stat.setDouble(6, longitude);
					stat.setDouble(7, latitude);
					stat.setDouble(8, latitude);
					stat.setDouble(9, latitude);
					stat.setDouble(10, latitude);
					stat.setDouble(11, longitude);
					stat.setDouble(12, longitude);
					stat.setString(13, userID);
					stat.setLong(14, swapID);
					ResultSet rset = stat.executeQuery();
					if(rset.next()) {
						details.setFirstName(rset.getString("first_name"));
						details.setNickName(rset.getString("nickname"));
						details.setEmail(rset.getString("email"));
						//details.setDistance(Math.round(rset.getDouble("distance")*100)/10d);
						Formatter formatter1 = new Formatter();
						formatter1.format("%.1f", rset.getDouble("distance"));
						details.setDistance(formatter1.toString());
						if(rset.getString("phone_number") != null)
						{
							Phone phone = new Phone();
							phone.setNumber(rset.getString("phone_number"));
							if(rset.getString("type")!= null)
								phone.setType(Enum.valueOf(PhoneNumberType.class,rset.getString("type")));
							if(rset.getInt("share_phone_number") == 1)
								phone.setShare(true);
							else 
								phone.setShare(false);
							details.setPhone(phone);
						}
				 }
			 }
		
			 
			//proposed Item

			    Item itm1 = new Item();
			String sql1 = "SELECT Item.item_id,Item.name,Item.type,Item.condition,Item.description from Item\n"
					+ "JOIN Swap ON Swap.proposed_item_id = Item.item_id \n"
					+ "WHERE Swap.swap_id = ?";
			PreparedStatement stmt1 = conn.prepareStatement(sql1);
			stmt1.setLong(1, swapID);
			ResultSet rslt = stmt1.executeQuery();
			if(rslt.next()) {
				itm1.setId(rslt.getLong("item_id"));
				itm1.setName(rslt.getString("name"));
				itm1.setGameType(rslt.getString("type"));
				itm1.setCondition(rslt.getString("condition"));
			
				if(rslt.getString("description") != null)
					itm1.setDescription(rslt.getString("description"));
			
		}
			details.setProposed_Item(itm1);
			
			//DesiredItem Item
			
			 Item itm2 = new Item();
		String sql3 = "SELECT Item.item_id,Item.name,Item.type,Item.condition,Item.description from Item\n"
				+ "JOIN Swap ON Swap.counterparty_item_id = Item.item_id \n"
				+ "WHERE Swap.swap_id = ?";
		PreparedStatement stmt2 = conn.prepareStatement(sql3);
		stmt2.setLong(1, swapID);
		ResultSet rsltSt = stmt2.executeQuery();
		if(rsltSt.next()) {
		
			itm2.setId(rsltSt.getLong("item_id"));
			itm2.setName(rsltSt.getString("name"));
			itm2.setGameType(rsltSt.getString("type"));
			itm2.setCondition(rsltSt.getString("condition"));
		
			if(rsltSt.getString("description") != null)
				itm2.setDescription(rsltSt.getString("description"));
		}
		details.setDesired_Item(itm2);
		conn.close();
		
	    } catch (SQLException e) {
			e.printStackTrace();
		}
		return details;
	}
	
	@Override
	public Boolean swapAccept(Long proposerItemID, Long counterPartyItemID) throws SQLException {
		try(Connection conn = dataSource.getConnection()) {
			String sql = "SELECT swap_id FROM swap WHERE proposed_item_id = ? and counterparty_item_id=?";
			 PreparedStatement statement = conn.prepareStatement(sql);
			 statement.setLong(1, proposerItemID);
			 statement.setLong(2, counterPartyItemID);
			 ResultSet rs = statement.executeQuery();
			 if(rs.next()) {
				 Long swapID= rs.getLong("swap_id");
			 
				 String sql1 = "INSERT INTO acknowledgedswap VALUES (?, 'ACCEPTED', CURRENT_DATE())";
				 PreparedStatement stat = conn.prepareStatement(sql1);
				 stat.setLong(1, swapID);
					int rowsInserted = stat.executeUpdate();
					conn.close();
					if (rowsInserted > 0) {
					    return true;
					}
			 }
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean swapReject(Long proposerItemID, Long counterPartyItemID) throws SQLException {
		try(Connection conn = dataSource.getConnection()) {
			String sql = "SELECT swap_id FROM swap WHERE proposed_item_id = ? and counterparty_item_id= ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setLong(1, proposerItemID);
			 statement.setLong(2, counterPartyItemID);
			 ResultSet rs = statement.executeQuery();
			 if(rs.next()) {
				 Long swapID= rs.getLong("swap_id");
			 
				 String sql1 = "INSERT INTO acknowledgedswap VALUES ( ?, 'REJECTED', CURRENT_DATE())";
				 PreparedStatement stat = conn.prepareStatement(sql1);
				 stat.setLong(1, swapID);
					int rowsInserted = stat.executeUpdate();
					conn.close();
					if (rowsInserted > 0) {
					    return true;
					}
			 }
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean upadteSwapRating(Long swapID, String userID, int rating) throws SQLException {
		try(Connection conn = dataSource.getConnection()) {
			String sql = "INSERT into RatedSwap (swap_id,email,rating) VALUES (?,?,?)";
			
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setLong(1, swapID);
			stat.setString(2,userID);
			stat.setInt(3, rating);
			int rowsInserted = stat.executeUpdate();
			conn.close();
			if (rowsInserted > 0) {
			    return true;
			}
			 
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
		}

}
