import threading
import sys
import os
import importlib

from addons.packet import *


class Addon(threading.Thread):
    name = '<module_id>'
    display_name = '<module_name>'
    channel = '<channel>'
    version = '<version>'

    def __init__(self):
        super().__init__()
        self.daemon = True
        self.packeter = None

        self.init()

    def set_packeter(self, packeter: Packeter):
        self.packeter = packeter

    def init(self):
        pass


class AddonManager:
    name = '<sys>'

    def __init__(self, manager: PacketManager):
        sys.path.insert(1, './addons')

        self.packeter = None
        self.manager = manager
        self.manager.register(self)

        self.found_addons = []
        self.loaded_addons = {}

    def set_packeter(self, packeter: Packeter):
        self.packeter = packeter

    def find_addons(self):
        self.found_addons = ['main', 'ui']

        dir_list = os.listdir('./addons')
        for d in dir_list:
            if os.path.exists(os.path.join(d, '__init__.py')):
                self.found_addons.append(d)

        print(self.found_addons)

    def load_addons(self):
        for addon in self.found_addons:
            a = importlib.import_module(addon)

            if hasattr(a, 'Main'):
                if issubclass(a.Main, Addon):
                    a_inst = a.Main()

                    self.manager.register(a_inst)
                    self.loaded_addons[a.Main.name] = a_inst

    def run_addons(self):
        for addon in self.loaded_addons.values():
            addon.start()

    def run(self):
        while True:
            pass
