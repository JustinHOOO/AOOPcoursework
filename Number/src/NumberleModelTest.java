


import org.junit.Test;

import static org.junit.Assert.*;

public class NumberleModelTest {

    @Test
    public void processInput() {
        NumberleModel model=new NumberleModel();
        model.initialize();
        assertEquals(false,model.processInput("1+2+3=6"));
    }

    @Test
    public void isGameOver() {
        NumberleModel model=new NumberleModel();
        model.initialize();
        model.processInput("1+2+3=6");
        model.processInput("1+2+3=6");
        model.processInput("1+2+3=6");
        model.processInput("1+2+3=6");
        model.processInput("1+2+3=6");
        model.processInput("1+2+3=6");
        assertEquals(true,model.isGameOver());
    }

    @Test
    public void isGameWon() {
        NumberleModel model=new NumberleModel();
        model.initialize();
        model.processInput("1+2+3=6");
        assertEquals(false,model.isGameWon());
    }


}
