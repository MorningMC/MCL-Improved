package minecraft.morningmc.mcli.minecraft.auth;

import minecraft.morningmc.mcli.utils.annotations.ObjectCollection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@ObjectCollection
public class AccountCollection {
	private static final Logger logger = LogManager.getLogger();
	
	public static AccountCollection instance = null;
	
	private final Map<UUID, Account> accounts = new HashMap<>();
}
