/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    //   Represent a Graph with vertices and edges
    // Representation invariant:
    //   edges vertex all contain in vertices
    //	 no more than one edge in edges that source and target is the same
    // Safety from rep exposure:
    //   vertices() make defensive copies
    //	 sources(), targets() return Map only contain immutable object
    
    public ConcreteEdgesGraph() {}
    
    public void checkRep() {
    	for (Edge<L> edge : edges) {
			assert vertices.contains(edge.getSource());
			assert vertices.contains(edge.getTarget());
		}
    }
    
    @Override public boolean add(L vertex) {
        boolean result = vertices.add(vertex);
        checkRep();
        return result;
    }
    
    @Override public int set(L source, L target, int weight) {
    	boolean finded = false;
    	int result = 0;    	
    	vertices.addAll(List.of(source, target));
    	
    	int i = 0;
    	while (i < edges.size()) {
    		Edge<L> edge = edges.get(i);
			if (edge.connected(source, target)) {
				result = edge.getWeight();
				finded = true;
				
				if (weight == 0) {
					edges.remove(i);
				} else {
					edges.set(i, new Edge<>(source, target, weight));
				}
				
				break;
			}
			
			i++;
    	}
    	
    	if (weight > 0 && !finded) {
    		edges.add(new Edge<L>(source, target, weight));
    	}
    	checkRep();
    	return result;
    }
    
    @Override public boolean remove(L vertex) {
    	// vertex not exist, no edge will be remove
    	if(!vertices.remove(vertex)) {
    		return false;
    	}
    	
    	int i = 0;
    	while (i < edges.size()) {
    		Edge<L> edge = edges.get(i);
			if (edge.getSource().equals(vertex) || 
					edge.getTarget().equals(vertex)) {
				edges.remove(i);
			} else {
				i++;
			}
    	}
    	return true;
    }
    
    @Override public Set<L> vertices() {
        return new HashSet<>(vertices);
    }
    
    @Override public Map<L, Integer> sources(L target) {
        if (!vertices.contains(target)) {
        	return new HashMap<>();
        }
        
        Map<L, Integer> result = new HashMap<>();
        for (Edge<L> edge : edges) {
			if (edge.getTarget().equals(target)) {
				result.put(edge.getSource(), edge.getWeight());
			}
		}
        return result;
    }
    
    @Override public Map<L, Integer> targets(L source) {
        if (!vertices.contains(source)) {
        	return new HashMap<>();
        }
        
        Map<L, Integer> result = new HashMap<>();
        for (Edge<L> edge : edges) {
			if (edge.getSource().equals(source)) {
				result.put(edge.getTarget(), edge.getWeight());
			}
		}
        return result;
    }
    
    /**
     * @return a String represent the Graph.
     * Graph will be represent in the follow format, 
     * edge will present first, then vertex:
     * 
     * Vertex to vertex with weight aside represent a edge:
     * 	"1" ---> "2" 1
     * 
     * 
     * Vertex with no arrow represent a vertex with no edge:
     * 	"1"
     * 
     * Empty Graph will be represent by a blank line.
     */
    @Override
    public String toString() {
    	String result = "";
    	Set<L> vertices = vertices();
    	
        for (Edge<L> edge : edges) {
        	result += edge + "\n";
        	vertices.removeAll(List.of(edge.getSource(), 
        			edge.getTarget()));
		}
        
        for (L vertex : new TreeSet<>(vertices)) {
        	result += "\"%s\"".formatted(vertex) + "\n";
		}
        return result.isEmpty() ? "\n" : result;
    }
    
}

/**
 * Represent a edge in ConcreteEdgesGraph from source 
 * to target with weight.
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge<L> {
    
	private final L source;
	private final L target;
	private final int weight;
    
    // Abstraction function:
    //   represent a edge from source to target with weight
    // Representation invariant:
    //   weight > 0
    // Safety from rep exposure:
    //   all fields are private, final and immutable
	//	 setWeight return a new Edge
    
	/**
	 * Create a Edge from source to target with weight
	 * @param source vertex edge goes from
	 * @param target vertex edge goes to
	 * @param weight Edge weight
	 */
	public Edge(L source, L target, int weight) {
		this.source = source;
		this.target = target;
		this.weight = weight;
	}
    
	public void checkRep() {
        assert weight > 0;
	}
    
	/**
	 * @return Edge source vertex
	 */
	public L getSource() {
        return source;
	}
	
	/**
	 * @return Edge target vertex
	 */
	public L getTarget() {
        return target;
	}
	
	/**
	 * Check whether source and target vertex are connected
	 * @param source vertex edge goes from
	 * @param target vertex edge goes to
	 * @return true if source and target are connected otherwise return false
	 */
	public boolean connected(L source, L target) {
        return this.source.equals(source) && this.target.equals(target);
	}
	
	/**
	 * @return Edge weight
	 */
	public int getWeight() {
        return weight;
	}
    
	@Override public String toString() {
        return "\"%s\" ---> \"%s\" %d".formatted(
        		source, target, weight);
    }
}
