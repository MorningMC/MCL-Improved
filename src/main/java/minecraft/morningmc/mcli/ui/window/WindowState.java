package minecraft.morningmc.mcli.ui.window;

/**
 * Represents the state of a window.
 * This class holds information about the window's dimensions, maximized state, iconified state, and fullscreen state.
 */
public class WindowState {
	public int width;
	public int height;
	public boolean maximized;
	public boolean iconified;
	public boolean fullscreen;
	
	/**
	 * Constructs a new {@link WindowState} instance with the specified width and height.
	 * The window is not maximized, iconified, or in fullscreen mode by default.
	 *
	 * @param width  The width of the window in pixel.
	 * @param height The height of the window in pixel.
	 */
	public WindowState(int width, int height) {
		this(width, height, false, false, false);
	}
	
	/**
	 * Constructs a new {@link WindowState} instance with the specified parameters.
	 *
	 * @param width      The width of the window in pixel.
	 * @param height     The height of the window in pixel.
	 * @param maximized  Whether the window is maximized or not.
	 * @param iconified  Whether the window is iconified or not.
	 * @param fullscreen Whether the window is in fullscreen mode or not.
	 */
	public WindowState(int width, int height, boolean maximized, boolean iconified, boolean fullscreen) {
		this.width = width;
		this.height = height;
		this.maximized = maximized;
		this.iconified = iconified;
		this.fullscreen = fullscreen;
	}
}
