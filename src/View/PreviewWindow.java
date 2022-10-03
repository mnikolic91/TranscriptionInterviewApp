package View;

import Controller.MainController;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

public class PreviewWindow extends JFrame {


    private JPanel mainScndPanel;
    private JComboBox comboBoxTopic;
    private JList mainListTopics;
    private JTextPane mainTextPane;
    private JScrollPane scrl;
    private String topics;
    MainController control = new MainController();


    public PreviewWindow(String title,String topic) {
        super(title);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainScndPanel);
        //manualno dodani topics u padajucem izborniku
        comboBoxTopic.addItem("choose topic");
        comboBoxTopic.addItem("python");
        comboBoxTopic.addItem("java");
        comboBoxTopic.addItem("bash");
        comboBoxTopic.addItem("c#");
        topics=topic;
        comboBoxTopic.setSelectedItem(topics);
        control.listFilesInFolder(topics, mainListTopics);


        this.pack();
        this.setVisible(true);

        //prilikom odabira topica u padajucem izborniku izlistavamo sadrzaj foldera u JListi
        comboBoxTopic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                //radimo novi JComboBox objekt
                JComboBox cb = (JComboBox) e.getSource();
                topics = (String) cb.getSelectedItem();
                //objekt klase TransAppModel koji poziva metodu listFilesInFolder sa parametrima string topics i JListe
                control.listFilesInFolder(topics, mainListTopics);

            }
        });


        mainListTopics.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                //dohvaca trenutno selektirani item u JListi
                Object selection = mainListTopics.getSelectedValue();
                //prebacujemo selection u string
                String filename = selection.toString();
                control.readTextInGUI(topics, filename, mainTextPane);

            }
        });
    }


}


