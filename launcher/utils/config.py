import pickle
import os

import launcher.dirinfo
from launcher.utils.windowsize import WindowSize


class Config:
    config_path = launcher.dirinfo.DirectoryInfo.config_path

    default = {
        'windowsize': WindowSize(1024, 632),
        'auth': {
            'list': [],
            'selected': None
        }
    }

    def __init__(self):
        self.value = self.default

    def __getitem__(self, item):
        path = item.split('.')
        value = self.value

        for p in path:
            value = value[p]

        return value

    def __setitem__(self, key, value):
        path = key.split('.')
        self.value = self.value

        for p in path[:-1]:
            self.value = self.value[p]

        self.value[path[-1]] = value

    @staticmethod
    def load():
        config = Config()

        '''
        if os.path.exists(Config.config_path):
            try:
                with open(Config.config_path, 'rb') as f:
                    config.value = pickle.load(f)

            except MemoryError:
                return Config()
        '''

        return config

    @staticmethod
    def save(config):
        with open(Config.config_path, 'wb') as f:
            pickle.dump(config.value, f)
