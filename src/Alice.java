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
    private ObjectInputStream in = null;
    

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
        utilisateur.setPublicKey(Functions.createPublicKey());
        utilisateur.setPrivateKey(Functions.createPrivateKey(utilisateur.getN(), utilisateur.getExposantPublic(), utilisateur.getIndicatriceEuler()));
    }

    private void run () {
        try {
            Socket s = new Socket("localhost", 30970);

            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());

            createInterface();

            createKeys();

            tReceived.setText(tReceived.getText() + " - Création des clefs publiques" + "\n");
            tReceived.setText(tReceived.getText() + " - Création des clefs privées" + "\n");

            tReceived.setText(tReceived.getText() + " - Envoie de la clef publique d'Alice" + "\n");

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

                String decode = Functions.decode(encode, utilisateur.getExposantPublic(), utilisateur.getN());
                tReceived.setText(tReceived.getText() + " - Texte déchiffré par Alice: " + decode + "\n");

                bSend.setEnabled(true);
            }

            out.close();
            in.close();
            s.close();

        } catch(Exception e) {
        }

        System.exit(0);
    }

    protected void createInterface() {
        // Affichage
        tReceived = new JTextArea(10, 40);
        tReceived.setEditable(false);
        JScrollPane scroll = new JScrollPane(tReceived);

        // Saisie
        tSend = new JTextField(30);

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
            if (tSend.getText().trim().length() > 0) {
                tReceived.setText(tReceived.getText() + " - Chiffrement du Texte: " + tSend.getText() + "\n");
                try {
                    out.writeUTF("OK");
                    out.flush();

                    out.writeObject(Functions.encode(tSend.getText(), utilisateur.getExposantPublicCorrespondant(), utilisateur.getNCorrespondant()));
                    out.flush();
                } catch (java.io.IOException e) {
                    System.out.print(e.getMessage());
                    tReceived.setText(tReceived.getText() + " - Erreur lors de l'encodage du mot: " + tSend.getText() + "\n");
                }
                tSend.setEnabled(false);
                bSend.setEnabled(false);
                tSend.setText("");
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
        } catch (IOException e) {
            e.printStackTrace();
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
