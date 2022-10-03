package View;

import Controller.MainController;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;


public class MainWindow extends JFrame {


    private JPanel mainPanel;
    private JTextField pasteTheLinkTextField;
    private JComboBox comboBoxTopic;
    private JTextField nameYourTranscriptTextField;
    private JButton convertButton;
    private JTextField topicsTextField;
    private JList mainListTopics;
    private String linkInput;
    private String nameInput;
    private String topics;
    private String s;

    MainController control = new MainController();


    //realiziramo GUI u kodu
    public MainWindow(String title) {

        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        //manualno dodani topics u padajucem izborniku
        comboBoxTopic.addItem("save to topic");
        comboBoxTopic.addItem("python");
        comboBoxTopic.addItem("java");
        comboBoxTopic.addItem("bash");
        comboBoxTopic.addItem("c#");

        this.pack();
        this.setVisible(true);


        //klikom na convert gumb otvara se drugi prozor sa convertanim tekstom
        //takoder osiguravamo da se prvi prozor zatvori
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == convertButton) {

                    //dohvacamo korisnikove unose u JTextFields
                    linkInput = pasteTheLinkTextField.getText();
                    nameInput = nameYourTranscriptTextField.getText();

                    //pozivamo metodu postGetTrans iz klase TransAppModel
                    //metoda trazi try/catch
                    try {
                        control.postGetTrans(nameInput, linkInput, topics);
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    //proba da li ispravno prikazuje navedene varijable
                    //System.out.println(linkInput);
                    //System.out.println(nameInput);


                    PreviewWindow preview = new PreviewWindow("Audio to text converter",s);


                }
            }
        });





        //dohvacamo korisnikov odabir teme u koju zeli spremiti svoj transkript
        comboBoxTopic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                topics = (String) cb.getSelectedItem();

            }
        });

        //prilikom klika na JListu otvaramo novi view sa odabirom tema i transkripata
        mainListTopics.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                s=(String)mainListTopics.getSelectedValue();
                PreviewWindow preview = new PreviewWindow("Audio to text converter",s);


                //dispose();
            }
        });
    }


}


