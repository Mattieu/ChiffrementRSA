package Functions;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Random;

public class Functions {

    public static String decode() throws UnsupportedEncodingException {
        StringBuilder s = new StringBuilder();

        BigInteger n = BigInteger.valueOf(5141);
        BigInteger u = BigInteger.valueOf(4279);

        BigInteger test [] = {BigInteger.valueOf(386), BigInteger.valueOf(737), BigInteger.valueOf(970), BigInteger.valueOf(204), BigInteger.valueOf(1858)};

        for (int i = 0; i < test.length; i++) {
            byte[] arrayByte = test[i].modPow(u, n).toByteArray();
            s.append(new String(arrayByte));
        }
        System.out.print("Déchiffrement: " + s.toString() + "\n");
        return s.toString();
    }


    public static final int BIT_LENGTH_PUBLIC = 100;

    public static BigInteger[] createPublicKey() {

        BigInteger p = BigInteger.probablePrime(BIT_LENGTH_PUBLIC, new Random());
        BigInteger q;

        do {
            q = BigInteger.probablePrime(BIT_LENGTH_PUBLIC, new Random());
        } while (p == q);

        BigInteger n = p.multiply(q);
        BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        BigInteger e;
        do {
            e = BigInteger.probablePrime(BIT_LENGTH_PUBLIC, new Random());
        } while (!m.gcd(e).equals(BigInteger.ONE));


        BigInteger[] resultat = {n, e, m};
        return resultat;
    }

    /*$public static BigInteger pgcd(BigInteger p, BigInteger q) {
        if (q == 0) return p;
        else return pgcd(q, p.divide(q));
    }*/

    public static BigInteger[] createPrivateKey(BigInteger n, BigInteger e, BigInteger m) {

        BigInteger e0 = e;
        BigInteger m0 = m;
        BigInteger p = BigInteger.valueOf(1);
        BigInteger q = BigInteger.valueOf(0);
        BigInteger r = BigInteger.valueOf(0);
        BigInteger s = BigInteger.valueOf(1);


        while (!m.equals(BigInteger.ZERO)) {
            BigInteger c = e.remainder(m);
            BigInteger quotient = e.divide(m);
            e = m;
            m = c;
            BigInteger nouveau_r = p.subtract(quotient.multiply(r));
            BigInteger nouveau_s = q.subtract(quotient.multiply(s));
            p = r;
            q = s;
            r = nouveau_r;
            s = nouveau_s;

        }
        BigInteger u = m0.add(p);

        BigInteger[] privateKey = {n,u};

        return privateKey;

    }

    private static byte[] convertInAscii(String s) throws UnsupportedEncodingException {
        return s.getBytes("ASCII");
    }

    public static BigInteger[] encode(String s, BigInteger e, BigInteger n) throws UnsupportedEncodingException {
        byte[] ascii = convertInAscii(s);

        System.out.print("Chiffrement du texte: " + s + "\n");
        BigInteger [] encode = new BigInteger[ascii.length];
        for (int i = 0; i < ascii.length; i++) {
            encode[i] = (BigInteger.valueOf(ascii[i]).modPow(e, n));
            System.out.print(encode[i]+" ");
        }
        System.out.print("\n");
        return encode;
    }

    public static String decode(BigInteger[] decode, BigInteger u, BigInteger n) throws UnsupportedEncodingException {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < decode.length; i++) {
            byte[] arrayByte = decode[i].modPow(u, n).toByteArray();
            s.append(new String(arrayByte));
        }
        System.out.print("Déchiffrement: " + s.toString() + "\n");
        return s.toString();
    }
}