package morningmc.foliage.minecraft.launch.options;

public class WindowSize {
    private boolean fullscreen;
    private int width;
    private int height;

    public WindowSize() {
        fullscreen = true;
    }

    public WindowSize(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("width < 0 or height < 0");
        }

        fullscreen = false;
        this.width = width;
        this.height = height;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
