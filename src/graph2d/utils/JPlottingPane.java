package graph2d.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.math.BigDecimal;

import javax.swing.JPanel;

import graph2d.utils.Parser.Expression;

public class JPlottingPane extends JPanel {
	
	private static final long serialVersionUID = 292583876481772305L;
	
	private Expression function;
	private Parser parser;
	
	private int scaleX;
	private int scaleY;
	
	private Point center;
	
	boolean rendering;
	
	public JPlottingPane() {
		
		super();
		
		this.refreshFunction("");
		
		this.setPreferredSize(new Dimension(500, 500));
		
		this.scaleX = 1;
		this.scaleY = 1;
		
		this.center = new Point(this.getWidth() / 2, this.getHeight() / 2);
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		this.center.x = this.getWidth() / 2;
		this.center.y = this.getHeight() / 2;
		
		// Background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		// X axis
		g.setColor(Color.BLACK);
		g.drawLine(10, this.getHeight() / 2, this.getWidth() - 10, this.getHeight() / 2);
		// Y axis
		g.drawLine(this.getWidth() / 2, 10, this.getWidth() / 2, this.getHeight() - 10);
		
		g.setColor(Color.RED);
		
		if (!this.isRendering()) return;
		
		this.parser.refreshVariable('x', new BigDecimal(-(this.getWidth() / 2 - 10) / this.scaleX));
		
		Point lastPoint = this.convertCoords(-this.getWidth() / 2 + 10, this.function.evaluate().intValue());
		
		// Cache value
		int xmax = (this.getWidth() / 2 - 10) / this.scaleX;
		
		for (int x = -(this.getWidth() / 2 - 10) / this.scaleX; x < xmax; x += this.scaleX) {
			
			this.parser.refreshVariable('x', new BigDecimal(x));
			
			try {
				
				int y = this.function.evaluate().multiply(new BigDecimal(this.scaleY)).intValue();
				
				Point point = this.convertCoords(x, y);
				
				if (lastPoint == null) {
					
					g.drawLine(point.x, point.y, point.x, point.y);
					
				} else {
					
					g.drawLine(lastPoint.x, lastPoint.y, point.x, point.y);
					
				}
				
				lastPoint = point;
				
			} catch (ArithmeticException ex) {
				
				lastPoint = null;
				
			}
			
		}
		
	}
	
	/**
	 * Converts a set of calculated coordinates into the set that the point
	 * would be drawn in on the screen (in the pane)
	 */
	private Point convertCoords(int x, int y) {
		
		return new Point(x + this.getWidth() / 2, this.getHeight() / 2 - y);
		
	}
	
	public void adjustScaleX(int scale) {
		
		this.scaleX = scale;
		this.repaint();
		
	}
	
	public void adjustScaleY(int scale) {
		
		this.scaleY = scale;
		this.repaint();
		
	}
	
	public boolean refreshFunction(String newFunc) {
		
		this.parser = new Parser(newFunc);
		
		try {
			
			this.function = this.parser.parseInput();
			this.setRendering(true);
			
		} catch (RuntimeException ex) {
			
			if (!(ex.getMessage().startsWith("Unexpected: ") || ex.getMessage().startsWith("Unknown function: ")))
				throw ex;
			
			this.setRendering(false);
			
		}
		
		this.repaint();
		
		return this.isRendering();
		
	}
	
	public boolean isRendering() {
		
		return this.rendering;
		
	}
	
	public void setRendering(boolean rendering) {
		
		this.rendering = rendering;
		
	}
	
}
