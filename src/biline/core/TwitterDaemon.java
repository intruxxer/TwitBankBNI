package biline.core;

import biline.config.*;
import biline.db.*;
import biline.twitter.*;
//import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.DirectMessage;
import twitter4j.FilterQuery;
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

import java.util.Date;


public class TwitterDaemon {
	
	private static String latestStatus;
	private static Status status;
	private static String savedDirectory;
	private static TwitterStream twitterStream;
	private static String recipientId;
	private static Twitter twitter;
	private static AsyncTwitter asyncTwitter;
	// *Implement a stream listener to track from the Stream prior to being assigned to stream. 
    // *A stream listener has multiple methods to respond to multiple events in streams accordingly.
	private static final UserStreamListener userStreamlistener = new UserStreamListener() {
	      
		   @Override
	        public void onStatus(Status status) {
	            System.out.println("onStatus @" + status.getUser().getScreenName() + " - " + status.getText());
	            //twitter = TwitterFactory.getSingleton();
	            //try {
	    			//String msg = "You got DM from @dev_amartha. Thanks for tweeting our hashtags!";
	    			//DirectMessage message = twitter.sendDirectMessage(status.getUser().getScreenName(), msg);
	    			//System.out.println("Sent: " + message.getText() + " to @" + message.getRecipientScreenName());
	    		//} catch (TwitterException e) {
	    		//	e.printStackTrace();
	    		//}
	            //AsyncTwitterFactory factory = new AsyncTwitterFactory();
	            //asyncTwitter = factory.getInstance();
	            asyncTwitter = AsyncTwitterFactory.getSingleton();
	            
	            String msg = "You got async DM from @dev_amartha. Thanks for tweeting our hashtags!";
    			asyncTwitter.sendDirectMessage(status.getUser().getScreenName(), msg);
    		    System.out.println("Sent: " + msg + " to @" + status.getUser().getScreenName());
	    		
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
	            System.out.println("onFollow source:@"
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
	            System.out.println("onException:" + ex.getMessage());
	        }

	};
	
	public TwitterDaemon() {	
		//Constructor stub
		setLatestStatus("default");
		setSaveDirectory();
	}

	public static void main(String[] args) throws TwitterException {
		/* Using RESTful API to Update Status */
		// [INIT] 
		// Create (min.) a requesting RESTful API instance
		// If we want to have multiple RESTful API instances
		   //Twitter twitter_one = new TwitterFactory().getInstance(new AccessToken("XXX","XXX"));
		   //Twitter twitter_two = new TwitterFactory().getInstance(new AccessToken("YYY","YYY"));
		// Else, we can use singleton to have only one instance, shared across application life-shelf
		   //Twitter twitter = TwitterFactory.getSingleton();
        
		// [1] Posting New Status to User's Twitter Account
		/*
		try {
			setLatestStatus("default");
			TwitterDaemon.status = twitter.updateStatus(latestStatus);
			System.out.println("Successfully updated the status to [" + TwitterDaemon.status.getText() + "].");
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		*/
		
		// [2] Sending DM to User's Twitter Account
		/*
		try {
			String msg = "You got DM from @dev_amartha. Thanks for tweeting our hashtags!";
			DirectMessage message = twitter.sendDirectMessage(recipientId, msg);
		    System.out.println("Sent: " + message.getText() + " to @" + message.getRecipientScreenName());
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		*/
		   
		/* Using Stream API */
		// [INIT]
		// Connects to the Streaming API - with Amartha OAUTH2 Key n Token
	       TwitterStreamBuilderUtil twitterStreamAmartha = new TwitterStreamBuilderUtil("amartha");
		   twitterStream = twitterStreamAmartha.getStream();
		   //twitterStream = new TwitterStreamFactory().getInstance();
		
		// [1] User Stream API - Stream Tweets of a Twitter User 
		// Sets keywords to track from the Stream
		   FilterQuery fq = new FilterQuery();
		   String keywords[] = { "#microfinance", "#life" };
		   fq.track(keywords);
		// Sets a stream listener to track from the Stream. 
	       twitterStream.addListener(userStreamlistener);
	       //twitterStream.filter(fq);
	       twitterStream.user("@dev_amartha #microfinance", "@dev_amartha #life");
	    // user() & filter() internally create threads respectively, manipulating TwitterStream; e.g. user() simply gets tweets that only mention @user
	    // user() & filter() then call the appropriate listener methods according to each stream events (such as status, favorite, RT, DM, etc) continuously.
	       
	       //System.out.println("Saving Stream API Result into Destination File: ");
	}
	
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
