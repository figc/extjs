import java.nio.file.Path;
import java.nio.file.Paths;

import com.den_4.inotify_java.EventQueueFull;
import com.den_4.inotify_java.Inotify;
import com.den_4.inotify_java.InotifyEvent;
import com.den_4.inotify_java.InotifyEventListener;
import com.den_4.inotify_java.enums.Event;

public class Test {

	public static void main(String[] args) throws Exception {
		Inotify i = new Inotify();
		System.out.println(i);
		System.out.println(Inotify.isLibraryLoaded());
		System.out.println("Active : " + i.isActive());
		
		
		Path path = Paths.get("/home/debian/tmp");
		path.getParent();
		
		i.addWatch(Paths.get("/tmp").toString(), Event.Create, Event.Delete, Event.Access);
		i.addListener(0, new InotifyEventListener() {
			@Override
			public void queueFull(EventQueueFull e) {
//				e.
			}
			
			@Override
			public void filesystemEventOccurred(InotifyEvent e) {
				if (e.isCreate()) {
					System.out.println("Created " + e.getContextualName());
				} else if(e.isDelete()) {
					System.out.println("Deleted " + e.getContextualName());
				} else {
					System.out.println(e);
				}
			}
		});
		
		i.isActive();
		for (;;) {
			System.out.println("waiting....");
			Thread.sleep(3000);
		}
	}
}
