import java.util.Scanner;

public class Day25_1 {

    static long MAGIC_VALUE = 20201227;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long publicKeyCard = scanner.nextInt();
        long publicKeyDoor = scanner.nextInt();

        CryptoDevice card = new CryptoDevice(publicKeyCard);
        CryptoDevice door = new CryptoDevice(publicKeyDoor);

        card.calculateLoopSize(7);
        door.calculateLoopSize(7);

        System.out.println(card.getSubjectNumberOf(publicKeyDoor));


    }

    static class CryptoDevice {

        long loopSize;
        long publicKey;

        CryptoDevice(long publicKey) {
            this.publicKey = publicKey;
        }

        long calculateLoopSize(long subjectNumber) {
            long currentValue = 1;
            long count = 0;
            while (currentValue != publicKey) {
                currentValue = currentValue * subjectNumber;
                currentValue = currentValue % MAGIC_VALUE;
                count++;
                System.out.println(currentValue + ": " + count);
            }
            loopSize = count;
            return count;
        }

        long getSubjectNumberOf(long value) {
            long finalVal = 1;
            for (long i = 0; i < loopSize; i++) {
                finalVal = finalVal * value;
                finalVal = finalVal % MAGIC_VALUE;

            }
            return finalVal;
        }



    }

}
