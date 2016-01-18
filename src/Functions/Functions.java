package Functions;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Random;

public class Functions {

    public static final int BIT_LENGTH_PUBLIC = 100;

    public static BigInteger[] createPublicKey() {

        BigInteger p = BigInteger.probablePrime(BIT_LENGTH_PUBLIC, new Random());
        BigInteger q;

        do {
            q = BigInteger.probablePrime(BIT_LENGTH_PUBLIC, new Random());
        } while (Objects.equals(p, q));

        BigInteger n = p.multiply(q);
        BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        BigInteger e;
        do {
            e = BigInteger.probablePrime(BIT_LENGTH_PUBLIC, new Random());
        } while (!m.gcd(e).equals(BigInteger.ONE));

        return new BigInteger[]{n, e, m};
    }

    public static BigInteger[] createPrivateKey(BigInteger n, BigInteger e, BigInteger m) {

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

        return new BigInteger[]{n,u};
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

        for (BigInteger d : decode) {
            byte[] arrayByte = d.modPow(u, n).toByteArray();
            s.append(new String(arrayByte));
        }
        System.out.print("Déchiffrement: " + s.toString() + "\n");
        return s.toString();
    }
}