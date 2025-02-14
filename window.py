import ttkbootstrap as ttk
import main
from log import Log
import locals

themes = ['vista', 'classic', 'cyborg', 'journal', 'darkly', 'flatly', 'clam', 'alt', 'solar', 'minty', 'litera', 'united', 'xpnative', 'pulse', 'cosmo', 'lumen', 'yeti', 'superhero', 'winnative', 'sandstone', 'default']


class WindowSize:
    def __init__(self, width: int | None = None, height: int | None = None):
        self.resize(width, height)

    def resize(self, width: int | None = None, height: int | None = None):
        if not width or not height:
            self.fullscreen = True

        else:
            self.fullscreen = False

        self.width = width
        self.height = height

    def __str__(self):
        if self.fullscreen:
            return ''

        return '%dx%d' % (self.width, self.height)


class Window:
    def __init__(self, title, winsize: WindowSize, theme, master=None, transient=False):
        if master:
            self.root = ttk.Toplevel(master)

            if transient:
                self.root.transient(master)

        else:
            self.root = ttk.Window()

        self.root.title(title)
        self.setwinsize(winsize)
        self.root.iconbitmap(main.Main.iconpath)
        self.root.resizable(False, False)

        self.style = ttk.Style(theme)

        self.main = None
        self.log = None

    def bindmain(self, main):
        self.main = main
        return self

    def bindlog(self, log: Log):
        self.log = log
        return self

    def mainloop(self):
        self.root.mainloop()
        return self

    def setwinsize(self, winsize: WindowSize):
        geometry = str(winsize)
        if not geometry:
            self.root['fullscreen'] = True

        else:
            self.root.geometry(geometry)

    def getwinsize(self) -> WindowSize:
        if self.root['fullscreen']:
            return WindowSize()

        width = self.root.winfo_width()
        height = self.root.winfo_height()
        return WindowSize(width, height)

    def close(self):
        self.root.quit()


class MainWindow(Window):
    def __init__(self, theme):
        super().__init__(main.Main.name, WindowSize(1280, 1024), theme)

        self.logvar = ttk.StringVar()
        self.loglabel = ttk.Label(self.root, textvariable=self.logvar, justify='left')
        self.loglabel.pack(fill='x', side='bottom')

        self.style.configure('foliage.TNotebook', tabposition='wn')
        self.mainnote = ttk.Notebook(self.root, style='foliage.TNotebook')
        self.mainnote.pack(expand=True, fill='both')

        tabtext = ['Accounts', 'Java Edition', 'Bedrock Edition', 'Processes', 'Downloads', 'Settings']
        self.tabs = []
        for text in tabtext:
            tab = ttk.Frame(self.root)
            self.tabs.append(tab)
            self.mainnote.add(tab, text=text)

        self.mainnote.select(1)

        self.jenote = ttk.Notebook(self.tabs[1])
        self.jenote.pack(expand=True, fill='both')

        jetabtext = ['Home', 'Clients', 'Profiles']
        self.jetabs = []
        for text in jetabtext:
            tab = ttk.Frame(self.tabs[1])
            self.jetabs.append(tab)
            self.jenote.add(tab, text=text)

        self.homeimage = ttk.PhotoImage(locals.assets('home.jpg'))
        # self.homelabel = ttk.Label(self.jetabs[0], image=self.homeimage)
        # self.homelabel.pack(expand=True, fill='both')

        self.homeoptionframe = ttk.Frame(self.jetabs[0])
        self.homeoptionframe.place(anchor='s', relwidth=1, relx=0, rely=0.9)


class GetStartedWindow(Window):
    def __init__(self, theme):
        super().__init__('Get Started', WindowSize(1024, 768), theme)


class LogWindow(Window):
    def __init__(self, theme, master, autoscroll=True):
        super().__init__('Output Log', WindowSize(1024, 768), theme, master)

        self.autoscroll = autoscroll

        self.text = ttk.ScrolledText(self.root)
        self.text.pack(expand=1, fill='both')

    def insertlog(self, log: str):
        self.text.insert('end', '')
        self.text.insert('end', log)
        self.text.insert('end', '\n')

        if self.autoscroll:
            self.text.see('end')
