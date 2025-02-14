import os
import minecraft.version


class MinecraftDir:
    def __init__(self, path):
        self.path = path
        self.versions = []
        self.version_dir = os.path.join(path, 'versions')
        self.libraries = os.path.join(path, 'libraries')
        self.assets = os.path.join(path, 'assets')

        self.search()

    def search(self):
        lis = os.listdir(self.version_dir)

        for version_ in lis:
            if os.path.exists(os.path.join(self.version_dir, version_, version_ + 'json')):
                self.versions.append(minecraft.version.Version(version_, self))
