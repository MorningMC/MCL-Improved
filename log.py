import time
from os.path import join
import ttkbootstrap as ttk
import main
import window


class Log:
    info = 'INFO'
    warn = 'WARN'
    err = 'ERROR'

    def __init__(self):
        t = time.localtime()
        tstr = time.strftime('%Y_%m_%d_%H_%M_%S.log', t)
        path = join(main.Main.logdir, tstr)

        self.logfile = open(path, 'w')
        self.window = None
        self.var = None

        self(self.info, 'Generated Log object')

    def bindlogwindow(self, window):
        self.window = window

        self(self.info, 'Bound log window to Log object')

    def bindlabelvar(self, var: ttk.StringVar):
        self.var = var

        self(self.info, 'Bound log label variable to Log object')

    def __call__(self, logtype, log):
        t = time.localtime()
        tstr = time.strftime('%Y:%m:%d:%H:%M:%S', t)
        logstr = '[%s][%s]%s\n' % (tstr, logtype, log)

        self.logfile.write(logstr)

        if self.var:
            self.var.set(logstr)

        if self.window:
            self.window.insertlog(logstr)

    def closelog(self):
        self(self.info, 'Closing log file')

        self.logfile.close()
