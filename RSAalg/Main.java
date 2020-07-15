import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Входящее сообщение:");
        String message = input.nextLine();
        new RSA(message);
    }
}

class RSA {
    private long m;
    private long param;

    RSA(String message) {
        Scanner input = new Scanner(System.in);
        long p = 0, q = 0;
        while (!IsPrimeNum(p)) {
            System.out.println("Введите первое простое число:");
            p = input.nextLong();
            if (!IsPrimeNum(p)) System.out.println("Вы ввели не простое число. Попробуйте снова.");
        }
        while (!IsPrimeNum(q)) {
            System.out.println("Введите второе простое число:");
            q = input.nextLong();
            if (!IsPrimeNum(p)) System.out.println("Вы ввели не простое число. Попробуйте снова.");
        }
        m = p * q;
        param = (p - 1) * (q - 1);
        long e = getE();
        long d = getD(e);
        long[] openKey = {e, m};
        long[] secretKey = {d, m};
        System.out.println("Открытый ключ: " + Arrays.toString(openKey));
        System.out.println("Секретный ключ: " + Arrays.toString(secretKey));
        ArrayList<String> encryptMess = encr(message, openKey);
        System.out.println("Зашифрованное сообщение: ");
        for (String o : encryptMess) System.out.print(o);
        System.out.println();
        String result = decr(encryptMess, secretKey);
        System.out.println("Расшифрованное сообщение: " + result);
    }

    private ArrayList<String> encr(String message, long[] openKey) {
        ArrayList<String> EncodeMessage = new ArrayList<>();
        String code;
        for (char c : message.toCharArray()) {
            BigInteger x = BigInteger.valueOf(c);
            code = String.valueOf(FastPow(x, openKey[0], openKey[1]));
            EncodeMessage.add(code);
        }
        return EncodeMessage;
    }

    private String decr(ArrayList<String> encodemessage, long[] secretKey) {
        StringBuilder DecodeMessage = new StringBuilder();
        for (String s : encodemessage) {
            BigInteger E = BigInteger.valueOf(Long.parseLong(s));
            long c = FastPow(E, secretKey[0], secretKey[1]).longValue();
            DecodeMessage.append((char) c);
        }
        return DecodeMessage.toString();
    }

    private boolean IsPrimeNum(long n) {
        if (n < 2) return false;
        if (n == 2) return true;
        for (long i = 2; i < n; i++)
            if (n % i == 0) return false;
        return true;
    }

    private long getE() {
        long e = 0;
        for (long i = 2; i <= param; i++)
            if ((param % i == 0) && (e % i == 0)) //если имеют общие делители
            {
                while (!IsPrimeNum(e)) e = (long) (Math.random() * param);
                i = 1;
            }

        return e;
    }

    private long getD(long e) {
        long d = 0;
        while ((d * e) % param != 1) {
            d = (long) (Math.random() * m);
        }
        return d;
    }

    private BigInteger FastPow(BigInteger a, long x, long m) {
        BigInteger res = BigInteger.valueOf(1);
        StringBuilder binpow = new StringBuilder(Long.toBinaryString(x));
        binpow.reverse();
        char[] bin = binpow.toString().toCharArray();
        BigInteger[] arr = new BigInteger[bin.length];
        arr[0] = a;
        for (int i = 1; i < arr.length; i++) {
            arr[i] = arr[i - 1].multiply(arr[i - 1]);
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].mod(BigInteger.valueOf(m));
            if (bin[i] == '1') res = res.multiply(arr[i]);
        }
        res = res.mod(BigInteger.valueOf(m));
        return res;
    }
}

