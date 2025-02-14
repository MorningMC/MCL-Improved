class UIWidget:
    FRAME = 'frame'
    BUTTON = 'button'
    TEXT = 'text'
    IMAGE = 'image'

    def __init__(self, type_: str, x: int, y: int, width: int, height: int, **kwargs):
        self.type = type_

        self.x = x
        self.y = y
        self.width = width
        self.height = height

        self.args = kwargs


class UIMap:
    ACCOLOR1 = (240, 240, 240)
    ACCOLOR2 = (80, 160, 50)
    BGCOLOR1 = (48, 48, 48)
    BGCOLOR2 = (30, 30, 30)

    FONT = 'minecraft_font.otf'

    def __init__(self, width: int = 1024, height: int = 576):
        self.screen_size = self.width, self.height = width, height

        self.widgets = []

    def append_widget(self, widget: UIWidget, height: int):
        self.widgets.insert(height, widget)
