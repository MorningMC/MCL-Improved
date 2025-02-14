class Event:
    QUIT = -1
    WINDOW_RESIZED = 0
    BUTTON_PRESSED = 8
    SET_UIMAP = 256

    def __init__(self, type_, **kwargs):
        self.type = type_
        self.args = kwargs

    def __eq__(self, other):
        return self.type == other

    def __getattr__(self, item):
        return self.args[item]
