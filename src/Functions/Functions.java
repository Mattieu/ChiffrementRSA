package Functions;

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
}