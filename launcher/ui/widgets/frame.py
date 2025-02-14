import pygame
from pygame.surface import Surface

from launcher.ui.widgets.widget import Widget


class Frame(Widget):

    def __init__(self, x: int, y: int, width: int, height: int, color, alpha: int = 255, outline: int = 0, cr=-1):
        super().__init__(x, y, width, height)

        self.color = color
        self.alpha = alpha
        self.outline = outline
        super().__init__(x, y, width, height)

        self.color = color
        self.outline = outline

        self.cr = cr

    def to_surface(self) -> Surface:
        surface = Surface((self.width, self.height))
        pygame.draw.rect(surface, self.color, pygame.Rect(0, 0, self.width, self.height),
                         self.outline, self.cr, self.cr, self.cr, self.cr)
        surface.set_alpha(self.alpha)
        return surface
