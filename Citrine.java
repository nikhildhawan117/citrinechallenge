import java.util.HashMap;
import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
public class Citrine {
		
	
	public static void main(String[] args) {
		HashMap<String, String[]> unit_conv = new HashMap<String, String[]>();
		String input = args[0];
		input = input.replaceAll("minute", "min");
		input = input.replaceAll("h(?!a|e|o)", "hour");
		input = input.replaceAll("(?<!n)d(?!e)", "day");
		input = input.replaceAll("tonne", "t");
		String curr_expr = "";
		String unit_name = "";
		String unit_mult = "";
		
		//initializing the unit conversion table
		unit_conv.put("min", new String[] {"s", "60"});
		unit_conv.put("hour",  new String[] {"s", "3600"});
		unit_conv.put("day",  new String[] {"s", "86400"});
		unit_conv.put("°",  new String[] {"rad", "0.017453292519943"});
		unit_conv.put("degree",  new String[] {"rad", "0.017453292519943"});
		unit_conv.put("'", new String[] {"rad", "0.000290888208666"});
		unit_conv.put("second",  new String[] {"rad", "0.000004848136811"});
		unit_conv.put("\"", new String[] {"rad", "0.000004848136811"});
		unit_conv.put("hectare", new String[] {"m^2", "10000"});
		unit_conv.put("ha", new String[] {"m^2", "10000"});
		unit_conv.put("litre", new String[] {"m^3", "0.001"});
		unit_conv.put("L", new String[] {"m^3", "0.001"});
		unit_conv.put("t", new String[] {"kg", "1000"});	
		
		//Finding the unit_name
		for(int i = 0; i < input.length(); i++) {
			
			if(input.charAt(i) == '(' || input.charAt(i) == ')' || input.charAt(i) == '*' || input.charAt(i) == '/') {
				unit_name+=input.charAt(i);
				unit_mult += input.charAt(i);
			}
		
			else {
				curr_expr += input.charAt(i);
					if(unit_conv.get(curr_expr) != null) {
						String name_conv = unit_conv.get(curr_expr)[0];
						String mult_conv = unit_conv.get(curr_expr)[1];
						unit_name+= (name_conv);
						unit_mult+= (mult_conv);
						curr_expr = "";
					}
			}
			
		}
		
		//convert to postfix notation
		LinkedList<Character> token_stack = new LinkedList<Character>();
		String postfix_res = "";
		for(int i = 0; i < unit_mult.length(); i++) {
			
			if(unit_mult.charAt(i) == '*' || unit_mult.charAt(i) == '/') {
				postfix_res+=" ";
				while(!token_stack.isEmpty() && token_stack.peek() != '(') {
					char op = token_stack.pop();
					postfix_res+=op;
				}
				token_stack.push(unit_mult.charAt(i));
			}
			
			else if(unit_mult.charAt(i) == '(') {
				token_stack.push('(');
			}	
			
			else if(unit_mult.charAt(i) == ')') {
				postfix_res+=" ";
				while(!token_stack.isEmpty() && token_stack.peek() != '(') {
					char op = token_stack.pop();
					postfix_res+=op;
				}
				token_stack.pop();		
			}
			
			else 
				postfix_res+=unit_mult.charAt(i);
		}
		
		//Append final operators
		postfix_res += " ";
		while(!token_stack.isEmpty()) {
			char next = token_stack.pop();
			if(next != '(' || next != ')')
				postfix_res += next;
		}

		//postfix eval
		LinkedList<String> tokens = new LinkedList<String>();
		String operand = "";
		for(int i = 0; i < postfix_res.length(); i++) {
			if(postfix_res.charAt(i) == '*') {
				BigDecimal operand1 = new BigDecimal(tokens.pop()).setScale(20, RoundingMode.HALF_UP);
				BigDecimal operand2 = new BigDecimal(tokens.pop()).setScale(20, RoundingMode.HALF_UP);
				BigDecimal expr = operand2.multiply(operand1).setScale(20, RoundingMode.HALF_UP);
				tokens.push(expr.toPlainString());
			}
			
			else if(postfix_res.charAt(i) == '/') {
				BigDecimal operand1 = new BigDecimal(tokens.pop()).setScale(20, RoundingMode.HALF_UP);
				BigDecimal operand2 = new BigDecimal(tokens.pop()).setScale(20, RoundingMode.HALF_UP);
				BigDecimal expr = operand2.divide(operand1, 20, RoundingMode.HALF_UP);
				tokens.push(expr.toPlainString());
			}
			
			else if(!operand.equals("") && postfix_res.charAt(i) == ' ') {
				tokens.push(operand);
				operand = "";
			}
			
			else if(postfix_res.charAt(i) != ' ') {
				operand += postfix_res.substring(i, i+1);
			}
	
		}
		
		//Round to 14th decimal place
		String res = tokens.pop();
		BigDecimal bd = new BigDecimal(res).setScale(14, RoundingMode.HALF_UP);
		System.out.print(unit_name + " " + bd.toPlainString());
		
		
	}

}
