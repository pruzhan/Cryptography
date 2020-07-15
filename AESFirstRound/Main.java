import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner c = new Scanner(System.in);
        System.out.println("Введите сообщение:");
        String message = c.next();
        System.out.println("Введите ключ:");
        String key = c.next();
        AES aes = new AES(message, key);
        aes.firstRoundKeyExp();
        aes.firstRound();
    }
}
