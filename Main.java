import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.FileSystems;
import java.nio.charset.StandardCharsets;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.thrift.transport.TTransportException;
import com.evernote.edam.type.Notebook;

public class Main {
    public static void main(String[] args) {
        try {
            Path tokenPath = FileSystems.getDefault().getPath("token.txt");
            String token = Files.readAllLines(tokenPath, StandardCharsets.UTF_8).get(0);

            EvernoteAuth evernoteAuth =
                new EvernoteAuth(EvernoteService.PRODUCTION, token);
            ClientFactory factory = new ClientFactory(evernoteAuth);
            UserStoreClient userStore = factory.createUserStoreClient();
            NoteStoreClient noteStore = factory.createNoteStoreClient();

            System.out.println("Loading notebooks...");
            List<Notebook> notebooks = noteStore.listNotebooks();
            for (Notebook notebook : notebooks) {
                System.out.println(notebook.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
