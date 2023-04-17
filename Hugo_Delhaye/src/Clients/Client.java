package Clients;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import server.models.*;
import java.util.Scanner;


public class Client {
    private int PORT;
    private String IP;
    private final static String REGISTER_COMMAND = "INSCRIRE";
    private final static String LOAD_COMMAND = "CHARGER";
    private  static String EXIT_COMMAND = "EXIT";
    Socket clientSocket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Scanner scanner = new Scanner(System.in);
    private String choixSession;
    private ArrayList<EventHandlerClient> handlers = new ArrayList<>();
    private ArrayList<Course> courses;
    private String[] sessions = new String[]{"Automne", "Hiver", "Été"};


    public Client(int Port, String IP) throws IOException, ClassNotFoundException {
        this.PORT = Port;
        this.IP = IP;
        this.clientSocket = new Socket(IP, Port);
        objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        objectInputStream = new ObjectInputStream(clientSocket.getInputStream());


    }

    public void addEventHandler(EventHandlerClient h) {
        this.handlers.add(h);
    }

    public void run() throws IOException, ClassNotFoundException {
        handleEvents(LOAD_COMMAND);

        while (true) {

            try {
                System.out.print(
                                "\n- Entrez \"CHARGER\" si vous voulez consulter le liste des cours disponible\n" +
                                "- Entrez \"INSCRIRE\" si vous voulez vous inscrire à un cours\n" +
                                "- Entrez \"EXIT\" si vous voulez quitter le serveur\n" +
                                "Choix : ");

                this.clientSocket = new Socket(this.IP, this.PORT);
                objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

                listen();

                if(EXIT_COMMAND.equals("BREAK")){
                    break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void listen() throws IOException, ClassNotFoundException {
        String cmd = scanner.nextLine();
        this.handleEvents(cmd);
    }

    private void handleEvents(String cmd) throws IOException, ClassNotFoundException {

        for (EventHandlerClient h : this.handlers) {
            h.handle(cmd);
        }
    }
    public void setCourses(String cmd) throws IOException, ClassNotFoundException {
        this.choixSession = scanner.nextLine();
        objectOutputStream.writeObject(cmd+" "+choixSession);
        this.courses = (ArrayList<Course>) objectInputStream.readObject();
    }

    public void setChoixSession(){
        System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours :");
        for( int i = 0; i<sessions.length; i++){
            System.out.println(sessions[i]);
        }
        System.out.println("Choix :");
    }

    public void afficherCourse(String choixSession, ArrayList<Course> courses){
        System.out.println("Les cours offets pendant la sessions d'"+choixSession+" sont :");
        for(int i=0; i<courses.size(); i++){
            Course course = courses.get(i);
            System.out.println(course.getName()+"    "+course.getCode());
        }
    }
    public void inscrire(String cmd) throws IOException, ClassNotFoundException {
            objectOutputStream.writeObject(cmd);
            System.out.print("Veuillez saisir votre prénom : ");
            String prenom = scanner.nextLine();
            System.out.print("Veuillez saisir votre nom : ");
            String nom = scanner.nextLine();
            System.out.print("Veuillez saisir votre adresse mail : ");
            String email = scanner.nextLine();
            System.out.print("Veuillez saisir votre matricule : ");
            String matricule = scanner.nextLine();
            System.out.print("Veuillez saisir le code du cours : ");
            String code  = scanner.nextLine();
            Course course = rechercheCours(code, this.choixSession);
            objectOutputStream.writeObject(new RegistrationForm(prenom, nom, email, matricule, course));
            String serverComment = (String)objectInputStream.readObject();
            System.out.println(serverComment);


    }
    private Course rechercheCours(String code, String choixSession){
        Course course = null;
        for(int i =0; i<courses.size(); i++){
            course = courses.get(i);
            if(course.getCode().equals(code)){
                if(course.getSession().equals(choixSession)){
                    break;
                }
            }
        }
        return course;
    }

    public static String getRegisterCommand() {
        return REGISTER_COMMAND;
    }

    public static String getLoadCommand() {
        return LOAD_COMMAND;
    }

    public static String getExitCommand() {
        return EXIT_COMMAND;
    }

    public String getChoixSession() {
        return choixSession;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void exit(String cmd) throws IOException {
            this.EXIT_COMMAND = "BREAK";
            objectOutputStream.close();
            objectInputStream.close();
            clientSocket.close();
    }
}