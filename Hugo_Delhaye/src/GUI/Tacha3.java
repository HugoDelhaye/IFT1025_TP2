package GUI;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Tacha3 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Modele leModele = new Modele();
        Vue laVue = new Vue();
        new Controleur(leModele, laVue);
        Scene scene = new Scene(laVue, 500.0, 500.0);
        stage.setScene(scene);
        stage.setTitle("Inscription UdeM");
        stage.show();


    }
}
