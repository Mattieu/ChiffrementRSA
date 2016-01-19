import Functions.Functions;
import Functions.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;

public class Alice extends JFrame implements WindowListener, ActionListener {

    private Utilisateur utilisateur;

    protected JTextArea tReceived;
    protected JTextField tSend;
    protected JButton bSend;

    private ObjectOutputStream out = null;


    public static void main(String[] args) {
        new Alice();
    }

    public Alice() {
        run();
    }

    private void createKeys() {
        // Création de l'utilisateur Alice
        utilisateur = new Utilisateur("Alice");

        // Création de la clef publique et privée
        tReceived.setText(tReceived.getText() + " - Création des clefs publiques" + "\n");
        utilisateur.setPublicKey(Functions.createPublicKey());

        tReceived.setText(tReceived.getText() + " - Création des clefs privées" + "\n");
        utilisateur.setPrivateKey(Functions.createPrivateKey(utilisateur.getN(), utilisateur.getExposantPublic(), utilisateur.getIndicatriceEuler()));
    }

    private void run () {
        try {
            Socket s = new Socket("192.168.99.170", 30970);

            out = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());

            createInterface();

            out.writeUTF("CREATEKEY");
            out.flush();

            createKeys();

            tReceived.setText(tReceived.getText() + "\n - Envoie de la clef publique d'Alice" + "\n");

            out.writeObject(utilisateur.getExposantPublic());
            out.flush();

            out.writeObject(utilisateur.getN());
            out.flush();

            if (in.readUTF().equals("RECU")) {
                tReceived.setText(tReceived.getText() + " - Clef publique d'Alice reçu par Bob" + "\n");
            }

            tReceived.setText(tReceived.getText() + " - Envoie de la clef publique de Bob" + "\n");
            BigInteger eBob =  (BigInteger) in.readObject();
            BigInteger nBob =  (BigInteger) in.readObject();
            utilisateur.setCorrespondant(eBob, nBob);

            tReceived.setText(tReceived.getText() + " - Clef publique de Bob reçu par Alice" + "\n");

            bSend.setEnabled(true);
            tSend.setEnabled(true);

            while (true) {
                if (in.readUTF().equals("QUIT")) {
                    break;
                } else {
                    tReceived.setText(tReceived.getText() + " - Texte chiffré reçu par Bob" + "\n");
                }

                if (in.readUTF().equals("DECODE")) {
                    tReceived.setText(tReceived.getText() + " - Texte déchiffré puis rechiffré par Bob" + "\n");
                }

                BigInteger[] encode = (BigInteger[]) in.readObject();
                tReceived.setText(tReceived.getText() + " - Texte chiffré reçu par Alice" + "\n");

                String decode = Functions.decode(encode, utilisateur.getU(), utilisateur.getN());
                tReceived.setText(tReceived.getText() + " - Texte déchiffré par Alice: " + decode + "\n");

                tSend.setEnabled(true);
                bSend.setEnabled(true);
            }

            out.close();
            in.close();
            s.close();

        } catch(Exception ignored) {
        }

        System.exit(0);
    }

    protected void createInterface() {

        setTitle("Chiffrement RSA");

        // Affichage
        tReceived = new JTextArea(20, 50);
        tReceived.setEditable(false);
        JScrollPane scroll = new JScrollPane(tReceived);

        // Saisie
        tSend = new JTextField(30);
        tSend.setEnabled(false);

        // Bouton Saisie et Saisie + Listener
        bSend = new JButton("Encode");
        bSend.addActionListener(this);
        bSend.setEnabled(false);

        // Fenetre
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(tSend);
        bottomPanel.add(bSend);

        // Layout
        setLayout(new BorderLayout());
        add(BorderLayout.SOUTH, bottomPanel);
        add(BorderLayout.CENTER, scroll);

        // Listener Fenetre
        addWindowListener(this);

        // Retrecir au maximum
        pack();

        // Afficher la fenetre
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == bSend) {
            String text = tSend.getText().trim();
            if (text.length() > 0) {

                tSend.setText("");
                tSend.setEnabled(false);
                bSend.setEnabled(false);
                tReceived.setText(tReceived.getText() + "\n - Chiffrement du Texte: " + tSend.getText() + "\n");

                try {
                    out.writeUTF("OK");
                    out.flush();

                    out.writeObject(Functions.encode(text, utilisateur.getExposantPublicCorrespondant(), utilisateur.getNCorrespondant()));
                    out.flush();
                } catch (java.io.IOException e) {
                    tSend.setEnabled(true);
                    bSend.setEnabled(true);
                    tReceived.setText(tReceived.getText() + " - Erreur lors de l'encodage du mot: " + tSend.getText() + "\n");
                }
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        try {
            out.writeUTF("QUIT");
            out.flush();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {
    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {
    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {
    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {
    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {
    }
}
