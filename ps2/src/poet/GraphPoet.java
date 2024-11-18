/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.xml.transform.Source;

import org.junit.Test;

import graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are multiple two-edge-long paths that have same weight, 
 * choose the one has minimum in alphabetical order.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // Abstraction function:
    //   graph represent a word affinity graph
    // Representation invariant:
    //   corpus file derive the poet's affinity graph
    // Safety from rep exposure:
    //   graph is private and final
    //	 getGraph() make a defensive copy
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(corpus));
        String line;
        while((line = reader.readLine()) != null) {
        	analysis(line);
        }
        reader.close();
    }
    
    /**
     * Analysis text to derive the poet's affinity graph
     * @param text contain corpus need to analysis
     */
    private void analysis(String text) {
    	Scanner scanner = new Scanner(text);
    	String pre = "", cur = "";
    	int preWeight = 0;
    	
    	while (scanner.hasNext()) {
    		if (pre.isEmpty()) {
    			cur = scanner.next().toLowerCase();
    			graph.add(cur);
    		} else {
        		cur = scanner.next().toLowerCase();
        		if ((preWeight = graph.set(pre, cur, 1)) != 0) {
        			graph.set(pre, cur, preWeight + 1);
        		}
    		}
			pre = cur;
    	}
    	scanner.close();
    }
    
    // TODO checkRep
    
    /**
     * Get the graph
     */
    public Graph<String> getGraph() {
    	Graph<String> g = Graph.empty();
    	for (String vertex : graph.vertices()) {
    		g.add(vertex);
    		for (Map.Entry<String, Integer> entry :
    			graph.targets(vertex).entrySet()) {
    			g.set(vertex, entry.getKey(), entry.getValue());
    		}
    	}
    	return g;
	}
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
    	Scanner scanner = new Scanner(input);
    	String pre = "", cur = "", bridge = "";
    	List<String> result = new ArrayList<>();

    	while (scanner.hasNext()) {
    		cur = scanner.next();
    		if (!pre.isEmpty()) {
    			bridge = choose(twoWayPaths(pre, cur));
    			if (!bridge.isEmpty()) result.add(bridge);
    		}
    		result.add(cur);
    		pre = cur;
    	}
    	scanner.close();
    	return String.join(" ", result);
    }
    
    /**
     * Get two-edge-long paths between w1 and w2.
     * @param w1 source word in word affinity graph
     * @param w2 target word in word affinity graph
     * @return a Map contain bridge words between input words "w1" and "w2" with weight along
     * two-edge-long paths from w1 to w2 in the affinity graph
     */
    private Map<String, Integer> twoWayPaths(String w1, String w2) {
    	Map<String, Integer> paths = new HashMap<>();
    	String bridge = "", twoDis = "";
    	w1 = w1.toLowerCase();
    	w2 = w2.toLowerCase();
    	
    	for (Map.Entry<String, Integer> immeEntry : graph.sources(w2).entrySet()) {
    		bridge = immeEntry.getKey();
    		for (Map.Entry<String, Integer> twoDistanceEntry : graph.sources(bridge).entrySet()) {
    			twoDis = twoDistanceEntry.getKey();
    			if (twoDis.equals(w1)) {
    				paths.put(bridge, immeEntry.getValue() + twoDistanceEntry.getValue());
    			}
    		}
    	}
    	return paths;
    }
    
    /**
     * Choose a bridge word with maximum-weight or minimum in alphabetical order if
     * weight is the same
     * @param candidate bridge words with weight along two-edge-long paths 
     * from w1 to w2 in the affinity graph
     * @return a bridge word with maximum-weight or minimum in alphabetical order if
     * weight is the same
     */
    private String choose(Map<String, Integer> candidate) {
    	Map.Entry<String, Integer> maximum = null;
    	for (Map.Entry<String, Integer> entry : candidate.entrySet()) {
    		if (maximum == null) {
    			maximum = entry;
    		} else if (maximum.getValue() < entry.getValue() ||
    				maximum.getKey().compareTo(entry.getKey()) > 0) {
				maximum = entry;
    		}
    	}
    	return maximum == null ? "" : maximum.getKey();
    }
    
    /**
     * Returns a String represent the word affinity graph.
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
    	return graph.toString();
    }
}
