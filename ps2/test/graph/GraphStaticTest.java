/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for static methods of Graph.
 * 
 * To facilitate testing multiple implementations of Graph, instance methods are
 * tested in GraphInstanceTest.
 */
public class GraphStaticTest {
    
    // Testing strategy
    //   empty()
    //     no inputs, only output is empty graph
    //     observe with vertices()
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.empty().vertices());
    }
    
    // TODO test other vertex label types in Problem 3.2
    @Test
    public void testEmptyIntegerLabel() {
    	Graph<Integer> graph = Graph.<Integer>empty();
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), graph.vertices());
        graph.set(1, 2, 1);
        assertEquals("expected empty() graph to have no vertices",
        		Set.of(1, 2), graph.vertices());
    }
    
    @Test
    public void testEmptyDoubleLabel() {
    	Graph<Double> graph = Graph.empty();
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), graph.vertices());
        graph.set(1.0, 2.0, 1);
        assertEquals("expected empty() graph to have no vertices",
                Set.of(1.0, 2.0), graph.vertices());
    }
}
