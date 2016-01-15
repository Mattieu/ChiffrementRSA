import Functions.Functions;
import Functions.Utilisateur;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
    }

    private void run() {
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            ServerSocket server = new ServerSocket(30970);
            Socket client = server.accept();

            createKeys();

            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String line;
            while (true) {
                line = in.readLine();
                if (line.equals("STOP")) break;
                System.out.println("client send> " + line);
                out.println("hello client");
            }

            out.close();
            in.close();
            client.close();

            server.close();

        } catch(Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        new Bob();
    }
}
