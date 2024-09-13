package minecraft.morningmc.mcli.minecraft.client.resources;

import minecraft.morningmc.mcli.minecraft.client.resources.marker.Marker;

import java.io.File;

/**
 * Represents a Minecraft shader pack.
 *
 * @param file        The resource pack file
 * @param marker      The marker associated with the resource pack.
 */
public record ShaderPack(File file, Marker marker) {
}
