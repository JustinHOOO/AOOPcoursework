// NumberleModel.java
import java.util.Random;
import java.util.Stack;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class NumberleModel extends Observable implements INumberleModel {
    private String targetNumber;
    private StringBuilder currentGuess;
    private int remainingAttempts;
    private boolean gameWon;

    @Override
    public void initialize() {
        Random rand = new Random();
        targetNumber = Integer.toString(rand.nextInt(10000000));
        currentGuess = getFormula();
        remainingAttempts = MAX_ATTEMPTS;
        gameWon = false;
        setChanged();
        notifyObservers();
    }

    @Override
    public boolean processInput(String input) {
    	if(input.equals(currentGuess.toString())) {
    		gameWon = true;
			return true;
    	}else {
    		remainingAttempts--;
            setChanged();
            notifyObservers();
            return false;
    	}
    }

    @Override
    public boolean isGameOver() {
        return remainingAttempts <= 0 || gameWon;
    }

    @Override
    public boolean isGameWon() {
        return gameWon;
    }

    @Override
    public String getTargetNumber() {
        return targetNumber;
    }

    @Override
    public StringBuilder getCurrentGuess() {
        return currentGuess;
    }

    @Override
    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    @Override
    public void startNewGame() {
        initialize();
    }
    //Reading files to randomly obtain equations
    public StringBuilder getFormula() {
    	List<String> formulaList = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("src/equations.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	formulaList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Random random=new Random();
        String formula=formulaList.get(random.nextInt(formulaList.size()));
        System.out.println(formula);
    	return new StringBuilder(formula);
    }
}
