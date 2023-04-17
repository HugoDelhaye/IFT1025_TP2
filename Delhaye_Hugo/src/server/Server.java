package server;

import java.io.File;
import server.models.*;
import java.util.Scanner;
import java.io.FileWriter;

import javafx.util.Pair;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 La classe Server implémente un server qui permet l'exécution de 2 commande : INSCRIRE un étudiant à un cours
 et CHARGER une list de cours.
 */
public class Server {
    /**
     Commande pour lancer le handler handleRegistration.
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";
    /**
     Commande pour lancer le handler handleLoadCourses.
     */
    public final static String LOAD_COMMAND = "CHARGER";
    /**
     Socket du server.
     */
    private final ServerSocket server;
    /**
     Socket du client.
     */
    private Socket client;
    /**
     Influx de données rentrantes dans la classe par les Sockets.
     */
    private ObjectInputStream objectInputStream;
    /**
     Influx de données sortantes de la classe par les Sockets.
     */
    private ObjectOutputStream objectOutputStream;
    /**
     ArrayList contenant les EventHandlers de la classe.
     */
    private final ArrayList<EventHandler> handlers;
    /**
     Créer un serveur avec les Handlers : handleLoadCourses et handleRegistration.

     @param port      Port auquel le server va se connecter.
     @exception IOException     Si les entrées ou les sortie sont intérompues.
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }
    /**
     Permet d'ajouter un EventHandler à l'ArrayList handlers.

     @param h       L'Evenethandler qui sera ajout  à l'ArrayList handlers.
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     Lance une boucle qui appelle les EventHandlers de l'ArrayList handlers.

     @param arg     Arguments String qui seront passés aux EventsHandlers.
     @param cmd     String qui permet d'appeller un EventHandlers.
     @exception ClassNotFoundException      Si le nom spécifié ne correspond à aucune classe définis.
     @exception IOException     Si les entrées ou les sortie sont intérompues.
     */
    private void alertHandlers(String cmd, String arg) throws IOException, ClassNotFoundException {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }
    /**
     Méthode qui sera appellée dans le launcher pour démarer le server.
     */
    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     Récupère la ligne de commande envoyée par le client sous forme d'une Pair de String.

     @exception ClassNotFoundException      Si le nom spécifié ne correspond à aucune classe définis.
     @exception IOException     Si les entrées ou les sortie sont intérompues.
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();

            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }
    /**
     Décompose la ligne de commande en une commande et des arguments.

     @param line     La ligne de commande.
     @return Pair       Contient la commande et les arguments.
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }
    /**
     Déconnecte le server du client.

     @exception IOException     Si les entrées ou les sortie sont intérompues.
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }
    /**
     Détermine si la méthode handleRegistration ou handleLoadCourses est appellée, selon l'argument cmd.

     @param arg     Aguments String qui seront passés aux EventsHandlers.
     @param cmd     String qui permet d'appeller un EventHandlers.
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param session la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String session) {
        try {
            Scanner scanner = new Scanner(new File("src/server/data/cours.txt"));

            ArrayList<Course> courses = new ArrayList<>();

            while (scanner.hasNextLine()) {
                // Lecture de la ligne et découpage en parties à l'aide de la virgule comme délimiteur.
                String line = scanner.nextLine();
                String[] parts = line.split("\t");
                String courseCode = parts[0];
                String courseName = parts[1];
                String courseSession = parts[2];

                if (courseSession.equals(session)) {
                    // Ajout d'un nouvel objet Course à la liste de courses.
                    courses.add(new Course(courseName, courseCode, courseSession));
                }
            }
            // Fermeture du scanner.
            scanner.close();

            // Écriture de la liste de courses dans un objet ObjectOutputStream.
            objectOutputStream.writeObject(courses);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.cc
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        try {

            Object obj = objectInputStream.readObject();

            if (obj instanceof RegistrationForm) {

                // Conversion de l'objet en une chaîne de caractères selon le format donné.
                RegistrationForm registrationForm = (RegistrationForm) obj;

                String registrationInfo = registrationForm.getCourse().getSession() + "\t" +
                        registrationForm.getCourse().getCode() + "\t" +
                        registrationForm.getMatricule() + "\t" +
                        registrationForm.getPrenom() + "\t" +
                        registrationForm.getNom() + "\t" +
                        registrationForm.getEmail();

                // Écriture de la chaîne de caractères dans un fichier.
                FileWriter writer = new FileWriter("src/server/data/inscription.txt", true);
                writer.write(registrationInfo + "\n");
                writer.close();

                // Envoi d'un message de confirmation au client.
                objectOutputStream.writeObject("Inscription enregistrée avec succès!");

            } else {
                objectOutputStream.writeObject("Erreur: objet incorrect!");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}


