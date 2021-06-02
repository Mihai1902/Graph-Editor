package com.ace.ucv.controller;

import com.ace.ucv.model.Edge;
import com.ace.ucv.model.Graph;
import com.ace.ucv.model.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GraphPanel extends JPanel implements MouseListener, MouseMotionListener {

   private Graph graph;

   private boolean mouseLeftButton = false;

   private int mouseX;

   private int mouseY;

   private Node nodeUnderCursor;

   private Edge edgeUnderCursor;

   private boolean chooseNodeB = false;

   private Node newEdgeNodeA;

   public GraphPanel(Graph g) {
      if (g == null) {
         graph = new Graph();
      } else {
         setGraph(g);
      }

      addMouseMotionListener(this);
      addMouseListener(this);
      setFocusable(true);
      requestFocus();
   }

   public void setGraph(Graph graph) {
      if (graph == null) {
         this.graph = new Graph();
      } else {
         this.graph = graph;
      }
   }

   @Override
   protected void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);

      if (graph != null) {
         graph.draw(graphics);
      }
   }

   public void createNewGraph() {
      setGraph(new Graph());
      repaint();
   }

   @Override
   public void mouseDragged(MouseEvent event) {
      if (mouseLeftButton) {
         moveGraphDrag(event.getX(), event.getY());
      } else {
         setMouseCursor(event);
      }
   }

   @Override
   public void mouseMoved(MouseEvent event) {
      setMouseCursor(event);
   }

   @Override
   public void mouseClicked(MouseEvent event) {
   }

   @Override
   public void mouseEntered(MouseEvent event) {
   }

   @Override
   public void mouseExited(MouseEvent event) {
   }

   @Override
   public void mousePressed(MouseEvent event) {
      if (event.getButton() == MouseEvent.BUTTON1) {
         mouseLeftButton = true;
      }
      setMouseCursor(event);
   }

   @Override
   public void mouseReleased(MouseEvent event) {
      if (event.getButton() == MouseEvent.BUTTON1) {
         mouseLeftButton = false;
         finalizeAddEdge();
      }

      if (event.getButton() == MouseEvent.BUTTON3) {
         chooseNodeB = false;
         if (nodeUnderCursor != null) {
            createNodePopupMenu(event, nodeUnderCursor);
         } else if (edgeUnderCursor != null) {
            createEdgePopupMenu(event, edgeUnderCursor);
         } else {
            createPlainPopupMenu(event);
         }
      }
      setMouseCursor(event);
   }

   private void createPlainPopupMenu(MouseEvent event) {
      JPopupMenu popupMenu = new JPopupMenu();
      JMenuItem newNodeMenuItem = new JMenuItem("New node");
      popupMenu.add(newNodeMenuItem);
      newNodeMenuItem.addActionListener((action) -> createNewNode(event.getX(), event.getY()));
      popupMenu.show(event.getComponent(), event.getX(), event.getY());
   }

   private void createNodePopupMenu(MouseEvent event, Node node) {
      JPopupMenu popupMenu = new JPopupMenu();
      JMenuItem removeNodeMenuItem = new JMenuItem("Remove node");
      popupMenu.add(removeNodeMenuItem);
      removeNodeMenuItem.addActionListener((action) -> removeNode(node));

      popupMenu.addSeparator();

      JMenuItem addEdgeMenuItem = new JMenuItem("Add edge");
      popupMenu.add(addEdgeMenuItem);
      addEdgeMenuItem.addActionListener((action) -> initializeAddEdge(node));

      if (nodeUnderCursor != null) {
         popupMenu.addSeparator();

         JMenuItem changeNodeRadiusMenuItem = new JMenuItem("Change node size");
         popupMenu.add(changeNodeRadiusMenuItem);
         changeNodeRadiusMenuItem.addActionListener((action) -> changeNodeRadius(node));

         JMenuItem changeTextMenuItem = new JMenuItem("Change node text");
         popupMenu.add(changeTextMenuItem);
         changeTextMenuItem.addActionListener((action) -> changeNodeText(node));

         JMenuItem changeNodeColor = new JMenuItem("Change node color");
         popupMenu.add(changeNodeColor);
         changeNodeColor.addActionListener((action) -> changeNodeColor(node));
      }
      popupMenu.show(event.getComponent(), event.getX(), event.getY());
   }


   private void createEdgePopupMenu(MouseEvent event, Edge edge) {
      JPopupMenu popupMenu = new JPopupMenu();
      JMenuItem removeEdgeMenuItem = new JMenuItem("Remove edge");
      popupMenu.add(removeEdgeMenuItem);
      removeEdgeMenuItem.addActionListener((action) -> removeEdge(edge));

      if (edge != null) {
         popupMenu.addSeparator();

         JMenuItem changeEdgeStrokeMenuItem = new JMenuItem("Change edge size");
         popupMenu.add(changeEdgeStrokeMenuItem);
         changeEdgeStrokeMenuItem.addActionListener((action) -> changeEdgeStroke(edge));

         JMenuItem changeEdgeTextMenuItem = new JMenuItem("Change edge text");
         popupMenu.add(changeEdgeTextMenuItem);
         changeEdgeTextMenuItem.addActionListener((action) -> changeEdgeText(edge));

         JMenuItem changeEdgeColorMenuItem = new JMenuItem("Change edge color");
         popupMenu.add(changeEdgeColorMenuItem);
         changeEdgeColorMenuItem.addActionListener((action) -> changeEdgeColor(edge));
      }
      popupMenu.show(event.getComponent(), event.getX(), event.getY());
   }

   public void setMouseCursor(MouseEvent event) {
      if (event != null) {
         nodeUnderCursor = graph.findNodeUnderCursor(event.getX(), event.getY());
         if (nodeUnderCursor == null) {
            edgeUnderCursor = graph.findEdgeUnderCursor(event.getX(), event.getY());
         }
         mouseX = event.getX();
         mouseY = event.getY();
      }

      int mouseCursor;
      if (nodeUnderCursor != null) {
         mouseCursor = Cursor.HAND_CURSOR;
      } else if (edgeUnderCursor != null) {
         mouseCursor = Cursor.CROSSHAIR_CURSOR;
         if (event != null) {
            edgeUnderCursor = graph.findEdgeUnderCursor(event.getX(), event.getY());
         }
         setToolTipText(edgeUnderCursor.toString());
      } else if (chooseNodeB) {
         mouseCursor = Cursor.WAIT_CURSOR;
      } else if (mouseLeftButton) {
         mouseCursor = Cursor.MOVE_CURSOR;
      } else {
         mouseCursor = Cursor.DEFAULT_CURSOR;
      }
      setCursor(Cursor.getPredefinedCursor(mouseCursor));
   }

   private void moveGraphDrag(int mouseX, int mouseY) {
      int dragX = mouseX - this.mouseX;
      int dragY = mouseY - this.mouseY;

      if (nodeUnderCursor != null) {
         nodeUnderCursor.moveNode(dragX, dragY);
      } else if (edgeUnderCursor != null) {
         edgeUnderCursor.moveEdge(dragX, dragY);
      } else {
         graph.moveGraph(dragX, dragY);
      }

      this.mouseX = mouseX;
      this.mouseY = mouseY;
      repaint();
   }

   private void createNewNode(int mouseX, int mouseY) {
      Color color = JColorChooser.showDialog(this, "Choose color", Color.WHITE);
      int radius = (Integer) JOptionPane.showInputDialog(this, "Choose size", "New node",
              JOptionPane.PLAIN_MESSAGE, null, Node.RADIUS_VALUES, Node.RADIUS_VALUES[0]);
      String text = JOptionPane.showInputDialog(this, "Input text:", "New node",
              JOptionPane.QUESTION_MESSAGE);
      graph.addNode(new Node(mouseX, mouseY, color, radius, text));
      repaint();
   }

   private void removeNode(Node node) {
      graph.removeNode(node);
      repaint();
   }

   private void initializeAddEdge(Node node) {
      if (nodeUnderCursor != null) {
         newEdgeNodeA = node;
         chooseNodeB = true;
         setMouseCursor(null);
      }
   }

   private void finalizeAddEdge() {
      if (chooseNodeB) {
         if (nodeUnderCursor != null) {
            if (nodeUnderCursor.equals(newEdgeNodeA)) {
               JOptionPane.showMessageDialog(this, "Choose different node!", "Error!",
                       JOptionPane.ERROR_MESSAGE);
            } else {
               try {
                  Node newEdgeNodeB = nodeUnderCursor;
                  Color color = JColorChooser.showDialog(this, "Choose color", Color.BLACK);
                  int stroke = (Integer) JOptionPane.showInputDialog(this, "Choose size",
                          "New edge", JOptionPane.PLAIN_MESSAGE, null, Edge.VALUES, Edge.VALUES[0]);
                  String text = JOptionPane.showInputDialog(this, "Input text:", "New edge",
                          JOptionPane.QUESTION_MESSAGE);
                  graph.addEdge(new Edge(newEdgeNodeA, newEdgeNodeB, color, stroke, text));
                  repaint();
               } catch (NullPointerException exception) {
                  JOptionPane.showMessageDialog(this, "Operation canceled.", "Canceled",
                          JOptionPane.INFORMATION_MESSAGE);
               }
            }
         }
         chooseNodeB = false;
      }
   }

   private void removeEdge(Edge edge) {
      graph.removeEdge(edge);
      repaint();
   }

   private void changeNodeRadius(Node node) {
      try {
         int radius = (Integer) JOptionPane.showInputDialog(this, "Choose radius:",
                 "Edit node", JOptionPane.QUESTION_MESSAGE, null, Node.RADIUS_VALUES, Node.RADIUS_VALUES[0]);
         node.setRadiusOfNode(radius);
         repaint();
      } catch (ClassCastException exception) {
         JOptionPane.showMessageDialog(this, "This node cannot have different radius.",
                 "Error!", JOptionPane.ERROR_MESSAGE);
      } catch (NullPointerException exception) {
         JOptionPane.showMessageDialog(this, "Operation canceled.", "Canceled",
                 JOptionPane.INFORMATION_MESSAGE);
      }
   }

   private void changeNodeText(Node node) {
      String text = JOptionPane.showInputDialog(this, "Input text:", "Edit node",
              JOptionPane.QUESTION_MESSAGE);
      try {
         node.setTextOfNode(text);
         repaint();
      } catch (NullPointerException exception) {
         JOptionPane.showMessageDialog(this, "Operation canceled.", "Canceled",
                 JOptionPane.INFORMATION_MESSAGE);
      }
   }

   private void changeNodeColor(Node node) {
      Color color = JColorChooser.showDialog(this, "Choose color", Color.BLACK);
      try {
         node.setColorOfNode(color);
         repaint();
      } catch (NullPointerException exception) {
         JOptionPane.showMessageDialog(this, "Operation canceled.", "Canceled",
                 JOptionPane.INFORMATION_MESSAGE);
      }
   }

   private void changeEdgeStroke(Edge edge) {
      try {
         int stroke = (Integer) JOptionPane.showInputDialog(this, "Choose value",
                 "Edit edge", JOptionPane.PLAIN_MESSAGE, null, Edge.VALUES, Edge.VALUES[0]);
         edge.setValueOfEdge(stroke);
         repaint();
      } catch (ClassCastException exception) {
         JOptionPane.showMessageDialog(this, "This edge cannot have different value.",
                 "Error!", JOptionPane.INFORMATION_MESSAGE);
      } catch (NullPointerException exception) {
         JOptionPane.showMessageDialog(this, "Operation canceled.", "Canceled",
                 JOptionPane.INFORMATION_MESSAGE);
      }
   }

   private void changeEdgeText(Edge edge) {
      try {
         String text = JOptionPane.showInputDialog(this, "Input text:", "Edit edge",
                 JOptionPane.QUESTION_MESSAGE);
         edge.setTextOfEdge(text);
         repaint();
      } catch (ClassCastException exception) {
         JOptionPane.showMessageDialog(this, "This edge cannot have different text.",
                 "Error!", JOptionPane.INFORMATION_MESSAGE);
      } catch (NullPointerException exception) {
         JOptionPane.showMessageDialog(this, "Operation canceled.", "Canceled",
                 JOptionPane.INFORMATION_MESSAGE);
      }
   }

   private void changeEdgeColor(Edge edge) {
      try {
//         Color color = JColorChooser.showDialog(this, "Choose color", Color.BLACK);
//         edge.setColorOfEdge(color);
         repaint();
      } catch (ClassCastException exception) {
         JOptionPane.showMessageDialog(this, "This edge cannot have different color.",
                 "Error!", JOptionPane.INFORMATION_MESSAGE);
      } catch (NullPointerException exception) {
         JOptionPane.showMessageDialog(this, "Operation canceled.", "Canceled",
                 JOptionPane.INFORMATION_MESSAGE);
      }
   }
}
