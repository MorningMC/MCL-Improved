package minecraft.morningmc.mcli.launcher.networking.api.mojang;

import minecraft.morningmc.mcli.launcher.networking.Requester;
import minecraft.morningmc.mcli.launcher.networking.api.mojang.texture.Texture;
import minecraft.morningmc.mcli.utils.annotations.StaticClass;

import java.net.*;
import java.util.*;

public class MojangAPI {
	
	@StaticClass
	public static class Provider {
		
		public static URL apiStatus() {
			return Requester.newURL("https://status.mojang.com/check");
		}
		
		public static URL nameHistory(UUID uuid) {
			return Requester.newURL("https://api.mojang.com/user/profiles/%s/names".formatted(uuid.toString().replace("-", "")));
		}
		
		public static URL texture(UUID uuid, Texture.Type textureType) {
			return Requester.newURL("https://api.mojang.com/user/profile/%s/%s".formatted(uuid.toString().replace("-", ""), textureType.name().toLowerCase()));
		}
		
		public static URL userInfo() {
			return Requester.newURL("https://api.mojang.com/user");
		}
		
		public static URL blockedServers() {
			return Requester.newURL("https://sessionserver.mojang.com/blockedservers");
		}
		
		public static URL salesStatistics() {
			return Requester.newURL("https://api.mojang.com/orders/statistics");
		}
	}
}
