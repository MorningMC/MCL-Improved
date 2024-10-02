package minecraft.morningmc.mcli.ui.window;

import javafx.scene.Parent;

/**
 * Interface for elements that can be drawn on the window.
 */
public interface Elements {
	
	/**
	 * Redraws the element based on the provided window size and modification flags.
	 *
	 * @param width     The width of the redraw area, or {@code 0} if the width has not changed, or the original width if {@code redrawAll} is {@code true}.
	 * @param height    The height of the redraw area, or {@code 0} if the height has not changed, or the original height if {@code redrawAll} is {@code true}.
	 * @param redrawAll Whether to redraw all elements, regardless of modifications.
	 * @return The redrawn element.
	 * @implNote {@code width} and {@code height} will both be their original values when {@code redrawAll} is {@code true},
	 *           so any expressions like {@code width > 0 | redrawAll} or {@code height > 0 | redrawAll} can be replaced by {@code width > 0} or {@code height > 0}
	 */
	Parent redraw(int width, int height, boolean redrawAll);
}
