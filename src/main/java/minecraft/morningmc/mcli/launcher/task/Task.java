package minecraft.morningmc.mcli.launcher.task;

import minecraft.morningmc.mcli.utils.interfaces.NamedObject;

import java.util.*;

public interface Task extends Runnable, NamedObject {
	
	void stop();
	
	List<Task> doNext();
}
