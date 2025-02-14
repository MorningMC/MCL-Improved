from pygame.locals import *
import threading

from launcher.ui.widgets import *
import launcher.event
import launcher.event.handle
from launcher.event import Event
from launcher.info import LauncherInfo
from launcher.utils.windowsize import WindowSize


class UIMain(threading.Thread):
    event_handle: launcher.event.handle.EventHandle = None

    def __init__(self):
        super().__init__()
        pygame.init()

        self.name = 'mcli_ui'
        self.daemon = True

        self.window = None
        self.size = None
        self.map = None

    def set_map(self, uimap: Map):
        self.map = uimap

    def set_windowsize(self, size: WindowSize):
        self.size = size

    def run(self):
        self.window = pygame.display.set_mode(self.size.to_tuple())

        icon = pygame.image.load(LauncherInfo.icon.getpath())
        pygame.display.set_caption(LauncherInfo.full_name())
        pygame.display.set_icon(icon)

        self.map.draw(self.window)
        pygame.display.update()

        try:
            while True:
                for event in self.event_handle.get():
                    if event.type == Event.SET_UIMAP:
                        self.map = event.args['map']
                        self.map.draw(self.window)
                        pygame.display.update()

                for pygame_event in pygame.event.get():
                    if pygame_event.type == QUIT:
                        raise KeyboardInterrupt

                    elif pygame_event.type == KEYUP:
                        if pygame_event.key == K_ESCAPE:
                            raise KeyboardInterrupt

                    elif pygame_event.type == MOUSEBUTTONUP:
                        for w in self.map.widgets:
                            if isinstance(w, Button):
                                if w.x <= pygame_event.pos[0] <= w.x + w.width and \
                                        w.y <= pygame_event.pos[1] <= w.y + w.height:
                                    self.event_handle.send(Event(Event.BUTTON_PRESSED, token=w.token), 'main')
                                    break

        except KeyboardInterrupt:
            pygame.quit()
            self.event_handle.send(Event(Event.QUIT), 'main')
