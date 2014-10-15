package co.s4n.sa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SocialWeb20Adapter {

	private static final Random rnd = new Random();

	public List<String> twitterFollowers() throws InterruptedException {
		Thread.sleep(rnd.nextInt(3000));
		List<String> amigos = new ArrayList<String>();
		amigos.add( "TwitterFollower" );
		return amigos;
	}

	public List<String> facebookFriends() throws InterruptedException {
		Thread.sleep(rnd.nextInt(3000));
		List<String> amigos = new ArrayList<String>();
		amigos.add( "FacebookFriend" );		
		return amigos;
	}

	public List<String> gmailContacts() throws InterruptedException {
		Thread.sleep(rnd.nextInt(3000));
		List<String> amigos = new ArrayList<String>();
		amigos.add( "GmailContact" );		
		return amigos;
	}

	public List<String> instagramFollowers() throws InterruptedException {
		Thread.sleep(rnd.nextInt(3000));
		List<String> amigos = new ArrayList<String>();
		amigos.add( "InstagramFollower" );		
		return amigos;
	}
}
