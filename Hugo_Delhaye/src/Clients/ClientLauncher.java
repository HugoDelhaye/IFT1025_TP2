package Clients;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Date;


public class ClientLauncher {
    private final static int PORT = 1337;
    private final static String IP = new String("127.0.0.1");
    public static void main(String[] args) {
        try {
            Client clt = new Client(PORT, IP);

            clt.addEventHandler((cmd) -> {
                if (cmd.equals(clt.getRegisterCommand()))
                    clt.inscrire(cmd);
            });

            clt.addEventHandler((cmd) -> {
                if (cmd.equals(clt.getExitCommand()))
                    clt.exit(cmd);
            });

            clt.addEventHandler((cmd) -> {
                if (cmd.equals(clt.getLoadCommand())){
                    clt.setChoixSession();
                    clt.setCourses(cmd);
                    clt.afficherCourse(clt.getChoixSession(), clt.getCourses());
            }});

            System.out.println("***Bienvue au portail d'inscription aux cours de l'UdeM***\n");
            clt.run();

        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}



