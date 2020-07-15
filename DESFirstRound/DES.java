public class DES {
    String x; // входное сообщение
    String k; // раундовый ключ
    int hex = 0x6f;

    DES(String input, String key) {
        this.x = input;
        this.k = key;
    }

    void firstRound() {
        char[] xarr;
        char[] karr;
        char[] newxarr = new char[64];
        char[] l0r0 = new char[64];
        char[] L0 = new char[32];
        char[] R0 = new char[32];
        int[] IPx = {58, 50, 42, 34, 26, 18, 10, 2,
                60, 52, 44, 36, 28, 20, 12, 4,
                62, 54, 46, 38, 30, 22, 14, 6,
                64, 56, 48, 40, 32, 24, 16, 8,
                57, 49, 41, 33, 25, 17, 9, 1,
                59, 51, 43, 35, 27, 19, 11, 3,
                61, 53, 45, 37, 29, 21, 13, 5,
                63, 55, 47, 39, 31, 23, 15, 7}; // матрица начальной перестановки IP(x)
        xarr = x.toCharArray();
        karr = k.toCharArray();
        System.out.println("Перестановка по IP(x):");
        for (int i = 0; i < 64; i++) {
            newxarr[i] = xarr[IPx[i] - 1];
            if (newxarr[i] == karr[i]) l0r0[i] = '0';
            else l0r0[i] = '1';
            System.out.print(newxarr[i]);
        }
        System.out.println("\nXOR");
        System.out.println(k + "\nResult:");
        for (char j : l0r0) System.out.print(j);
        for (int i = 0; i < 32; i++) {
            L0[i] = l0r0[i];
            R0[i] = l0r0[i + 32];
        }
        System.out.print("\nL0 = ");
        System.out.print(L0);
        System.out.print("\nR0 = ");
        System.out.print(R0);
        System.out.println();
        int[] PC1 = {57, 49, 41, 33, 25, 17, 9, 1,
                58, 50, 42, 34, 26, 18, 10, 2,
                59, 51, 43, 35, 27, 19, 11, 3,
                60, 52, 44, 36, 63, 55, 47, 39,
                31, 23, 15, 7, 62, 54, 46, 38,
                30, 22, 14, 6, 61, 53, 45, 37,
                29, 21, 13, 5, 28, 20, 12, 4}; //матрица перестановки PC-1
        char[] PC1k = new char[56];
        System.out.print("PC-1(k) = ");
        for (int i = 0; i < 56; i++) {
            PC1k[i] = karr[PC1[i] - 1];
            System.out.print(PC1k[i]);
        }
        char[] C0 = new char[28];
        char[] D0 = new char[28];
        for (int i = 0; i < 28; i++) {
            C0[i] = PC1k[i];
            D0[i] = PC1k[i + 28];
        }
        System.out.print("\nC0 = ");
        System.out.print(C0);
        System.out.print("\nD0 = ");
        System.out.print(D0);
        System.out.print("\nСдвиг влево на 1 бит:");
        char[] C1 = new char[28];
        char[] D1 = new char[28];
        for (int i = 0; i < 27; i++) {
            C1[i] = C0[i + 1];
            D1[i] = D0[i + 1];
        }
        C1[27] = C0[0];
        D1[27] = D0[0];
        System.out.print("\nC1 = ");
        System.out.print(C1);
        System.out.print("\nD1 = ");
        System.out.print(D1);
        int[] PC2 = {14, 17, 11, 24, 1, 5, 3, 28,
                15, 6, 21, 10, 23, 19, 12, 4,
                26, 8, 16, 7, 27, 20, 13, 2,
                41, 52, 31, 37, 47, 55, 30, 40,
                51, 45, 33, 48, 44, 49, 39, 56,
                34, 53, 46, 42, 50, 36, 29, 32}; // матрица перестановки PC-2
        char[] keyPC = new char[56];
        for (int i = 0; i<28; i++){
            keyPC[i] = C1[i];
            keyPC[i+28]=D1[i];
        }
        System.out.println("\nПолный ключ:");
        System.out.println(keyPC);
        // 0110001101101111011011010111000001110101011101000110010101110010
        // 0110110101100101011100100110001101100101011001000110010101110011
        char[] k1 = new char[48];
        System.out.println("Генерация подключа k1:");
        for (int i = 0; i < 48; i++) {
            k1[i] = keyPC[PC2[i] - 1];
            System.out.print(k1[i]);
        }
        int[] E = {32, 1, 2, 3, 4, 5,
                4, 5, 6, 7, 8, 9,
                8, 9, 10, 11, 12, 13,
                12, 13, 14, 15, 16, 17,
                16, 17, 18, 19, 20, 21,
                20, 21, 22, 23, 24, 25,
                24, 25, 26, 27, 28, 29,
                28, 29, 30, 31, 32, 1}; //матрица расширения
        char[] ER0 = new char[48];
        char[] S = new char[48];
        System.out.print("\nE(R0) = ");
        for (int i = 0; i < 48; i++) {
            ER0[i] = R0[E[i] - 1];
            System.out.print(ER0[i]);
            if (k1[i] == ER0[i]) S[i] = '0';
            else S[i] = '1';
        }
        System.out.println("\nXOR k1^E(R0):");
        char[][] Sarr = new char[8][6];
        int sx = 0, sy = 0;
        for (char j : S) {
            Sarr[sx][sy] = j;
            System.out.print(Sarr[sx][sy]);
            sy++;
            if (sy % 6 == 0) {
                System.out.print(' ');
                sy = 0;
                sx++;
            }
        }
        System.out.println();
        int[] Barr = new int[8];
        int check = 0;
        System.out.println("Подстановка:");
        for (char[] j : Sarr) {
            check++;
            int[] strnumbin = {j[0] - 48, j[5] - 48};
            int strnum = fromBin(strnumbin);
            System.out.print("(" + strnum + ", ");
            int[] colnumbin = {j[1] - 48, j[2] - 48, j[3] - 48, j[4] - 48};
            int colnum = fromBin(colnumbin);
            System.out.print(colnum + ")");
            int B = fB(colnum, strnum, check);
            Barr[check - 1] = B;
            System.out.println(" = " + B);
        }
        char[] Sres = new char[32];
        check = 0;
        for (int i : Barr) {
            char[] res = toBin(i);
            for (char j : res) {
                Sres[check] = j;
                check++;
            }
        }
        System.out.println("После подстановки:");
        System.out.println(Sres);
        int[] P = {16, 7, 20, 21, 29, 12, 28, 17,
                1, 15, 23, 26, 5, 18, 31, 10,
                2, 8, 24, 14, 32, 27, 3, 9,
                19, 13, 30, 6, 22, 11, 4, 25}; //матрица перестановки P
        char[] f = new char[32];
        for (int i = 0; i < 32; i++) {
            f[i] = Sres[P[i] - 1];
        }
        System.out.println("После перестановки:");
        System.out.println(f);
        char[] R1 = new char[32];
        for (int i = 0; i < 32; i++) {
            if (L0[i] == f[i]) R1[i] = '0';
            else R1[i] = '1';
        }
        System.out.println("R1 = L0^f:");
        System.out.println(R1);
        System.out.println("L1 = R0:");
        char[] L1 = R0;
        System.out.println(L1);
        System.out.println("Результат первого раунда:");
        System.out.print(L1);
        System.out.print(R1);
    }

    int fromBin(int[] bin) {
        int res = 0;
        int pow = bin.length - 1;
        for (int i : bin) {
            if (i == 1) res += Math.pow(2, pow);
            pow--;
        }
        return res;
    }

    char[] toBin(int Bi) {
        char[] bin = new char[4];
        int i = 3;
        while (Bi != 0) {
            bin[i] = (char) (Bi % 2 + 48);
            i--;
            Bi /= 2;
        }
        while (i >= 0) {
            bin[i] = '0';
            i--;
        }
        return bin;
    }

    int fB(int colnum, int strnum, int blocknum) {
        int[][][] parameters = {
                {{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                        {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                        {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                        {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13},
                },
                {{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                        {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                        {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                        {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
                },
                {{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                        {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                        {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                        {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
                },
                {{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                        {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                        {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                        {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
                },
                {{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                        {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                        {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                        {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3},
                },
                {{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                        {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                        {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                        {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
                },
                {{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                        {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                        {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                        {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
                },
                {{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                        {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                        {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                        {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
                }
        };
        int B = parameters[blocknum - 1][strnum][colnum];
        return B;
    }
}
