import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Day22_1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String nextLine = scanner.nextLine();
        List<Integer> listOfCards = new ArrayList<>();
        nextLine = scanner.nextLine();
        while (!nextLine.equals("")) {
            listOfCards.add(Integer.parseInt(nextLine));
            nextLine = scanner.nextLine();
        }
        Deck player1 = new Deck(listOfCards);
        nextLine = scanner.nextLine();
        List<Integer> listOfCards2 = new ArrayList<>();
        while (scanner.hasNext()) {
            nextLine = scanner.nextLine();
            listOfCards2.add(Integer.parseInt(nextLine));
        }
        Deck player2 = new Deck(listOfCards2);

        System.out.println(player1.deck + "\n" + player2.deck);
        while  (player1.size() != 0 && player2.size() != 0) {
            int player1Card = player1.pollCard();
            int player2Card = player2.pollCard();
            System.out.println(player1.deck + "\n" + player2.deck);

            if (player1Card > player2Card) {
                player1.addCardsAtBottom(player1Card, player2Card);
            } else {
                player2.addCardsAtBottom(player2Card, player1Card);
            }
        }

        boolean isOneWin = player2.size() == 0;
        int answer = isOneWin ? player1.getScore() : player2.getScore();
        System.out.println(answer);
    }

    static class Deck {
        Queue<Integer> deck = new LinkedList<>();

        Deck(List<Integer> listOfCards) {
            for (int i : listOfCards) {
                deck.add(i);
            }
        }

        void addCardsAtBottom(int winningCard, int losingCard) {
            deck.add(winningCard);
            deck.add(losingCard);
        }

        int pollCard() {
            return deck.poll();
        }

        int size() {
            return deck.size();
        }

        int getScore() {
            System.out.println(deck);
            int deckSize = size();
            int total = 0;
            for (int i = deckSize; i > 0; i--) {
                total += i * deck.poll();
            }
            return total;
        }
    }


}
