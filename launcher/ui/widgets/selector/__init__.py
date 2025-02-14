from pygame.surface import Surface

from launcher.ui.widgets.widget import Widget
from launcher.ui.widgets.map import Map
from launcher.ui.widgets.button import Button


class Selection(Button):

    def __init__(self, x: int, y: int, width: int, height: int, token: str, selected: bool = False):
        super().__init__(x, y, width, height, token)

        self.selected = selected

    def set_selected(self, selected: bool):
        self.selected = selected


class Selector(Map):

    def append_widget(self, widget: Selection | list, layer: int = -1):

        if isinstance(widget, list):
            for w in widget:
                w.set_selected(False)
                self.widgets.append(w)

        else:
            widget.set_selected(False)

            if layer < 0:
                self.widgets.append(widget)

            else:
                self.widgets.insert(layer, widget)

        return self

    def on_click(self, x: int, y: int):
        pass

    def select(self, index: int):
        for widget in self.widgets:
            widget.set_selected(False)

        self.widgets[index].set_selected(True)
