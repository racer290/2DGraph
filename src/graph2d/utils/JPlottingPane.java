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
	private String rawFunction;
	
	private int scaleX;
	private int scaleY;
	
	private Point center;
	
	public JPlottingPane() {
		
		super();
		this.rawFunction = "sin x";
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
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(Color.BLACK);
		g.drawLine(10, this.getHeight() / 2, this.getWidth() - 10, this.getHeight() / 2);
		g.drawLine(this.getWidth() / 2, 10, this.getWidth() / 2, this.getHeight() - 10);
		
		g.setColor(Color.RED);
		
		Parser parser = new Parser(this.rawFunction);
		this.function = parser.parseInput();
		
		parser.refreshVariable('x', BigDecimal.ZERO);
		
		Point lastPoint = null;
		
		try {
			
			lastPoint = this.convertCoords(0, this.function.evaluate().intValue());
			
		} catch (ArithmeticException ex) {
		} catch (NumberFormatException ex) {
			
			if (!ex.getMessage().equals("Infinite or NaN")) throw ex;
			
		}
		
		for (int x = -(this.getWidth() / 2 - 10) / this.scaleX; x < (this.getWidth() / 2 - 10)
				/ this.scaleX; x *= this.scaleX) {
			
			parser.refreshVariable('x', new BigDecimal(x));
			
			try {
				
				int y = this.function.evaluate().intValue() * this.scaleY;
				
				Point point = this.convertCoords(x, y);
				
				if (lastPoint == null) {
					
					g.drawLine(point.x, point.y, point.x, point.y);
					
				} else {
					
					g.drawLine(lastPoint.x, lastPoint.y, point.x, point.y);
					
				}
				
				System.out.println(
						"real x: " + x + ", drawn x: " + point.x + "\nreal y: " + y + ", drawn y: " + point.y + '\n'
						+ Math.sin(Math.toRadians(x)));
				
				lastPoint = point;
				
			} catch (NumberFormatException ex) {
				
				if (!ex.getMessage().equals("Infinite or NaN")) throw ex;
				
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
		
	}
	
	public void adjustScaleY(int scale) {
		
		this.scaleY = scale;
		
	}
	
	public void refreshFunction(String newFunc) {
		
		this.rawFunction = newFunc;
		this.repaint();
		
	}
	
}
