import Functions.Functions;
import Functions.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
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
    }

    private void run () {
        try {
            Socket s = new Socket("localhost", 30970);

            createInterface();

            createKeys();

            tReceived.setText(tReceived.getText() + "Création des clefs publiques" + "\n");
            tReceived.setText(tReceived.getText() + "Création des clefs privées" + "\n");

            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());

            tReceived.setText(tReceived.getText() + "Envoie de la clef publique d'Alice" + "\n");
            out.writeObject(utilisateur.getN());
            out.writeObject(utilisateur.getExposantPublic());

            if (in.readUTF() == "RECU") {
                tReceived.setText(tReceived.getText() + "Clef publique d'Alice reçu par Bob" + "\n");
            }

            tReceived.setText(tReceived.getText() + "Envoie de la clef publique de Bob" + "\n");


            while (true) {
                String reponse = in.readLine();

                if (reponse.equals("QUIT")) {
                    out.println("QUIT");
                    out.flush();
                    break;
                }

                tReceived.setText(tReceived.getText() + reponse + "\n");
            }

            out.close();
            in.close();
            s.close();

        } catch(Exception e) {
            System.err.println("Client: "+e);
        }
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
                out.println(tSend.getText());
                out.flush();
                tSend.setText("");
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        out.println("QUIT");
        out.flush();
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
