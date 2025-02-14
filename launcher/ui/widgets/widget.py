from pygame.surface import Surface


class Widget:

    def __init__(self, x: int, y: int, width: int, height: int):
        self.x = x
        self.y = y
        self.width = width
        self.height = height

    def to_surface(self) -> Surface:
        surface = Surface((self.width, self.height))
        return surface

    def draw(self, parent: Surface):
        parent.blit(self.to_surface(), (self.x, self.y))
