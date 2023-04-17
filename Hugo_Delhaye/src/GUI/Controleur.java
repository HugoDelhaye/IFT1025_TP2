package GUI;

import java.io.IOException;
/**
 * La classe Controleur permet de faire intéragir une instince de la classe Vue et
 * une insctance de la classe Modele
 */
public class Controleur {
    /**
     La Vue du Controleur.
     */
    private Vue vue;
    /**
     Le Modele du Controleur.
     */
    private Modele modele;

    /**
     Construit un Controleur avec une vue, un modèle, et un comportement.

     @param laVue       La vue du Controleur.
     @param modele      Le modele de Controleur.
     */
    public Controleur(Modele modele, Vue laVue) {
        this.modele = modele;
        this.vue = laVue;
        this.vue.getBouttonCharger().setOnAction((action) -> {
            this.charger();
        });
        this.vue.getBouttonEnvoyer().setOnAction((action) -> {
            try {
                this.inscrire();
            } catch (IOException e) {
                handleAlert();
            }
        });
    }
    /**
     Affiche la liste des cours disponibles selon la session sélectionnée dans l'interface graphique.
     */
    private void charger() {
        this.modele.chercheListCours(vue.getSessions());
        this.vue.updateListCours(this.modele.getListCours());
    }
    /**
     Inscris un étudiant à un cours.

     @exception IOException     Si les entrées ou les sortie sont intérompues.
     */
    private void inscrire() throws IOException {
            this.modele.enregistement(this.vue.getInfoInscription());
            this.vue.setConfirmation();
            this.vue.getConfirmation().show();
            this.vue.resetTextField();
            }
    /**
     Affiche une fenêtre d'erreur contenant les informations non-valide concernant une inscription.
     Les types d'information non-valide sont : le prénom de l'étudiant, le nom de l'étudiant,
     le matricule de l'étudiant et l'adresse email de l'étudiant.
     */
    private void handleAlert(){
        this.vue.setAlert(this.modele.getErreurs());
        this.vue.getAlert().show();
    }

}
