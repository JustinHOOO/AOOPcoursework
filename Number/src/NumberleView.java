// NumberleView.java
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observer;
import java.util.Stack;

public class NumberleView implements Observer {
    private final INumberleModel model;
    private final NumberleController controller;
    private final JFrame frame = new JFrame("Numberle");
    private final JTextField[][] fill = new JTextField[6][7];
    private final JButton[] key=new JButton[17];
    private int Abscissa=0;
    private int Ordinate=0;
    public NumberleView(INumberleModel model, NumberleController controller) {
        this.controller = controller;
        this.model = model;
        this.controller.startNewGame();
        ((NumberleModel)this.model).addObserver(this);
        initializeFrame();
        this.controller.setView(this);
        update((NumberleModel)this.model, null);
    }

    public void initializeFrame() {
          
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(null);
        for(int i=0;i<fill.length;i++) {
        	for(int j=0;j<fill[i].length;j++) {
        		fill[i][j]=new JTextField();
        		fill[i][j].setEditable(false);
        		fill[i][j].setBounds((125+j*50), (40+i*50), 40, 40);
        		fill[i][j].setBorder(new LineBorder(Color.black));
				fill[i][j].setHorizontalAlignment(JTextField.CENTER);
				frame.add(fill[i][j]);
        	}
        }
        String[] button= {"0","1","2","3","4","5","6","7","8","9","<-","+","-","*","/","=","Enter"};
        for(int i=0;i<10;i++) {
			key[i] = new JButton(button[i]);
			key[i].setBounds((70 + i * 45), 360, 42, 40);
			frame.add(key[i]);
        }
        key[10]=new JButton(button[10]);
        key[10].setBounds(70, 410, 80, 40);
        frame.add(key[10]);
        for(int i=11;i<16;i++) {
        	key[i]=new JButton(button[i]);
        	key[i].setBounds((165+(i-11)*50), 410, 42, 40);
        	frame.add(key[i]);
        }
        key[16]=new JButton(button[16]);
        key[16].setBounds(420, 410, 80, 40);
        for(int i=0;i<key.length;i++) {
        	int index=i;
        	key[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(key[index].getText().equals("<-")) {
						if(Ordinate>0) {
							Ordinate--;
							fill[Abscissa][Ordinate].setText("");
						}
					}else if(key[index].getText().equals("Enter")) {
						if(Ordinate<7) {
							JOptionPane.showMessageDialog(null, "Formula too short");
						}else {
							String formula="";
							for(int j=0;j<7;j++) {
								formula+=fill[Abscissa][j].getText();
							}
							if(judge(formula)) {
								controller.processInput(formula);
								if(controller.isGameWon()) {
									setColor(formula);
									int result = JOptionPane.showConfirmDialog(
							                null, 
							                "You win the game!", 
							                "Confirmation",
							                JOptionPane.YES_NO_OPTION, 
							                JOptionPane.WARNING_MESSAGE, 
							                null 
							            );


							            if (result == JOptionPane.YES_OPTION) {
							            	init();
							            } else if (result == JOptionPane.NO_OPTION) {
							               System.exit(0);
							            }
								}else if(controller.isGameOver()) {
									setColor(formula);
									int result = JOptionPane.showConfirmDialog(
							                null, 
							                "You Loss the game!", 
							                "Confirmation",
							                JOptionPane.YES_NO_OPTION, 
							                JOptionPane.WARNING_MESSAGE, 
							                null 
							            );


							            if (result == JOptionPane.YES_OPTION) {
							                init();
							            } else if (result == JOptionPane.NO_OPTION) {
							               System.exit(0);
							            }
								}else {
									JOptionPane.showMessageDialog(null, "You guessed wrong");
									setColor(formula);
									Abscissa++;
									Ordinate=0;
								}
							}else {
								JOptionPane.showMessageDialog(null, "Your formula wrong");
							}
						}
					}else {
						if(Ordinate<7) {
							fill[Abscissa][Ordinate].setText(key[index].getText());
							Ordinate++;
						}
					}
				}
			});
        }
        frame.add(key[16]);
        frame.setVisible(true);
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        //attemptsLabel.setText("Attempts remaining: " + controller.getRemainingAttempts());
    }
    public void init() {
    	Abscissa=0;
    	Ordinate=0;
    	for(int i=0;i<fill.length;i++) {
			for(int j=0;j<fill[i].length;j++) {
				
				fill[i][j].setText("");
				fill[i][j].setBackground(null);
			}
		}
    	for(int i=0;i<17;i++) {
    		key[i].setBackground(null);
        }
    	controller.startNewGame();
	}
  //Set position color after entering the equation
    public void setColor(String formula) {
    	char[] guess=formula.toCharArray();
    	String currentGuess=controller.getCurrentGuess().toString();
		char[] randomGuess=currentGuess.toCharArray();
		for(int i=0;i<7;i++) {
			if(guess[i]==randomGuess[i]) {
				fill[Abscissa][i].setBackground(Color.GREEN);
				for(int j=0;j<17;j++) {
					if(key[j].getText().equals(String.valueOf(guess[i]))) {
						key[j].setBackground(Color.GREEN);
					}
				}
			}else if(currentGuess.contains(String.valueOf(guess[i]))) {
				fill[Abscissa][i].setBackground(Color.ORANGE);
					for(int j=0;j<17;j++) {
						if(key[j].getText().equals(String.valueOf(guess[i]))) {
							key[j].setBackground(Color.ORANGE);
						}
					}													
			}else {
				fill[Abscissa][i].setBackground(Color.GRAY);
				for(int j=0;j<17;j++) {
					if(key[j].getText().equals(String.valueOf(guess[i]))) {
						key[j].setBackground(Color.GRAY);
					}
				}	
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
                i--; //Because 'i' has already been incremented in the loop above, so here it needs to be decremented once
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
}