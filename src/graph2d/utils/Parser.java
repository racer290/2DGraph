package graph2d.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public class Parser {
	
	protected int pos;
	protected int ch;
	protected String expression;
	
	private HashMap<Character, BigDecimal> variables;
	
	public Parser(String expression) {
		
		this.pos = -1;
		this.expression = expression;
		
		this.variables = new HashMap<>();
		
	}
	
	public void refreshVariable(char variable, BigDecimal value) {
		
		if (this.variables.containsKey(variable)) {
			
			this.variables.replace(variable, value);
			
		} else {
			
			this.variables.put(variable, value);
			
		}
		
	}
	
	private void nextChar() {
		
		this.ch = (++this.pos < this.expression.length()) ? this.expression.charAt(this.pos) : -1;
		
	}
	
	private boolean eat(int charToEat) {
		
		while (this.ch == ' ') {
			
			this.nextChar();
			
		}
		
		if (this.ch == charToEat) {
			
			this.nextChar();
			return true;
			
		}
		
		return false;
		
	}
	
	public Expression parseInput() {
		
		this.nextChar();
		Expression ex = this.parseExpression();
		if (this.pos < this.expression.length()) throw new RuntimeException("Unexpected: " + (char) this.ch);
		return ex;
		
	}
	
	private Expression parseExpression() {
		
		Expression ex = this.parseTerm();
		
		for (;;) {
			
			if (this.eat('+')) {
				
				Expression a = ex, b = this.parseTerm();
				
				ex = () -> a.evaluate().add(b.evaluate());
				
			} else if (this.eat('-')) {
				
				Expression a = ex, b = this.parseTerm();
				
				ex = () -> a.evaluate().subtract(b.evaluate());
				
			} else
				return ex;
			
		}
		
	}
	
	private Expression parseTerm() {
		
		Expression ex = this.parseFactor();
		
		for (;;) {
			
			if (this.eat('*')) {
				
				Expression a = ex, b = this.parseFactor();
				
				ex = () -> a.evaluate().multiply(b.evaluate());
				
			} else if (this.eat('/')) {
				
				Expression a = ex, b = this.parseFactor();
				
				ex = () -> a.evaluate().divide(b.evaluate(), RoundingMode.HALF_EVEN/*cus nicer calculation (/2 etc.)*/);
				
			} else
				return ex;
			
		}
		
	}
	
	private Expression parseFactor() {
		
		Expression ex;
		
		if (this.eat('+')) return this.parseFactor();
		if (this.eat('-')) return () -> this.parseFactor().evaluate().negate();
		
		int startPos = this.pos;
		
		if (this.eat('(')) {
			
			ex = this.parseExpression();
			
			this.eat(')');
			
		} else if (this.eat('|')) {
			
			Expression a = this.parseExpression();
			
			ex = () -> a.evaluate().abs();
			
			this.eat('|');
			
		} else if ((this.ch >= '0' && this.ch <= '9') || this.ch == '.') {
			
			while ((this.ch >= '0' && this.ch <= '9') || this.ch == '.') {
				
				this.nextChar();
				
			}
			
			BigDecimal d = new BigDecimal(Double.parseDouble(this.expression.substring(startPos, this.pos)));
			
			ex = () -> d;
			
		} else if (this.ch >= 'a' && this.ch <= 'z') {
			
			while (this.ch >= 'a' && this.ch <= 'z') {
				
				this.nextChar();
				
			}
			
			String str = this.expression.substring(startPos, this.pos);
			
			if (str.equals("sqrt")) {
				
				Expression a = this.parseFactor();
				
				ex = () -> new BigDecimal(Math.sqrt(a.evaluate().doubleValue()));
				
			} else if (str.equals("sin")) {
				
				Expression a = this.parseFactor();
				
				ex = () -> new BigDecimal(Math.sin(Math.toRadians(a.evaluate().doubleValue())));
				
			} else if (str.equals("cos")) {
				
				Expression a = this.parseFactor();
				
				ex = () -> new BigDecimal(Math.cos(Math.toRadians(a.evaluate().doubleValue())));
				
			} else if (str.equals("tan")) {
				
				Expression a = this.parseFactor();
				
				ex = () -> new BigDecimal(Math.tan(Math.toRadians(a.evaluate().doubleValue())));
				
			} else if (str.length() == 1) {
				
				char variable = str.charAt(0);
				
				ex = () -> this.variables.get(variable);
				
			} else
				throw new RuntimeException("Unknown function: " + str);
			
		} else
			throw new RuntimeException("Unexpected: " + (char) this.ch);
		
		if (this.eat('^')) {
			
			Expression a = ex, b = this.parseFactor();
			
			ex = () -> new BigDecimal(Math.pow(a.evaluate().doubleValue(), b.evaluate().doubleValue()));
			
		}
		
		return ex;
		
	}
	
	@FunctionalInterface
	public interface Expression {
		
		public BigDecimal evaluate();
		
	}
	
}
