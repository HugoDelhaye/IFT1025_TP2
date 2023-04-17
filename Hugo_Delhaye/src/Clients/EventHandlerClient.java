package Clients;

import java.io.IOException;

@FunctionalInterface
public interface EventHandlerClient {
    void handle(String cmd) throws IOException, ClassNotFoundException;
}
