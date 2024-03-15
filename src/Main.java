import java.time.Duration;
import java.time.Instant;
import java.util.*;


class Score {
    private int score;
    private int diceLeft;

    public int getDiceLeft() {
        return diceLeft;
    }

    public int getScore() {
        return score;
    }

    public void setDiceLeft(int diceLeft) {
        this.diceLeft = diceLeft;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

public class Main {

    public static void main(String[] args) {
        Instant start = Instant.now();

        int numberOfSimulations = 1000;
        int numberOfDice = 5;
        List<Integer> totals = new ArrayList<>();

        for (int i = 0; i < numberOfSimulations; i++) {
            totals.add(scoreGame(numberOfDice));
        }

        totals.sort(Comparator.naturalOrder());

        printOutput(numberOfSimulations, numberOfDice, totals);

        Instant finish = Instant.now();
        System.out.println("Total simulation took " + (Duration.between(start, finish).toMillis()) + " milliseconds.");
    }

    private static List<Integer> getDiceNumbersList(int numberOfDice) {
        List<Integer> nums = new ArrayList<>();
        for (int i = 0; i < numberOfDice; i++) {
            nums.add((int) (Math.floor(Math.random() * 6) + 1));
        }
        return nums;
    }

    private static Score scoreRound(List<Integer> diceNumbers) {
        Score score = new Score();
        if (diceNumbers.contains(3)) {
            diceNumbers.removeAll(Collections.singleton(3));
            score.setScore(0);
            score.setDiceLeft(diceNumbers.size());
        } else {
            int lowestNumber = diceNumbers.stream().min(Integer::compareTo).get();
            score.setScore(lowestNumber);
            score.setDiceLeft(diceNumbers.size() - 1);
        }
        return score;
    }

    private static int scoreGame(int numberOfDice) {
        List<Integer> dice = getDiceNumbersList(numberOfDice);
        int score = 0;
        while (!dice.isEmpty()) {
            Score round = scoreRound(dice);
            score += round.getScore();
            dice = getDiceNumbersList(round.getDiceLeft());
        }
        return score;
    }

    private static Map<Integer, Integer> countTotals(List<Integer> totals) {
        Map<Integer, Integer> totalsList = new HashMap<>();
        int highestTotal = totals.stream().max(Integer::compareTo).get();
        for (int i = 0; i <= highestTotal; i++) {
            int finalI = i;
            int totalNumberOfOccurrences = (int) totals.stream().filter(c -> c.equals(finalI)).count();
            totalsList.put(i, totalNumberOfOccurrences);
        }
        return totalsList;
    }

    private static void printOutput(int numberOfSimulations, int numberOfDice, List<Integer> totals) {
        System.out.println("Number of simulations was " + numberOfSimulations + " using " + numberOfDice +" dice.");
        Map<Integer, Integer> totalsCounted = countTotals(totals);
        for (int i: totalsCounted.keySet()) {
            System.out.println("Total " + i + " occurred " + totalsCounted.get(i)+ " times.");
        }
    }

}