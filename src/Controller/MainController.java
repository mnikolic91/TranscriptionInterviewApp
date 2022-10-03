package Controller;

import Model.Converter;
import com.google.gson.Gson;

import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MainController {

    Converter transcript = new Converter();


    //metoda koja salje zahtjev AssemblyAI API-ju, dohvaca i transcriptira audio u tekstualni format, te ga ispisuje u lokalni text file
    //https://www.assemblyai.com/
    //*****************************************************************************
    //*****kod adaptiran od youtubera coding with john https://www.youtube.com/watch?v=9oq7Y8n1t00&ab_channel=CodingwithJohn*****
    //*****************************************************************************

    public void postGetTrans(String name, String url, String topics) throws URISyntaxException, IOException, InterruptedException {
        //novi objekt klase InfoGetSet koja nam olaksava pristup json elementima API-ja

        //postavljamo audio URL koji zelimo transkribirati
        transcript.setAudio_url(url);

        //pozivamo importirani Gson koji nam sluzi kao posrednik za koristenje json podataka kod poziva API-ja
        //gson jar file downloadamo na linku: https://search.maven.org/artifact/com.google.code.gson/gson/2.9.1/jar
        //potrebno je dodati gson modul u novi library
        Gson gson = new Gson();
        String jsonReq = gson.toJson(transcript);

        //radimo prvi post request
        //saljemo request Assembly AI-u da zelimo zapoceti transkripciju
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript"))
                .header("Authorization", "cf5a76dce0974df09a942c2160efb204")
                .POST(HttpRequest.BodyPublishers.ofString(jsonReq))
                .build();

        //pozivamo novog klijenta
        HttpClient httpClient = HttpClient.newHttpClient();

        //prizivamo body od post requesta gdje mozemo vidjeti id requesta, status, text i slicno
        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(postResponse.body());
        transcript = gson.fromJson(postResponse.body(), Converter.class);
        System.out.println(transcript.getId());


        //sastavljamo get request
        //jako slicno kao post request, ali sada dodajemo id u nastavku linka
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
                .header("Authorization", "cf5a76dce0974df09a942c2160efb204")
                .build();

        //provjera da li prepoznaje id
        //System.out.println("https://api.assemblyai.com/v2/transcript/" + transcript.getId());

        //ulazimo u while petlju dok assembly AI ne zavrsi transkripciju audia
        //petlja se prekida kad status procesa bude zavrsen
        while (true) {
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            transcript = gson.fromJson(getResponse.body(), Converter.class);
            System.out.println(transcript.getStatus());

            if ("completed".equals(transcript.getStatus())) {
                //otvaramo buffered write gdje cemo zapisivati nas transcript
                BufferedWriter writer = new BufferedWriter(new FileWriter(transcript.getCurrentUsersHomeDir() + "\\app_topics\\" + topics + "\\" + name + ".txt"));
                writer.write(transcript.getText());
                //zatvaramo buffered writer
                writer.close();
                //prekidamo vezu sa klijentom
                break;

            } else if ("processing".equals(transcript.getStatus())) {
                Thread.sleep(1000);
            } else {
                System.err.println("Invalid input!");
                break;
            }
        }


        //provjera da li smo dohvatili tekst
        //System.out.println(transcript.getText());


    }


    //metoda koja ispisuje sve fileove u folderu
    public void listFilesInFolder(String name, JList listField) {


        //folder objekt kojem pridruzujemo putanju
        File folder = new File(transcript.getCurrentUsersHomeDir() + "\\app_topics\\" + name + "\\");
        //pozivamo DefaultListModel za JList
        DefaultListModel model = new DefaultListModel();
        //petlja koja puni model
        for (File f : folder.listFiles()) {
            model.addElement(f.getName());
        }
        //ispis modela u JListi
        listField.setModel(model);
    }

    //metoda koja koristi BufferedReader kojom ispisujemo tekst u JTextPane
    public void readTextInGUI(String topics, String filename, JTextPane name) {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(transcript.getCurrentUsersHomeDir() + "\\app_topics\\" + topics + "\\" + filename));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            name.setText(everything);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}






