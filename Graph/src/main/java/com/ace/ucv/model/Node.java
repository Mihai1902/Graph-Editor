package com.ace.ucv.model;

import java.awt.*;
import java.io.Serializable;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

public class Node implements Serializable {

   public static final Integer[] RADIUS_VALUES = {20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
           30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40};
   protected int x;
   protected int y;
   protected int radius;
   protected Color color;

   private String text;

   public Node(int x, int y, Color color, int radius, String text) {
      this.x = x;
      this.y = y;
      this.radius = radius;
      this.color = color;
      setTextOfNode(text);
   }

   public int getX() {
      return x;
   }

   public int getY() {
      return y;
   }

   public void setColorOfNode(Color color) {
      this.color = color;
   }

   public void setRadiusOfNode(int radius) {
      if (radius < RADIUS_VALUES[0]) {
         this.radius = RADIUS_VALUES[0];
      } else if (radius > RADIUS_VALUES[RADIUS_VALUES.length - 1]) {
         this.radius = RADIUS_VALUES[RADIUS_VALUES.length - 1];
      } else {
         this.radius = radius;
      }
   }

   public void setTextOfNode(String text) {
      if (text == null) {
         this.text = EMPTY_STRING;
      } else {
         this.text = text;
      }
   }

   public void drawNode(Graphics graphics) {
      graphics.setColor(color);
      graphics.fillOval(x - radius, y - radius, radius + radius, radius + radius);
      graphics.setColor(Color.BLACK);
      graphics.drawOval(x - radius, y - radius, radius + radius, radius + radius);

      FontMetrics fontMetrics = graphics.getFontMetrics();
      int xOfText = x - fontMetrics.stringWidth(text) * 2;
      int yOfText = y - fontMetrics.getHeight() / 2 + fontMetrics.getAscent();
      graphics.drawString(" " + text + "," + radius, xOfText, yOfText);
   }

   public boolean isNodeUnderCursor(int mouseX, int mouseY) {
      int a = x - mouseX;
      int b = y - mouseY;

      return a * a + b * b <= radius * radius;
   }

   public void moveNode(int distanceX, int distanceY) {
      x += distanceX;
      y += distanceY;
   }
}
