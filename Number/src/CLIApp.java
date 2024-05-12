import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class CLIApp {
	static INumberleModel model = new NumberleModel();
    static NumberleController controller = new NumberleController(model);
    public static void main(String[] args) {
    	Scanner sc=new Scanner(System.in);
        String formula="";
		controller.startNewGame();
        System.out.println("---Welcome Guess Number Game---");
    	while(true) {

    		System.out.print("Please enter formula(length 7):");
    		formula=sc.next();
    		if(judge(formula)) {
    			controller.processInput(formula);
    			if(controller.isGameWon()) {
    				while(true) {
    					System.out.println("You win the game!");
        				System.out.println("1.Continuing the game");
        				System.out.println("2.End the game");
        				System.out.print("Please enter:");
        	    		int result=sc.nextInt();
        	    		if(result==1) {
        	    			controller.startNewGame();
        	    			break;
        	    		}else if(result==2) {
        	    			System.exit(0);
        	    		}else {
        	    			System.out.println("Input error, please re-enter");
        	    		}
    				}
    				
    			}else if(controller.isGameOver()) {
    				while(true) {
    					System.out.println("You loss the game!");
        				System.out.println("1.Continuing the game");
        				System.out.println("2.End the game");
        				System.out.print("Please enter:");
        	    		int result=sc.nextInt();
        	    		if(result==1) {
        	    			controller.startNewGame();
        	    			break;
        	    		}else if(result==2) {
        	    			System.exit(0);
        	    		}else {
        	    			System.out.println("Input error, please re-enter");
        	    		}
    				}
    				
    			}else {
					System.out.println("Life:"+controller.getRemainingAttempts());
    				setColor(formula);
    			}
    		}else {
    			System.out.println("Your formula wrong");
    		}
    	}
    }
    public static boolean judge(String formula) {
    	String decompose[]=formula.split("=");
    	if(decompose.length==2) {
    		int left=calculateExpression(decompose[0]);
    		int right=calculateExpression(decompose[1]);

    		if(left==right) {
    			return true;
    		}
    	}
    	return false;
    }
    public static int calculateExpression(String expression) {
        Stack<Integer> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            
            if (Character.isDigit(c)) {
                int operand = 0;
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    operand = operand * 10 + (expression.charAt(i++) - '0');
                }
                i--; // 因为上面循环中i已经自增了，所以这里需要自减一次
                operands.push(operand);
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                    operands.push(applyOperation(operators.pop(), operands.pop(), operands.pop()));
                }
                operators.push(c);
            }
        }
        
        while (!operators.isEmpty()) {
            operands.push(applyOperation(operators.pop(), operands.pop(), operands.pop()));
        }
        
        return operands.pop();
    }

    private static int applyOperation(char op, int b, int a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return a / b;
            default: throw new IllegalArgumentException("Unsupported operation: " + op);
        }
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '^') return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false;
        return true;
    }
    public static void setColor(String formula) {
    	char[] guess=formula.toCharArray();
    	String currentGuess=controller.getCurrentGuess().toString();
		char[] randomGuess=currentGuess.toCharArray();
		List<String> state=new ArrayList<String>();
		for(int i=0;i<7;i++) {
			if(guess[i]==randomGuess[i]) {
				state.add("green");
			}else if(currentGuess.contains(String.valueOf(guess[i]))) {
				state.add("orange");											
			}else {
				state.add("gray");
			}
		}
		System.out.println(state);
    }
}
