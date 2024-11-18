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

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {
    
    private final List<Vertex<L>> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   Represent a graph with vertices
    // Representation invariant:
    //   vertices has no same vertex
    //	 vertex in Vertex sources or targets must in vertices
    // Safety from rep exposure:
    //   vertices(), sources(), targets() make a defensive copy
    //	 vertices is private and final
    
    public ConcreteVerticesGraph() {}
    
    // TODO checkRep
    
    @Override public boolean add(L vertex) {
        if (find(vertex) != null) {
			return false;
        }
        vertices.add(new Vertex<L>(vertex));
        return true;
    }
    
    @Override public int set(L source, L target, int weight) {
    	return findOrCreate(source).to(findOrCreate(target), weight);
    }
    
    @Override public boolean remove(L vertex) {
        int i = index(vertex);
        if (i == -1) {
        	return false;
        }
        
        Vertex<L> v = vertices.get(i);
        for (L source : v.sources().keySet()) {
        	Vertex<L> sv = find(source);
        	assert sv != null;
        	sv.to(v, 0);
        }
        for (L target : v.targets().keySet()) {
        	Vertex<L> tv = find(target);
        	assert tv != null;
        	v.to(tv, 0);
        }
        vertices.remove(i);
        return true;
    }
    
    @Override public Set<L> vertices() {
        return new HashSet<>(vertices.stream().map(v -> v.identity()).toList());
    }
    
    @Override public Map<L, Integer> sources(L target) {
        Vertex<L> vertex = find(target);
        return vertex == null ? new HashMap<>() : vertex.sources();
    }
    
    @Override public Map<L, Integer> targets(L source) {
        Vertex<L> vertex = find(source);
        return vertex == null ? new HashMap<>() : vertex.targets();
    }
    
    private int index(L vertex) {
        for (int i = 0; i < vertices.size(); i++)
			if (vertices.get(i).identity().equals(vertex))
				return i;
        return -1;
    }
    
    private Vertex<L> find(L vertex) {
        for (int i = 0; i < vertices.size(); i++)
			if (vertices.get(i).identity().equals(vertex))
				return vertices.get(i);
        return null;
    }
    
    private Vertex<L> findOrCreate(L vertex) {
    	Vertex<L> nv = find(vertex);
    	if (nv != null) {
    		return nv;
    	}
        nv = new Vertex<L>(vertex);
    	vertices.add(nv);
        return nv;
    }
    
    /**
     * Returns a L represent the Graph.
     * Graph will be represent in the follow format, 
     * vertex will appear in insert order:
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
    @Override public String toString() {
    	String result = "";
    	for (Vertex<L> vertex : vertices) {
    		if (vertex.sources().size() == 0 && vertex.targets().size() == 0) {
    			result += vertex + "\n";
    		} else {
    			for (Map.Entry<L, Integer> entry : vertex.targets().entrySet()) {
    				result += "\"%s\" ---> \"%s\" %d\n".formatted(
    						vertex.identity(), entry.getKey(), entry.getValue());
    			}
    		}
    	}
    	return result.isEmpty() ? "\n" : result;
    }
}

/**
 * Represent a Vertex with edges from or to that vertex.
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex<L> {
    
	private final L id;
	private final Map<L, Integer> targets = new HashMap<>();
	private final Map<L, Integer> sources = new HashMap<>();
    
    // Abstraction function:
    //   A vertex with source vertex point to it and from that vertex to the targets
    // Representation invariant:
    //   targets and sources weight always > 0
    // Safety from rep exposure:
    //   id, targets, sources are private, final and immutable
	//	 targets, sources map field are immutable
	//	 targets(), sources() make a defensive copy
    
	public Vertex(L id) {
		this.id = id;
	}
    
    // TODO checkRep
    
	public L identity() {
		return id;
	}
	
	/**
	 * Add a edge from that vertex to target, remove edge if weight is zero else
	 * update weight if edge exist.
	 * 
	 * @param target vertex
	 * @param edge weight
	 * @return 0 if edge not exist else return previous weight
	 */
	public int to(Vertex<L> target, int weight) {
		Integer result = null;
		if (weight == 0) {
			result = this.targets.remove(target.id);
			target.sources.remove(id);
		} else {
			result = this.targets.put(target.id, weight);
			target.sources.put(id, weight);
		}
		return result == null ? 0 : result;
	}
	
	/**
	 * Test whether source vertex connected to target.
	 * @param target vertex
	 * @return true if edge exist else return false
	 */
	public boolean connected(Vertex<L> target) {
		return targets.containsKey(target.id);
	}
	
	/**
	 * Get the target vertices with directed edges from a source vertex and the
     * weights of those edges.
     * 
     * @return a map where the key set is the set of labels of vertices such
     *         that this graph includes an edge from source to that vertex, and
     *         the value for each key is the (nonzero) weight of the edge from
     *         source to the key
	 */
	public Map<L, Integer> targets() {
		return new HashMap<>(targets);
	}
	
    /**
     * Get the source vertices with directed edges to a target vertex and the
     * weights of those edges.
     * 
     * @return a map where the key set is the set of labels of vertices such
     *         that this graph includes an edge from that vertex to target, and
     *         the value for each key is the (nonzero) weight of the edge from
     *         the key to target
     */
    public Map<L, Integer> sources() {
    	return new HashMap<>(sources);
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
    	return id.toString();
    }
}
