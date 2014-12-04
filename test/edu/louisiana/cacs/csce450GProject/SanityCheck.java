package edu.louisiana.cacs.csce450Project;

/*
 * This file is intended to provide instruction on how to create unit tests.
 *
 */


// Provides the @Test annotation needed
// to mark methods to be run as unit tests.
import org.junit.Test;

// Provides the various assert functions,
// including assertTrue, assertFalse, assertEquals,
// assertNull, assertNotNull, assertThat, etc.
import static org.junit.Assert.*; 

// Provides the various Hamcrest matchers that
// may be used with the more complicated
// (but more expressive) assertThat syntax.
import static org.hamcrest.CoreMatchers.*;

public class SanityCheck
{
    @Test
    public void testAddTwo(){
        RandomCode rc = new RandomCode();
        int a = 2;
        int b = 3;
        assertEquals(a+b,rc.addTwo(a,b));
    }

    @Test
    public void testSubTwo(){
        RandomCode rc = new RandomCode();
        int a = 2;
        int b = 3;
        assertEquals(a-b,rc.subTwo(a,b));
    }

    @Test
    public void testMultTwo(){
        RandomCode rc = new RandomCode();
        int a = 2;
        int b = 3;
        assertEquals(a*b,rc.multTwo(a,b));
    }

    @Test
    public void testDivide(){
        RandomCode rc = new RandomCode();
        float a = 2.0f;
        float b = 3.0f;
        assertEquals(a/b,rc.divide(a,b),0.0000000001);
    }

    @Test
    public void testDivideByZero(){
        RandomCode rc = new RandomCode();
        float a = 2.0f;
        float b = 0.0f;
        try{
            rc.divide(a,b);
        }catch(IllegalArgumentException ex){
        }
    }

    @Test
    public void testThatAssertTrueAndAssertFalseWork(){
        // When verifying that a boolean value is a certain
        // value, one can use assertTrue or assertFalse
        assertTrue(true);
        assertFalse(false);
        assertTrue(!false);
        assertFalse(!true);
    }
    
    @Test
    public void testThatHamcrestVersionsOfAssertTrueFalseWork(){
        // It is possible to express the function
        // "testThatAssertTrueAndAssertFalseWork", above
        // using the Hamcrest matchers as in:
        assertThat(true,is(true));
        assertThat(false,is(not(true)));
        assertThat(!false,is(true));
        assertThat(!true,is(false));
    }
    
    @Test
    public void testThatAssertEqualsWorks(){
        // For verifying that two objects have the same
        // value (using the ".equals()" method), one can
        // use the assertEquals function:
        
        String helloworld = new String("Hello world!");
        assertEquals("Hello world!",helloworld); // Note: order is "expected", then "actual"
    }
    
    @Test
    public void testThatHamcrestVersionOfAssertEqualsWorks(){
        // It is possible to express the function
        // "testThatAssertEqualsWorks", above
        // using the Hamcrest matchers as in:
        String helloworld = new String("Hello world!");
        assertThat(helloworld,is(equalTo("Hello world!")));
    }
    
    @Test
    public void testThatAssertSameAndAssertNotSameWork(){
        // When comparing the identity of two variables
        // (i.e. whether they point to the same memory location)
        // one can use assertSame and assertNotSame
        
        String poolstr = "Hello world!"; // This string lives in the string pool
        String heapstr = new String(poolstr); // This string lives in the heap and is distinct from poolstr
        String poolstrptr = poolstr; // This now points to the same location as poolstr
        
        assertSame(poolstr,poolstrptr);
        assertNotSame(poolstr,heapstr);
    }
    
    @Test
    public void testThatHamcrestVersionOfAssertSameAndAssertNotSameWork(){
        // It is possible to express the function
        // "testThatAssertSameAndAssertNotSameWork", above
        // using the Hamcrest matchers as in:
        String poolstr = "Hello world!"; // This string lives in the string pool
        String heapstr = new String(poolstr); // This string lives in the heap and is distinct from poolstr
        String poolstrptr = poolstr; // This now points to the same location as poolstr
        
        assertThat(poolstrptr,is(sameInstance(poolstr)));
        assertThat(heapstr,is(not(sameInstance(poolstr))));
    }
    
    @Test
    public void testThatAssertNullAndAssertNotNullWork(){
        // We can verify that values are or are not null as in:
        assertNull(null);
        assertNotNull("This string isn't null.");
    }
    
    @Test
    public void testThatHamcrestVersionOfAssertNullAndNotNullWork(){
        // Or, using the Hamcrest variety:
        assertThat(null,is(nullValue()));
        assertThat("This string isn't null.",is(not(nullValue())));
    }
}
