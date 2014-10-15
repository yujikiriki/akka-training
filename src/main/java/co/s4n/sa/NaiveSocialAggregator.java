package co.s4n.sa;

import java.util.ArrayList;
import java.util.List;

public class NaiveSocialAggregator {
	private SocialWeb20Adapter adapter = new SocialWeb20Adapter();
	
	public List<String> amigos() {
		List<String> amigos = new ArrayList< String >();
		try {
			amigos.addAll( adapter.gmailContacts() );
			amigos.addAll( adapter.instagramFollowers() );
			amigos.addAll( adapter.twitterFollowers() );
			amigos.addAll( adapter.facebookFriends() );
		} catch ( Exception e ) {
			System.out.println( "Error: " + e.getMessage() );
			e.printStackTrace();
		}
		return amigos;
	}

}
