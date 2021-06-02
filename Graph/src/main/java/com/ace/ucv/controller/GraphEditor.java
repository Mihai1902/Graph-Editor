package com.ace.ucv.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphEditor extends JFrame implements ActionListener {

   private static final String TITLE = "Graph Editor";

   private final GraphPanel graphPanel = new GraphPanel(null);

   private final JMenuBar menuBar = new JMenuBar();

   private final JMenu fileMenu = new JMenu("File");

   private final JMenuItem newGraphMenuItem = new JMenuItem("New graph");

   public GraphEditor() {
      super(TITLE);
      setSize(800, 600);
      setResizable(true);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setContentPane(graphPanel);
      UIManager.put("OptionPane.messageFont", new Font("Monospaced", Font.BOLD, 12));
      addActionListeners();
      createMenuBar();
      setVisible(true);
   }

   private void addActionListeners() {
      newGraphMenuItem.addActionListener(this);
   }

   private void createMenuBar() {
      fileMenu.add(newGraphMenuItem);
      menuBar.add(fileMenu);
      setJMenuBar(menuBar);
   }

   @Override
   public void actionPerformed(ActionEvent event) {
      Object eventSource = event.getSource();

      if (eventSource == newGraphMenuItem) {
         graphPanel.createNewGraph();
      }
   }
}
