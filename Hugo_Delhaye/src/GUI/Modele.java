package GUI;


import server.models.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
La classe Modele contient la logique interne de l'application "Inscription UdeM".
 */
public class Modele {
    /**
     Une liste des cours disponible a une seesion particulière.
     */
    private ArrayList<Course> listCours = new ArrayList<>();
    /**
     Une liste des erreurs dans l'entrée des données de l'étudiant souhaitant s'inscrire.
     */
    private String erreurs;

    /**
     Construit un Modele.
     */
    public Modele(){
    }
    /**
     Retourne la valeur de la propriété erreurs.

     @return    La valeur de la propriété erreurs.
     */
    public String getErreurs(){
        return erreurs;
    }
    /**
     Retourne la valeur de la propriété listCours.

     @return    La valeur de la propriété listCours.
     */
    public ArrayList<Course> getListCours() {
        return listCours;
    }
    /**
     Définis la valeur de la propriété erreurs.

     @param erreurs     Les erreurs comisent lors de l'inscription.
     */
    private void setErreurs(String erreurs) {
        this.erreurs = erreurs;
    }
    /**
     Définis la valeur de la propriété listCours.

     @param listCours     La liste des cours proposé lors d'une session spécifiée.
     */

    private void setListCours(ArrayList<Course> listCours) {
        this.listCours = listCours;
    }

    /**
     Chercher une valeur pourdéfinir la valeur de la propriété listCours.

     @param session     session à laquelle les cours de listCours vont appartenir.
     */
    public void chercheListCours(String session) {
        try {
            Scanner scanner = new Scanner(new File("src/server/data/cours.txt"));
            ArrayList<Course> courseArrayList = new ArrayList<>();

            while (scanner.hasNextLine()) {
                // Lecture de la ligne et découpage en parties.
                String line = scanner.nextLine();
                String[] parts = line.split("   ");
                String courseCode = parts[0];
                String courseName = parts[1];
                String courseSession = parts[2];

                if (courseSession.equals(session)) {
                    // Ajout d'un nouvel objet Course à la liste de courses.
                    courseArrayList.add(new Course(courseName, courseCode, courseSession));
                }
            }
            setListCours(courseArrayList);
            // Fermeture du scanner.
            scanner.close();

        }catch (FileNotFoundException e) {
            this.listCours.add(new Course("La liste des cours est introuvable","Erreur",""));
        }
    }

    /**
     Écrit dans le fichier "inscription" les information relatives à l'étudiant et au cours auquel
     il souhaite s'inscrire.

     @param form     Informations relatives à l'inscription.
     */
    public void enregistement(RegistrationForm form) throws IOException {
        if (infoConforme(form)) {

            String registrationInfo;

            // Conversion du RegistrationForm en une chaîne de caractères selon le format donné.

            registrationInfo = form.getCourse().getSession() + "\t" +
                                        form.getCourse().getCode() + "\t" +
                                        form.getPrenom()+ "\t" +
                                        form.getNom()+ "\t" +
                                        form.getMatricule()+ "\t" +
                                        form.getEmail();

            // Écriture de la chaîne de caractères dans un fichier.
            FileWriter writer = new FileWriter("src/server/data/inscription.txt", true);
            writer.write(registrationInfo + "\n");
            writer.close();
        }else{throw new IOException();}

    }
    /**
     Vérifie si les informations sur l'étudiant ont un format convenable.

     @param form    RegistrationForm contenant les informations de l'étudiant.
     @return    Si les données entréées ont un format convenable.
     */
    private boolean infoConforme(RegistrationForm form) {
        String erreursList = "";
        boolean vérifié = true;

        if(form.getPrenom().isEmpty()){
            erreursList = erreursList + "Le champ \"Prénom\" est invalide\n";
            vérifié = false;
        }
        if(form.getNom().isEmpty()){
            erreursList = erreursList + "Le champ \"Nom\" est invalide\n";
            vérifié = false;
        }
        if(!form.getEmail().contains("@")){
            erreursList = erreursList + "Le champ \"Email\" est invalide\n";
            vérifié = false;
        }
        if(form.getMatricule().length()!= 8){
            erreursList = erreursList + "Le champ \"Matricule\" est invalide";
            vérifié = false;
        }

        setErreurs(erreursList);
        return vérifié;
    }



}
