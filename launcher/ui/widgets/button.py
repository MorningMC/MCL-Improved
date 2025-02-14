from pygame.surface import Surface

from launcher.utils.assets import Assets
from launcher.ui.widgets.widget import Widget
from launcher.ui.widgets.frame import Frame
from launcher.ui.widgets.image import Image
from launcher.ui.widgets.text import Text


class Button(Widget):

    def __init__(self, x: int, y: int, width: int, height: int, token: str):
        super().__init__(x, y, width, height)

        self.token = token


class PageButton(Button):

    def __init__(self, x: int, y: int, width: int, height: int, token: str, selected: bool = False,
                 text: str = '', second_text=None, icon: Assets | Surface = Assets('icon.png')):
        super().__init__(x, y, width, height, token)

        self.selected = selected

        self.text = text
        self.second_text = second_text
        self.icon = icon

    def to_surface(self) -> Surface:
        surface = Surface((self.width, self.height))
        surface.set_colorkey((0, 0, 0), 0)

        if self.selected:
            Frame(1, self.height // 2 - 15, 5, 30, (82, 165, 53), cr=2).draw(surface)

        Image(19, self.height // 2 - 15, 30, 30, self.icon).draw(surface)
        Text(71, 0, self.width - 71, self.height, self.text, self.second_text).draw(surface)

        return surface


class TabButton(Button):

    def __init__(self, x: int, y: int, width: int, height: int, token: str, selected: bool = False, text: str = ''):
        super().__init__(x, y, width, height, token)

        self.selected = selected

        self.text = text

    def to_surface(self) -> Surface:
        surface = Surface((self.width, self.height))
        surface.set_colorkey((0, 0, 0), 0)

        if self.selected:
            Frame(self.width // 2 - 15, 1, 30, 5, (82, 165, 53), cr=2).draw(surface)

        Text(0, 7, self.width, self.height - 7, self.text, xp=0.5, yp=0).draw(surface)

        return surface
