package graph2d.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import graph2d.utils.JPlottingPane;

public class Graph2DMain {
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				initGUI();
				
			}
			
		});
		
	}
	
	private static void initGUI() {
		
		JFrame frame = new JFrame("2D Graph Draw");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel helperPanel = new JPanel(new GridBagLayout());
		
		JPanel functionPanel = new JPanel(new GridBagLayout());
		JPanel scalePanel = new JPanel(new GridBagLayout());
		
		TitledBorder functionBorder = BorderFactory.createTitledBorder("Function");
		TitledBorder scaleBorder = BorderFactory.createTitledBorder("Scale");
		
		functionPanel.setBorder(functionBorder);
		scalePanel.setBorder(scaleBorder);
		
		JTextField functionField = new JTextField();
		JLabel functionValidLabel = new JLabel("Invalid Function");
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.anchor = GridBagConstraints.CENTER;
		
		gbc.fill = GridBagConstraints.NONE;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		functionPanel.add(functionField, gbc);
		
		gbc.gridy = 1;
		
		functionPanel.add(functionValidLabel, gbc);
		
		JSlider scaleX = new JSlider();
		JSlider scaleY = new JSlider();
		
		gbc.gridy = 0;
		
		scalePanel.add(scaleX, gbc);
		
		gbc.gridy = 1;
		
		scalePanel.add(scaleY, gbc);
		
		JPlottingPane plotPane = new JPlottingPane();
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		
		gbc.gridheight = 2;
		
		gbc.anchor = GridBagConstraints.CENTER;
		
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.weightx = 0.5;
		
		helperPanel.add(plotPane, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		gbc.anchor = GridBagConstraints.NORTH;
		
		gbc.fill = GridBagConstraints.NONE;
		
		gbc.weightx = 0;
		gbc.weighty = 0.5;
		
		helperPanel.add(functionPanel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		
		gbc.anchor = GridBagConstraints.SOUTH;
		
		gbc.fill = GridBagConstraints.NONE;
		
		gbc.weightx = 0;
		gbc.weighty = 0.5;
		
		helperPanel.add(scalePanel, gbc);
		
		frame.setContentPane(helperPanel);
		
		frame.pack();
		
		frame.setVisible(true);
		
	}
	
}
