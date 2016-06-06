import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.thrift.transport.TTransportException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("USAGE: java Main OUTPUT_DIR");
            System.exit(1);
        }

        Path outputPath = Paths.get(args[0]);
        try {
            Files.createDirectory(outputPath);
        } catch (IOException e) {
            System.err.println("Failed to create output directory.");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            Path tokenPath = Paths.get("token.txt");
            String token = Files.readAllLines(tokenPath, StandardCharsets.UTF_8).get(0);

            EvernoteAuth evernoteAuth =
                new EvernoteAuth(EvernoteService.PRODUCTION, token);
            ClientFactory factory = new ClientFactory(evernoteAuth);
            UserStoreClient userStore = factory.createUserStoreClient();
            NoteStoreClient noteStore = factory.createNoteStoreClient();

            System.out.println("Loading notebooks and notes...");
            List<Notebook> notebooks = noteStore.listNotebooks();
            for (Notebook notebook : notebooks) {
                System.out.println("Notebook: " + notebook.getName());

                Path notebookPath = outputPath.resolve(notebook.getName());
                Files.createDirectory(notebookPath);

                NoteFilter filter = new NoteFilter();
                filter.setNotebookGuid(notebook.getGuid());
                filter.setOrder(NoteSortOrder.CREATED.getValue());
                filter.setAscending(true);

                NoteList noteList = noteStore.findNotes(filter, 0, 100000);
                List<Note> notes = noteList.getNotes();
                for (Note note : notes) {
                    String fixed_title = note.getTitle().replace(' ', '_').replace('/', '-');
                    Path notePath = notebookPath.resolve(fixed_title);
                    // Note has .getContent, but it doesn't actually do anything
                    String noteContent = noteStore.getNoteContent(note.getGuid());
                    if (noteContent != null) {
                        Files.write(notePath, noteContent.getBytes());
                        System.out.println(" * " + note.getTitle());
                    } else {
                        System.out.println(" x " + note.getTitle());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
