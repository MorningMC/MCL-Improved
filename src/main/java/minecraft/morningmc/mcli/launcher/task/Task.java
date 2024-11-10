package minecraft.morningmc.mcli.launcher.task;

import java.util.*;

public interface Task extends Runnable {
	
	String name();
	
	void stop();
	
	List<Task> doNext();
}
