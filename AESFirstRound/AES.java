class AES {
    private String message;
    private String key;
    private int[][] expKey = new int[4][4];
    private int[][] Sbox = {
            {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
            {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
            {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
            {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
            {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
            {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
            {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
            {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
            {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
            {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
            {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
            {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
            {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
            {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
            {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
            {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}
    };

    AES(String message, String key) {
        this.message = message;
        this.key = key;
    }

    void firstRoundKeyExp() {
        System.out.println("<- Первый раунд KeyExpansion ->\n");
        int[][] messageArr = new int[4][4];
        int[][] keyArr = new int[4][4];
        int row = 0;
        int col = 0;
        for (int i = 0; i < 32; i += 2) {
            String xmess = message.substring(i, i + 2);
            String kmess = key.substring(i, i + 2);
            messageArr[row][col] = Integer.parseInt(xmess, 16);
            keyArr[row][col] = Integer.parseInt(kmess, 16);
            row++;
            if (row == 4) {
                row = 0;
                col++;
            }
        }
        System.out.println("Сообщение:");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(Integer.toHexString(messageArr[i][j]) + " ");
            }
            System.out.println();
        }
        System.out.println("Ключ:");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(Integer.toHexString(keyArr[i][j]) + " ");
            }
            System.out.println();
        }
        int[] keyExpVec = {keyArr[1][3], keyArr[2][3], keyArr[3][3], keyArr[0][3]};
        System.out.println("Вектор:");
        for (int i : keyExpVec) System.out.print(Integer.toHexString(i) + "\n");
        int[] roundconst = {0x01, 0x00, 0x00, 0x00};
        int[] roundSboxres = new int[4];
        System.out.println("Результат подстановки Sbox:");
        for (int i = 0; i < 4; i++) {
            int rownum = keyExpVec[i] / 0x10;
            int colnum = keyExpVec[i] % 0x10;
            roundSboxres[i] = Sbox[rownum][colnum];
            System.out.println(Integer.toHexString(roundSboxres[i]));
        }
        int[] roundcol = new int[4];
        System.out.println("XOR вектора с раундовыми константами:");
        for (int i = 0; i < 4; i++) {
            roundcol[i] = roundSboxres[i] ^ roundconst[i];
            System.out.println(Integer.toHexString(roundcol[i]));
        }
        System.out.println("Результат первого раунда KeyExpansion:");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                expKey[j][i] = roundcol[j] ^ keyArr[j][i];
                roundcol[j] = expKey[j][i];
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) System.out.print(Integer.toHexString(expKey[i][j]) + " ");
            System.out.println();
        }
    }

    void firstRound() {
        System.out.println("\n<- Первый раунд алгоритма AES ->\n");
        int[][] messageArr = new int[4][4];
        int[][] keyArr = new int[4][4];
        int row = 0;
        int col = 0;
        for (int i = 0; i < 32; i += 2) {
            String xmess = message.substring(i, i + 2);
            String kmess = key.substring(i, i + 2);
            messageArr[row][col] = Integer.parseInt(xmess, 16);
            keyArr[row][col] = Integer.parseInt(kmess, 16);
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }
        System.out.println("Сообщение:");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(Integer.toHexString(messageArr[i][j]) + " ");
            }
            System.out.println();
        }
        System.out.println("Ключ:");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(Integer.toHexString(keyArr[i][j]) + " ");
            }
            System.out.println();
        }
        int[][] messxorkey = new int[4][4];
        System.out.println("XOR сообщения с ключом:");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                messxorkey[i][j] = messageArr[i][j] ^ keyArr[i][j];
                System.out.print(Integer.toHexString(messxorkey[i][j]) + " ");
            }
            System.out.println();
        }
        System.out.println("Результат SubBytes:");
        int[][] Sboxres = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int rownum = messxorkey[i][j] / 0x10;
                int colnum = messxorkey[i][j] % 0x10;
                Sboxres[i][j] = Sbox[rownum][colnum];
                System.out.print(Integer.toHexString(Sboxres[i][j]) + " ");
            }
            System.out.println();
        }
        System.out.println("Результат ShiftRows:");
        int[][] ShiftRowsRes = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4 - i; j++) {
                ShiftRowsRes[i][j] = Sboxres[i][j + i];
            }
            for (int j = 4 - i; j < 4; j++) {
                if (i < 2) ShiftRowsRes[i][j] = Sboxres[i][0];
                else if (i < 3) ShiftRowsRes[i][j] = Sboxres[i][j - i];
                else ShiftRowsRes[i][j] = Sboxres[i][j - i + 2];
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(Integer.toHexString(ShiftRowsRes[i][j]) + " ");
            }
            System.out.println();
        }
        int[][] mix = {
                {0x02, 0x03, 0x01, 0x01},
                {0x01, 0x02, 0x03, 0x01},
                {0x01, 0x01, 0x02, 0x03},
                {0x03, 0x01, 0x01, 0x02}
        };
        System.out.println("Результат MixColumns:");
        int[][] mixRes = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int[] str = new int[4];
                for (int a = 0; a < 4; a++) {
                    str[a] = GF(ShiftRowsRes[a][i], mix[j][a]);
                }
                int x = 0;
                for (int a = 0; a < 4; a++) {
                    x ^= str[a];
                }
                mixRes[j][i] = x;
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(Integer.toHexString(mixRes[i][j]) + " ");
            }
            System.out.println();
        }
    }

    private int GF(int x, int mult) {
        int res = 0;
        if (mult == 1) res = x;
        else if (mult == 2) {
            StringBuilder f = new StringBuilder(Integer.toBinaryString(x * mult));
            if (f.length() < 8) {
                while (f.length() != 8) f.insert(0, '0');
            }
            if (f.length() < 9) res = Integer.parseInt(f.toString(), 2);
            else {
                f.deleteCharAt(0);
                StringBuilder t = new StringBuilder("00010001");
                for (int i = 0; i < 8; i++) {
                    if (f.charAt(i) != t.charAt(i)) f.setCharAt(i, '1');
                    else f.setCharAt(i, '0');
                }
                res = Integer.parseInt(f.toString(), 2);
            }
        } else if (mult == 3) {
            StringBuilder f = new StringBuilder(Integer.toBinaryString(x * 2));
            StringBuilder g = new StringBuilder(Integer.toBinaryString(x));
            while (f.length() < 8) f.insert(0, '0');
            while (g.length() < 8) g.insert(0, '0');
            if (f.length() < 9) {
                for (int i = 0; i < 8; i++) {
                    if (f.charAt(i) != g.charAt(i)) f.setCharAt(i, '1');
                    else f.setCharAt(i, '0');
                }
            } else {
                StringBuilder t = new StringBuilder("00010001");
                for (int i = 0; i < 8; i++) {
                    if (f.charAt(i+1) != g.charAt(i)) f.setCharAt(i+1, '1');
                    else f.setCharAt(i+1, '0');
                }
                f.deleteCharAt(0);
                for (int i = 0; i < 8; i++) {
                    if (f.charAt(i) != t.charAt(i)) f.setCharAt(i, '1');
                    else f.setCharAt(i, '0');
                }
            }
            res = Integer.parseInt(f.toString(), 2);
        }
        return res;
    }
}
