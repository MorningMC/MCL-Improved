import main


class JavaEnvironment:
    def __init__(self, javapath, maxmem, minmem, natives_path, classpath,
                 javaargs='-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump -Dos.name=Windows 11 -Dos.version=10.0'):
        self.javapath = javapath
        self.maxmem = maxmem
        self.minmem = minmem
        self.natives_path = natives_path
        self.classpath = classpath
        self.javaargs = javaargs

    def genjavaargs(self):
        args = '"' + self.javapath + '" '
        args += self.javaargs
        args += ' -Xmx%dM' % self.maxmem
        args += ' -Xmn%dM' % self.minmem
        args += ' -Djava.library.path=' + self.natives_path
        args += ' -Dminecraft.launcher.brand=' + main.Main.name
        args += ' -Dminecraft.launcher.version=' + main.Main.version
        args += ' -cp ' + self.classpath
        args += ' -Dlog4j.formatMsgNoLookups=true '

        return args
