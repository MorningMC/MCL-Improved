from pygame.surface import Surface

from launcher.ui.widgets.widget import Widget


class Map(Widget):

    def __init__(self, x: int, y: int, width: int, height: int, *widgets):
        super().__init__(x, y, width, height)

        self.widgets = list(widgets)

    def append_widget(self, widget: Widget | list, layer: int = -1):
        if isinstance(widget, list):
            self.widgets.extend(widget)

        else:
            if layer < 0:
                self.widgets.append(widget)

            else:
                self.widgets.insert(layer, widget)

        return self

    def to_surface(self) -> Surface:
        surface = Surface((self.width, self.height))

        for widget in self.widgets:
            widget.draw(surface)

        return surface

    def duplicate(self):
        return Map(self.x, self.y, self.width, self.height, *self.widgets)
