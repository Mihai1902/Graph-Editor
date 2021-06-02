package com.ace.ucv.model;

import java.awt.*;
import java.io.Serializable;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

public class Edge implements Serializable {

   public static final Integer[] VALUES = {1, 2, 3, 4, 5};

   private final Node nodeA;

   private final Node nodeB;

   protected Color color;

   private int value;

   private String text;

   public Edge(Node nodeA, Node nodeB, Color color, int value, String text) {
      this.nodeA = nodeA;
      this.nodeB = nodeB;
      this.value = value;
      this.color = color;
      this.text = text;
   }

   public void setValueOfEdge(int value) {
      if (value < VALUES[0]) {
         this.value = VALUES[0];
      } else if (value > VALUES[VALUES.length - 1]) {
         this.value = VALUES[VALUES.length - 1];
      } else {
         this.value = value;
      }
   }

   public Node getNodeA() {
      return nodeA;
   }

   public Node getNodeB() {
      return nodeB;
   }

   public void drawEdge(Graphics graphics) {
      int xOfNodeA = getNodeA().getX();
      int yOfNodeA = getNodeA().getY();
      int xOfNodeB = getNodeB().getX();
      int yOfNodeB = getNodeB().getY();

      Graphics2D graphics2D = (Graphics2D) graphics;
      graphics2D.setStroke(new BasicStroke(value));
      graphics2D.setColor(color);
      graphics2D.drawLine(xOfNodeA, yOfNodeA, xOfNodeB, yOfNodeB);
      graphics2D.setStroke(new BasicStroke());
   }

   public boolean isEdgeUnderCursor(int mouseX, int mouseY) {
      if (mouseX < Math.min(nodeA.getX(), nodeB.getX()) ||
              mouseX > Math.max(nodeA.getX(), nodeB.getX()) ||
              mouseY < Math.min(nodeA.getY(), nodeB.getY()) ||
              mouseY > Math.max(nodeA.getY(), nodeB.getY())) {
         return false;
      }
      int A = nodeB.getY() - nodeA.getY();
      int B = nodeB.getX() - nodeA.getX();
      double distance = Math.abs(A * mouseX - B * mouseY + nodeB.getX()
              * nodeA.getY() - nodeB.getY() * nodeA.getX()) / Math.sqrt(A * A + B * B);
      return distance <= VALUES[4];
   }

   public void setTextOfEdge(String text) {
      if (text == null) {
         this.text = EMPTY_STRING;
      } else {
         this.text = text;
      }
   }

   public void setColorOfEdge(Color color) {
      this.color = color;
   }

   public void moveEdge(int distanceX, int distanceY) {
      nodeA.moveNode(distanceX, distanceY);
      nodeB.moveNode(distanceX, distanceY);
   }

   @Override
   public String toString() {
      String colorHex = "#" + Integer.toHexString(color.getRGB()).substring(2).toUpperCase();
      return "Value: " + value + ", Color: " + colorHex + ", Key: " + text;
   }
}
