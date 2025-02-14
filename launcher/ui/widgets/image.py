import pygame
from pygame.surface import Surface

from launcher.utils.assets import Assets
from launcher.ui.widgets.widget import Widget


class Image(Widget):

    def __init__(self, x: int, y: int, width: int, height: int, image: Assets | Surface, alpha: int = 255):
        super().__init__(x, y, width, height)

        self.image = image
        self.width = width
        self.height = height
        self.alpha = alpha

    def to_surface(self) -> Surface:
        image = (pygame.image.load(self.image.getpath()).convert_alpha()
                 if isinstance(self.image, Assets) else self.image)
        image = pygame.transform.scale(image, (self.width, self.height))
        image.set_alpha(self.alpha)
        return image
