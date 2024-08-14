package minecraft.morningmc.mcli.ui.settings;

import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

public class Constants {
	/** {@link NbtLoader} for loading and saving {@link Constants} objects from/to NBT data. */
	public static final NbtLoader<Constants, CompoundTag> loader = new NbtLoader<>() {
		@Override
		public Constants load(CompoundTag tag) {
			Constants constants = new Constants();
			
			constants.edgeThickness = tag.getShort("edgeThickness").getValue();
			constants.bottomThickness = tag.getShort("bottomThickness").getValue();
			constants.iconSizeFactor = tag.getDouble("iconSizeFactor").getValue();
			constants.shadowOffset = tag.getShort("shadowOffset").getValue();
			
			return null;
		}

		@Override
		public CompoundTag save(Constants object) {
			CompoundTag tag = new CompoundTag();
			
			tag.putShort("edgeThickness", object.edgeThickness);
			tag.putShort("bottomThickness", object.bottomThickness);
			tag.putDouble("iconSizeFactor", object.iconSizeFactor);
			tag.putShort("shadowOffset", object.shadowOffset);
			
			return tag;
		}
	};
	
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
		
		constants.edgeThickness = 3;
		constants.bottomThickness = 6;
		constants.iconSizeFactor = 0.5;
		constants.shadowOffset = 2;
		
		return constants;
	}
}
