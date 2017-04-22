package graph2d.utils;

import java.util.HashMap;

public class Parser {
	
	protected int pos;
	protected int ch;
	protected String expression;
	
	private HashMap<Character, Double> variables;
	
	public Parser(String expression) {
		
		this.pos = -1;
		this.expression = expression;
		
		this.variables = new HashMap<>();
		
	}
	
	public void refreshVariable(char variable, double value) {
		
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
			
			System.out.println("Ate: " + (char) charToEat);
			
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
				ex = (() -> a.evaluate() + b.evaluate());
				
			} else if (this.eat('-')) {
				
				Expression a = ex, b = this.parseTerm();
				ex = (() -> a.evaluate() - b.evaluate());
				
			} else
				return ex;
			
		}
		
	}
	
	private Expression parseTerm() {
		
		Expression ex = this.parseFactor();
		
		for (;;) {
			
			if (this.eat('*')) {
				
				Expression a = ex, b = this.parseFactor();
				
				ex = (() -> a.evaluate() * b.evaluate()); // multiplication
				
			} else if (this.eat('/')) {
				
				Expression a = ex, b = this.parseFactor();
				
				ex = (() -> a.evaluate() / b.evaluate()); // division
				
			} else
				return ex;
			
		}
		
	}
	
	// Grammar:
	// expression = term | expression `+` term | expression `-` term
	// term = factor | term `*` factor | term `/` factor
	// factor = `+` factor | `-` factor | `(` expression `)`
	// | number | strtionName factor | factor `^` factor
	
	private Expression parseFactor() {
		
		Expression ex;
		
		if (this.eat('+')) return this.parseFactor(); // unary plus
		if (this.eat('-')) return () -> this.parseFactor().evaluate() * -1; // unary
		// minus
		
		int startPos = this.pos;
		
		if (this.eat('(')) { // parentheses
			
			ex = this.parseExpression();
			
			this.eat(')');
			
		} else if (this.eat('|')) {
			
			Expression a = this.parseExpression();
			
			ex = () -> Math.abs(a.evaluate());
			
			this.eat('|');
			
		} else if ((this.ch >= '0' && this.ch <= '9') || this.ch == '.') { // numbers
			
			while ((this.ch >= '0' && this.ch <= '9') || this.ch == '.') {
				
				this.nextChar();
				
			}
			
			double d = Double.parseDouble(this.expression.substring(startPos, this.pos));
			
			ex = (() -> d);
			
		} else if (this.ch >= 'a' && this.ch <= 'z') { // FIXME Variablen
			
			// functions
			while (this.ch >= 'a' && this.ch <= 'z') {
				
				this.nextChar();
				
			}
			
			String str = this.expression.substring(startPos, this.pos);
			
			if (str.equals("sqrt")) {
				
				Expression a = this.parseFactor();
				
				ex = () -> Math.sqrt(a.evaluate());
				
			} else if (str.equals("sin")) {
				
				Expression a = this.parseFactor();
				
				ex = () -> Math.sin(Math.toRadians(a.evaluate()));
				
			} else if (str.equals("cos")) {
				
				Expression a = this.parseFactor();
				
				ex = () -> Math.cos(Math.toRadians(a.evaluate()));
				
			} else if (str.equals("tan")) {
				
				Expression a = this.parseFactor();
				
				ex = () -> Math.tan(Math.toRadians(a.evaluate()));
				
			} else if (str.length() == 1) {
				
				char variable = str.charAt(0);
				
				ex = () -> this.variables.get(variable);
				
			} else
				throw new RuntimeException("Unknown function: " + str);
			
		} else
			throw new RuntimeException("Unexpected: " + (char) this.ch);
		
		if (this.eat('^')) {
			
			Expression a = ex, b = this.parseFactor();
			
			ex = () -> Math.pow(a.evaluate(), b.evaluate()); // exponentiation
			
		}
		
		return ex;
		
	}
	
	@FunctionalInterface
	public interface Expression {
		
		public double evaluate();
		
	}
	
}
