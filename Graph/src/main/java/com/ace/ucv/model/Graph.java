package com.ace.ucv.model;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Graph implements Serializable {

   private List<Node> nodes;

   private List<Edge> edges;

   public Graph() {
      setNodes(new ArrayList<>());
      setEdges(new ArrayList<>());
   }

   public List<Node> getNodes() {
      return nodes;
   }

   public void setNodes(List<Node> nodes) {
      this.nodes = nodes;
   }

   public List<Edge> getEdges() {
      return edges;
   }

   public void setEdges(List<Edge> edges) {
      this.edges = edges;
   }

   public void draw(Graphics graphics) {
      for (Edge edge : getEdges()) {
         edge.drawEdge(graphics);
      }

      for (Node node : getNodes()) {
         node.drawNode(graphics);
      }
   }

   public void addNode(Node node) {
      nodes.add(node);
   }

   public void addEdge(Edge newEdge) {
      for (Edge edge : edges) {
         if (newEdge.equals(edge)) {
            return;
         }
      }
      edges.add(newEdge);
   }

   public Node findNodeUnderCursor(int mouseX, int mouseY) {
      for (Node node : nodes) {
         if (node.isNodeUnderCursor(mouseX, mouseY)) {
            return node;
         }
      }
      return null;
   }

   public Edge findEdgeUnderCursor(int mouseX, int mouseY) {
      for (Edge edge : edges) {
         if (edge.isEdgeUnderCursor(mouseX, mouseY)) {
            return edge;
         }
      }
      return null;
   }

   public void removeNode(Node nodeUnderCursor) {
      removeAttachedEdges(nodeUnderCursor);
      nodes.remove(nodeUnderCursor);
   }

   protected void removeAttachedEdges(Node nodeUnderCursor) {
      edges.removeIf(edge -> edge.getNodeA().equals(nodeUnderCursor) || edge.getNodeB().equals(nodeUnderCursor));
   }

   public void removeEdge(Edge edgeUnderCursor) {
      edges.remove(edgeUnderCursor);
   }

   public void moveGraph(int distanceX, int distanceY) {
      for (Node node : nodes) {
         node.moveNode(distanceX, distanceY);
      }
   }
}
