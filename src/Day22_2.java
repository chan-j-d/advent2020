import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

public class Day22_2 {

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

        Game game = new Game(player1, player2);
        int answer = game.determineWinnerAndGetScore();
        System.out.println(answer);

    }

    static class Game {

        static int globalGameCount = 0;

        Deck deck1;
        Deck deck2;
        int gameCount = globalGameCount++;
        boolean hasRecursed = false;

        List<ListPair> previousArrangements;

        Game(Deck deck1, Deck deck2) {
            this.deck1 = deck1;
            this.deck2 = deck2;
            previousArrangements = new ArrayList<>();
        }

        Game(List<Integer> deck1, List<Integer> deck2) {
            this.deck1 = new Deck(deck1);
            this.deck1 = new Deck(deck2);
            previousArrangements = new ArrayList<>();
        }

        int conductTurn() {
            System.out.println("Game: " + gameCount + "\ndeck1: " + deck1 + "\ndeck2: " + deck2);

            ListPair pairToCheck = new ListPair(deck1.deck, deck2.deck);
            if (previousArrangements.contains(pairToCheck)) {
                System.out.println(previousArrangements.contains(pairToCheck));
                hasRecursed = true;
                return 1;
            } else {
                previousArrangements.add(pairToCheck);
            }

            int player1Card = deck1.pollCard();
            int player2Card = deck2.pollCard();
            System.out.println("Game after poll: " + gameCount + "\ndeck1: " + deck1 + "\ndeck2: " + deck2);

            if (player1Card > deck1.size() || player2Card > deck2.size()) {
                if (player1Card > player2Card) {
                    deck1.addCardsAtBottom(player1Card, player2Card);
                    return 1;
                } else {
                    deck2.addCardsAtBottom(player2Card, player1Card);
                    return 2;
                }
            } else {
                List<Integer> deck1Copy = new ArrayList<>();
                LinkedList<Integer> deck1LinkedListCopy = new LinkedList<>(deck1.deck);
                for (int i = 0; i < player1Card; i++) {
                    deck1Copy.add(deck1LinkedListCopy.poll());
                }
                List<Integer> deck2Copy = new ArrayList<>();
                LinkedList<Integer> deck2LinkedListCopy = new LinkedList<>(deck2.deck);
                for (int i = 0; i < player2Card; i++) {
                    deck2Copy.add(deck2LinkedListCopy.poll());
                }
                int winner = new Game(new Deck(deck1Copy), new Deck(deck2Copy)).determineWinner();
                if (winner == 1) {
                    deck1.addCardsAtBottom(player1Card, player2Card);
                } else {
                    deck2.addCardsAtBottom(player2Card, player1Card);
                }
                return winner;
            }

        }

        int determineWinnerAndGetScore() {
            return determineWinner() == 1 ? deck1.getScore() : deck2.getScore();
        }

        int determineWinner() {
            while  (deck1.size() != 0 && deck2.size() != 0) {
                 conductTurn();
                 if (hasRecursed) {
                     return 1;
                 }
            }
            boolean isOneWin = deck2.size() == 0;
            return isOneWin ? 1 : 0;
        }

    }

    static class ListPair {
        LinkedList<Integer> linkedList1;
        LinkedList<Integer> linkedList2;

        ListPair(LinkedList<Integer> linkedList1, LinkedList<Integer> linkedList2) {
            this.linkedList1 = new LinkedList<>(linkedList1);
            this.linkedList2 = new LinkedList<>(linkedList2);
        }

        public int hashCode() {
            return Arrays.hashCode(new int[] {linkedList1.hashCode(), linkedList2.hashCode()});
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof ListPair)) {
                return false;
            } else {
                ListPair other = (ListPair) o;
                return other.linkedList1.equals(linkedList1)
                        && other.linkedList2.equals(linkedList2);
            }
        }

        public String toString() {
            return linkedList1.toString() + "\n" + linkedList2;
        }
    }

    static class Deck {
        LinkedList<Integer> deck = new LinkedList<>();

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

        public String toString() {
            return deck.toString();
        }
    }


}
