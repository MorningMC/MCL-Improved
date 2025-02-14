package morningmc.foliage.minecraft.launch.options;

public class WindowSize {
    private boolean fullscreen;
    private int width;
    private int height;

    @Deprecated
    public WindowSize() {
        this.fullscreen = true;
    }

    public WindowSize(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("width < 0 or height < 0");
        }

        this.fullscreen = false;
        this.width = width;
        this.height = height;
    }

    public static WindowSize fullscreen() {
        return new WindowSize();
    }

    public static WindowSize window(int width, int height) {
        return new WindowSize(width, height);
    }

    public boolean isFullScreen() {
        return fullscreen;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return fullscreen ? "Fullscreen" : String.valueOf(width) + 'x' + height;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof WindowSize) {
            WindowSize another = (WindowSize) obj;
            return fullscreen == another.fullscreen
                    && (fullscreen || (width == another.width && height == another.height));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return fullscreen ? 1 : 31 * width + height;
    }
}
