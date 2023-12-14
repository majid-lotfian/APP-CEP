package org.example;
import java.io.Serializable;
import java.util.*;

class Graph<T> implements Serializable {

    // We use Hashmap to store the edges in the graph
    private Map<T, List<T> > map = new HashMap<T, List<T>>();

    // This function adds a new vertex to the graph
    public void addVertex(T s)
    {
        map.put(s, new LinkedList<T>());

    }

    // This function adds the edge
    // between source to destination
    public void addEdge(T source,
                        T destination,
                        boolean bidirectional)
    {

        if (!map.containsKey(source))
            addVertex(source);

        if (!map.containsKey(destination))
            addVertex(destination);

        map.get(source).add(destination);
        if (bidirectional == true) {
            map.get(destination).add(source);
        }
    }

    // This function gives the count of vertices
    public void getVertexCount()
    {
        System.out.println("The graph has "
                + map.keySet().size()
                + " vertex");
    }

    public int getOutgoingEdgesCount(T s){

        int count=0;
        //System.out.println("within graph class");
        //System.out.println(map.get(s));
        count =map.get(s).size();
        return count;

    }

    // This function gives the count of edges
    public void getEdgesCount(boolean bidirection)
    {
        int count = 0;
        for (T v : map.keySet()) {
            count += map.get(v).size();
        }
        if (bidirection == true) {
            count = count / 2;
        }
        System.out.println("The graph has "
                + count
                + " edges.");
    }

    // This function gives whether
    // a vertex is present or not.
    public boolean hasVertex(T s)
    {
        if (map.containsKey(s)) {
            return true;
        }
        else {
            return false;
        }
    }

    // This function gives whether an edge is present or not.
    public boolean hasEdge(T s, T d)
    {
        if (map.get(s).contains(d)) {
            return true;
        }
        else {
            return false;
        }
    }

    // Prints the adjancency list of each vertex.
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        for (T v : map.keySet()) {
            builder.append(v.toString() + ": ");
            for (T w : map.get(v)) {
                builder.append(w.toString() + " ");
            }
            builder.append("\n");
        }

        return (builder.toString());
    }
}
