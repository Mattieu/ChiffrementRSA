package Functions;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Random;

public class Functions {

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

        BigInteger [] encode = new BigInteger[ascii.length];
        for (int i = 0; i < ascii.length; i++) {
            encode[i] = (BigInteger.valueOf(ascii[i]).modPow(e, n));
        }

        return encode;
    }
}