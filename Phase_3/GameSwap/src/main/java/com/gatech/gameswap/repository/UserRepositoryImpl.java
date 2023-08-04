package com.gatech.gameswap.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLType;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import javax.sql.DataSource;

import org.apache.logging.log4j.util.Strings;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gatech.gameswap.model.Location;
import com.gatech.gameswap.model.Phone;
import com.gatech.gameswap.model.Phone.PhoneNumberType;
import com.gatech.gameswap.model.User;
import com.gatech.gameswap.model.UserStats;


@Repository
public class UserRepositoryImpl implements UserRepository{


	@Autowired
	private DataSource dataSource;


	Logger logger = Logger.getLogger(UserRepositoryImpl.class);


	@Override
	public Boolean updateUser(User user) {
		Boolean success = false;
		try(Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);

			// Verify if target phone number is already present against any user other than this user
			if(user.getPhone()!=null && !Strings.isBlank(user.getPhone().getNumber())) {
				boolean phoneExists = false;
				try(PreparedStatement pstmt = conn.prepareStatement("select 1 from PhoneNumber where phone_number = ? and email <> ?")){
					pstmt.setString(1, user.getPhone().getNumber());
					pstmt.setString(2, user.getEmail());
					try(ResultSet rs = pstmt.executeQuery()){
						while(rs.next()) {
							phoneExists = true;
						}
					}
				}
				if(phoneExists) {
					logger.error("Phone number already exists against another user. Rejecting request.");
					return false;
				}
			}

			try(PreparedStatement pstmt = conn.prepareStatement("UPDATE User SET postal_code = ?, first_name = ?, last_name = ?, nickname = ?, password = IFNULL(?, password) WHERE email = ?")){
				pstmt.setString(1, user.getLocation().getPostalCode());
				pstmt.setString(2, user.getFirstName());
				pstmt.setString(3, user.getLastName());
				pstmt.setString(4, user.getNickName());
				if(Strings.isBlank(user.getPassword())) {
					pstmt.setNull(5, Types.VARCHAR);
				} else {
					pstmt.setString(5, user.getPassword());
				}
				pstmt.setString(6, user.getEmail());

				boolean successUser = pstmt.executeUpdate() > 0;
				boolean successPhone = false;
				User currentUserState = getUser(user.getEmail());

				//delete phone number since it no longer exists on user
				if(currentUserState.getPhone()!=null && !Strings.isBlank(currentUserState.getPhone().getNumber()) && (user.getPhone()==null || Strings.isBlank(user.getPhone().getNumber()))) {
					try(PreparedStatement pstmt2 = conn.prepareStatement("delete from PhoneNumber WHERE email = ?")){
						pstmt2.setString(1, user.getEmail());
						successPhone = pstmt2.executeUpdate() > 0;
					}
				}
				//create phone number since user has a phone now
				else if((currentUserState.getPhone()==null || Strings.isBlank(currentUserState.getPhone().getNumber())) && user.getPhone()!=null && !Strings.isBlank(user.getPhone().getNumber())) {
					try(PreparedStatement pstmt2 = conn.prepareStatement("insert into PhoneNumber(phone_number, email, type, share_phone_number) values (?, ?, ?, ?)")) {
						pstmt2.setString(1, user.getPhone().getNumber());
						pstmt2.setString(2, user.getEmail());
						pstmt2.setString(3, user.getPhone().getType().toString());
						pstmt2.setBoolean(4, user.getPhone().isShare());
						successPhone = pstmt2.executeUpdate() > 0;
					}
				}
				//update existing phone number if it exists in request
				else if(user.getPhone()!=null && !Strings.isBlank(user.getPhone().getNumber())){
					try(PreparedStatement pstmt2 = conn.prepareStatement("UPDATE PhoneNumber SET phone_number=?, type=?, share_phone_number=? WHERE email = ?")){
						pstmt2.setString(1, user.getPhone().getNumber());
						pstmt2.setString(2, user.getPhone().getType().toString());
						pstmt2.setBoolean(3, user.getPhone().isShare());
						pstmt2.setString(4, user.getEmail());
						successPhone = pstmt2.executeUpdate() > 0;
					}
				} else {
					successPhone = true;
				}

				success = successUser && successPhone;
				if (success) {
					conn.commit();
					logger.info("User and phone record was updated successfully!");
				} else {
					conn.rollback();
					logger.error("Failed to update user and/or phone record. Rolling back.");
				}

			}
		} catch(Exception e) {
			success = null;
			logger.error("Error in updateUser()", e);
		}
		return success;
	}


	@Override
	public Boolean createUser(User user) {
		logger.info("Entered createUser()");
		Boolean success = false;
		try(Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);

			if(user.getPhone()!=null && !Strings.isBlank(user.getPhone().getNumber()) && checkPhoneExists(user.getPhone().getNumber())) {
				logger.error("Phone number already exists against another user. Rejecting request.");
				return false;
			}

			if(checkEmailExists(user.getEmail())) {
				logger.error("Email already exists against another user. Rejecting request.");
				return false;
			}

			try(PreparedStatement pstmt = conn.prepareStatement("insert into user(email, postal_code, first_name, last_name, nickname, password) values (?, ?, ?, ?, ?, ?)")) {
				pstmt.setString(1, user.getEmail());
				pstmt.setString(2, user.getLocation().getPostalCode()); 
				pstmt.setString(3, user.getFirstName());
				pstmt.setString(4, user.getLastName());
				pstmt.setString(5, user.getNickName());
				pstmt.setString(6, user.getPassword());

				boolean successUser = pstmt.executeUpdate() > 0;
				boolean successPhone = false;

				if(user.getPhone()!=null && !Strings.isBlank(user.getPhone().getNumber())) {
					try(PreparedStatement pstmt2 = conn.prepareStatement("insert into phonenumber(phone_number, email, type, share_phone_number) values (?, ?, ?, ?)")) {
						pstmt2.setString(1, user.getPhone().getNumber());
						pstmt2.setString(2, user.getEmail());
						pstmt2.setString(3, user.getPhone().getType().toString());
						pstmt2.setBoolean(4, user.getPhone().isShare());

						successPhone = pstmt2.executeUpdate() > 0;
					}
				} else {
					successPhone = true;
				}

				success = successUser && successPhone;
				if (success) {
					conn.commit();
					logger.info("User and phone record was created successfully!");
				} else {
					conn.rollback();
					logger.error("Failed to created user and/or phone record. Rolling back.");
				}
			}
		} catch (Exception e) {
			success = null;
			logger.error("Error in createUser()", e);
		}
		logger.info("Completed createUser()");
		return success;
	}


	@Override
	public String authenticateUser(String email, String password) {
		logger.info("Entered authenticateUser()");
		String name = null;
		Boolean success = null;
		try(Connection conn = dataSource.getConnection()){
			try(PreparedStatement pstmt = conn.prepareStatement("select u.email, u.first_name, u.last_name, u.nickname, p.phone_number, p.type, p.share_phone_number, l.postal_code, l.city, l.state from User u "
					+ "inner join Location l on u.postal_code=l.postal_code "
					+ "left outer join phonenumber p on u.email = p.email where (u.email = ? OR p.phone_number = ?) "
					+ "and u.password = ?")){
				pstmt.setString(1, email);
				pstmt.setString(2, email);
				pstmt.setString(3, password);
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next())
						name = rs.getString("email");
				}
			}
		} catch (Exception e) {
			success = null;
			logger.error("Error in authenticateUser()", e);
		}
		logger.info("Completed authenticateUser()");
		return name;
	}


	@Override
	public User getUser(String email) {
		logger.info("Entered getUser()");
		User user = null;
		try(Connection conn = dataSource.getConnection()){
			try(PreparedStatement pstmt = conn.prepareStatement("select u.first_name, u.last_name, u.nickname, p.phone_number, p.type, p.share_phone_number, l.postal_code, l.city, l.state from User u "
					+ "inner join Location l on u.postal_code=l.postal_code "
					+ "left outer join phonenumber p on u.email = p.email "
					+ "where u.email = ?")){
				pstmt.setString(1, email);
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) {
						user = new User();
						user.setEmail(email);
						user.setFirstName(rs.getString("first_name"));
						user.setLastName(rs.getString("last_name"));
						user.setNickName(rs.getString("nickname"));

						if(rs.getString("phone_number")!=null) {
							Phone phone = new Phone();
							phone.setNumber(rs.getString("phone_number"));
							phone.setType(PhoneNumberType.valueOf(rs.getString("type")));
							phone.setShare(rs.getBoolean("share_phone_number"));
							user.setPhone(phone);
						}

						Location location = new Location();
						location.setPostalCode(rs.getString("postal_code"));
						location.setCity(rs.getString("city"));
						location.setState(rs.getString("state"));
						user.setLocation(location);
					}
				}
			}

			if(user!=null) {

				UserStats userStats = new UserStats();
				try(PreparedStatement pstmt = conn.prepareStatement("select s.proposed_date FROM Swap s WHERE s.counterparty_email = ? and s.swap_id not in (select swap_id from AcknowledgedSwap)")){
					pstmt.setString(1, email);
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date = new Date();
					Boolean unacceptedDays = false;
					try(ResultSet rs = pstmt.executeQuery()){
						while(rs.next()) {
							System.out.println(java.time.LocalDate.now());
							System.out.println(rs.getString("proposed_date"));
							//Date d1 = sdf.parse(java.time.LocalDate.now().toString());
				            //Date d2 = sdf.parse(rs.getString("proposed_date"));
				            LocalDate localDate1 = java.time.LocalDate.now();
				    		LocalDate localDate2 = LocalDate.parse(rs.getString("proposed_date"));
							System.out.println(localDate1);
							System.out.println(localDate2);
				    		long noOfDaysDifference = ChronoUnit.DAYS.between(localDate2, localDate1);
				    		System.out.println(noOfDaysDifference);
							//long noOfDaysBetween = ChronoUnit.DAYS.between(java.time.LocalDate.now(), rs.getString("proposed_date"));
							//long difference_In_Time = d1.getTime() - d2.getTime();
							//long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
							if(noOfDaysDifference > 5) {
								unacceptedDays = true;
								userStats.setUnAccpetedDays(unacceptedDays);
							}
							System.out.println("check "+sdf.format(date));
							
						}
					}
				}
				
				try(PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(1) unaccepted_swap_count FROM Swap s WHERE s.counterparty_email = ? and s.swap_id not in (select swap_id from AcknowledgedSwap)")){
					pstmt.setString(1, email);
					try(ResultSet rs = pstmt.executeQuery()){
						while(rs.next()) {
							userStats.setUnacceptedSwapCount(rs.getInt("unaccepted_swap_count"));
						}
					}
				}
				try(PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(1) unrated_swap_count from Swap s JOIN AcknowledgedSwap a ON s.swap_id = a.swap_id WHERE a.status = 'ACCEPTED' AND (s.proposer_email = ? OR s.counterparty_email = ?) AND a.swap_id NOT IN (SELECT swap_id from RatedSwap where email = ?)")){
					pstmt.setString(1, email);
					pstmt.setString(2, email);
					pstmt.setString(3, email);
					try(ResultSet rs = pstmt.executeQuery()){
						while(rs.next()) {
							userStats.setUnratedSwapCount(rs.getInt("unrated_swap_count"));
						}
					}
				}
				try(PreparedStatement pstmt = conn.prepareStatement("select avg(rating) as rating from swap s\n"
						+ "join ratedswap r on s.swap_id = r.swap_id where (proposer_email = ? or counterparty_email = ?) \n"
						+ "and r.email != ?")){
					pstmt.setString(1, email);
					pstmt.setString(2, email);
					pstmt.setString(3, email);
					try(ResultSet rs = pstmt.executeQuery()){
						while(rs.next()) {
							if(rs.getObject("rating")!=null) {
								Formatter formatter1 = new Formatter();
						        formatter1.format("%.2f", rs.getDouble("rating"));
						        userStats.setRating(formatter1.toString());
								//userStats.setRating(Math.round(rs.getDouble("rating")*100)/100d);
							}
						}
					}
				}

				user.setUserStats(userStats);
			}
		} catch (Exception e) {
			user = null;
			logger.error("Error in getUser()", e);
		}
		logger.info("Completed getUser()");
		return user;
	}


	@Override
	public Boolean checkEmailExists(String email) {
		Boolean success = false;
		try(Connection conn = dataSource.getConnection()){
			try(PreparedStatement pstmt = conn.prepareStatement("select 1 from User where email = ?")){
				pstmt.setString(1, email);
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) {
						success = true;
					}
				}
			}
		}catch (Exception e) {
			logger.error("Error in checkEmailExists()", e);
		}
		return success;
	}


	@Override
	public Boolean checkPhoneExists(String phone) {
		Boolean success = false;
		try(Connection conn = dataSource.getConnection()){
			try(PreparedStatement pstmt = conn.prepareStatement("select 1 from PhoneNumber where phone_number = ?")){
				pstmt.setString(1, phone);
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) {
						success = true;
					}
				}
			}
		}catch (Exception e) {
			success = null;
			logger.error("Error in checkPhoneExists()", e);
		}
		return success;
	}


	@Override
	public List<Location> getLocations() {
		List<Location> locations = new ArrayList<>();
		try(Connection conn = dataSource.getConnection()){
			try(PreparedStatement pstmt = conn.prepareStatement("select city, state, postal_code from location")){
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) {
						Location location = new Location();
						location.setPostalCode(rs.getString("postal_code"));
						location.setCity(rs.getString("city"));
						location.setState(rs.getString("state"));
						locations.add(location);
					}
				}
			}
		}catch (Exception e) {
			locations = null;
			logger.error("Error in getLocations()", e);
		}
		return locations;
	}


	@Override
	public Double getRating(String email) {
		return null;
	}
}

