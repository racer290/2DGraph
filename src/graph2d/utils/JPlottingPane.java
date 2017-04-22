package graph2d.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import graph2d.utils.Parser.Expression;

public class JPlottingPane extends JPanel {
	
	private static final long serialVersionUID = 292583876481772305L;
	
	private Expression function;
	private String rawFunction;
	
	public JPlottingPane() {
		
		super();
		this.rawFunction = "| x * -1|";
		this.setPreferredSize(new Dimension(500, 500));
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(Color.BLACK);
		g.drawLine(10, 10, 10, this.getHeight() - 10);
		g.drawLine(10, this.getHeight() - 10, this.getWidth() - 10, this.getHeight() - 10);
		
		Parser parser = new Parser(this.rawFunction);
		this.function = parser.parseInput();
		
		parser.refreshVariable('x', 0);
		Point lastPoint = this.convertCoords(0, (int) Math.round(this.function.evaluate()));
		
		for (int x = 0; x < this.getWidth() - 10; x++) {
			
			parser.refreshVariable('x', x);
			int y = (int) Math.round(this.function.evaluate());
			
			Point point = this.convertCoords(x, y);
			
			g.drawLine(lastPoint.x, lastPoint.y, point.x, point.y);
			
			System.out.println(point.x + " " + point.y);
			
			lastPoint = point;
			
		}
		
	}
	
	private Point convertCoords(int x, int y) {
		
		return new Point(x + 10, this.getHeight() - y - 10);
		
	}
	
	public void refreshFunction(String newFunc) {
		
		this.rawFunction = newFunc;
		this.repaint();
		
	}
	
}
