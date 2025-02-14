import os
import main
from minecraft.java import JavaEnvironment
from minecraft.mcopt import MinecraftOpt


class Launch:
    def __init__(self, java: JavaEnvironment, mcopt: MinecraftOpt):
        self.java = java
        self.mcopt = mcopt

    def launch(self):
        self.mcopt.profile.version.validjson()

        self.genlaunchargs(main.Main.launchscript)
        os.system(main.Main.launchscript)

    def genlaunchargs(self, file):
        with open(file, 'w') as executable:
            os.chdir(self.mcopt.mcdir.path)

            template = self.mcopt.profile.version.genargtemplate()

            cmdline = self.java.genjavaargs() + self.mcopt.genmcargs(template)
            executable.writelines(cmdline)
