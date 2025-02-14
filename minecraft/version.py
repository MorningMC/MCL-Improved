import json
import zipfile
from ntpath import join
from .mcdir import MinecraftDir


def extract(file, path):
    zipf = zipfile.ZipFile(file, 'r')
    for each in zipf.namelist():
        zipf.extract(each, path)
    zipf.close()


class Version:
    def __init__(self, name: str, mcdir: MinecraftDir):
        self.name = name
        self.mcdir = mcdir
        self.dir = join(mcdir.version_dir, name)
        self.json = join(self.dir, name + '.json')
        self.jar = join(self.dir, name + '.jar')
        self.natives_path = join(self.dir, name + '-natives')
        self.classpath = '"'

        self.validjson()

    def validjson(self):
        with open(self.json, 'r') as jsonfile:
            dic = json.load(jsonfile.read())

        self.type = dic['type']
        self.indexname = dic['assetIndex']['id']
        self.mainclass = dic['mainClass']

        for lib in dic['libraries']:
            if 'classifiers' in lib['downloads']:
                for native in lib['downloads']:
                    if native == 'artifact':
                        dirct_path = self.natives_path
                        path = join(self.mcdir.libraries, lib['downloads'][native]['path'])
                        extract(path, dirct_path)

                    elif native == 'classifiers':
                        for each in lib['downloads'][native].values():
                            dirct_path = join(self.mcdir.libraries, lib['downloads'][native]['path'])
                            path = join(self.mcdir.libraries, each['path'])
                            extract(path, dirct_path)

            else:
                self.classpath += self.mcdir.libraries + lib['downloads']['artifact']['path'] + ';'

        self.classpath += self.jar + '"'

    def genargtemplate(self):
        args = ' '

        with open(self.json, 'r') as jsonfile:
            dic = json.load(jsonfile.read())

        for arg in dic['arguments']['game']:
            if isinstance(arg, str):
                args += arg + ' '

            elif isinstance(arg, dict):
                if isinstance(arg['value'], str):
                    args += arg['value'] + ' '

                elif isinstance(arg['value'], list):
                    for each in arg['value']:
                        args += each + ' '

        return args


class Profile:
    def __init__(self, name: str, version: Version, dir_: MinecraftDir):
        self.name = name
        self.version = version
        self.dir = dir_
