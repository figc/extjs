import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class TestWatch {

	public static void main(String[] args) throws IOException {
		
		WatchService wservice = FileSystems.getDefault().newWatchService();
		Path path = Paths.get("/tmp");
		WatchKey key = path.register(wservice, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
		System.out.println("Starting loop...");
		for (;;) {
			try {
				key = wservice.take();// poll(60, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("Found " + key);
			List<WatchEvent<?>> events = key.pollEvents();
			System.out.println("Process events for key : " + events.size());
			for (WatchEvent<?> event : events) {
				WatchEvent.Kind<?> ek = event.kind();
				if (ek == StandardWatchEventKinds.ENTRY_CREATE) {
					System.out.println("created "  + ek.name());						
				} else if (ek == StandardWatchEventKinds.ENTRY_DELETE) {
					System.out.println("Deleted " + ek.name());
				} else {
					System.out.println("CONTEXT : " + event.context());
				}
			}
			
			if (key.reset()) {
				break;
			}
		}
		wservice.close();
		System.out.println("Stopping...");
	}
}
