import Functions.Functions;
import Functions.Utilisateur;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

public class Bob {

    private Utilisateur utilisateur;

    public Bob() {
        run();
    }

    private void createKeys() {
        // Création de l'utilisateur Bob
        utilisateur = new Utilisateur("Bob");

        // Création de la clef publique et privée
        utilisateur.setPublicKey(Functions.createPublicKey());
        utilisateur.setPrivateKey(Functions.createPrivateKey(utilisateur.getN(), utilisateur.getExposantPublic(), utilisateur.getIndicatriceEuler()));
    }

    private void run() {
        try {
            ServerSocket server = new ServerSocket(30970);
            Socket client = server.accept();

            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());

            if (in.readUTF().equals("CREATEKEY"))
                createKeys();

            BigInteger eAlice =  (BigInteger) in.readObject();
            BigInteger nAlice =  (BigInteger) in.readObject();
            utilisateur.setCorrespondant(eAlice, nAlice);
            out.writeUTF("RECU");
            out.flush();

            out.writeObject(utilisateur.getExposantPublic());
            out.flush();

            out.writeObject(utilisateur.getN());
            out.flush();

            while (true) {
                if (in.readUTF().equals("QUIT")) {
                    out.writeUTF("QUIT");
                    out.flush();
                    break;
                }

                BigInteger[] encode = (BigInteger[]) in.readObject();

                out.writeUTF("RECU");
                out.flush();

                String decode = Functions.decode(encode, utilisateur.getU(), utilisateur.getN());

                out.writeUTF("DECODE");
                out.flush();

                out.writeObject(Functions.encode(decode, utilisateur.getExposantPublicCorrespondant(), utilisateur.getNCorrespondant()));
                out.flush();
            }

            out.close();
            in.close();
            client.close();

        } catch(Exception ignored) {
        }
    }

    public static void main(String[] args) {
        new Bob();
    }
}
