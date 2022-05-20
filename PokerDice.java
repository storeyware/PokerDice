import java.util.ArrayList;
import java.util.HashMap;

public class PokerDice {
    private int rolls;
    private int score;
    private int rounds;
    private ArrayList<GVdie> dice;
    private HashMap<Integer, Integer> tally;

    // constants
    private final static int FIVE_OF_A_KIND = 50;
    private final static int FOUR_OF_A_KIND = 40;
    private final static int THREE_OF_A_KIND = 25;
    private final static int FULL_HOUSE = 35;
    private final static int SMALL_STRAIGHT = 30;
    private final static int LARGE_STRAIGHT = 45;
    private final static int NUM_DICE = 5;
    private final static int NUM_FACE_VALUES = 6;

    public int getNumRolls() {return rolls;}

    public int getScore() {return score;}

    public int getNumRounds() {return rounds;}

    public ArrayList<GVdie> getDice() {return dice;}

    public boolean okToRoll() {
        if (rolls > 2) {
            return false;
        }else {
            return true;
        }
    }

    public boolean gameOver() {
        if (rounds == 7) {
            return true;
        }else {
            return false;
        }
    }

    public PokerDice() {
        dice = new ArrayList<GVdie>();
        for (int i = 0; i < NUM_DICE; i++) {
            dice.add(new GVdie());
        }

        tally = new HashMap<Integer, Integer>();
        resetGame();

    }

    public void resetGame() {
        rolls = 0;
        rounds = 0;
        score = 0;

        // set all dice to blank and unselect
        // methods created in GVdie class
        for (GVdie d : dice) {
            d.setBlank();
            d.setHeld(false); // false = unselected
        }

        // set tally to zero
        for (int i = 0; i < NUM_FACE_VALUES; i++) {
            tally.put(i + 1, 0);
        }
    }

    public String diceToString() {

        String s = "";

        for (GVdie d : dice) {
            s += d.getValue();
        }

        String result = "[";
        for (int i = 0; i < s.length(); i++)

            if (i != s.length()-1) {
                result += s.charAt(i) + ",";
            }else {
                result += s.charAt(i) +"]";
            }
        return result;
    }

    public void tallyDice() {

        // set tally to zero
        for (int i = 0; i < NUM_DICE +1; i++) {
            tally.put(i+1, 0);
        }

        for (GVdie d : dice){
            tally.put(d.getValue(), tally.get(d.getValue())+1);
        }
        System.out.println("HashMap: " + tally);
    }

    public void setDice(int [] values) {

        for (int i = 0; i < values.length; i++) {
            do {
                dice.get(i).roll();
            }while(dice.get(i).getValue() != values[i]);
        }
    }

    public void rollDice () {
        for (GVdie d : dice) {
            if (!d.isHeld()) {
                d.roll();
            }
        }
        rolls += 1;
    }
    // helpers

    private void nextRound() {
        rounds++;
        rolls = 0;

        for (GVdie d : dice) {
            d.setBlank();
            d.setHeld(false); // false = unselected
        }
    }

    private boolean hasStraight(int length) {
        tallyDice();
        int count = 0;
        for(int key : tally.keySet()){
            if(tally.get(key) >= 1){
                count++;
                if(count == length){
                    return true;
                }
            }else{
                count = 0;
            }
        }
        return false;
    }

    private boolean hasStrictPair() {
        tallyDice();
        for (int key : tally.keySet()){
            if (tally.get(key) == 2){
                return true;
            }
        }
        return false;
    }

    private boolean hasMultiples(int count) {
        tallyDice();
        for (int key : tally.keySet()){
            if (tally.get(key) >= count){
                return true;
            }
        }
        return false;
    }

    public void checkThreeOfAKind() {

        tallyDice();
        if (hasMultiples(3)) {
            score+=THREE_OF_A_KIND;
        }
        nextRound();
    }

    public void checkFourOfAKind() {

        tallyDice();
        if (hasMultiples(4)) {
            score+=FOUR_OF_A_KIND;
        }
        nextRound();
    }

    public void checkFiveOfAKind() {

        tallyDice();
        if (hasMultiples(5)) {
            score+=FIVE_OF_A_KIND;
        }
        nextRound();
    }

    public void checkFullHouse() {
        tallyDice();
        if (tally.containsValue(3) && hasStrictPair() || tally.containsValue(5)) {
            score+=FULL_HOUSE;
        }
        nextRound();
    }

    public void checkSmallStraight() {
        if (hasStraight(4)) {
            score+=SMALL_STRAIGHT;
        }
        nextRound();
    }

    public void checkLargeStraight() {
        if (hasStraight(5)) {
            score+=LARGE_STRAIGHT;
        }
        nextRound();
    }

    public void checkChance() {
        int sum = 0;
        for (GVdie d : dice) {
            sum += d.getValue();
        }
        score += sum;
        nextRound();
    }
}
