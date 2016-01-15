/**
 * Created by etudiant on 15/01/16.
 */
public class Main {
    public static void main(String[] args) {

        Utilisateur alice = new Utilisateur("Alice");
        Utilisateur bob = new Utilisateur("Bob");

        alice.setPublicKey(Functions.createPublicKey());
        bob.setPublicKey(Functions.createPublicKey());
    }
}


