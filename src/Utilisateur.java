import java.math.BigInteger;

/**
 * Created by etudiant on 15/01/16.
 */
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
}
