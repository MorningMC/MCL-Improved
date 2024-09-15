package minecraft.morningmc.mcli.minecraft.client.resources;

import minecraft.morningmc.mcli.minecraft.client.resources.marker.Marker;

import java.io.File;

/**
 * Represents a Minecraft resource pack.
 *
 * @param file        The resource pack file.
 * @param format      The format of the resource pack.
 * @param description A description of the resource pack.
 * @param marker      The marker associated with the resource pack.
 */
public record ResourcePack(File file, byte format, String description, Marker marker) {
}
