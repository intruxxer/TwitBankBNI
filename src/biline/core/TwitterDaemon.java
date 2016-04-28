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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;
import java.util.Map;


public class TwitterDaemon {
	
	private static final String twitterUser    = "bni46";
	//bni46 //bni46_cs //fahmivanhero //bilinedev //dev_amartha
	private static final String twitterAccount = "BNI";
	//BNI //BNICustomerCare //fahmivanhero //biline_dev //dev_amartha
	
	private static String latestStatus;
	private static Status status;
	private static String savedDirectory;
	
	private static TwitterStream twitterStream;
	private static TwitterStream twitterStreamDM;
	private static TwitterLogging twitterLog;
	
	private static String recipientId;
	private static String directMsg;
	
	private static Twitter twitter;
	private static Twitter twitterDM;
	private static AsyncTwitter asyncTwitter;
	private static AsyncTwitter asyncTwitterDM;

	private static Connection con;
	private static Statement stm;
	private static ResultSet rs;
	private static ResultSet rs2;
	private static ResultSet rs3;
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
	private static TwitterStatisticsUtil statLogger;
	
	public TwitterDaemon() {	
		//Constructor
		setLatestStatus("default");
		setSaveDirectory();
	}

	public static void main(String[] args) throws TwitterException {
		// *Listeners' shared (data structured) objects
		hashtags         = new ArrayList<String>();
		menus 		     = new ArrayList<String>();
		aliasmenus 		 = new ArrayList<String>();
		
		tagExtractor     = new TwitterTweetExtractorUtil("");
		statLogger		 = new TwitterStatisticsUtil();
		twitterLog   	 = new TwitterLogging();
		
		directMessagesForMentions       = new ArrayList<String>();
		directMessagesPromoAndServices  = new ArrayList<String>();
		
		dateFormat       =  new SimpleDateFormat("yyyy-MM-dd");
		
		// *Connecting to DB & Instantiate Accessing DB Object
		stm = null; rs = null; rs2 = null; rs3 = null;
		try{
			db_object = new MysqlConnect();
			con 	  = db_object.getConnection(); 
		} catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
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
		// Connects to the Streaming API - with OAUTH2 Key n Token
	       TwitterStreamBuilderUtil twitterStreamBuilder = new TwitterStreamBuilderUtil(twitterUser);
		   twitterStream   = twitterStreamBuilder.getStream();
		   twitterStreamDM = twitterStreamBuilder.getStream();
		   //twitterStream = new TwitterStreamFactory().getInstance();
		   
		   //AsyncTwitterFactory factory = new AsyncTwitterFactory();
           //asyncTwitter = factory.getInstance();
           // OR //
		   asyncTwitter   = AsyncTwitterFactory.getSingleton();
           asyncTwitterDM = AsyncTwitterFactory.getSingleton();
           
           twitter        = TwitterFactory.getSingleton();
           twitterDM      = TwitterFactory.getSingleton();
           //twitter      = new TwitterFactory().getInstance();
           // *twitter w/o Async must be prepared with TwitterException 
           // *either  (1) ..throws block OR (2) try-catch block
		
		// [1] User Stream API - Stream Tweets of a Twitter User 
		// Sets (a) the followed users id/handler name (optional) [+] (b) what keywords 
		// to be tracked from the Stream.
		   
		   //filters[0] = [1145,1426,2456]
	       //filters[1] = [#microfinance,#amartha,#life]
		   
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
			System.out.print("\n[KEYWORDS] Trackings: ");
			int n = 0;
			while (rs.next()){
				if(n % 10 == 0)
					System.out.print("\n");
				System.out.print("#" + rs.getString("hashtag_term") + " ");
				track.add("@" + twitterUser + " #" + rs.getString("hashtag_term"));
				n++;
	   		}
			System.out.print("\n[KEYWORDS] " + n + " keywords being tracked." + "\n");
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
	       twitterStreamDM.addRateLimitStatusListener(rateLimitStatusListener);
	    
	       // *TO LISTEN TO TIMELINE   
	       //FilterQuery filter = new FilterQuery();
		   //String keywords[] = { "#microfinance", "#life" };

		   //filter.track(keywords);

		   //fq.track(keywords);

	       // *TO LISTEN TO STATUS MENTIONS
	       //twitterStream.filter(filter);
	       //twitterStream.filter("@dev_amartha #microfinance, @dev_amartha #life");
	       twitterStream.filter( new FilterQuery( track.toArray( new String[track.size()] ) ) );
	    
	       // *TO LISTEN TO DM & OUR USER'S ACTIVITY
	       twitterStreamDM.user( );
	    // Methods: user() & filter() internally create threads respectively, manipulating TwitterStream; e.g. user() simply gets all tweets from its following users.
	    // Methods: user() & filter() then call the appropriate listener methods according to each stream events (such as status, favorite, RT, DM, etc) continuously.
	       /*
	       //try {
	       //   String endpointAPI = "/direct_messages/new";
	       //   Map<String ,RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus();
	            //System.out.println("rate limit keysets:\n\n" + rateLimitStatus.keySet() + "\n");
	            /* 
	             * /application/rate_limit_status
	             * /account/verify_credentials
	             * /direct_messages/new
	             * 
	             * */
	       /*
	            System.out.println( "rate limit:" + rateLimitStatus.get(endpointAPI).getLimit() );
	            System.out.println( "remaining limit:" + rateLimitStatus.get(endpointAPI).getRemaining() );
	            System.out.println( "reset time (s):" + rateLimitStatus.get(endpointAPI).getResetTimeInSeconds() );
	            System.out.println( "remaining time before reset (s):" + rateLimitStatus.get(endpointAPI).getSecondsUntilReset() );
	       */
	            /*
	            for (String endpoint : rateLimitStatus.keySet()) {
	                RateLimitStatus status = rateLimitStatus.get(endpoint);
	                System.out.println("Endpoint: " + endpoint);
	                System.out.println(" Limit: " + status.getLimit());
	                System.out.println(" Remaining: " + status.getRemaining());
	                System.out.println(" ResetTimeInSeconds: " + status.getResetTimeInSeconds());
	                System.out.println(" SecondsUntilReset: " + status.getSecondsUntilReset());
	            }
	            */
	            //System.exit(0);
	        //} catch (TwitterException te) {
	        //    te.printStackTrace();
	        //    System.out.println("Failed to get rate limit status: " + te.getMessage());
	        //    //System.exit(-1);
	        //}
	        /**/
	}
	
	// *Implement a stream listener to track from the Stream prior to being assigned to stream. 
    // *A stream listener has unimplemented multiple methods to respond to multiple events in streams accordingly.
	private static final UserStreamListener userStreamlistener = new UserStreamListener() {
	      
		   @Override
	        public void onStatus(Status status) {
			   	
	            System.out.println("onStatus @" + status.getUser().getScreenName() + " - " + status.getText());
	            //try {
	    			//String msg = "You got DM from @" + twitterUser + ". Thanks for tweeting our hashtags!";
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
	            					+ "AND tbl_promotions.promotion_enddate >= '" + today + "' AND tbl_promotions.promotion_deleted = '0'";
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
	     		   	
	     		   	// *Log for Statistics
		            statLogger.eventsLog("1", status.getUser().getScreenName(), tag);
				}
	            //System.out.println("DMes are: "); 
	            //System.out.println(directMessagesForMentions);
	            
	            // *We then send out all possible DM per hashTAGS
	            int i = 0;
	            for (String msg : directMessagesForMentions) {
		            recipientId = status.getUser().getScreenName();
		            directMsg = msg;
		            try {
		            	if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
	     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
	     				}
						//System.out.println("Sent: " + message.getText() + " to @" + status.getUser().getScreenName());
			            //asyncTwitter.sendDirectMessage(recipientId, directMsg);
		    		    //System.out.println("Sent: " + directMsg + " to @" + status.getUser().getScreenName());
					} catch (TwitterException e) {
						e.printStackTrace();
					}
		            
		            // *Log for Statistics
		            if(i <hashtags.size()){
		            	twitterLog.saveOutwardDM(status.getUser().getScreenName(), msg, "Mention Reply", hashtags.get(i));
		            }
		            i++;
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
	            					+ "AND tbl_promotions.promotion_enddate >= '" + today + "' AND tbl_promotions.promotion_deleted = '0'";
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
	        	/*
	        	System.out.println("onFollow fromUserStream with implementation. Follower:@"
		                + source.getScreenName() + " Followed:@"
		                + followedUser.getScreenName());
	        	*/
	        	
	        	//String responseDMQuery = "SELECT message_content FROM tbl_directmessages WHERE message_type = 'followed' ORDER BY message_id ASC";
     			//String responseDM      = "";
     			/*
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
     			*/
	        	/*
	        	// We then send out all possible menu via a single DM
	        	recipientId = source.getScreenName(); directMsg = responseDM;
	     		try {
					if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
		     			DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
		     		}
					//System.out.println("Sent: " + message.getText() + " to @" + source.getScreenName());
		            //asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
	    		    //System.out.println("Sent: " + directMsg + " to @" + source.getScreenName());
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				*/
	        }
			
	        @Override
	        public void onUnfollow(User source, User followedUser) {
	            
	        }

	        @Override
	        public void onDirectMessage(DirectMessage directMessage) {
	        	
	            System.out.println("onDirectMessage from @" + directMessage.getSenderScreenName() + " '" + directMessage.getText() + "' ");
	            
	            // *We extract hashTAGS from status then lowercase all the tags
	            hashtags = tagExtractor.parseTweetForHashtags(directMessage.getText());
	            //System.out.println("Hashtags are: " + hashtags.toString());
	            
	            // LATER WE WILL DECIDE WHAT RESPONSES/HOW TO RESPOND 
	            // BASED ON FIRST COMMAND/FIRST TAG
	            // First thing first, detect the tweet's text whether it has at least one hashtag.
	            // To simplify, APART OF COMMANDS daftar, openaccount, csopen ALL HASHTAGS ARE LOWER CASED.
	            if(hashtags.size() > 0)
	            {
	            	command = hashtags.get(0).toLowerCase();
	            	if( !command.equals("daftar") || !command.equals("open") || !command.equals("csopen") || !command.equals("csregister") )
	            	{
	            		if( !command.equals("openaccount") || !command.equals("registercs") ){
	            			ListIterator<String> iterator = hashtags.listIterator();
				            while (iterator.hasNext())
				            {
				                iterator.set(iterator.next().toLowerCase());
				            }
	            		}
	            		
	            	}
	            }
	            else
	            {
	            	command = "";
	            	return;
	            }
	            //System.out.println("Command: " + command);
	            for(int n=1;n<hashtags.size();n++)
	            //tags n = 0 is command 
	            //tags 0 < n < size are #hashtags
	            {	
	            	if(!command.equals(""))
	            		twitterLog.saveInwardDM(directMessage.getSenderScreenName(), directMessage.getText(), command, hashtags.get(n));
	            }
	            
	            
	            //****************************************** LIST OF FEATURES IN TWITBANK BNI ******************************************//
	            
	            // (1) #menu | #help / #helppromo / #helpcs -> #HelpBNI 
	            // (2) #daftar #nama_lengkap #hape 
	            // (3) #promo keywords 
	            // (4) #cs keywords -> #AskBNI
	            // (5) #openaccount #taplusmuda for customers & #csopen #taplusmuda
	            
	            // (1) #menu | #help / #helppromo / #helpcs
	            if( command.equals("menu") || command.equals("help") || 
	            	command.equals("helppromo") || command.equals("helpcs") || 
	            	command.equals("helpbni") )
	            {
	            	// *We compose DMes sending list of menus/helps/cses
	            	stm = null; rs = null;
	            	String menuQuery = "";
	            	if( command.equals("menu") || command.equals("help") ){
	            		menuQuery = "SELECT hashtag_term, hashtag_alias FROM tbl_hashtags WHERE hashtag_category = 'menu' AND hashtag_deleted = '0'";
	            		statLogger.eventsLog("2", directMessage.getSenderScreenName(), command);
	            	}
	            	else if( command.equals("helppromo") ){
	            		menuQuery = "SELECT hashtag_term, hashtag_alias FROM tbl_hashtags WHERE hashtag_category = 'promo' AND hashtag_deleted = '0'";
	            		statLogger.eventsLog("5", directMessage.getSenderScreenName(), command);
	            	}
	            	else if( command.equals("helpcs") || command.equals("helpbni") ){
	            		menuQuery = "SELECT hashtag_term, hashtag_alias FROM tbl_hashtags WHERE hashtag_category = 'cs' AND hashtag_deleted = '0'";
	            		statLogger.eventsLog("2", directMessage.getSenderScreenName(), command);
	            	}
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
		     		//(1A) #menu | #help [DONE] >> also #askbni
		     		if( command.equals("menu") || command.equals("help") )
		     		{
			            for (String msg : menus) {
				            switch (msg.toLowerCase()) {
				            	case "menu":
				            	case "help":
				            		 //directMsg 	= "Anda dapat mengirim DM dengan \"#Help\" (tanpa double quote) untuk mengakses daftar menu layanan BNI (@" + twitterAccount + ") via Twitter. ";
				                     break;
				            	case "daftar":
				            		 directMsg 	= "Anda dapat mendaftar layanan BNI (@" + twitterAccount + ") via Twitter DM dengan format:\n #daftar #nama_lengkap #nohandphone \n";
				            		 directMsg += "\nContoh:\n  #daftar #Andi_Waluyo #62213456789 \n\nNote: \nNama Awal dan Akhir dipisah dengan \"_\". Gunakan 62 (tanpa prefix \"+\") ";
				            		 directMsg += "sebagai pengganti digit \"0\" di depan nomor telepon Anda.";
				            		 break;
				            	//case "promo":
				            	case "helppromo":
				            		 directMsg 	= "Anda dapat mengakses informasi promo BNI (@" + twitterAccount + ") via Twitter DM dengan format:\n #Promo (spasi) #Keyword \n";
				            		 directMsg += "\nContoh:\n #Promo #Travel \n\nDM kami dengan mengetik #HelpPromo untuk mengetahui semua #Keyword yang ada untuk #Promo dari BNI.";
				            		 break;
				            	//case "cs":	 
				            	case "helpcs":
				            	case "helpbni":
				            		 directMsg 	= "Anda dapat mengakses informasi tentang produk dan layanan BNI (@" + twitterAccount + ") via Twitter DM dengan format:\n #AskBNI (spasi) #Keyword \n";
				            		 directMsg += "\nContoh:\n #AskBNI #Taplus \n\nDM kami dengan mengetik #HelpBNI untuk mengetahui semua #keyword yang ada untuk #AskBNI.";
				            		 break;
				            	default:
				            		 break;
				            }
				            
				            try {
				            	if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
			     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
			     					twitterLog.saveOutwardDM(recipientId, directMsg, "AskBNI", msg);
			     				}
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
		     			
		     			directMsg = "Ketik #Promo dan gunakan keywords berikut: \n" + keywords + "\nuntuk mendapatkan promo-promo menarik & terbaru dari BNI.\nContoh Penggunaan: send DM, ketik: #Promo (spasi) #Hotel";
		     			try {
		     				if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
		     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
		     					twitterLog.saveOutwardDM(recipientId, directMsg, "HelpPromo", "HelpPromo");
		     				}
							//System.out.println("Sent: " + directMessage.getSenderScreenName() + message.getText() + " to @" + directMessage.getSenderScreenName());
				            //asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
			    		    //System.out.println("Sent: " + directMsg + " to @" + status.getUser().getScreenName());
						} catch (TwitterException e) {
							e.printStackTrace();
						}
		     		}
		     		
		     		//(1C) #helpcs /#helpbni [DONE]
		     		else if( command.equals("helpcs") || command.equals("helpbni") )
		     		{
		     			String keywords = "";
		     			for (String alias : aliasmenus) {
		     				keywords += "#" + alias + "\n";
		     			}
		     			
		     			directMsg = "Ketik #AskBNI dan gunakan keywords berikut: \n" + keywords + "\nuntuk mengakses topik-topik layanan nasabah dari BNI.\nContoh Penggunaan: send DM, ketik: #AskBNI (spasi) #Taplus";
		     			try {
		     				if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
		     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
		     					twitterLog.saveOutwardDM(recipientId, directMsg, "HelpBNI", "HelpBNI");
		     				}
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
	            else if( command.equals("daftar") )
	            {
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
	            		directMsg   = "Yth Bp/Ibu " + name + ", Mohon Maaf. Untuk format penulisan nomor telepon membutuhkan prefix 62 (tanpa prefix \"+\") ";
	            		directMsg  += "sebagai pengganti angka 0 pada digit depan nomor telepon Anda. Cth: 0811345890 menjadi 62811345890.";
	            	}
	            	
	            	if(rawPhoneNo.length() < 9 || rawPhoneNo.length() > 15)
	            	{
	            		String regex = "\\d+";
	            		if(name.matches(regex))
	            			name = "";
	            		else
	            			name = " " + name;
	            		
	            		errorDaftar = true;
	            		directMsg   = "Yth Bp/Ibu" + name + ", Mohon Maaf. Nomor telepon anda haruslah numerik/digit. Panjang nomor telepon anda minimal 9 digit, maksimal 15 digit ";
	            		directMsg  += "(termasuk prefix kode negara 62 (tanpa \"+\") pada no telepon Anda).";
	            	}
	            	
	            	if(errorDaftar)
	            	{
	            		recipientId = directMessage.getSenderScreenName();
			            try {
			            	if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
		     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
		     					twitterLog.saveOutwardDM(recipientId, directMsg, "Error #Daftar", "#Daftar");
		     				}
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
	            		
	            		String daftarResponseQuery = "SELECT message_content FROM tbl_directmessages WHERE message_type = 'daftar' AND message_deleted = '0' ORDER BY message_id ASC";
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
			            
			            //System.out.println("Registered user:" + directMessage.getSenderScreenName() + " will be responded by DM." );
			            
			            try {
			            	if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
		     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
		     					twitterLog.saveOutwardDM(recipientId, directMsg, "Sukses #Daftar", "Daftar");
		     				}
							//System.out.println("Sent: " + message.getText() + " to @" + directMessage.getSenderScreenName());
				            //asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
			    		    //System.out.println("Sent: " + directMsg + " to @" + status.getUser().getScreenName());
						} catch (TwitterException e) {
							e.printStackTrace();
						}
			            
	            		statLogger.eventsLog("3", directMessage.getSenderScreenName(), name);
	            	}
	            	
	            	hashtags.clear();
		             
	            }//else if(DM for Register)
	            
	            // (3) #promo #keyword #keyword [DONE]
	            else if( command.equals("promo") )
	            {
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
			            				+ "AND tbl_promotions.promotion_enddate >= '" + today + "' " 
			            				+ "AND tbl_promotions.promotion_deleted = '0' AND tbl_hashtags.hashtag_deleted = '0'";
			            	//System.out.println(promoQuery);
			            	
				     		try {
						     		if(con == null){
						     			db_object.openConnection();
						  				con = db_object.getConnection();
						  	        }
					     			stm = con.createStatement();
					     			rs  = stm.executeQuery(promoQuery);
					     			if ( !rs.next() ) { 
					     				//directMessagesPromoAndServices.add("Yth. Bp/Ibu, Mohon maaf. Promo #" + tag + " dari BNI yang Anda inginkan tidak tersedia.");
					     				directMessagesPromoAndServices.add("");
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
				     		
				     			statLogger.eventsLog("4", directMessage.getSenderScreenName(), tag);
				     		
						}
			            	
				        // *We then send out all possible DM per hashTAGS
				        for (String msg : directMessagesPromoAndServices) {
					            recipientId = directMessage.getSenderScreenName();
					            directMsg = msg;
					            try {
					            	if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
				     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
				     					twitterLog.saveOutwardDM(recipientId, directMsg, "Promo", hashtags.toArray().toString());
				     				}
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
	            		//UNCOMMENT
	            		//recipientId = directMessage.getSenderScreenName();
	            		//directMsg = "Yth. Bp/Ibu, Mohon Maaf. Permintaan info #Promo memerlukan minimal satu #keyword topik. Cth: #Promo #Travel, #Promo #Hotel #eCommerce. ";
		     			//try {
						//	if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
     					//		DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
     					//	}
							//System.out.println("Sent: " + message.getText() + " to @" + directMessage.getSenderScreenName());
				            //asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
			    		    //System.out.println("Sent: " + directMsg + " to @" + status.getUser().getScreenName());
						//} catch (TwitterException e) {
						//	e.printStackTrace();
						//}
	            		statLogger.eventsLog("4", directMessage.getSenderScreenName(), "");
	            	}
	            	
			        hashtags.clear(); 
			        directMessagesPromoAndServices.clear();
	            }//else if(DM for Promo)
	            
	            // (4) #cs or #askbni + #keyword #keyword || #askbni + #poin + #cardNo [DONE] 
	            else if( command.equals("cs") || command.equals("askbni") )
	            {
	            	if(hashtags.size() > 1)
	            	{
	            		String askPoint   = hashtags.get(1).toLowerCase();
	            		if(askPoint.equalsIgnoreCase("poin") || askPoint.equalsIgnoreCase("point")) //Asking about point
	            		{
	            			// *We compose DM #AskBNI #Poin per #cardNo
	            			String pointCardno  = hashtags.get(2);
	            			//String pointQuery = "SELECT point_monthly, point_quarterly, point_grandprize FROM tbl_points WHERE point_cardno = '" + pointCardno + "'";
	            			String pointMonthlyQuery    = "SELECT point_monthly FROM tbl_points_monthly WHERE point_cardno = '" + pointCardno + "'";
	            			String pointQuarterlyQuery  = "SELECT point_quarterly FROM tbl_points_quarterly WHERE point_cardno = '" + pointCardno + "'";
	            			String pointGrandprizeQuery = "SELECT point_grandprize FROM tbl_points_grandprize WHERE point_cardno = '" + pointCardno + "'";
	            			
	            			//System.out.println("Asking String: "    + hashtags.toString());
	            			//System.out.println("Requested CardNo: " + pointCardno);
	            			
	            			stm = null; rs = null;
	            			try {
					     		if(con == null){
					     			db_object.openConnection();
					  				con = db_object.getConnection();
					  	        }
				     			stm  = con.createStatement();
				     			rs   = stm.executeQuery(pointMonthlyQuery);
				     			stm  = con.createStatement();
				     			rs2  = stm.executeQuery(pointQuarterlyQuery);
				     			stm  = con.createStatement();
				     			rs3  = stm.executeQuery(pointGrandprizeQuery);
				     			if ( !rs.next() && !rs2.next() && !rs3.next() ) { 
				     				directMessagesPromoAndServices.add("Yth. Bp/Ibu, Mohon maaf. Info poin untuk Kartu Debit Anda #" + pointCardno + " tidak tersedia atau ada kesalahan penulisan No. Kartu. " 
				     												+  "Silakan periksa kembali 16 Digit No. Kartu Anda. Bila masih bermasalah, mohon menghubungi BNI Call 1500046.");
				     			} 
				     			else
				     			{	String pointMonthly = "0", pointQuarterly = "0", pointGrandPrize = "0";
				     				rs.beforeFirst(); rs2.beforeFirst(); rs3.beforeFirst();
				     				while (rs.next()) { pointMonthly    = rs.getString("point_monthly");     break; }
				     				while (rs2.next()){ pointQuarterly  = rs2.getString("point_quarterly");  break; }
				     				while (rs3.next()){ pointGrandPrize = rs3.getString("point_grandprize"); break; }
				     				
				     				Calendar c = Calendar.getInstance();
				     				c.add(Calendar.MONTH, -1);
				     			    c.set(Calendar.DATE, 1);
				     				String[] strMonths = new String[] { "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember" };
				     				directMessagesPromoAndServices.add(
				     						"Poin Rejeki BNI Taplus untuk nomor kartu : " + pointCardno + "\n\n"
				     						+ "1. Poin Bulanan : " + pointMonthly  + "\n"
				     						+ "2. Poin 3 Bulanan : " + pointQuarterly  + "\n"
				     						+ "3. Poin Grand Prize : " + pointGrandPrize + "\n\n" 
				     						+ "Terus tingkatkan saldo dan transaksi Anda. \n" + "Periode Poin : " + c.get(Calendar.DATE) + " - " + c.getActualMaximum(Calendar.DATE) + " " + strMonths[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR)
				     				);
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
	            			
	            			// *We then send out points' information to customers
					        for (String msg : directMessagesPromoAndServices) {
						            recipientId = directMessage.getSenderScreenName();
						            directMsg = msg;
						            try {
						            	if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
					     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
					     					twitterLog.saveOutwardDM(recipientId, directMsg, "Point", "Point");
					     				}
										//System.out.println("Sent: " + message.getText() + " to @" + directMessage.getSenderScreenName());
										//asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
						    		    //System.out.println("Sent: " + directMsg + " to @" + directMessage.getSenderScreenName());
									} catch (TwitterException e) {
										e.printStackTrace();
									}
					         }
					        
					        statLogger.eventsLog("6", directMessage.getSenderScreenName(), "Point");
	            		}
	            		else //Not Asking Point
	            		{
		            		// *We compose DM per #AskBNI hashTAGS
	            			//System.out.println("Hashtag size: " + hashtags.size());
				            String csQuery = "";
				            stm = null; rs = null;
				            for (String tag : hashtags) {
				            	if ( tag.toLowerCase().equals("cs") || tag.toLowerCase().equals("askbni") )
				            	      continue;
				            	csQuery = "SELECT * FROM tbl_customerservices LEFT JOIN tbl_hashtags " 
				            			  + "ON tbl_hashtags.hashtag_id = tbl_customerservices.cs_hashtag "
				            			  + "WHERE tbl_hashtags.hashtag_term = '" + tag + "' AND tbl_hashtags.hashtag_deleted = '0' AND tbl_customerservices.cs_deleted = '0'";
				            	//System.out.println(csQuery);
				            	
					     		try {
							     		if(con == null){
							     			db_object.openConnection();
							  				con = db_object.getConnection();
							  	        }
						     			stm = con.createStatement();
						     			rs  = stm.executeQuery(csQuery);
						     			if ( !rs.next() ) { 
						     				//directMessagesPromoAndServices.add("Yth. Bp/Ibu, Mohon maaf. Info tentang #" + tag + " yang Anda inginkan tidak tersedia atau ada kesalahan penulisan. " 
						     				//								+  "Contoh penulisan yang benar:\n#Promo (spasi) #Travel \n#AskBNI (spasi) #Taplus");
						     				directMessagesPromoAndServices.add("");
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
					     			
					     		statLogger.eventsLog("6", directMessage.getSenderScreenName(), tag);
					     		
							 }
				
					         // *We then send out all possible DM per hashTAGS
					         for (String msg : directMessagesPromoAndServices) {
						            recipientId = directMessage.getSenderScreenName();
						            directMsg = msg;
						            try {
						            	if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
					     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
					     					twitterLog.saveOutwardDM(recipientId, directMsg, "AskBNI", hashtags.toArray().toString());
					     				}
										//System.out.println("Sent: " + message.getText() + " to @" + directMessage.getSenderScreenName());
										//asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
						    		    //System.out.println("Sent: " + directMsg + " to @" + directMessage.getSenderScreenName());
									} catch (TwitterException e) {
										e.printStackTrace();
									}
					         }
	            		}//Else: Not Asking Point
	            	}
	            	
	            	else if( hashtags.size() == 1 && command.equals("cs") )
	            	{	
	            		//UNCOMMENT
	            		//recipientId = directMessage.getSenderScreenName();
	            		//directMsg = "Yth. Bp/Ibu, Mohon Maaf. Permintaan info #CS yang melalui #AskBNI memerlukan minimal satu #keyword topik. Cth: #AskBNI #KartuHilang, #AskBNI #TaplusMuda #KartuTertelan. ";
		     			//try {
	            			//if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
	            			//	DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
	            			//}
							//System.out.println("Sent: " + message.getText() + " to @" + directMessage.getSenderScreenName());
				            //asyncTwitterDM.sendDirectMessage(recipientId, directMsg);
			    		    //System.out.println("Sent: " + directMsg + " to @" + status.getUser().getScreenName());
						//} catch (TwitterException e) {
						//	e.printStackTrace();
						//}
	            		statLogger.eventsLog("6", directMessage.getSenderScreenName(), "");
	            	}
	            	else if( hashtags.size() == 1 && command.equals("askbni") )
	            	{
	            		// *We compose DMes sending list of menus/helps/cses
		            	stm = null; rs = null;
		            	String menuQuery = "SELECT hashtag_term, hashtag_alias FROM tbl_hashtags WHERE hashtag_category = 'menu' AND hashtag_deleted = '0'";
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
				        for (String msg : menus) {
					            switch (msg.toLowerCase()) {
					            	//case "menu":
					            	//case "help":
					            		 //directMsg 	= "Anda dapat mengirim DM dengan \"#Help\" (tanpa double quote) untuk mengakses daftar menu layanan BNI (@" + twitterAccount + ") via Twitter. ";
					                     //break;
					            	case "daftar":
					            		 directMsg 	= "Anda dapat mendaftar layanan BNI (@" + twitterAccount + ") via Twitter DM dengan format:\n #daftar #nama_lengkap #nohandphone \n";
					            		 directMsg += "\nContoh:\n  #daftar #Andi_Waluyo #62213456789 \n\nNote: \nNama Awal dan Akhir dipisah dengan \"_\". Gunakan 62 (tanpa prefix \"+\") ";
					            		 directMsg += "sebagai pengganti digit \"0\" di depan nomor telepon Anda.";
					            		 break;
					            	//case "promo":
					            	case "helppromo":
					            		 directMsg 	= "Anda dapat mengakses informasi promo BNI (@" + twitterAccount + ") via Twitter DM dengan format:\n #Promo (spasi) #Keyword \n";
					            		 directMsg += "\nContoh:\n #Promo #Travel \n\nDM kami dengan mengetik #HelpPromo untuk mengetahui semua #Keyword yang ada untuk #Promo dari BNI.";
					            		 break;
					            	//case "cs":	 
					            	case "helpcs":
					            	case "helpbni":
					            		 directMsg 	= "Anda dapat mengakses informasi tentang produk dan layanan BNI (@" + twitterAccount + ") via Twitter DM dengan format:\n #AskBNI (spasi) #Keyword \n";
					            		 directMsg += "\nContoh:\n #AskBNI #Taplus \n\nDM kami dengan mengetik #HelpBNI untuk mengetahui semua #keyword yang ada untuk #AskBNI.";
					            		 break;
					            	default:
					            		 break;
					            }
					            
					            try {
					            	//System.out.println("recipient: " + recipientId + " & DM: " + directMsg);
					            	if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
				     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
				     					twitterLog.saveOutwardDM(recipientId, directMsg, "AskBNI", "#AskBNI");
				     					
				     				}
								} catch (TwitterException e) {
									e.printStackTrace();
								}
				        }
				        
				        statLogger.eventsLog("6", directMessage.getSenderScreenName(), "AskBNI");
		            	
	            	}
			        
	            	hashtags.clear(); menus.clear(); aliasmenus.clear();
			        directMessagesPromoAndServices.clear();
	            }//else if(DM for CS & AskBNI)  
	            
	            // (5) #openaccount #taplusmuda + #Full_Name #PhoneNo [DONE] || #csopenaccount #taplusmuda + #AccountNo #AccountCode #CSEmployeeCode [DONE]
	            else if( command.equals("openaccount") || command.equals("open") || command.equals("csopen") || command.equals("csregister") || command.equals("registercs") )
	            {	
	            	//Once user gets the command right, server will reply with feedback accordingly.
        			recipientId = directMessage.getSenderScreenName();

        			Date currentDate            	 = new Date();
        			SimpleDateFormat codeFormat      = new SimpleDateFormat("yyMd");
        			SimpleDateFormat codeStdFormat   = new SimpleDateFormat("yyMMddHHmmss");
        			SimpleDateFormat validDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        			SimpleDateFormat readableFormat  = new SimpleDateFormat("dd MMMMM yyyy");
        			SimpleDateFormat dateDigit		 = new SimpleDateFormat("dd");
        			SimpleDateFormat monthDigit		 = new SimpleDateFormat("MM");
        			SimpleDateFormat yearDigit		 = new SimpleDateFormat("yyyy");
        			
        			String campaign_code = "";
        			String[] strMonths   = new String[] { "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember" };
        			
	            	if(command.equals("openaccount") || command.equals("open")) // [DONE]
	            	{
	            		boolean requirement  = true; 
	        			String campaign_tag  = hashtags.get(1);
	           
	            		//Check: number of  hashtags to be at least 4 (four)
	            		if(hashtags.size() < 4)
	            		{ 
	            			directMsg 	= "Maaf, format pesan Twitter DM untuk promo pembukaan rekening tabungan BNI anda tidak sesuai. ";
	            			directMsg  += "Silakan DM dengan format: #OpenAccount #TaplusMuda #Nama_Lengkap #No_HP.";
	            			directMsg  += "\nContoh:\n #OpenAccount #TaplusMuda #Denny_Dalvian #08118824686";
	            			try {
				            	//System.out.println("#OpenAccount #TaplusMuda with recipient: " + recipientId + " & DM: " + directMsg);
	            				if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
			     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
			     				}
				            	requirement = false;
							} catch (TwitterException e) {
								e.printStackTrace();
							}
	            		}
	            		
	            		//Check: 2nd hashtags must exist in campaign table "taplusmuda" WHEN num of hashtags is indeed 4 (four) or more.
	            		//2nd hashtags will determine the Reference Code also.
	            		
	            		if(requirement)
	            		{
	            			Date now = new Date();
	    	            	today    = validDateFormat.format(now);
	            			String campaigntypeQuery  = "SELECT campaign_code FROM tbl_account_campaign WHERE campaign_tag = '" + campaign_tag  + "' " 
	            									  + "AND campaign_enddate >= '" + today + "' AND campaign_status = 1 AND campaign_deleted = '0' LIMIT 1";
	            			System.out.println("\n[Open Query] "+ campaigntypeQuery);
	            			stm = null; rs = null;
	            	        try {
	            		     	if(con == null){
	            		     		db_object.openConnection();
	            		  			con = db_object.getConnection();
	            		        }
	            		     	stm = con.createStatement();	     		
	            		     	rs  = stm.executeQuery(campaigntypeQuery);
	            		     	while (rs.next()){
	            		     		campaign_code = rs.getString("campaign_code");
	            		     		System.out.println("Campaign type's Code: "+ campaign_code);
	            		     	}
	            	        } catch (SQLException e) {
	            	 	   	 	e.printStackTrace();
	            	     	} 
	            	        finally {
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
	            	        
	            			if(campaign_code.equalsIgnoreCase("")){
	            				directMsg 	= "Maaf, Pesan Twitter DM tentang Promo Pembukaan Rekening yg Anda inginkan tidak tersedia/telah berakhir. ";
		            			directMsg  += "\nSilakan DM dengan Promo Pembukaan Rekening yg tersedia/masih berlaku dgn DM: #OpenAccount #PromoPembukaanRekening #Nama_Lengkap #No_HP.";
		            			directMsg  += "\nContoh:\n #OpenAccount #TaplusMuda #Denny_Dalvian #08118824686";
		            			try {
					            	//System.out.println("#OpenAccount #PromoRekening with recipient: " + recipientId + " & DM: " + directMsg);
		            				if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
				     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
				     				}
					            	requirement = false;
								} catch (TwitterException e) {
									e.printStackTrace();
								}
	            			}
	            			
	            		}
	            		
	            		
	            		if(requirement)
	            		{
	            			//Proceed as all req. satisfied by Customer
	            			
	            			//Check the latest sequence number of already-DB-recorded account openings.
	            			Integer lastSeqAccNumber = 1;
	            			String lastAccountQuery  = "SELECT id as last_id FROM tbl_account_users ORDER BY id DESC LIMIT 1";
							stm = null; rs = null;
				            try {
						     	if(con == null){
						     		db_object.openConnection();
						  			con = db_object.getConnection();
						        }
						     	stm = con.createStatement();	     		
						     	rs  = stm.executeQuery(lastAccountQuery);
						     	while (rs.next()){
						     		lastSeqAccNumber = rs.getInt("last_id") + 1;
						     	}
				            } catch (SQLException e) {
				     	   	 	e.printStackTrace();
					     	} 
				            finally {
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
	            			
	            			String valid_date 			= validDateFormat.format(currentDate);
	            			String account_date		    = dateFormat.format(currentDate); 
	            			String account_code         = "BNI" + campaign_code + codeFormat.format(currentDate) + lastSeqAccNumber.toString();
	            			//NOTE: next time, check whether it already exists in DB.
	            			//For now, it is CLOSE to safe, unique as it contains time until seconds.
	            			String account_holder   		= hashtags.get(2);
	            			String account_phone    		= hashtags.get(3);
	            			String account_handler  		= recipientId; 
	            			String account_merchandise 		= "0";
	            			String account_merchandise_name = "Merchandise BNI";
	            			
	            			//Retrieving promotional merchandise from BNI, which is valid in period, in active promo, and of course: in stock.
	            			String merchandiseQuery = "SELECT merchandise_id, merchandise_name FROM tbl_account_merchandise " 
	            									+ "WHERE " + account_date + " <= valid_until AND merchandise_active = 1 AND merchandise_stock >= merchandise_redeemed "
	            									+ "ORDER BY merchandise_priority ASC LIMIT 0,1";
	            			stm = null; rs = null;
				            try {
						     	if(con == null){
						     		db_object.openConnection();
						  			con = db_object.getConnection();
						        }
						     	stm = con.createStatement();	     		
						     	rs  = stm.executeQuery(merchandiseQuery);
						     	while (rs.next()){
						     		account_merchandise 	 = rs.getString("merchandise_id");
						     		account_merchandise_name = rs.getString("merchandise_name");
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
				            
				            //After having all data, inserting the whole account-opening data into our database.
				            //Sanitize the ACC HOLDER
				            
				            //Sanitize [1]: Remove '_'
				            account_holder = account_holder.replace("_", " "); 
				            
				            //Sanitize [2]: Capitalize Each Word of Names
				            String[] account_holder_tokens = account_holder.split(" ");
				            account_holder 				   = "";

				            for(int i = 0; i < account_holder_tokens.length; i++){
				                char capLetter = Character.toUpperCase(account_holder_tokens[i].charAt(0));
				                account_holder +=  " " + capLetter + account_holder_tokens[i].substring(1);
				            }
				            account_holder = account_holder.trim();
				            
				            //System.out.println(account_holder);
				            String accountOpeningQuery = "INSERT INTO tbl_account_users(account_code, account_holder, account_handler, account_phone, account_merchandise, account_merchandise_name, account_code_date) " 
				            						   + "VALUES ('" + account_code + "', '" + account_holder + "', '" + account_handler + "', '" + account_phone + "', '" + account_merchandise + "', '" + account_merchandise_name + "', '" + valid_date + "')";
				            stm = null; 
				            try {
							     	if(con == null){
							     		db_object.openConnection();
							  			con = db_object.getConnection();
							        }
							     	stm = con.createStatement();	     		
							     	stm.executeUpdate(accountOpeningQuery);
							     	//System.out.println("Registered Taplus Muda Customer: " + directMessage.getSenderScreenName() + " [" + account_holder + " | " + account_code + "]" );
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
				            
				            //Finally, send DM to Customer with Account Reference Code
				            String AccountOpeningResponseQuery = "SELECT message_content FROM tbl_directmessages WHERE message_type = 'customer_open_account' AND message_deleted = '0' ORDER BY message_id ASC";
				            stm = null; rs = null; directMsg = "";
				            try {
						     	if(con == null){
						     		db_object.openConnection();
						  			con = db_object.getConnection();
						        }
						     	stm = con.createStatement();	     		
						     	rs  = stm.executeQuery(AccountOpeningResponseQuery);
						     	while (rs.next()){
						     		directMsg   += rs.getString("message_content");
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
				            
				            //Prior to sending, replace some variables within the DM template & format the output valid date
				            Date convertedValidDate = null;
							try {
								//Change from String to Date() by Parsing the String of Date.
								DateFormat parser  = new SimpleDateFormat("yyyy-MM-dd");
								convertedValidDate = (Date) parser.parse(valid_date);
							} catch (ParseException pe) {
								pe.printStackTrace();
							}
							//2nd, Format the Date Object into format we wish for.
				            String valid_date_digit  = dateDigit.format(convertedValidDate);
				            String valid_month_digit = monthDigit.format(convertedValidDate);
				            String valid_year_digit  = yearDigit.format(convertedValidDate);
				            valid_date				 = valid_date_digit + " " + strMonths[Integer.parseInt(valid_month_digit)-1] + " " + valid_year_digit;
				            //valid_date			 = readableDateFormat.format(convertedValidDate);
				            
				            directMsg = directMsg.replace("@account_holder", account_holder);
				            directMsg = directMsg.replace("@account_code", account_code);
				            directMsg = directMsg.replace("@account_handler", account_handler);
				            directMsg = directMsg.replace("@account_merchandise_name", account_merchandise_name);
				            directMsg = directMsg.replace("@valid_date", valid_date);
				            
				            try {
				            	//System.out.println("#OpenAccount #TaplusMuda with recipient: " + recipientId + " & DM: " + directMsg);
				            	if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
			     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
			     					twitterLog.saveOutwardDM(recipientId, directMsg, "Open Account", "#OpenAccount");
			     				}
							} catch (TwitterException e) {
								e.printStackTrace();
							}
				            
				            statLogger.eventsLog("7", directMessage.getSenderScreenName(), "OpenAccount");
				            
	            		}
	            		
	            	}//END #OpenAccount
	            	
	            	else if(command.equals("csopen")) // [DONE]
	            	{
	            		boolean requirement   = true;
	            		String campaign_tag   = hashtags.get(1);
	            		String account_no     = hashtags.get(2).toUpperCase(); 
            			String account_code   = hashtags.get(3).toUpperCase(); 
            			String account_cs_no  = hashtags.get(4).toUpperCase();
	            		
	            		//Check: number of  hashtags to be at least 5 (five)
	            		if(hashtags.size() < 5)
	            		{ 
	            			directMsg 	= "Maaf, Reporting Pembukaan Rekening Tabungan BNI Nasabah Anda tidak sesuai. ";
	            			directMsg  += "Silakan DM dengan format: #CSOpen #PromoRekening #NoRekNasabah #NoRef #NPP.";
	            			directMsg  += "\nContoh:\n #CSOpen #TaplusMuda #0191063890 #BNI123456 #NPP30000";
	            			try {
				            	//System.out.println("#CSOpen #TaplusMuda with recipient: " + recipientId + " & DM: " + directMsg);
	            				if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
			     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
			     				}
				            	requirement			  = false;
							} catch (TwitterException e) {
								e.printStackTrace();
							}
	            		}
	            		
	            		//Check: 2nd hashtags must exist in campaign table such as "taplusmuda" WHEN num of hashtags is indeed 5 (five) or more.
	            		//2nd hashtags will determine the Reference Code also.
	            		if(requirement)
	            		{
	            			Date now = new Date();
	    	            	today    = validDateFormat.format(now);
	    	            	String campaigntypeQuery  = "SELECT campaign_code FROM tbl_account_campaign WHERE campaign_tag = '" + campaign_tag  + "' " 
									                  + "AND campaign_enddate >= '" + today + "' AND campaign_status = 1 AND campaign_deleted = '0' LIMIT 1";
	            			System.out.println("\n[CSOpen Query] "+ campaigntypeQuery);
	            			stm = null; rs = null;
	            	        try {
	            		     	if(con == null){
	            		     		db_object.openConnection();
	            		  			con = db_object.getConnection();
	            		        }
	            		     	stm = con.createStatement();	     		
	            		     	rs  = stm.executeQuery(campaigntypeQuery);
	            		     	while (rs.next()){
	            		     		campaign_code = rs.getString("campaign_code");
	            		     		System.out.println("Campaign type's Code: "+ campaign_code);
	            		     	}
	            	        } catch (SQLException e) {
	            	 	   	 	e.printStackTrace();
	            	     	} 
	            	        finally {
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
	            	        
	            			if(campaign_code.equalsIgnoreCase("")){
	            				directMsg 	= "Maaf, Reporting Promo Pembukaan Rekening Nasabah Anda gagal untuk promo yg diinginkan karena tidak tersedia/telah berakhir. ";
		            			directMsg  += "\nSilakan DM dengan Promo lainnya yg tersedia/masih berlaku untuk Pembukaan Rekening dgn DM: #CSOpen #PromoRekening #NoRekNasabah #NoRef #NPP.";
		            			directMsg  += "\nContoh:\n #CSOpen #TaplusMuda #0191063890 #BNI123456 #NPP30000";
		            			try {
					            	//System.out.println("#OpenAccount #PromoRekening with recipient: " + recipientId + " & DM: " + directMsg);
		            				if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
				     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
				     				}
					            	requirement = false;
								} catch (TwitterException e) {
									e.printStackTrace();
								}
	            			}
	            			
	            		}
	            		
	            		//Check: Genuine report or not. 
	            		//1. Avoid non-exist-ref. no. report attempt over a single account is carried out by CS Officer.
	            		if(requirement)
	            		{
	            			// We need to check whether a CS has accourately referenced a customer's ref. no. in #CSOpen,
	            			// Preventing unnecessary lengthy process for assigning CS.
	            			// Hence, Trx Information Ref. Code is checked & confirmed as an already existing Ref. Code.
	            			String accInfoQuery = "SELECT id, account_holder FROM tbl_account_users " 
	            							   	+ "WHERE  account_code = '" + account_code + "' "  
	            							   	+ "ORDER BY id ASC LIMIT 0,1";
	            			stm = null; rs = null;
				            try {
						     	if(con == null){
						     		db_object.openConnection();
						  			con = db_object.getConnection();
						        }
						     	stm = con.createStatement();	     		
						     	rs  = stm.executeQuery(accInfoQuery);
						     	while (!rs.next()){
						     		rs.beforeFirst();
						     		directMsg 	= "Maaf, report pembukaan rekening " + account_no + " tabungan BNI Nasabah anda dengan No Referensi #" + account_code + " gagal tercatat karena No Referensi Anda yg invalid.";
			            			directMsg  += "Silakan periksa dan koreksi kembali laporan pembukaan rekening tabungan dengan No Referensi yg valid dari nasabah anda. ";
			            			directMsg  += "\nFormat:\n #CSOpen #TaplusMuda #NoRekNasabah #NoRef #NPP";
			            			directMsg  += "\nContoh:\n #CSOpen #TaplusMuda #0191063890 #BNI1234567890 #NPP30000";
			            			try {
			            				if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
					     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
					     				}
							     		requirement	= false;
				            			break;
									} catch (TwitterException e) {
										e.printStackTrace();
									}
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
				            
	            		}
	            		
	            		//2. Avoid non-exist-CS Code report attempt over a single account is carried out by CS Officer.
	            		if(requirement)
	            		{
	            			// We need to check whether a CS has precisely reported her Officer Code for #OpenAccount correctly,
	            			// Preventing CS not getting her KPI Point increased fairly.
	            			// Hence, CS Officer Code is checked & confirmed as a one-time only report from CS and associated to single CS Officer.
	            			String csInfoQuery = "SELECT cs_id FROM tbl_account_cs " 
	            							   + "WHERE  cs_employee_code = '" + account_cs_no + "' "  
	            							   + "ORDER BY cs_id ASC LIMIT 0,1";
	            			stm = null; rs = null;
				            try {
						     	if(con == null){
						     		db_object.openConnection();
						  			con = db_object.getConnection();
						        }
						     	stm = con.createStatement();	     		
						     	rs  = stm.executeQuery(csInfoQuery);
						     	while(!rs.next()){
						     		rs.beforeFirst();
						     		directMsg 	= "Maaf, report pembukaan rekening " + account_no + " tabungan BNI Nasabah anda dengan No Referensi #" + account_code + " gagal tercatat karena Officer Code Anda yg invalid. ";
			            			directMsg  += "Silakan periksa dan koreksi kembali laporan pembukaan rekening tabungan dengan CS Officer Code Anda yg valid. ";
			            			directMsg  += "\nFormat:\n #CSOpen #TaplusMuda #NoRekNasabah #NoRef #NPP";
			            			directMsg  += "\nContoh:\n #CSOpen #TaplusMuda #0191063890 #BNI1234567890 #NPP30000";
			            			try {
			            				if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
					     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
					     				}
			            				requirement	= false;
			            				break;
									} catch (TwitterException e) {
										e.printStackTrace();
									}
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
				            
	            		}
	            		
	            		//3. Avoid CS reports #OpenAccount from others' Twitter Account other than her own account. (UNTESTED)
	            		if(requirement)
	            		{
	            			// If we want to only allow report attempt from HER OWN registered Twitter Account,
					     	// We need to check whether a CS has eported #OpenAccount using her own Twitter handler,
	            			// Preventing CS getting her KPI Point increased unfairly by other account.
	            			// Hence, CS Officer Account handler is checked & confirmed as a registered, valid Twitter account of a CS Officer.
	            			String registeredCSTwitterAccount = "";
	            			String csAccTwitterQuery = "SELECT cs_twitter FROM tbl_account_cs WHERE cs_employee_code = '" + account_cs_no + "' "  
	            							         + "LIMIT 0,1";
	            			stm = null; rs = null;
				            try {
						     	if(con == null){
						     		db_object.openConnection();
						  			con = db_object.getConnection();
						        }
						     	stm = con.createStatement();	     		
						     	rs  = stm.executeQuery(csAccTwitterQuery);
						     	while(rs.next()){
						     		registeredCSTwitterAccount = rs.getString("cs_twitter");
						     		if(!recipientId.equalsIgnoreCase(registeredCSTwitterAccount)){
						     			directMsg 	= "Maaf, report pembukaan rekening " + account_no + " tabungan BNI Nasabah anda dengan No Referensi #" + account_code + " gagal tercatat ";
				            			directMsg  += "karena anda tidak menggunakan Akun Twitter sesuai yg terdaftar. Silakan laporkan kembali pembukaan rekening tabungan menggunakan Akun Twitter Anda yg terdaftar di sistem. ";
				            			directMsg  += "\n\n(Gunakan akun @" + registeredCSTwitterAccount + " Anda)";
				            			try {
				            				if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
						     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
						     				}
				            				requirement	= false;
										} catch (TwitterException e) {
											e.printStackTrace();
										}
						     		}
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
				            
	            		}
	            		
	            		//4. Avoid a single account's multiple report attempt carried out by CS Officer.
	            		if(requirement)
	            		{
	            			// We need to check whether a CS has peviously reported same #OpenAccount successfully,
	            			// Preventing CS KPI getting increased by cheating.
	            			// Hence, Customer Individual Information is checked & confirmed as a one-time only report from CS and associated to single CS Officer.
	            			String customerInfoQuery = "SELECT account_no, account_cs, account_cs_name FROM tbl_account_users " 
	            							   		 + "WHERE  account_no = '" + account_no + "' OR account_cs > 0 AND account_code = '" + account_code + "' "  
	            							   		 + "ORDER BY account_no ASC LIMIT 0,1";
	            			stm = null; rs = null;
				            try {
						     	if(con == null){
						     		db_object.openConnection();
						  			con = db_object.getConnection();
						        }
						     	stm = con.createStatement();	     		
						     	rs  = stm.executeQuery(customerInfoQuery);
						     	while (rs.next()){
						     		directMsg 	= "Maaf, report pembukaan rekening:";
						     		directMsg  += "\n\nNomor Rekening: " + account_no + " \nRef. No.: #" + account_code + " \nStatus: Sudah Tercatat pada Database. \n";
			            			directMsg  += "\nCustomer Service tidak diperkenankan melaporkan berkali-kali atas pembukaan satu rekening tabungan dengan nomor referensi yg sama untuk nasabah yang sama. ";
			            			try {
			            				if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
					     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
					     				}
							     		requirement	= false;
									} catch (TwitterException e) {
										e.printStackTrace();
									}
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
				            
	            		}
	            		
	            		//All Passed.
	            		if(requirement)
	            		{
	            			//Proceed as all req. satisfied by CS
	            			
	            			//DEFAULT VALUE leads to DIRTY DATA
	            			//String account_holder   		  = "Customer of BNI";
	            			//String account_handler  		  = "Handler of Customer"; 
	            			//String account_merchandise      = "0";
	            			//String account_merchandise_name = "Merchandise BNI";
	            			//String valid_date				  = "0000-00-00";
	            			
	            			String account_holder   		= null;
	            			String account_handler  		= null; 
	            			String account_merchandise      = null;
	            			String account_merchandise_name = null;
	            			String valid_date				= null;
	            			
	            			String cs_id       = "0";
	            			String cs_fullname = "CS BNI 46";
	            			Integer cs_point   = 0;

		        			SimpleDateFormat csDateUpdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				            Date now = new Date();
			            	today    = csDateUpdateFormat.format(now);
				            
				            //(A) Update the reporting CS Officer's KPI Point.
				            String csPointQuery = "UPDATE tbl_account_cs SET cs_point = cs_point + 100 "
         						   				+ "WHERE cs_employee_code = '" + account_cs_no + "' ";
				            stm = null; 
				            try {
							     	if(con == null){
							     		db_object.openConnection();
							  			con = db_object.getConnection();
							        }
							     	stm = con.createStatement();	     		
							     	stm.executeUpdate(csPointQuery);
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
				            
				            //Retrieving CS Information to be associated to Customer's Account that a CS has recently opened.
	            			String csInfoQuery = "SELECT cs_id, cs_fullname, cs_point FROM tbl_account_cs " 
	            							   + "WHERE cs_employee_code = '" + account_cs_no + "' "
	            							   + "ORDER BY cs_id ASC LIMIT 0,1";
	            			stm = null; rs = null;
				            try {
						     	if(con == null){
						     		db_object.openConnection();
						  			con = db_object.getConnection();
						        }
						     	stm = con.createStatement();	     		
						     	rs  = stm.executeQuery(csInfoQuery);
						     	while (rs.next()){
						     		cs_id 	     = rs.getString("cs_id");
						     		cs_fullname  = rs.getString("cs_fullname");
						     		cs_point     = rs.getInt("cs_point");
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
			            	
				            //(B) After Having Updated CS KPI Point & Retrieved CS Profile,
				            //    Then we update the customer's recently opened account data in our database & associate CS v Customers respectively.
				            String accountOpeningQuery = "UPDATE tbl_account_users SET account_no = '" + account_no + "', account_cs = '" + cs_id 
				            						   + "', account_cs_name = '" + cs_fullname + "', created_at = '" + today + "' "
				            						   + "WHERE account_code = '" + account_code + "'";
				            stm = null; 
				            try {
							     	if(con == null){
							     		db_object.openConnection();
							  			con = db_object.getConnection();
							        }
							     	stm = con.createStatement();	     		
							     	stm.executeUpdate(accountOpeningQuery);
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
				            
				            //(C) Retrieving Customer Individual Information to confirm a report from CS as well to update the Merchandise stock redeemed by the customer.
				            //    This data is used for composing DM feedback upon CS finished open account process.
	            			String customerInfoQuery = "SELECT account_holder, account_handler, account_merchandise, account_merchandise_name, account_code_date FROM tbl_account_users " 
	            							   		 + "WHERE  account_code = '" + account_code + "' " + "ORDER BY id ASC LIMIT 0,1";
	            			stm = null; rs = null;
				            try {
						     	if(con == null){
						     		db_object.openConnection();
						  			con = db_object.getConnection();
						        }
						     	stm = con.createStatement();	     		
						     	rs  = stm.executeQuery(customerInfoQuery);
						     	while (rs.next()){
						     		account_holder 	 		 = rs.getString("account_holder");
						     		account_handler  		 = rs.getString("account_handler");
						     		account_merchandise      = rs.getString("account_merchandise");
						     		account_merchandise_name = rs.getString("account_merchandise_name");
						     		valid_date 				 = rs.getString("account_code_date");
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
				            
				            //(D) Update the No. Merchandise redeemed stock (Incrementing No. of Merchandise's Redeemed Stock) 
				            String merchandiseStockQuery = "UPDATE tbl_account_merchandise SET merchandise_redeemed = merchandise_redeemed + 1 "
						   								 + "WHERE merchandise_id = " + account_merchandise + " "; 
				            stm = null; 
				            try {
							     	if(con == null){
							     		db_object.openConnection();
							  			con = db_object.getConnection();
							        }
							     	stm = con.createStatement();	     		
							     	stm.executeUpdate(merchandiseStockQuery);
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
				            
				            
				            //Finally, send DM to CS & Customer Respectively for Successful Opening Account's Confirmation. 
				            //Populate all data by Customers' Account DB.
				            
				            //1. DM to CS
				            String AccountOpeningResponseQuery = "SELECT message_content FROM tbl_directmessages WHERE message_type = 'cs_open_account_self' AND message_deleted = '0' ORDER BY message_id ASC";
				            stm = null; rs = null; directMsg = "";
				            try {
						     	if(con == null){
						     		db_object.openConnection();
						  			con = db_object.getConnection();
						        }
						     	stm = con.createStatement();	     		
						     	rs  = stm.executeQuery(AccountOpeningResponseQuery);
						     	while (rs.next()){
						     		directMsg   += rs.getString("message_content");
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
				            
				            //Prior to sending, replace some variables within the DM template & format the output of valid date.
				            SimpleDateFormat readableDateFormat = new SimpleDateFormat("dd MMMMM yyyy");
	            			String point_date 				    = readableDateFormat.format(currentDate);
	            			
	            			Date convertedValidDate = null;
							try {
								//1st, Change from String to Date() by Parsing the String of Date.
								DateFormat parser  = new SimpleDateFormat("yyyy-MM-dd");
								convertedValidDate = (Date) parser.parse(valid_date);
							} catch (ParseException pe) {
								pe.printStackTrace();
							}
							//2nd, Format the Date Object into format we wish for.
				            String valid_date_digit  = dateDigit.format(convertedValidDate);
				            String valid_month_digit = monthDigit.format(convertedValidDate);
				            String valid_year_digit  = yearDigit.format(convertedValidDate);
				            valid_date				 = valid_date_digit + " " + strMonths[Integer.parseInt(valid_month_digit)-1] + " " + valid_year_digit;
				            //valid_date			 = readableDateFormat.format(convertedValidDate);
	            			
				            directMsg = directMsg.replace("@account_holder", account_holder);
				            directMsg = directMsg.replace("@account_code", account_code);
				            directMsg = directMsg.replace("@account_handler", account_handler);
				            directMsg = directMsg.replace("@account_merchandise_name", account_merchandise_name);
				            directMsg = directMsg.replace("@valid_date", valid_date);
				            directMsg = directMsg.replace("@until_date", point_date);
				            directMsg = directMsg.replace("@cs_point", cs_point.toString());
				            
				            try {
				            	//System.out.println("#CSOpen #TaplusMuda with CS recipient: " + recipientId + " & DM: " + directMsg);
				            	if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
			     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
			     					twitterLog.saveOutwardDM(recipientId, directMsg, "Open Account", "#CSOpen");
			     				}
							} catch (TwitterException e) {
								e.printStackTrace();
							}
				            
				            
				            //2. DM to Customer
				            AccountOpeningResponseQuery = "SELECT message_content FROM tbl_directmessages WHERE message_type = 'cs_open_account_customer' AND message_deleted = '0' ORDER BY message_id ASC";
				            stm = null; rs = null; directMsg = "";
				            try {
						     	if(con == null){
						     		db_object.openConnection();
						  			con = db_object.getConnection();
						        }
						     	stm = con.createStatement();	     		
						     	rs  = stm.executeQuery(AccountOpeningResponseQuery);
						     	while (rs.next()){
						     		directMsg   += rs.getString("message_content");
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
				            
				            try {
				            	//System.out.println("#CSOpen #TaplusMuda with Customer recipient: " + recipientId + " & DM: " + directMsg);
				            	if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
			     					DirectMessage message = twitterDM.sendDirectMessage(account_handler, directMsg);
			     					twitterLog.saveOutwardDM(recipientId, directMsg, "Open Account", "#CustOpen");
			     				}
							} catch (TwitterException e) {
								e.printStackTrace();
							}
				            
				            statLogger.eventsLog("8", directMessage.getSenderScreenName(), "CSOpen");
				            
	            		}
	            	} //END #CSOpen
	            	
	            	else if(command.equals("csregister") || command.equals("registercs")) // [DONE]
	            	{
	            		boolean requirement = true; 
	            		//#CSRegister #Nama_Lengkap #NumPegawai #Unit_Kerja
	     	           
	            		//Check: number of  hashtags to be at least 4 (four)
	            		if(hashtags.size() < 4)
	            		{ 
	            			directMsg 	= "Maaf, format pesan Twitter DM anda untuk pendaftaran akun CS Officer program Twitter Banking tidak sesuai. ";
	            			directMsg  += "Silakan DM dengan format: #CSRegister #Nama_Lengkap #NumPegawai #Unit_Kerja.";
	            			directMsg  += "\nContoh:\n #CSRegister #Nungki_Kusumawati #NPP8888 #KC_Bojonegoro.";
	            			directMsg  += "\nGunakan Kode KC sbg Kantor Cabang, KLN sbg Kantor Layanan Nasabah, atau KW sbg Kantor Wilayah.";
	            			try {
				            	//System.out.println("#CSRegister #Nama_Lengkap #NumPegawai #Unit_Kerja with recipient: " + recipientId + " & DM: " + directMsg);
	            				if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
			     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
			     				}
				            	requirement	= false;
							} catch (TwitterException e) {
								e.printStackTrace();
							}
	            		}
	            		
	            		if(requirement)
	            		{
	            			//Proceed as all req. satisfied by CS Officer
	            			String officer_fullname    		= hashtags.get(1);
	            			String officer_employee_code   	= hashtags.get(2);
	            			String officer_uker				= hashtags.get(3);
	            			String officer_handler 			= directMessage.getSenderScreenName();
	            			
	            			//SANITATION FOR USER INPUT
	            			officer_employee_code = officer_employee_code.toUpperCase();
	            			//a) Sanitize CS Full Name
	            			//[1]: Remove '_'
	            			officer_fullname = officer_fullname.replace("_", " ");
				            
				            //[2]: Capitalize Each Word of Names
				            String[] officer_fullname_tokens = officer_fullname.split(" ");
				            officer_fullname 				 = "";

				            for(int i = 0; i < officer_fullname_tokens.length; i++){
				                char capLetter = Character.toUpperCase(officer_fullname_tokens[i].charAt(0));
				                officer_fullname +=  " " + capLetter + officer_fullname_tokens[i].substring(1);
				            }
				            officer_fullname = officer_fullname.trim();
				            
				            //b) Sanitize CS Unit Kerja
				            //[1]: Remove '_'
				            officer_uker	 = officer_uker.replace("_", " "); 
				            
				            //[2]: Capitalize Each Word of Names
				            String[] officer_uker_tokens = officer_uker.split(" ");
				            officer_uker 			     = "" + officer_uker_tokens[0].toUpperCase();

				            for(int i = 1; i < officer_uker_tokens.length; i++){
				                char capLetter = Character.toUpperCase(officer_uker_tokens[i].charAt(0));
				                officer_uker +=  " " + capLetter + officer_uker_tokens[i].substring(1);
				            }
				            officer_uker = officer_uker.trim();
				            
	            			//A. CHECK whether CS OFFICER RECORD already exists.
	            			String existingCSQuery = "SELECT cs_fullname, cs_employee_code, cs_unit_kerja FROM tbl_account_cs WHERE cs_employee_code = '" + officer_employee_code + "' AND deleted = '0' ORDER BY cs_id ASC LIMIT 1";
				            stm = null; rs = null; directMsg = "";
				            try {
						     	if(con == null){
						     		db_object.openConnection();
						  			con = db_object.getConnection();
						        }
						     	stm = con.createStatement();	     		
						     	rs  = stm.executeQuery(existingCSQuery);
						     	while (rs.next()){
						     		directMsg   += "CS Officer bernama " + rs.getString("cs_fullname")  + " (No Officer: " + rs.getString("cs_employee_code") + ") telah terdaftar & aktif.";
						     		directMsg   += "\nAnda tidak perlu mendaftar sbg CS Officer lagi.\nTerima Kasih.";
						     		requirement = false;
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
	            			
	            			//B. IF NOT EXISTS, INSERT into CS OFFICER DB.
				            if(requirement){
			            		String csRegistrationQuery = "INSERT INTO tbl_account_cs(cs_employee_code, cs_fullname, cs_twitter, cs_unit_kerja) " 
			            				   + "VALUES ('" + officer_employee_code + "', '" + officer_fullname + "', '" + officer_handler + "', '" + officer_uker + "')";
			            		stm = null; 
			            		try {
			            		if(con == null){
				            		db_object.openConnection();
				            		con = db_object.getConnection();
			            		}
			            			stm = con.createStatement();	     		
			            			stm.executeUpdate(csRegistrationQuery);
			            			//System.out.println("Registered CS Officer: " + officer_fullname + " [" + officer_handler + " | " + officer_employee_code + "]" );
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
			            		
			            		directMsg   += "Selamat, Anda baru saja terdaftar dengan data sebagai berikut:\n" + "\nNama: "  + officer_fullname + "\nNPP: " + officer_employee_code + "\nUnit: " + officer_uker + "\n";
					     		directMsg   += "\nGunakan akun @" + officer_handler + " ini untuk mengirimkan laporan kepada @" + twitterAccount + "\n\nFormat Direct Message untuk laporan:\n\n #CSOpen #TaplusMuda #NoRekNasabah #NoRef #NPP \n";
					     		directMsg   += "\nTerima Kasih.";
					     		
					     		statLogger.eventsLog("9", directMessage.getSenderScreenName(), "CSRegister");
				            }
				            
		            		//C. REPLY to CS OFFICER twitter's Account notifying SUCCESS/ERROR.
		            		try {
				            	//System.out.println("#CSRegister #Nama_Lengkap #NumPegawai #Unit_Kerja with Customer recipient: " + recipientId + " & DM: " + directMsg);
				            	if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
			     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
			     					twitterLog.saveOutwardDM(recipientId, directMsg, "CS Register", "#CSRegister");
			     				}
							} catch (TwitterException e) {
								e.printStackTrace();
							}
	            		}
	            		
	            		
	            	}//END #CSRegister
			        
	            	hashtags.clear(); menus.clear(); aliasmenus.clear();
			        directMessagesPromoAndServices.clear();
			        
	            }//else if(DM for Open Account & CS Open Account | Taplus Muda)
	            
	            //****************************************** END LIST OF FEATURES IN TWITBANK BNI ******************************************//
	            
	            // (1) #menu | #help / #helppromo / #helpcs -> #HelpBNI 
	            // (2) #daftar #nama_lengkap #hape 
	            // (3) #promo keywords 
	            // (4) #cs keywords -> #AskBNI
	            // (5) #openaccount #taplusmuda for customers & #csopen #taplusmuda
	            
	            //IF NO COMMAND from (1) to (5) is found:
	            else
	            {
	            	/*
	            	recipientId = directMessage.getSenderScreenName();
        			directMsg 	= "Yth. Bp/Ibu, Mohon Maaf. Permintaan info ataupun promo via Twitter DM @BNI anda tidak valid. ";
        			directMsg  += "Silakan DM: #Promo / #HelpBNI / #Daftar / #AskBNI untuk info & registrasi CS BNI 46. ";
        			directMsg  += "Untuk promo pembukaan rekening tabungan BNI 46, silakan DM: #OpenAccount #TaplusMuda #Nama_Lengkap #No_HP.\n\n";
        			directMsg  += "Contoh:\n (1) #Promo #Travel \n (2) #HelpBNI \n (3) #Daftar #Andi_Waluyo #62213456789  \n (4) #AskBNI #KartuHilang \n\n atau \n\n (5) #OpenAccount #TaplusMuda #Denny_Dalvian #08118824686";
        			try {
		            	//System.out.println("#OpenAccount #TaplusMuda with recipient: " + recipientId + " & DM: " + directMsg);
		            	if(!recipientId.equalsIgnoreCase(twitterAccount) && !directMsg.equalsIgnoreCase("")){
		     					DirectMessage message = twitterDM.sendDirectMessage(recipientId, directMsg);
		     			}
					} catch (TwitterException e) {
						e.printStackTrace();
					}
        			
        			hashtags.clear(); menus.clear(); aliasmenus.clear();
			        directMessagesPromoAndServices.clear();
			        */
	            }
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
			int remaining = event.getRateLimitStatus().getRemaining();
			int resetInterval = event.getRateLimitStatus().getSecondsUntilReset() * 1000;
			if(remaining < 2)
			{
				try {
					Thread.sleep(resetInterval+5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void onRateLimitReached(RateLimitStatusEvent event) {
			System.out.println("onRateLimitReached: " + event.toString());
			System.out.println("Limit["+event.getRateLimitStatus().getLimit() + "], Remaining[" +event.getRateLimitStatus().getRemaining()+"]");
			try {
				Thread.sleep(900000+5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
