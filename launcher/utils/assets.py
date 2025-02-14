import os
import launcher.dirinfo


class Assets:
    rootpath = launcher.dirinfo.DirectoryInfo.assets_root

    def __init__(self, name: str):
        self.name = name

    def getpath(self):
        return os.path.join(self.rootpath, self.name)
