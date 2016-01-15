package Functions;

import java.math.BigInteger;

public class Utilisateur {

    private String name;

    private BigInteger n;
    private BigInteger exposantPublic;
    private BigInteger indicatriceEuler;

    public Utilisateur(String name) {
        this.name = name;
    }

   public void setPublicKey(BigInteger[] publicKey) {
       n = publicKey[0];
       exposantPublic = publicKey[1];
       indicatriceEuler = publicKey[2];
   }

    public BigInteger getExposantPublic() {
        return exposantPublic;
    }

    public BigInteger getN() {
        return n;
    }
}
