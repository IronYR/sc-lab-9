/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<String>();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    //   partition on graph:
    //		empty graph
    //	 	one vertex has self loop
    //		one vertex has no self loop
    //    	more than one vertex, all one vertex has edge
    //		more than one vertex, one vertex has no edge
    //		more than one vertex, more than one vertex has no edge
    
    @Test
    public void testToStringEmptyGraph() {
    	Graph<String> graph = emptyInstance();
    	assertEquals("expected empty graph", "\n", graph.toString());
    }
    
    @Test
    public void testToStringSelfLoop() {
    	Graph<String> graph = emptyInstance();
    	graph.set("1", "1", 1);
    	assertEquals("expected empty graph", "\"1\" ---> \"1\" 1\n", graph.toString());
    }
    
    @Test
    public void testToStringAllConnected() {
    	Graph<String> graph = emptyInstance();
    	graph.set("1", "1", 1);
    	graph.set("2", "3", 1);
    	String expected = "\"1\" ---> \"1\" 1\n" + 
    			"\"2\" ---> \"3\" 1\n";
    	assertEquals("expected empty graph", expected, graph.toString());
    }
    
    @Test
    public void testToStringIsolate() {
    	Graph<String> graph = emptyInstance();
    	graph.set("1", "1", 1);
    	graph.set("2", "3", 1);
    	graph.add("4");
    	String expected = "\"1\" ---> \"1\" 1\n" + 
    			"\"2\" ---> \"3\" 1\n" + 
    			"\"4\"\n";
    	assertEquals("expected empty graph", expected, graph.toString());
    }
    
    @Test
    public void testToStringMoreIsolate() {
    	Graph<String> graph = emptyInstance();
    	graph.set("1", "1", 1);
    	graph.set("2", "3", 1);
    	graph.add("4");
    	graph.add("5");
    	graph.add("6");
    	String expected = "\"1\" ---> \"1\" 1\n" + 
    			"\"2\" ---> \"3\" 1\n" + 
    			"\"4\"\n" +
    			"\"5\"\n" + 
    			"\"6\"\n";
    	assertEquals("expected empty graph", expected, graph.toString());
    }
    
    /*
     * Testing Edge...
     */
    
    // Testing strategy for Edge
    // Edge():
    //   partition on source and target:
    //		source and target different
    //		source and target equal
        
    /**
     * cover source and target different
     */
    @Test
    public void testEdge() {
    	Edge<String> edge = new Edge<>("1", "2", 10);
    	assertEquals("expect source", "1", edge.getSource());
    	assertEquals("expect target", "2", edge.getTarget());
    	assertEquals("expect weight", 10, edge.getWeight());
    	assertTrue("expect connected", edge.connected("1", "2"));
    }
    
    /**
     * cover source and target equal
     */
    @Test
    public void testEdgeSelfLoop() {
    	Edge<String> edge = new Edge<>("1", "1", 10);
    	assertEquals("expect source", "1", edge.getSource());
    	assertEquals("expect target", "1", edge.getTarget());
    	assertEquals("expect weight", 10, edge.getWeight());
    	assertTrue("expect connected", edge.connected("1", "1"));
    }
}
