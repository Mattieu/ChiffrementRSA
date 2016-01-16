package Functions;

import java.math.BigInteger;

public class Utilisateur {

    private String name;

    private BigInteger n;
    private BigInteger exposantPublic;
    private BigInteger indicatriceEuler;
    private BigInteger u;

    private BigInteger nCorrespondant;
    private BigInteger exposantPublicCorrespondant;

    public Utilisateur(String name) {
        this.name = name;
    }

    public void setPublicKey(BigInteger[] publicKey) {
       n = publicKey[0];
       exposantPublic = publicKey[1];
       indicatriceEuler = publicKey[2];
    }

    public void setPrivateKey(BigInteger[] privateKey) {
        u = privateKey[1];
    }

    public void setCorrespondant(BigInteger e, BigInteger n) {
        exposantPublicCorrespondant = e;
        nCorrespondant = n;
    }


    public BigInteger getExposantPublic() {
        return exposantPublic;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getIndicatriceEuler() {
        return indicatriceEuler;
    }

    public BigInteger getNCorrespondant() {
        return nCorrespondant;
    }

    public BigInteger getExposantPublicCorrespondant() {
        return exposantPublicCorrespondant;
    }
}
