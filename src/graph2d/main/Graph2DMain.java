package graph2d.main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import graph2d.utils.JPlottingPane;

public class Graph2DMain {
	
	private JPlottingPane plotPane;
	private JTextField functionField;
	private JLabel functionValidLabel;
	
	public static void main(String[] args) {
		
		new Graph2DMain();
		
	}
	
	public Graph2DMain() {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				Graph2DMain.this.initGUI();
				
			}
			
		});
		
	}
	
	private void initGUI() {
		
		JFrame frame = new JFrame("2D Graph Draw");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel helperPanel = new JPanel(new GridBagLayout());
		
		JPanel functionPanel = new JPanel(new GridBagLayout());
		JPanel scalePanel = new JPanel(new GridBagLayout());
		
		TitledBorder functionBorder = BorderFactory.createTitledBorder("Function");
		TitledBorder scaleBorder = BorderFactory.createTitledBorder("Scale");
		
		functionPanel.setBorder(functionBorder);
		scalePanel.setBorder(scaleBorder);
		
		this.functionField = new JTextField();
		this.functionField.setMinimumSize(new Dimension(50, this.functionField.getHeight()));
		
		this.functionField.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent evt) {
				
				Graph2DMain.this.processKeyEvent(evt);
				
			}
			
		});
		
		this.functionValidLabel = new JLabel("Invalid Function");
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.anchor = GridBagConstraints.CENTER;
		
		gbc.fill = GridBagConstraints.NONE;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		functionPanel.add(this.functionField, gbc);
		
		gbc.gridy = 1;
		
		functionPanel.add(this.functionValidLabel, gbc);
		
		JSlider scaleSliderX = new JSlider();
		
		scaleSliderX.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent evt) {
				
				Graph2DMain.this.XState(evt);
				
			}
			
		});
		
		JSlider scaleSliderY = new JSlider();
		
		scaleSliderY.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent evt) {
				
				Graph2DMain.this.YState(evt);
				
			}
			
		});
		
		gbc.gridy = 0;
		
		scalePanel.add(scaleSliderX, gbc);
		
		gbc.gridy = 1;
		
		scalePanel.add(scaleSliderY, gbc);
		
		this.plotPane = new JPlottingPane();
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		
		gbc.gridheight = 2;
		
		gbc.anchor = GridBagConstraints.CENTER;
		
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.weightx = 0.5;
		
		helperPanel.add(this.plotPane, gbc);
		
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
	
	public void processKeyEvent(KeyEvent evt) {
		
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			
			if (this.plotPane.refreshFunction(this.functionField.getText().trim())) {
				
				this.functionValidLabel.setText("Function plotted!");
				
			} else {
				
				this.functionValidLabel.setText("Invalid function!");
				
			}
			
		}
		
	}
	
	public void XState(ChangeEvent evt) {
		
		JSlider source = (JSlider) evt.getSource();
		
		if (!source.getValueIsAdjusting()) {
			
			int rawScale = source.getValue();
			
			int scale = 1;
			
			if (rawScale < 50) {
				
				rawScale = 50 - rawScale;
				
				int i = rawScale / 5;
				
				scale = (1 / (10 ^ i)) * i % 5 * 2;
				
			} else {
				
				rawScale -= 50;
				
				int i = rawScale / 5;
				
				scale = (10 ^ i) * i % 5 * 2;
				
			}
			
			this.plotPane.adjustScaleX(scale);
			
		}
		
	}
	
	public void YState(ChangeEvent evt) {
		
		JSlider source = (JSlider) evt.getSource();
		
		if (!source.getValueIsAdjusting()) {
			
			int rawScale = source.getValue();
			
			int scale = 1;
			
			if (rawScale < 50) {
				
				rawScale = 50 - rawScale;
				
				int i = rawScale / 5;
				
				scale = (1 / (10 ^ i)) * i % 5 * 2;
				
			} else {
				
				rawScale -= 50;
				
				int i = rawScale / 5;
				
				scale = (10 ^ i) * i % 5 * 2;
				
			}
			
			this.plotPane.adjustScaleY(scale);
			
		}
		
	}
	
}
