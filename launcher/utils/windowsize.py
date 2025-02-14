class WindowSize:

    def __init__(self, width: int = None, height: int = None):
        self.width = None
        self.height = None
        self.fullscreen = False

        self.resize(width, height)

    def resize(self, width: int = None, height: int = None):
        self.width = width
        self.height = height
        self.fullscreen = False

        if not width and not height:
            self.width = 0
            self.height = 0
            self.fullscreen = True

    def fullscreen(self):
        self.resize()

    def to_tuple(self):
        return self.width, self.height
