import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner c = new Scanner(System.in);
        System.out.println("Входное сообщение:");
        String x = c.next();//"0011010011100100001100101110011000111001111001010011000111101010"; // входное сообщение
        System.out.println("Раундовый ключ:");
        String k = c.next();//"0100101101011110010011110100111101010011010011100100110001010100"; // раундовый ключ
        DES des = new DES(x, k);
        des.firstRound();
    }
}
