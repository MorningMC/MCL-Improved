import pickle
from os.path import exists, join, expandvars
import locals
import auth
from launch import Launch
from minecraft.java import JavaEnvironment
from minecraft.mcopt import MinecraftOpt
from window import *
from log import Log


class Main:
    name = 'Foliage Launcher'
    version = '0.0.0.8'

    workdir = expandvars('%AppData%\\.foliage-launcher')
    tempdir = join(workdir, 'temp')
    logdir = join(workdir, 'logs')
    assetsdir = join(workdir, 'assets')

    optionsfile = join(workdir, 'options.bin')
    launchscript = join(workdir, 'launch.bat')
    iconpath = join(assetsdir, 'foliage.ico')

    def __init__(self):
        locals.validfile()

        self.log = Log()

        if exists(self.optionsfile):
            self.firstuse = False
            self.loadoptions()

        else:
            self.log(Log.warn, 'First using! Loading default options...')
            self.firstuse = True
            self.options = locals.options

    def run(self):
        theme = self.options['launcher']['theme']
        self.window = MainWindow('superhero').bindmain(self).bindlog(self.log)

        self.log.bindlabelvar(self.window.logvar)

        self.window.mainloop()

    def quit(self):
        self.log(Log.info, 'Closing Launcher...')

        self.window.close()
        self.log.closelog()
        self.saveoptions()

        raise SystemExit

    def launchhandle(self):
        try:
            pass

        except locals.LauncherException as cause:
            pass

    def get_started(self):
        theme = self.options['launcher']['theme']
        window = GetStartedWindow(theme).bindmain(self).bindlog(self.log)

        window.mainloop()

    def loadoptions(self):
        self.log(Log.info, 'Loading options...')

        with open(self.optionsfile, 'rb') as options:
            self.options = pickle.load(options)

    def saveoptions(self):
        self.log(Log.info, 'Saving options...')

        with open(self.optionsfile, 'wb') as options:
            pickle.dump(self.options, options)


if __name__ == '__main__':
    main = Main()

    try:
        #if main.firstuse:
            #main.get_started()

        #else:
            main.run()

    except SystemExit:
        main.quit()
