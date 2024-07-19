import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class FactorsTester {

	@Test
	void testPerfect1()
	{	
		// TEST 1: should throw the exception because the parameter value is less than 1
		assertThrows(IllegalArgumentException.class, () -> FactorsUtility.perfect(0));
	}
	
	@Test
	void testPerfect2()
	{	
		// TEST 2: should succeed because 1 is a valid parameter value, but is not a perfect number
		assertFalse(FactorsUtility.perfect(1));
	}
	
	@Test
	void testPerfect3()
	{	
		// TEST 3: should succeed because 6 is a valid parameter value, and is a perfect number
		assertTrue(FactorsUtility.perfect(6));
	}
	
	@Test
	void testPerfect4()
	{	
		// TEST 4: should succeed because 7 is a valid parameter value, but is not a perfect number
		// I've coded this using assertEquals to show that there's often more than one way to write a test 
		boolean expected = false;
		assertEquals(expected, FactorsUtility.perfect(7));
	}
	
	@Test
	void getFactors1()
	{	
		// TEST 5: should throw the exception because the parameter value is less than 0
		assertThrows(IllegalArgumentException.class, () -> FactorsUtility.getFactors(-1));
	}
	
	@Test
	void getFactors2()
	{	
		// TEST 6: should succeed because 0 is a valid parameter value, returns an empty list.
		ArrayList<Integer> expected2 = new ArrayList<Integer>();
		assertEquals(expected2,FactorsUtility.getFactors(0));
	}
	
	@Test
	void getFactors3()
	{	
		// TEST 7: should succeed because 1 is a valid parameter value, returns an empty list.
		ArrayList<Integer> expected3 = new ArrayList<Integer>();
		assertEquals(expected3,FactorsUtility.getFactors(1));
	}
	
	@Test
	void getFactors4()
	{	
		// TEST 8: should succeed because 2 is a valid parameter value, returns a list : {1} 
		ArrayList<Integer> expected4 = new ArrayList<Integer>();
		expected4.add(1);
		assertEquals(expected4,FactorsUtility.getFactors(2));
	}
	
	@Test
	void getFactors5()
	{	
		// TEST 9: should succeed because 12 is a valid parameter value, returns a list : {1,2,3,4,6}
		ArrayList<Integer> expected5 = new ArrayList<Integer>();
		expected5.add(1);
		expected5.add(2);
		expected5.add(3);
		expected5.add(4);
		expected5.add(6);
		assertEquals(expected5,FactorsUtility.getFactors(12));
	}
	
	@Test
	void testfactor1()
	{	
		// TEST 10: should throw the exception because the parameter value of a is less than 0
		assertThrows(IllegalArgumentException.class, () -> FactorsUtility.factor(-1,5));
	}
	
	@Test
	void testfactor2()
	{	
		// TEST 11: should throw the exception because the parameter value of b is less than 1
		assertThrows(IllegalArgumentException.class, () -> FactorsUtility.factor(5,0));
	}
	
	@Test
	void testfactor3()
	{	
		// TEST 12: should succeed because both are valid parameter values, every number is a factor of 0.  
		assertTrue(FactorsUtility.factor(0,5));
	}
	
	@Test
	void testfactor4()
	{	
		// TEST 13: should succeed because both are valid parameter values, 1 is a factor of every number.
		assertTrue(FactorsUtility.factor(5,1));
	}
	
	@Test
	void testfactor5()
	{	
		// TEST 14: should succeed because both are valid parameter values, 2 is a factor of 6.
		assertTrue(FactorsUtility.factor(6,2));
	}
	
	@Test
	void testfactor6()
	{	
		// TEST 15: should succeed because both are valid parameter values, 5 is not a factor of 6.
		assertFalse(FactorsUtility.factor(6,5));
	}

}