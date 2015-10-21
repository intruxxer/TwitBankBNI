package biline.core;

import biline.config.*;
import biline.db.*;
import biline.twitter.*;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.DirectMessage;
import twitter4j.FilterQuery;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;


public class TwitterDaemon {
	
	private static final String twitterUser = "bni46";
	//bni46 //fahmivanhero //amartha 
	
	private static String latestStatus;
	private static Status status;
	private static String savedDirectory;
	
	private static TwitterStream twitterStream;
	private static TwitterStream twitterStreamDM;
	
	private static String recipientId;
	private static String directMsg;
	
	private static Twitter twitter;
	private static Twitter twitterDM;
	private static AsyncTwitter asyncTwitter;
	private static AsyncTwitter asyncTwitterDM;

	private static Connection con;
	private static Statement stm;
	private static ResultSet rs;
	private static String command;
	private static MysqlConnect db_object;
	
	private static SimpleDateFormat dateFormat;
	private static String today;
	
	private static ArrayList<String> hashtags;
	private static ArrayList<String> menus;
	private static ArrayList<String> aliasmenus;
	private static ArrayList<String> directMessagesPromoAndServices;
	private static ArrayList<String> directMessagesForMentions;
	private static TwitterTweetExtractorUtil tagExtractor;
	
	public TwitterDaemon() {	
		//Constructor
		setLatestStatus("default");
		setSaveDirectory();
	}

	public static void main(String[] args) throws TwitterException {
		// *Listener shared objects
		hashtags     = new ArrayList<String>();
		menus 		 = new ArrayList<String>();
		aliasmenus 		 = new ArrayList<String>();
		
		tagExtractor = new TwitterTweetExtractorUtil("");
		
		directMessagesForMentions       = new ArrayList<String>();
		directMessagesPromoAndServices  = new ArrayList<String>();
		
		dateFormat   =  new SimpleDateFormat("yyyy-MM-dd");
		
		// *Connecting to DB & Instantiate Accessing DB Object
		stm = null; rs = null;
		try{
			db_object = new MysqlConnect();
			con 	  = db_object.getConnection(); 
		} catch(ClassNotFoundException nfe){
			nfe.printStackTrace();
		} catch(SQLException sqle){
			sqle.printStackTrace();
		}
		
		/* Using RESTful API to Update Status */
		// [INIT] 
		// Create (min.) a requesting RESTful API instance
		// If we want to have multiple RESTful API instances
		   //Twitter twitter_one = new TwitterFactory().getInstance(new AccessToken("XXX","XXX"));
		   //Twitter twitter_two = new TwitterFactory().getInstance(new AccessToken("YYY","YYY"));
		// Else, we can use singleton to have only one instance, shared across application life-shelf
		   //Twitter twitter = TwitterFactory.getSingleton();
        
		// [1] Posting non-Async 'New Status' to User's Twitter Account
		
			//try {
			//	setLatestStatus("default");
			//	TwitterDaemon.status = twitter.updateStatus(latestStatus);
			//	System.out.println("Successfully updated the status to [" + TwitterDaemon.status.getText() + "].");
			//} catch (TwitterException e) {
			//	e.printStackTrace();
			//}
		
		// [2] Sending non-Async 'DM' to User's Twitter Account
		
			//try {
			//   String msg = "You got DM from @dev_amartha. Thanks for tweeting our hashtags!";
			//   DirectMessage message = twitter.sendDirectMessage(recipientId, msg);
		    //   System.out.println("Sent: " + message.getText() + " to @" + message.getRecipientScreenName());
			//} catch (TwitterException e) {
			//	 e.printStackTrace();
			//}
		   
		/* Using Stream API */
		// [INIT]
		// Connects to the Streaming API - with Amartha OAUTH2 Key n Token
	       TwitterStreamBuilderUtil twitterStreamBuilder = new TwitterStreamBuilderUtil(twitterUser);
		   twitterStream   = twitterStreamBuilder.getStream();
		   twitterStreamDM = twitterStreamBuilder.getStream();
		   //twitterStream = new TwitterStreamFactory().getInstance();
		   
		   //AsyncTwitterFactory factory = new AsyncTwitterFactory();
           //asyncTwitter = factory.getInstance();
           // OR //
		   asyncTwitter   = AsyncTwitterFactory.getSingleton();
           asyncTwitterDM = AsyncTwitterFactory.getSingleton();
           
           twitter = TwitterFactory.getSingleton();
           twitterDM = TwitterFactory.getSingleton();
           //twitter      = new TwitterFactory().getInstance();
           // *twitter w/o Async must be prepared with TwitterException 
           // *either  (1) ..throws block OR (2) try-catch block
		
		// [1] User Stream API - Stream Tweets of a Twitter User 
		// Sets (a) the followed users id/handler name (optional) [+] (b) what keywords 
		// to be tracked from the Stream.
		   
		   //args[0] = [1145,1426,2456]
	       //args[1] = [#microfinance,#amartha,#life]
		   
		   //ArrayList<Long>   follow  = new ArrayList<Long>();
	       //ArrayList<String> track   = new ArrayList<String>();
		   ArrayList<String> track   = new ArrayList<String>();
		   String listOfTagsQuery    = "SELECT * FROM tbl_hashtags"; 
		   
		   try {
			   if(con == null){
				  db_object.openConnection();
				  con = db_object.getConnection();
	        }   
			stm = con.createStatement();
			rs  = stm.executeQuery(listOfTagsQuery);
			System.out.print("Trackings: ");
			while (rs.next()){
				System.out.print("#" + rs.getString("hashtag_term") + " ");
				track.add("@" + twitterUser + " #" + rs.getString("hashtag_term"));
	   		   }
		   } catch (SQLException e) {
			e.printStackTrace();
		   } finally{
			   if(con != null){
					  try {
						db_object.closeConnection();
					  } catch (SQLException e) {
						e.printStackTrace();
					  } finally{
						  con = null;
					  }
		        }
		   }
		   
		   //for (String arg : args) {
	       //   if (isNumericalArgument(arg)) {
	       //     	  for (String id : arg.split(",")) {
	       //       	follow.add(Long.parseLong(id));
	       // 		  }
           //   } else {
	       //     track.addAll(Arrays.asList(arg.split(",")));
	       //   }
	       //}
	       //long[] followArray = new long[follow.size()];
	       //for (int i = 0; i < follow.size(); i++) {
	       //      followArray[i] = follow.get(i);
	       //}
		   
		   //String[] trackArray = track.toArray(new String[track.size()]);
		   
		// Sets stream listener(s) to track events from the Stream: 
		// (1) User stream & (2) Stream's rate limit. 
	       twitterStream.addListener(userStreamlistener);
	       twitterStream.addRateLimitStatusListener(rateLimitStatusListener);
	       twitterStreamDM.addListener(userDMStreamlistener);
	    
	       // *TO LISTEN TO TIMELINE   
	       //FilterQuery filter = new FilterQuery();
		   //String keywords[] = { "#microfinance", "#life" };

		   //filter.track(keywords);

		   //fq.track(keywords);

	       // *TO LISTEN TO STATUS MENTIONS
	       //twitterStream.filter(filter);
	       //twitterStream.filter("@dev_amartha #microfinance,@dev_amartha #life");
	       twitterStream.filter( new FilterQuery( track.toArray( new String[track.size()] ) ) );
	    
	       // *TO LISTEN TO DM & OUR USER'S ACTIVITY
	       twitterStreamDM.user( );
	    // Methods: user() & filter() internally create threads respectively, manipulating TwitterStream; e.g. user() simply gets all tweets from its following users.
	    // Methods: user() & filter() then call the appropriate listener methods according to each stream events (such as status, favorite, RT, DM, etc) continuously.
	       
	}
	
	// *Implement a stream listener to track from the Stream prior to being assigned to stream. 
    // *A stream listener has unimplemented multiple methods to respond to multiple events in streams accordingly.
	private static final UserStreamListener userStreamlistener = new UserStreamListener() {
	      
		   @Override
	        public void onStatus(Status status) {
	            System.out.println("onStatus @" + status.getUser().getScreenName() + " - " + status.getText());
	            //try {
	    			//String msg = "You got DM from @dev_amartha. Thanks for tweeting our hashtags!";
	    			//DirectMessage message = twitter.sendDirectMessage(status.getUser().getScreenName(), msg);
	    			//System.out.println("Sent: " + message.getText() + " to @" + message.getRecipientScreenName());
	    		//} catch (TwitterException e) {
	    		//	e.printStackTrace();
	    		//}
	            
	            // *We extract hashTAGS from status
	            hashtags = tagExtractor.parseTweetForHashtags(status.getText());
	            // *We compose DM per hashTAGS
	            String promotionsQuery = "";
	            stm = null; rs = null;
	            
	            for (String tag : hashtags) {
	            	Date now = new Date();
	            	today    = dateFormat.format(now);
	            	promotionsQuery = "SELECT * FROM tbl_promotions LEFT JOIN tbl_hashtags " 
	            					+ "ON tbl_hashtags.hashtag_id = tbl_promotions.promotion_hashtag "
	            					+ "WHERE tbl_hashtags.hashtag_term = '" + tag + "' "
	            					+ "AND tbl_promotions.promotion_enddate >= '" + today + "'";
	            	//System.out.println(promotionsQuery);
	            	
	     		   	try {
			     		if(con == null){
			     			db_object.openConnection();
			  				con = db_object.getConnection();
			  	        }
		     			stm = con.createStatement();
		     			rs  = stm.executeQuery(promotionsQuery);
		     			while (rs.next()){
		     				directMessagesForMentions.add(rs.getString("promotion_content"));
		     				//System.out.println("DM with: " + tag);
		     	   		   }
	     		   	} catch (SQLException e) {
	     		   		e.printStackTrace();
	     		   	} finally{
		     		   	if(con != null){
		  				  try {
							db_object.closeConnection();
		  				  } catch (SQLException e) {
							e.printStackTrace();
		  				  } finally{
		  				  		con = null;
		     		   		}
		     		   	}
	     		   	}
				}
	            //System.out.println("DMes are: "); 
	            //System.out.println(directMessagesForMentions);
	            
	         // *We then send out all possible DM per hashTAGS
	            for (String msg : directMessagesForMentions) {
		            recipientId = status.getUser().getScreenName();
		            directMsg = msg;
		            try {
						DirectMessage message = twitter.sendDirectMessage(recipientId, directMsg);
						//System.out.println("Sent: " + message.getText() + " to @" + status.getUser().getScreenName());
			            //asyncTwitter.sendDirectMessage(recipientId, directMsg);
		    		    //System.out.println("Sent: " + directMsg + " to @" + status.getUser().getScreenName());
					} catch (TwitterException e) {
						e.printStackTrace();
					}
	            }
	            
	         // *Freeing up memory   
	            hashtags.clear(); 
	            directMessagesForMentions.clear();
	        }

	        @Override
	        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
	            System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
	        }

	        @Override
	        public void onDeletionNotice(long directMessageId, long userId) {
	            System.out.println("Got a direct message deletion notice id:" + directMessageId);
	        }

	        @Override
	        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
	            System.out.println("Got a track limitation notice:" + numberOfLimitedStatuses);
	        }

	        @Override
	        public void onScrubGeo(long userId, long upToStatusId) {
	            System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
	        }

	        @Override
	        public void onStallWarning(StallWarning warning) {
	            System.out.println("Got stall warning:" + warning);
	        }

	        @Override
	        public void onFriendList(long[] friendIds) {
	            System.out.print("onFriendList");
	            for (long friendId : friendIds) {
	                System.out.print(" " + friendId);
	            }
	            System.out.println();
	        }

	        @Override
	        public void onFavorite(User source, User target, Status favoritedStatus) {
	            System.out.println("onFavorite source:@"
	                + source.getScreenName() + " target:@"
	                + target.getScreenName() + " @"
	                + favoritedStatus.getUser().getScreenName() + " - "
	                + favoritedStatus.getText());
	        }

	        @Override
	        public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
	            System.out.println("onUnFavorite source:@"
	                + source.getScreenName() + " target:@"
	                + target.getScreenName() + " @"
	                + unfavoritedStatus.getUser().getScreenName()
	                + " - " + unfavoritedStatus.getText());
	        }

	        @Override
	        public void onFollow(User source, User followedUser) {
	            System.out.println("onFollow fromFilterStream source:@"
	                + source.getScreenName() + " target:@"
	                + followedUser.getScreenName());
	        }

	        @Override
	        public void onUnfollow(User source, User followedUser) {
	            System.out.println("onFollow source:@"
	                + source.getScreenName() + " target:@"
	                + followedUser.getScreenName());
	        }

	        @Override
	        public void onDirectMessage(DirectMessage directMessage) {
	            System.out.println("onDirectMessage text:"
	                + directMessage.getText());
	        }

	        @Override
	        public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
	            System.out.println("onUserListMemberAddition added member:@"
	                + addedMember.getScreenName()
	                + " listOwner:@" + listOwner.getScreenName()
	                + " list:" + list.getName());
	        }

	        @Override
	        public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
	            System.out.println("onUserListMemberDeleted deleted member:@"
	                + deletedMember.getScreenName()
	                + " listOwner:@" + listOwner.getScreenName()
	                + " list:" + list.getName());
	        }

	        @Override
	        public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
	            System.out.println("onUserListSubscribed subscriber:@"
	                + subscriber.getScreenName()
	                + " listOwner:@" + listOwner.getScreenName()
	                + " list:" + list.getName());
	        }

	        @Override
	        public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
	            System.out.println("onUserListUnsubscribed subscriber:@"
	                + subscriber.getScreenName()
	                + " listOwner:@" + listOwner.getScreenName()
	                + " list:" + list.getName());
	        }

	        @Override
	        public void onUserListCreation(User listOwner, UserList list) {
	            System.out.println("onUserListCreated  listOwner:@"
	                + listOwner.getScreenName()
	                + " list:" + list.getName());
	        }

	        @Override
	        public void onUserListUpdate(User listOwner, UserList list) {
	            System.out.println("onUserListUpdated  listOwner:@"
	                + listOwner.getScreenName()
	                + " list:" + list.getName());
	        }

	        @Override
	        public void onUserListDeletion(User listOwner, UserList list) {
	            System.out.println("onUserListDestroyed  listOwner:@"
	                + listOwner.getScreenName()
	                + " list:" + list.getName());
	        }

	        @Override
	        public void onUserProfileUpdate(User updatedUser) {
	            System.out.println("onUserProfileUpdated user:@" + updatedUser.getScreenName());
	        }

	        @Override
	        public void onUserDeletion(long deletedUser) {
	            System.out.println("onUserDeletion user:@" + deletedUser);
	        }

	        @Override
	        public void onUserSuspension(long suspendedUser) {
	            System.out.println("onUserSuspension user:@" + suspendedUser);
	        }

	        @Override
	        public void onBlock(User source, User blockedUser) {
	            System.out.println("onBlock source:@" + source.getScreenName()
	                + " target:@" + blockedUser.getScreenName());
	        }

	        @Override
	        public void onUnblock(User source, User unblockedUser) {
	            System.out.println("onUnblock source:@" + source.getScreenName()
	                + " target:@" + unblockedUser.getScreenName());
	        }

	        @Override
	        public void onRetweetedRetweet(User source, User target, Status retweetedStatus) {
	            System.out.println("onRetweetedRetweet source:@" + source.getScreenName()
	                + " target:@" + target.getScreenName()
	                + retweetedStatus.getUser().getScreenName()
	                + " - " + retweetedStatus.getText());
	        }

	        @Override
	        public void onFavoritedRetweet(User source, User target, Status favoritedRetweet) {
	            System.out.println("onFavroitedRetweet source:@" + source.getScreenName()
	                + " target:@" + target.getScreenName()
	                + favoritedRetweet.getUser().getScreenName()
	                + " - " + favoritedRetweet.getText());
	        }

	        @Override
	        public void onQuotedTweet(User source, User target, Status quotingTweet) {
	            System.out.println("onQuotedTweet" + source.getScreenName()
	                + " target:@" + target.getScreenName()
	                + quotingTweet.getUser().getScreenName()
	                + " - " + quotingTweet.getText());
	        }

	        @Override
	        public void onException(Exception ex) {
	            ex.printStackTrace();
	            System.out.println("onException filterListener:" + ex.getMessage());
	        }

	};

	
	private static final UserStreamListener userDMStreamlistener = new UserStreamListener() {
	      
		   @Override
	        public void onStatus(Status status) {
			   /*
			   System.out.println("onStatus from DM Stream @" + status.getUser().getScreenName() + " - " + status.getText());
	            
	            // *We extract hashTAGS from status
	            hashtags = tagExtractor.parseTweetForHashtags(status.getText());
	            // *We compose DM per hashTAGS
	            String promotionsQuery = "";
	            stm = null; rs = null;
	            
	            for (String tag : hashtags) {
	            	Date now = new Date();
	            	today    = dateFormat.format(now);
	            	promotionsQuery = "SELECT * FROM tbl_promotions LEFT JOIN tbl_hashtags " 
	            					+ "ON tbl_hashtags.hashtag_id = tbl_promotions.promotion_hashtag "
	            					+ "WHERE tbl_hashtags.hashtag_term = '" + tag + "' "
	            					+ "AND tbl_promotions.promotion_enddate >= '" + today + "'";
	            	System.out.println(promotionsQuery);
	            	
	     		   	try {
			     		if(con == null){
			     			db_object.openConnection();
			  				con = db_object.getConnection();
			  	        }
		     			stm = con.createStatement();
		     			rs  = stm.executeQuery(promotionsQuery);
		     			while (rs.next()){
		     				directMessagesForMentions.add("[" + rs.getString("promotion_title") + "] " + rs.getString("promotion_content"));
		     				System.out.println("DM with: " + tag);
		     	   		   }
	     		   	} catch (SQLException e) {
	     		   		e.printStackTrace();
	     		   	} finally{
		     		   	if(con != null){
		  				  try {
							db_object.closeConnection();
		  				  } catch (SQLException e) {
							e.printStackTrace();
		  				  } finally{
		  				  		con = null;
		     		   		}
		     		   	}
	     		   	}
				}
	            //System.out.println("DMes are: "); 
	            //System.out.println(directMessagesForMentions);
	            
	         // *We then send out all possible DM per hashTAGS
	            for (String msg : directMessagesForMentions) {
		            recipientId = status.getUser().getScreenName();
		            directMsg = msg;
		            try {
						DirectMessage message = twitter.sendDirectMessage(recipientId, directMsg);
						System.out.println("Sent: " + message.getText() + " to @" + status.getUser().getScreenName());
			            //asyncTwitter.sendDirectMessage(recipientId, directMsg);
		    		    //System.out.println("Sent: " + directMsg + " to @" + status.getUser().getScreenName());
					} catch (TwitterException e) {
						e.printStackTrace();
					}
	            }
	            
	         // *Freeing up memory   
	            hashtags.clear(); 
	            directMessagesForMentions.clear();
	            */
	        }

	        @Override
	        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
	        }

	        @Override
	        public void onDeletionNotice(long directMessageId, long userId) {
	        }

	        @Override
	        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
	        }

	        @Override
	        public void onScrubGeo(long userId, long upToStatusId) {
	            
	        }

	        @Override
	        public void onStallWarning(StallWarning warning) {
	            
	        }

	        @Override
	        public void onFriendList(long[] friendIds) {
	            
	        }

	        @Override
	        public void onFavorite(User source, User target, Status favoritedStatus) {
	            
	        }

	        @Override
	        public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
	            
	        }

	        @Override
	        public void onFollow(User source, User followedUser) {
	        	System.out.println("onFollow fromUserStream with implementation. Follower:@"
		                + source.getScreenName() + " Followed:@"
		                + followedUser.getScreenName());
	        	
	        	String responseDMQuery = "SELECT message_content FROM tbl_directmessages WHERE message_type = 'followed' ORDER BY message_id ASC";
     			String responseDM      = "";
     			
	        	try {
		     		if(con == null){
		     			db_object.openConnection();
		  				con = db_object.getConnection();
		  	        }
	     			stm = con.createStatement();
	     			rs  = stm.executeQuery(responseDMQuery);
	     			
	     			while (rs.next()){
	     				responseDM = responseDM + rs.getString("message_content") + " ";
	     	   		   }
     			} catch (SQLException e) {
     		   		e.printStackTrace();
     			} finally{
	     		   	if(con != null){
	  				  try {
						db_object.closeConnection();
	  				  } catch (SQLException e) {
						e.printStackTrace();
	  				  } finally{
	  				  		con = null;
	     		   		}
	     		   	}
     		   	}
     		
	        	// *We then send out all possible menu via a single DM
	        	recipientId = source.getScreenName(); directMsg = responseDM;
	     		try {
					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
					//System.out.println("Sent: " + message.getText() + " to @" + source.getScreenName());
		            //asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
	    		    //System.out.println("Sent: " + directMsg + " to @" + source.getScreenName());
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				
	        }

	        @Override
	        public void onUnfollow(User source, User followedUser) {
	            
	        }

	        @Override
	        public void onDirectMessage(DirectMessage directMessage) {
	        	
	            System.out.println("onDirectMessage text:" + directMessage.getText());
	            
	            // *We extract hashTAGS from status then lowercase all the tags
	            hashtags = tagExtractor.parseTweetForHashtags(directMessage.getText());
	            ListIterator<String> iterator = hashtags.listIterator();
	            while (iterator.hasNext())
	            {
	                iterator.set(iterator.next().toLowerCase());
	            }
	            //System.out.println("Hashtags are: " + hashtags.toString());
	            
	            // LATER WE WILL DECIDE WHAT RESPONSES/HOW TO RESPOND 
	            // BASED ON FIRST COMMAND/FIRST TAG
	            // (1) #menu / #helppromo / #helpcs -> #HelpBNI | (2) #daftar #nama_lengkap #hape | (3) #promo keywords | (4) #cs keywords -> #AskBNI
	            if(hashtags.size() > 0)
	            	command = hashtags.get(0).toLowerCase();
	            else
	            	command = "";
	            //System.out.println("Command: " + command);
	            
	            // (1) #menu / #helppromo / #helpcs
	            if( command.equals("menu") || command.equals("helppromo") || command.equals("helpcs") || command.equals("helpbni") )
	            {
	            	// *We compose DMes sending list of menus/helps/cses
	            	stm = null; rs = null;
	            	String menuQuery = "";
	            	if( command.equals("menu") )
	            		menuQuery = "SELECT hashtag_term, hashtag_alias FROM tbl_hashtags WHERE hashtag_category = 'menu' AND hashtag_deleted = '0'";
	            	else if( command.equals("helppromo") )
	            		menuQuery = "SELECT hashtag_term, hashtag_alias FROM tbl_hashtags WHERE hashtag_category = 'promo' AND hashtag_deleted = '0'";
	            	else if( command.equals("helpcs") || command.equals("helpbni") )
	            		menuQuery = "SELECT hashtag_term, hashtag_alias FROM tbl_hashtags WHERE hashtag_category = 'cs' AND hashtag_deleted = '0'";
	            	//System.out.println(menuQuery);
	            	
	            	try {
			     		if(con == null){
			     			db_object.openConnection();
			  				con = db_object.getConnection();
			  	        }
		     			stm = con.createStatement();
		     			rs  = stm.executeQuery(menuQuery);
		     			
		     			while (rs.next()){
		     				menus.add( rs.getString("hashtag_term") );
		     				aliasmenus.add( rs.getString("hashtag_alias") );
		     	   		}
		     			//System.out.println(menus.toString());
		     			//System.out.println(aliasmenus.toString());
		     		} catch (SQLException e) {
		     		   		e.printStackTrace();
		     		} finally{
			     		   	if(con != null){
			  				  try {
								db_object.closeConnection();
			  				  } catch (SQLException e) {
								e.printStackTrace();
			  				  } finally{
			  				  		con = null;
			     		   		}
			     		   	}
		     		}
	            	
		     		// *We then send out all possible menu via a single DM
		     		recipientId = directMessage.getSenderScreenName();
		     		//(1A) #menu [DONE]
		     		if( command.equals("menu") )
		     		{
			            for (String msg : menus) {
				            switch (msg.toLowerCase()) {
				            	case "menu":  
				            		 directMsg 	= "Anda dapat mengirim DM dengan \"#menu\" (tanpa double quote) untuk mengakses daftar menu layanan BNI (@bni46) via Twitter. ";
				                     break;
				            	case "daftar":
				            		 directMsg 	= "Anda dapat mendaftar layanan BNI (@bni46) via Twitter DM dengan format:\n #daftar #nama_lengkap #nohandphone \n";
				            		 directMsg += "\nContoh:\n  #daftar #Andi_Waluyo #62213456789 \n\nNote: \nNama Awal dan Akhir dipisah dengan \"_\". Gunakan 62 sebagai pengganti digit \"0\" di depan nomor telepon Anda.";
				            		 break;
				            	//case "promo":
				            	case "helppromo":
				            		 directMsg 	= "Anda dapat mengakses informasi promo BNI (@bni46) via Twitter DM dengan format:\n #Promo + #Keyword \n";
				            		 directMsg += "\nContoh:\n #Promo #Travel \n\nDM kami dengan mengetik #HelpPromo untuk mengetahui semua #Keyword yang ada untuk #Promo dari BNI.";
				            		 break;
				            	//case "cs":	 
				            	case "helpcs":
				            	case "helpbni":
				            		 directMsg 	= "Anda dapat mengakses informasi tentang produk dan layanan BNI (@bni46) via Twitter DM dengan format:\n #AskBNI + #Keyword \n";
				            		 directMsg += "\nContoh:\n #AskBNI #Taplus \n\nDM kami dengan mengetik #HelpBNI untuk mengetahui semua #keyword yang ada untuk #AskBNI.";
				            		 break;
				            	default:
				            		 break;
				            }
				            
				            try {
								DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
								//System.out.println("Sent: " + message.getText() + " to @" + recipientId);
					            //asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
				    		    //System.out.println("Sent: " + directMsg + " to @" + status.getUser().getScreenName());
							} catch (TwitterException e) {
								e.printStackTrace();
							}
			            }
		     		}
		     		
		     		//(1B) #helppromo [DONE]
		     		else if( command.equals("helppromo") )
		     		{
		     			String keywords = "";
		     			for (String alias : aliasmenus) {
		     				keywords += "#" + alias + "\n";
		     			}
		     			
		     			directMsg = "Ketik #promo dan gunakan keywords berikut: \n" + keywords + "\nuntuk mendapatkan promo-promo menarik & terbaru dari BNI.";
		     			try {
							DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
							//System.out.println("Sent: " + directMessage.getSenderScreenName() + message.getText() + " to @" + directMessage.getSenderScreenName());
				            //asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
			    		    //System.out.println("Sent: " + directMsg + " to @" + status.getUser().getScreenName());
						} catch (TwitterException e) {
							e.printStackTrace();
						}
		     		}
		     		
		     		//(1C) #helpcs /#AskBNI [DONE]
		     		else if( command.equals("helpcs") || command.equals("helpbni") )
		     		{
		     			String keywords = "";
		     			for (String alias : aliasmenus) {
		     				keywords += "#" + alias + "\n";
		     			}
		     			
		     			directMsg = "Ketik #AskBNI dan gunakan keywords berikut: \n" + keywords + "\nuntuk mengakses topik-topik layanan nasabah dari BNI.";
		     			try {
							DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
							//System.out.println("Sent: " + message.getText() + " to @" + directMessage.getSenderScreenName());
				            //asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
			    		    //System.out.println("Sent: " + directMsg + " to @" + status.getUser().getScreenName());
						} catch (TwitterException e) {
							e.printStackTrace();
						}
		     		}
  
		            menus.clear();
		            aliasmenus.clear();
		            hashtags.clear();
	            }
	            
	            // (2) #daftar #namalengkap #phone [DONE]
	            else if( command.equals("daftar") ){
	            	Boolean errorDaftar = false;
	            	//System.out.println("Register Tags: " + hashtags.get(0) + " & " + hashtags.get(1) + " & " + hashtags.get(2));
	            	//System.out.println("Hashtags Daftar User:" + hashtags.toString());
	            	
	            	String rawName = ""; String name = "";
	            	if(hashtags.size() > 1)
	            	{
	            		rawName   = hashtags.get(1); 
	            		name	  = rawName.replace("_", " "); 
	            		name      = name.replace(".", " ");
	            	}
	            	
	            	String  rawPhoneNo = ""; String  ccPhoneNo  = ""; String  NumPhoneNo = "";
	            	if(hashtags.size() > 2)
	            	{
		            	rawPhoneNo  = hashtags.get(2);
		            	ccPhoneNo   = rawPhoneNo.substring(0, 2);
		            	NumPhoneNo  = rawPhoneNo.substring(2);
	            	}
	            	
	            	if(!ccPhoneNo.equals("62"))
	            	{
	            		errorDaftar = true;
	            		directMsg   = "Yth Bp/Ibu " + name + ", Mohon Maaf. Untuk format penulisan nomor telepon membutuhkan prefix 62 sebagai pengganti angka 0 pada digit depan nomor telepon Anda. Cth: 0811345890 menjadi 62811345890.";
	            	}
	            	
	            	if(rawPhoneNo.length() < 11 || rawPhoneNo.length() > 15)
	            	{
	            		errorDaftar = true;
	            		directMsg   = "Yth Bp/Ibu " + name + ", Mohon Maaf. Untuk panjang nomor telepon anda minimal 11 digit, maksimal 15 digit (termasuk prefix kode negara 62 pada no telepon Anda).";
	            	}
	            	
	            	if(errorDaftar)
	            	{
	            		recipientId = directMessage.getSenderScreenName();
			            try {
							DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
							//System.out.println("Sent: " + message.getText() + " to @" + status.getUser().getScreenName());
				            //asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
			    		    //System.out.println("Sent: " + directMsg + " to @" + status.getUser().getScreenName());
						} catch (TwitterException e) {
							e.printStackTrace();
						}
	            	}
	            	
	            	else
	            	{
	            		rawPhoneNo = "+" + rawPhoneNo;
	            		String daftarQuery = "INSERT INTO tbl_users(user_fullname, user_twitname, user_phonenum) " 
	            				   		   + "VALUES ('" + name + "', '" + directMessage.getSenderScreenName() + "', '" + rawPhoneNo + "')";
	            		
	            		String daftarResponseQuery = "SELECT message_content FROM tbl_directmessages WHERE message_type = 'daftar' ORDER BY message_id ASC";
	            		//System.out.println(daftarQuery); //System.out.println(daftarResponseQuery);
		         	
			            stm = null; 
			            try {
						     	if(con == null){
						     		db_object.openConnection();
						  			con = db_object.getConnection();
						        }
						     	stm = con.createStatement();	     		
						     	stm.executeUpdate(daftarQuery);
						     	//System.out.println("Registered user: " + directMessage.getSenderScreenName() + " [" + name + " | " + rawPhoneNo + "]" );
			            } catch (SQLException e) {
				     	   	 	e.printStackTrace();
				     	} 
			            finally{
				    		   	if(con != null){
				  				  try {
									db_object.closeConnection();
				  				  } catch (SQLException e) {
									e.printStackTrace();
				  				  } finally{
				  				  		con = null;
				     		   		}
				     		   	}
		  		   		}
			            
			            recipientId = directMessage.getSenderScreenName();
			            directMsg   = "Terima kasih telah mendaftar dengan data \nNAMA: " + name + " \nNOMOR HP: " +  rawPhoneNo + "\n";
			            
			            stm = null; rs = null;
			            try {
					     	if(con == null){
					     		db_object.openConnection();
					  			con = db_object.getConnection();
					        }
					     	stm = con.createStatement();	     		
					     	rs  = stm.executeQuery(daftarResponseQuery);
					     	while (rs.next()){
					     		directMsg   += " " + rs.getString("message_content");
					     	}
			            } catch (SQLException e) {
			     	   	 	e.printStackTrace();
				     	} 
			            finally{
				    		   	if(con != null){
				  				  try {
									db_object.closeConnection();
				  				  } catch (SQLException e) {
									e.printStackTrace();
				  				  } finally{
				  				  		con = null;
				     		   		}
				     		   	}
		  		   		}
			            
			            System.out.println("Registered user:" + directMessage.getSenderScreenName() + " will be responded by DM." );
			            
			            try {
							DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
							//System.out.println("Sent: " + message.getText() + " to @" + directMessage.getSenderScreenName());
				            //asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
			    		    //System.out.println("Sent: " + directMsg + " to @" + status.getUser().getScreenName());
						} catch (TwitterException e) {
							e.printStackTrace();
						}
			        }
	            	
	            	hashtags.clear();
		             
	            }//else if(DM for Register)
	            
	            // (3) #promo #keyword #keyword [DONE]
	            else if( command.equals("promo") ){
	            	if(hashtags.size() > 1)
	            	{
		            	// *We compose DM per hashTAGS
			            String promoQuery = "";
			            stm = null; rs = null;
			            for (String tag : hashtags) {
			            	if ( tag.toLowerCase().equals("promo") )
			            	      continue;
			            	Date now = new Date();
			            	//Date now = Calendar.getInstance().getTime();
			            	today    = dateFormat.format(now);
			            	//System.out.println(today);
			            	
			            	promoQuery  = "SELECT * FROM tbl_promotions LEFT JOIN tbl_hashtags " 
			            				+ "ON tbl_hashtags.hashtag_id = tbl_promotions.promotion_hashtag "
			            				+ "WHERE tbl_hashtags.hashtag_term = '" + tag + "' "
			            				+ "AND tbl_promotions.promotion_enddate >= '" + today + "'";
			            	//System.out.println(promoQuery);
			            	
				     		try {
						     		if(con == null){
						     			db_object.openConnection();
						  				con = db_object.getConnection();
						  	        }
					     			stm = con.createStatement();
					     			rs  = stm.executeQuery(promoQuery);
					     			if ( !rs.next() ) { 
					     				directMessagesPromoAndServices.add("Yth. Bp/Ibu, Mohon maaf. Promo #" + tag + " dari BNI yang Anda inginkan tidak tersedia.");
					     			} 
					     			else
					     			{
					     				rs.beforeFirst();
					     				while (rs.next()){
						     				directMessagesPromoAndServices.add(rs.getString("promotion_content"));
						     	   		}
					     			}
				     			} catch (SQLException e) {
				     		   		e.printStackTrace();
				     			} finally{
					     		   	if(con != null){
					  				  try {
										db_object.closeConnection();
					  				  } catch (SQLException e) {
										e.printStackTrace();
					  				  } finally{
					  				  		con = null;
					     		   		}
					     		   	}
				     		   	}
				     		
						}
			            	
				        // *We then send out all possible DM per hashTAGS
				        for (String msg : directMessagesPromoAndServices) {
					            recipientId = directMessage.getSenderScreenName();
					            directMsg = msg;
					            try {
									DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
									//System.out.println("Sent: " + message.getText() + " to @" + directMessage.getSenderScreenName());
									//asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
					    		    //System.out.println("Sent: " + directMsg + " to @" + directMessage.getSenderScreenName());
								} catch (TwitterException e) {
									e.printStackTrace();
								}
				        }
			        
	            	}
	            	else if(hashtags.size() == 1)
	            	{
	            		recipientId = directMessage.getSenderScreenName();
	            		directMsg = "Yth. Bp/Ibu, Mohon Maaf. Permintaan info #promo memerlukan minimal satu #keyword topik. Cth: #promo #travel, #promo #hotel #ecommerce. ";
		     			try {
							DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
							//System.out.println("Sent: " + message.getText() + " to @" + directMessage.getSenderScreenName());
				            //asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
			    		    //System.out.println("Sent: " + directMsg + " to @" + status.getUser().getScreenName());
						} catch (TwitterException e) {
							e.printStackTrace();
						}
	            	}
	            	
			        hashtags.clear(); 
			        directMessagesPromoAndServices.clear();
	            }//else if(DM for Promo)
	            
	         // (4) #cs #keyword #keyword [DONE]
	            else if( command.equals("cs") || command.equals("askbni") ){
	            	if(hashtags.size() > 1)
	            	{
		            	// *We compose DM per hashTAGS
			            String csQuery = "";
			            stm = null; rs = null;
			            for (String tag : hashtags) {
			            	if ( tag.toLowerCase().equals("cs") || tag.toLowerCase().equals("askbni") )
			            	      continue;
			            	csQuery = "SELECT * FROM tbl_customerservices LEFT JOIN tbl_hashtags " 
			            				+ "ON tbl_hashtags.hashtag_id = tbl_customerservices.cs_hashtag "
			            				+ "WHERE tbl_hashtags.hashtag_term = '" + tag + "' ";
			            	//System.out.println(csQuery);
			            	
				     		try {
						     		if(con == null){
						     			db_object.openConnection();
						  				con = db_object.getConnection();
						  	        }
					     			stm = con.createStatement();
					     			rs  = stm.executeQuery(csQuery);
					     			if ( !rs.next() ) { 
					     				directMessagesPromoAndServices.add("Yth. Bp/Ibu, Mohon maaf. Layanan Customer Services BNI tentang #" + tag + " yang Anda inginkan tidak tersedia.");
					     			} 
					     			else
					     			{
					     				rs.beforeFirst();
					     				while (rs.next()){
						     				directMessagesPromoAndServices.add("[" + rs.getString("cs_title") + "] \n\r" + rs.getString("cs_content"));
						     	   		}
					     			}
				     			} catch (SQLException e) {
				     		   		e.printStackTrace();
				     			} finally{
					     		   	if(con != null){
					  				  try {
										db_object.closeConnection();
					  				  } catch (SQLException e) {
										e.printStackTrace();
					  				  } finally{
					  				  		con = null;
					     		   		}
					     		   	}
				     		   	}
				     		
							}
			
				         // *We then send out all possible DM per hashTAGS
				         for (String msg : directMessagesPromoAndServices) {
					            recipientId = directMessage.getSenderScreenName();
					            directMsg = msg;
					            try {
									DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
									//System.out.println("Sent: " + message.getText() + " to @" + directMessage.getSenderScreenName());
									//asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
					    		    //System.out.println("Sent: " + directMsg + " to @" + directMessage.getSenderScreenName());
								} catch (TwitterException e) {
									e.printStackTrace();
								}
				         }
	            	}
	            	
	            	else if(hashtags.size() == 1)
	            	{
	            		recipientId = directMessage.getSenderScreenName();
	            		directMsg = "Yth. Bp/Ibu, Mohon Maaf. Permintaan info #AskBNI memerlukan minimal satu #keyword topik. Cth: #AskBNI #kartuhilang, #AskBNI #taplusmuda #kartutertelan. ";
		     			try {
							DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
							//System.out.println("Sent: " + message.getText() + " to @" + directMessage.getSenderScreenName());
				            //asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
			    		    //System.out.println("Sent: " + directMsg + " to @" + status.getUser().getScreenName());
						} catch (TwitterException e) {
							e.printStackTrace();
						}
	            	}    
			            hashtags.clear(); 
			            directMessagesPromoAndServices.clear();
	            }//else if(DM for CS)  
	        }

	        @Override
	        public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
	            
	        }

	        @Override
	        public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
	            
	        }

	        @Override
	        public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
	            
	        }

	        @Override
	        public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
	           
	        }

	        @Override
	        public void onUserListCreation(User listOwner, UserList list) {
	            
	        }

	        @Override
	        public void onUserListUpdate(User listOwner, UserList list) {
	            
	        }

	        @Override
	        public void onUserListDeletion(User listOwner, UserList list) {
	            
	        }

	        @Override
	        public void onUserProfileUpdate(User updatedUser) {
	            
	        }

	        @Override
	        public void onUserDeletion(long deletedUser) {
	        
	        }

	        @Override
	        public void onUserSuspension(long suspendedUser) {
	            
	        }

	        @Override
	        public void onBlock(User source, User blockedUser) {
	            
	        }

	        @Override
	        public void onUnblock(User source, User unblockedUser) {
	            
	        }

	        @Override
	        public void onRetweetedRetweet(User source, User target, Status retweetedStatus) {
	            
	        }

	        @Override
	        public void onFavoritedRetweet(User source, User target, Status favoritedRetweet) {
	            
	        }

	        @Override
	        public void onQuotedTweet(User source, User target, Status quotingTweet) {
	        	System.out.println("onQuotedTweet: " + source.getScreenName()
                + " target:@" + target.getScreenName()
                + quotingTweet.getUser().getScreenName()
                + " - " + quotingTweet.getText());
	        }
	        
	        @Override
	        public void onException(Exception ex) {
	            ex.printStackTrace();
	            System.out.println("onException:" + ex.getMessage());
	        }

	};
	
	// *Implement a Stream's rate limit listener to track from the Stream if limitation occurs esp. in Rate Limit Response 402. 
    // *A Stream's rate limit listener has two unimplemented methods to respond to Rate Limit Response 402 events in streams accordingly.
	private static final RateLimitStatusListener rateLimitStatusListener = new RateLimitStatusListener() {
		
		@Override
		public void onRateLimitStatus(RateLimitStatusEvent event) {
			System.out.println("onRateLimitStatus: " + event.toString());
			System.out.println("Limit["+event.getRateLimitStatus().getLimit() + "], Remaining[" +event.getRateLimitStatus().getRemaining()+"]");
		}
		
		@Override
		public void onRateLimitReached(RateLimitStatusEvent event) {
			System.out.println("onRateLimitReached: " + event.toString());
			System.out.println("Limit["+event.getRateLimitStatus().getLimit() + "], Remaining[" +event.getRateLimitStatus().getRemaining()+"]");
		}
		
	};
	
	public static void setSaveDirectory(){
		TwitterDaemon.savedDirectory = "save";
	}
	
	public static String getSavedDirectory(){
		return savedDirectory;
	}
	
	public static void setLatestStatus(String status){
		if(status.equals("default"))
			TwitterDaemon.latestStatus = "Tweet status posting " + new Date().toString() +" .";
		else
			TwitterDaemon.latestStatus = status;
	}
	
	public static String getLatestStatus(){
		return latestStatus;
	}

}
