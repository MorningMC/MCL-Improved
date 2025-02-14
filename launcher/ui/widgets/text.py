import pygame
from pygame.surface import Surface

from launcher.utils.assets import Assets
from launcher.ui.widgets.widget import Widget


FONT = Assets('minecraft_font.otf')


class Text(Widget):

    def __init__(self, x: int, y: int, width: int, height: int, text, second_text=None,
                 align: int = 0, alpha: int = 255, xp=0, yp=0.5,
                 font: Assets = FONT, size=20, color=(255, 255, 255),
                 sfont: Assets = FONT, ssize=10, scolor=(128, 128, 128)):
        super().__init__(x, y, width, height)

        self.text = text
        self.second_text = second_text

        self.font = pygame.font.Font(font.getpath(), size)
        self.color = color

        self.sfont = pygame.font.Font(sfont.getpath(), ssize)
        self.scolor = scolor

        self.align = align
        self.alpha = alpha
        self.xp = xp
        self.yp = yp

    def to_surface(self) -> Surface:
        surface = Surface((self.width, self.height))
        surface.set_colorkey((0, 0, 0), 0)
        surface.set_alpha(self.alpha)

        if self.second_text is None:
            text_surface = self.font.render(self.text, True, self.color)

        else:
            text = self.font.render(self.text, True, self.color)
            second_text = self.sfont.render(self.second_text, True, self.scolor)

            wider = text.get_width() >= second_text.get_width()

            text_surface = pygame.Surface((text.get_width() if wider else second_text.get_height(),
                                           text.get_height() + second_text.get_height()))

            text_surface.blit(text, (0, 0) if wider else
                              (text_surface.get_width() * self.align - text.get_width() * self.align, 0))
            text_surface.blit(second_text, (0, 0) if not wider else
                              (text_surface.get_width() * self.align - second_text.get_width() * self.align,
                               text.get_height()))

        surface.blit(text_surface, (self.width * self.xp - text_surface.get_width() * self.xp,
                                    self.height * self.yp - text_surface.get_height() * self.yp))

        return surface
