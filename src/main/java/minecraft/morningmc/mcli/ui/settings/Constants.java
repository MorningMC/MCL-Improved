package minecraft.morningmc.mcli.ui.settings;

import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

public class Constants {
	/** {@link NbtLoader} for loading and saving {@link Constants} objects from/to NBT data. */
	public static final NbtLoader<Constants, CompoundTag> loader = new NbtLoader<>() {
		@Override
		public Constants load(CompoundTag tag) {
			Constants constants = new Constants();
			
			constants.titleHeight = tag.getShort("title_height").getValue();
			constants.edgeThickness = tag.getShort("edge_thickness").getValue();
			constants.bottomThickness = tag.getShort("bottom_thickness").getValue();
			constants.iconSizeFactor = tag.getDouble("icon_size_factor").getValue();
			constants.shadowOffset = tag.getShort("shadow_offset").getValue();
			
			return constants;
		}

		@Override
		public CompoundTag save(Constants object) {
			CompoundTag tag = new CompoundTag();
			
			tag.putShort("title_height", object.titleHeight);
			tag.putShort("edge_thickness", object.edgeThickness);
			tag.putShort("bottom_thickness", object.bottomThickness);
			tag.putDouble("icon_size_factor", object.iconSizeFactor);
			tag.putShort("shadow_offset", object.shadowOffset);
			
			return tag;
		}
	};
	
	// Title bar
	/** The height of the title bar in pixel. */
	public short titleHeight;
	
	// Button
	/** The thickness of the edge of a button in pixel. */
	public short edgeThickness;
	
	/** The thickness of the bottom of a button in pixel. */
	public short bottomThickness;
	
	/** The factor of the size of the icon for the button. */
	public double iconSizeFactor;
	
	// Text
	/** The offset of the shadow of a text in pixel. */
	public short shadowOffset;
	
	/**
	 * Initializes the {@link Constants} in default.
	 *
	 * @return The initialized {@link Constants} instance.
	 */
	public static Constants initDefault() {
		Constants constants = new Constants();
		
		constants.titleHeight = 48;
		constants.edgeThickness = 3;
		constants.bottomThickness = 6;
		constants.iconSizeFactor = 0.5;
		constants.shadowOffset = 2;
		
		return constants;
	}
}
