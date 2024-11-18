/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;
import java.util.Map;
import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<String>();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */
    
    // Testing strategy for ConcreteVerticesGraph.toString()
    //   partition vertex number: 0, 1, >1
    //	 partition on edges:
    //		no vertex have edge
    //		one vertex have edge
    //		more than one vertex have edge
    //		all vertex have edge
    //	 partition on whether is self loop

    // cover vertex number 0
    @Test
    public void testToStringEmptyGraph() {
    	Graph<String> graph = emptyInstance();
    	assertEquals("expected graph", "\n", graph.toString());
    }
    
    /**
     * cover one vertex have edge
     * 	vertex number 1
     */
    @Test
    public void testToStringSelfLoop() {
    	Graph<String> graph = emptyInstance();
    	graph.set("1", "1", 1);
    	assertEquals("expected graph", "\"1\" ---> \"1\" 1\n", graph.toString());
    }
    
    /**
     * cover no vertex have edge
     * 	vertex number 1
     */
    @Test
    public void testToStringNoEdge() {
    	Graph<String> graph = emptyInstance();
    	graph.add("1");
    	assertEquals("expected graph", "1\n", graph.toString());
    }
    
    /**
     * cover one vertex have edge
     * 	vertex number > 1
     */
    @Test
    public void testToStringOneEdge() {
    	Graph<String> graph = emptyInstance();
    	graph.add("1");
    	graph.set("2", "3", 1);
    	assertEquals("expected graph", "1\n\"2\" ---> \"3\" 1\n", graph.toString());
    }
    
    /**
     * cover more than one vertex have edge
     * 	vertex number > 1
     */
    @Test
    public void testToStringMultipleEdge() {
    	Graph<String> graph = emptyInstance();
    	graph.add("1");
    	graph.set("2", "3", 1);
    	graph.set("3", "4", 1);
    	assertEquals("expected graph", "1\n\"2\" ---> \"3\" 1\n\"3\" ---> \"4\" 1\n", graph.toString());
    }
    
    /**
     * cover all vertex have edge
     * 	vertex number > 1
     */
    @Test
    public void testToStringNoIsolate() {
    	Graph<String> graph = emptyInstance();
    	graph.add("1");
    	graph.set("2", "3", 1);
    	graph.set("3", "4", 1);
    	graph.set("4", "1", 1);
    	assertEquals("expected graph", "\"2\" ---> \"3\" 1\n\"3\" ---> \"4\" 1\n\"4\" ---> \"1\" 1\n", graph.toString());
    }
    
    /*
     * Testing Vertex...
     */
    
    // Testing strategy for Vertex
    // 
    // to():
    //	partition on target and weight:
    //		target in targets and weight 0
    // 		target in targets and weight > 0
    //		target not in targets and weight 0
    //		target not in targets and weight > 0
    //	partition on target: same or not same
    // connected():
    //	partition on target: in or not in
    // to(), connected(), targets(), sources():
    //	partition on targets: 0, 1, >1
    //	partition on sources: 0, 1, >1
    
    /**
     * cover target not in targets and weight > 0
     * 	target same
     * 	targets 0
     * 	sources 0
     */
    @Test
    public void testToSelfLoop() {
    	Vertex<String> vertex = new Vertex<>("1");
    	assertEquals("expected not exist", 0, vertex.to(vertex, 1));
    	assertTrue("expected connected", vertex.connected(vertex));
    	assertEquals("expected targets", Map.of("1", 1), vertex.targets());
    	assertEquals("expected source", Map.of("1", 1), vertex.sources());
    }
    
    /**
     * cover target in targets and weight > 0
     * 	target not same
     * 	targets 1
     * 	sources 1
     */
    @Test
    public void testToUpdateWeight() {
    	Vertex<String> vertex1 = new Vertex<>("1");
    	Vertex<String> vertex2 = new Vertex<>("2");
    	Vertex<String> vertex3 = new Vertex<>("3");
    	assertEquals("expected edge not exist", 0, vertex1.to(vertex3, 1));
    	assertEquals("expected edge not exist", 0, vertex2.to(vertex1, 1));
    	
    	assertEquals("expected targets", Map.of("1", 1), vertex3.sources());
    	assertEquals("expected targets", Map.of("3", 1), vertex1.targets());
    	assertEquals("expected targets", Map.of("2", 1), vertex1.sources());
    	
    	assertEquals("expected edge exist", 1, vertex1.to(vertex3, 2));
    	assertTrue("expected connected", vertex1.connected(vertex3));
    	assertEquals("expected targets", Map.of("3", 2), vertex1.targets());
    	assertEquals("expected targets", Map.of("2", 1), vertex1.sources());
    	assertEquals("expected targets", Map.of("1", 2), vertex3.sources());
    }
    
    /**
     * cover target not in targets and weight 0
     * 	target not same
     * 	targets >1
     * 	sources >1
     */
    @Test
    public void testToNoEffect() {
    	Vertex<String> vertex1 = new Vertex<>("1");
    	Vertex<String> vertex2 = new Vertex<>("2");
    	Vertex<String> vertex3 = new Vertex<>("3");
    	Vertex<String> vertex4 = new Vertex<>("4");
    	Vertex<String> vertex5 = new Vertex<>("5");
    	Vertex<String> vertex6 = new Vertex<>("6");

    	assertEquals("expected edge not exist", 0, vertex1.to(vertex3, 1));
    	assertEquals("expected edge not exist", 0, vertex2.to(vertex3, 2));
    	assertEquals("expected edge not exist", 0, vertex3.to(vertex4, 1));
    	assertEquals("expected edge not exist", 0, vertex3.to(vertex5, 1));
    	
    	assertEquals("expected targets", Map.of("1", 1, "2", 2), vertex3.sources());
    	assertEquals("expected targets", Map.of("4", 1, "5", 1), vertex3.targets());
    	
    	assertEquals("expected edge not exist", 0, vertex3.to(vertex6, 0));
    	assertEquals("expected targets", Map.of("1", 1, "2", 2), vertex3.sources());
    	assertEquals("expected targets", Map.of("4", 1, "5", 1), vertex3.targets());
    	assertEquals("expected targets number", 0, vertex6.sources().size());
    }
    
    /**
     * cover target in targets and weight 0
     * 	target not same
     * 	targets 1
     * 	sources 0
     */
    @Test
    public void testToRemoveEdge() {
    	Vertex<String> vertex1 = new Vertex<>("1");
    	Vertex<String> vertex2 = new Vertex<>("2");
    	assertEquals("expected edge not exist", 0, vertex1.to(vertex2, 1));
    	
    	assertEquals("expected targets number", 0, vertex1.sources().size());
    	assertEquals("expected targets", Map.of("2", 1), vertex1.targets());
    	assertEquals("expected source", Map.of("1", 1), vertex2.sources());
    	
    	assertEquals("expected edge exist", 1, vertex1.to(vertex2, 0));
    	assertEquals("expected targets number", 0, vertex1.sources().size());
    	assertEquals("expected targets number", 0, vertex1.targets().size());
    	assertEquals("expected targets number", 0, vertex2.sources().size());
    }
    
    /**
     * cover target not in
     * 	targets 0
     * 	source 0
     */
    @Test
    public void testConnect() {
    	Vertex<String> vertex1 = new Vertex<>("1");
    	Vertex<String> vertex2 = new Vertex<>("2");
    	assertFalse("expected not connected", vertex1.connected(vertex2));
    	assertFalse("expected not connected", vertex2.connected(vertex1));
    }
    
    /**
     * cover target not in
     * 	targets 1
     * 	source 1
     */
    @Test
    public void testConnectNotInOneTarget() {
    	Vertex<String> vertex1 = new Vertex<>("1");
    	Vertex<String> vertex2 = new Vertex<>("2");
    	Vertex<String> vertex3 = new Vertex<>("3");
    	Vertex<String> vertex4 = new Vertex<>("4");
    	assertEquals("expected edge not exist", 0, vertex1.to(vertex2, 1));
    	assertEquals("expected edge not exist", 0, vertex2.to(vertex3, 1));
    	assertFalse("expected not connected", vertex2.connected(vertex4));
    }
    
    /**
     * cover target not in
     * 	targets >1
     * 	source >1
     */
    @Test
    public void testConnectNotInMultiTargets() {
    	Vertex<String> vertex1 = new Vertex<>("1");
    	Vertex<String> vertex2 = new Vertex<>("2");
    	Vertex<String> vertex3 = new Vertex<>("3");
    	Vertex<String> vertex4 = new Vertex<>("4");
    	Vertex<String> vertex5 = new Vertex<>("5");
    	Vertex<String> vertex6 = new Vertex<>("6");
    	assertEquals("expected edge not exist", 0, vertex1.to(vertex3, 1));
    	assertEquals("expected edge not exist", 0, vertex2.to(vertex3, 1));
    	assertEquals("expected edge not exist", 0, vertex3.to(vertex4, 1));
    	assertEquals("expected edge not exist", 0, vertex3.to(vertex5, 1));
    	assertFalse("expected not connected", vertex3.connected(vertex6));
    }
    
    /**
     * cover target in
     *  targets >1
     * 	source 0
     */
    @Test
    public void testConnectIn() {
    	Vertex<String> vertex1 = new Vertex<>("1");
    	Vertex<String> vertex2 = new Vertex<>("2");
    	Vertex<String> vertex3 = new Vertex<>("3");
    	assertEquals("expected edge not exist", 0, vertex1.to(vertex2, 1));
    	assertEquals("expected edge not exist", 0, vertex1.to(vertex3, 1));
    	assertTrue("expected connected", vertex1.connected(vertex3));
    }
    
    /**
     * cover targets 0
     * 	sources 0
     */
    @Test
    public void testTargets() {
    	Vertex<String> vertex1 = new Vertex<>("1");
    	assertEquals("expected targets number", 0, vertex1.targets().size());
    }
    
    /**
     * cover targets 1
     * 	sources 1
     */
    @Test
    public void testTargetsOne() {
    	Vertex<String> vertex1 = new Vertex<>("1");
    	Vertex<String> vertex2 = new Vertex<>("2");
    	assertEquals("expected edge not exist", 0, vertex1.to(vertex2, 1));
    	assertEquals("expected edge not exist", 0, vertex2.to(vertex1, 1));
    	assertEquals("expected targets", Map.of("2", 1), vertex1.targets());
    	assertEquals("expected targets", Map.of("1", 1), vertex2.targets());
    }
    
    /**
     * cover targets >1
     * 	sources >1
     */
    @Test
    public void testTargetsMultiple() {
    	Vertex<String> vertex1 = new Vertex<>("1");
    	Vertex<String> vertex2 = new Vertex<>("2");
    	Vertex<String> vertex3 = new Vertex<>("3");
    	Vertex<String> vertex4 = new Vertex<>("4");
    	Vertex<String> vertex5 = new Vertex<>("5");
    	assertEquals("expected edge not exist", 0, vertex1.to(vertex3, 1));
    	assertEquals("expected edge not exist", 0, vertex2.to(vertex3, 1));
    	assertEquals("expected edge not exist", 0, vertex3.to(vertex4, 1));
    	assertEquals("expected edge not exist", 0, vertex3.to(vertex5, 1));
    	assertEquals("expected targets", Map.of("3", 1), vertex1.targets());
    	assertEquals("expected targets", Map.of("3", 1), vertex2.targets());
    	assertEquals("expected targets", Map.of("4", 1, "5", 1), vertex3.targets());
    }
    
    /**
     * cover targets 0
     * 	sources 0
     */
    @Test
    public void testSources() {
    	Vertex<String> vertex1 = new Vertex<>("1");
    	assertEquals("expected targets number", 0, vertex1.sources().size());
    }
    
    /**
     * cover targets 1
     * 	sources 1
     */
    @Test
    public void testSourcesOne() {
    	Vertex<String> vertex1 = new Vertex<>("1");
    	Vertex<String> vertex2 = new Vertex<>("2");
    	assertEquals("expected edge not exist", 0, vertex1.to(vertex2, 11));
    	assertEquals("expected edge not exist", 0, vertex2.to(vertex1, 22));
    	assertEquals("expected targets", Map.of("2", 22), vertex1.sources());
    	assertEquals("expected targets", Map.of("1", 11), vertex2.sources());
    }
    
    /**
     * cover targets >1
     * 	sources >1
     */
    @Test
    public void testSourcesMultiple() {
    	Vertex<String> vertex1 = new Vertex<>("1");
    	Vertex<String> vertex2 = new Vertex<>("2");
    	Vertex<String> vertex3 = new Vertex<>("3");
    	Vertex<String> vertex4 = new Vertex<>("4");
    	Vertex<String> vertex5 = new Vertex<>("5");
    	assertEquals("expected edge not exist", 0, vertex1.to(vertex3, 11));
    	assertEquals("expected edge not exist", 0, vertex2.to(vertex3, 22));
    	assertEquals("expected edge not exist", 0, vertex3.to(vertex4, 44));
    	assertEquals("expected edge not exist", 0, vertex3.to(vertex5, 55));
    	assertEquals("expected targets", Map.of("3", 44), vertex4.sources());
    	assertEquals("expected targets", Map.of("3", 55), vertex5.sources());
    	assertEquals("expected targets", Map.of("1", 11, "2", 22), vertex3.sources());
    }
    
    /**
     * cover targets 0
     * 	sources 0
     */
    @Test
    public void testToString() {
    	Vertex<String> vertex = new Vertex<>("1");
    	assertEquals("expected blank line", "1", vertex.toString());
    }
}
