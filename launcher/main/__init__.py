from launcher.ui import *
from launcher.ui.rules import UIRules
from launcher.event.handle import EventHandle
from launcher.event import Event
from launcher.utils.config import Config


class Main:
    event_handle: EventHandle = None

    ui_thread: UIMain = None
    config: Config = None

    def run(self):
        current_page = 'page:account'
        current_tab = None

        self.ui_thread.set_windowsize(self.config['windowsize'])
        self.ui_thread.set_map(UIRules.get_map(self.config, current_page, current_tab))
        self.ui_thread.start()

        while True:
            for event in self.event_handle.get():
                if event == Event.QUIT:
                    self.quit()

                elif event == Event.WINDOW_RESIZED:
                    pass

                elif event.type == Event.BUTTON_PRESSED:
                    token = event.token
                    print(token)

                    if token.startswith('page:'):
                        current_page = token
                        current_tab = None

                        self.event_handle.send(Event(Event.SET_UIMAP,
                                                     map=UIRules.get_map(self.config, current_page, current_tab)), 'ui')

                    elif token.startswith('tab:'):
                        current_tab = token

                        self.event_handle.send(Event(Event.SET_UIMAP,
                                                     map=UIRules.get_map(self.config, current_page, current_tab)), 'ui')

    def quit(self):
        Config.save(self.config)

        raise SystemExit
