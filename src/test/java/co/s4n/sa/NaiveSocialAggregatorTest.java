package co.s4n.sa;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;


public class NaiveSocialAggregatorTest {

	@Test
	public void test() {
		NaiveSocialAggregator sa = new NaiveSocialAggregator();
		Long ini = System.currentTimeMillis();
	    List<String> amigos = sa.amigos();
	    Long end = System.currentTimeMillis();
	    System.out.println( "Execution time: " + ( ( end - ini ) / 1000 ) );
		assertTrue( amigos.size() > 0 );
	}

}
