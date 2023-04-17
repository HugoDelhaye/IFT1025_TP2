package Client.GUI;

import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import server.models.*;
import java.util.ArrayList;

/**
 La classe Vue définis ce qui doit être affiché dans l'application "Inscription UdeM".
 */
public class Vue extends HBox{
    // ##################### Alerte Erreur #################
    /**
     Une Alert affichée lors d'une erreur d'entrée.
     */
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    /**
     Une Alert affiché lors de la confirmation de l'inscription.
     */
    private Alert confirmation = new Alert(Alert.AlertType.INFORMATION);

    // #####################partie de gauche#################
    //##### partie du haut #####
    /**
     Label de la partie droite de Vue
     */
    private final Label labelListeCours = new Label("Liste de cours");
    /**
     TableView contenant la liste des cours proposés durant une session spécifiée.
     */
    private final TableView<Course> table = new TableView();
    /**
     TableColumnn contenant la liste des codes des cours proposés durant une session spécifiée.
     */
    private final TableColumn<Course, String> codeCol = new TableColumn<>("Code");
    /**
     TableColumnn contenant la liste des nom des cours proposés durant une session spécifiée.
     */
    private final TableColumn<Course, String> nomCol = new TableColumn<>("Cours");
    /**
     ScrollPane pour naviger dans table.
     */
    private final ScrollPane scroll = new ScrollPane();

    //#####Partie du bas#####
    /**
     Boutton utilisé pour charger une liste de cours.
     */
    private final Button bouttonCharger = new Button("charger");
    /**
     ChoiceBox pour choisir une session.
     */
    private final ChoiceBox<String> sessions = new ChoiceBox();

    //######################partie droite#####################
    // déclaré de haut en bas
    /**
     Label de la partie droite de Vue.
     */
    private final Label labelInscription = new Label("Formulaire d'inscription");
    /**
     TextField pour renseigner le prénom.
     */
    private final TextField textFieldPrenom = new TextField();
    /**
     Label de textFieldPrenom.
     */
    private final Label labelPrenom = new Label("Prénom        ");
    /**
     TextField pour renseigner le nom.
     */
    private final TextField textFieldNom = new TextField();
    /**
     Label de textFieldNom.
     */
    private final Label labelNom = new Label("Nom            ");
    /**
     TextField pour renseigner le matricule.
     */
    private final TextField textFieldMatricule = new TextField();
    /**
     Label de textFieldMatricule.
     */
    private final Label labelMatricule = new Label("Matricule     ");
    /**
     TextField pour renseigner l'Email.
     */
    private final TextField textFieldEmail = new TextField();
    /**
     Label de textFieldEmail.
     */
    private final Label labelEmail = new Label("Email            ");
    /**
     Button pour envoyer une inscription.
     */
    private final Button bouttonEnvoyer = new Button("envoyer");
    /**
     Construit une HBox composé des propriétés de Vue.
     */
    public Vue(){
        // partie haut gauche
        ////table view
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().addAll(codeCol, nomCol);
        table.resizeColumn(nomCol,30);
        scroll.setContent(table);
        ////titre
        labelListeCours.setFont(new Font(14));
        //// boxgauche
        VBox boxListCours = new VBox(labelListeCours, table, scroll);
        boxListCours.setMargin(labelListeCours, new Insets(20, 20,20, 20));
        boxListCours.setAlignment(Pos.TOP_CENTER);

        //partie bas gauche
        sessions.getItems().addAll("Automne", "Hiver", "Été");
        HBox boxCharger = new HBox(40, sessions, bouttonCharger);
        boxCharger.setAlignment(Pos.CENTER);

        // partie gauche entière
        VBox boxChargerCours = new VBox(10, boxListCours, boxCharger);


        // partie droite
        ////box pour rentrer les infos
        HBox boxPrenom = new HBox(labelPrenom, textFieldPrenom);
        HBox boxNom = new HBox(labelNom, textFieldNom);
        HBox boxMatricule = new HBox(labelMatricule, textFieldMatricule);
        HBox boxEmail = new HBox(labelEmail, textFieldEmail);
        ////titre
        labelInscription.setFont(new Font(14));
        ////box droite
        VBox boxInscription = new VBox(10, labelInscription,boxPrenom, boxNom, boxEmail, boxMatricule, bouttonEnvoyer);
        boxInscription.setMargin(labelInscription, new Insets(20, 20,20, 20));
        boxInscription.setAlignment(Pos.TOP_CENTER);

        // la vue
        this.getChildren().addAll(boxChargerCours, boxInscription);
        this.setSpacing(10);

    }
    /**
     Retourne la valeur de la propriété session.

     @return    La valeur de la propriété session.
     */
    public String getSessions() {
        return sessions.getValue();
    }
    /**
     Retourne la valeur de la propriété bouttonCharger.

     @return    La valeur de la propriété bouttonCharger.
     */

    public Button getBouttonCharger() {
        return bouttonCharger;
    }
    /**
     Retourne la valeur de la propriété bouttonEnvoyer.

     @return    La valeur de la propriété bouttonEnvoyer.
     */
    public Button getBouttonEnvoyer() {
        return bouttonEnvoyer;
    }
    /**
     Retourne la valeur du texte de la propriété textFieldPrenom.

     @return    La valeur du texte de la propriété textFieldPrenom.
     */
    public String getTextFieldPrenom() {
        return textFieldPrenom.getText();
    }
    /**
     Retourne la valeur du texte de la propriété textFieldNom.

     @return    La valeur du texte de la propriété textFieldNom.
     */
    public String getTextFieldNom() {
        return textFieldNom.getText();
    }
    /**
     Retourne la valeur du texte de la propriété textFieldMatricule.

     @return    La valeur du texte de la propriété textFieldMatricule.
     */
    public String getTextFieldMatricule() {
        return textFieldMatricule.getText();
    }
    /**
     Retourne la valeur du texte de la propriété textFieldEmail.

     @return    La valeur du texte de la propriété textFieldEmail.
     */
    public String getTextFieldEmail() {
        return textFieldEmail.getText();
    }
    /**
     Retourne la propriété alert.

     @return    La propriété alert.
     */
    public Alert getAlert() {
        return alert;
    }
    /**
     Retourne la propriété confirmation.

     @return    La propriété confirmation.
     */
    public Alert getConfirmation() {
        return confirmation;
    }
    /**
     Retourne la valeur du cours selectionné dans table.

     @return    La valeur du cours selectionné dans table.
     */
    public Course getCoursSelected(){
        ObservableList<Course> coursObs = this.table.getSelectionModel().getSelectedItems();
        Object[] cours = coursObs.toArray();
        Course leCours = (Course)cours[0];
        this.table.getSelectionModel().clearSelection();

        return leCours;
    }
    /**
     Retourne Un RegistrationForm contenant les valeurs de getTextFieldPrenom(), getTextFieldNom(),
     getTextFieldEmail(), getTextFieldMatricule() et le cours selectionné.

     @return    Un RegistrationForm contenant les valeurs de getTextFieldPrenom(), getTextFieldNom(),
     getTextFieldEmail(), getTextFieldMatricule() et le cours selectionné.
     */

    public RegistrationForm getInfoInscription(){

        ObservableList<Course> coursObs = this.table.getSelectionModel().getSelectedItems();
        Object[] cours = coursObs.toArray();
        Course leCours = (Course)cours[0];

        return new RegistrationForm(getTextFieldPrenom(), getTextFieldNom(), getTextFieldEmail(), getTextFieldMatricule(), leCours);
    }
    /**
     Créer un String félicitant l'étudiant et confirmant l'inscription.
     */

    public void setConfirmation(){
        this.confirmation.setContentText("Félicitation ! " + this.getTextFieldNom() +" "+ this.getTextFieldPrenom() +
                " est inscrit(e) avec succès pour le cours " + this.getCoursSelected().getCode());
        textFieldPrenom.setStyle("-fx-border-color : back");
        textFieldNom.setStyle("-fx-border-color : back");
        textFieldEmail.setStyle("-fx-border-color : back");
        textFieldMatricule.setStyle("-fx-border-color : back");
    }
    /**
     Remplie la propriété alert avec un String énumérant les erreurs lorsque que l'on a remplie les TextField pour s'incrire
     à un cours, encadre en rouge les TextField ou se sont produis les erreurs.

     @param erreurs   String formulant les erreurs comisent par l'étudiant.
     */
    public void setAlert(String erreurs) {

        this.alert.setContentText("Le formulaire est invalide.\n" + erreurs);

        if(erreurs.contains("Prénom")){
            textFieldPrenom.setStyle("-fx-border-color : red");
        }else{
            textFieldPrenom.setStyle("-fx-border-color : back");
        }
        if(erreurs.contains("Nom")){
            textFieldNom.setStyle("-fx-border-color : red");
        }else{
            textFieldNom.setStyle("-fx-border-color : back");
        }
        if(erreurs.contains("Email")){
            textFieldEmail.setStyle("-fx-border-color : red");
        }else{
            textFieldEmail.setStyle("-fx-border-color : back");
        }
        if(erreurs.contains("Matricule")){
            textFieldMatricule.setStyle("-fx-border-color : red");
        }else{
            textFieldPrenom.setStyle("-fx-border-color : back"); }
    }
    /**
     Refresh le contenue de table.

     @param arg   Liste de Course proposé lors d'une session spécifiée.
     */
    public void updateListCours(ArrayList arg){
        ObservableList<Course> data = FXCollections.observableArrayList(arg);
        this.table.setItems(data);
    }
    /**
     Réinitialise tous les TextField.
     */
    public void resetTextField(){
        this.textFieldPrenom.clear();
        this.textFieldNom.clear();
        this.textFieldMatricule.clear();
        this.textFieldEmail.clear();
    }
}

