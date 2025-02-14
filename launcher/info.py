import launcher.utils.assets


class LauncherInfo:
    long_name = 'Minecraft Launcher Improved'
    name = 'MCL Improved'
    short_name = 'MCLI'

    version = (0, 4, 0, 18)

    icon = launcher.utils.assets.Assets('icon.png')

    @staticmethod
    def long_full_name():
        return '%s %s' % (LauncherInfo.long_name, LauncherInfo.long_version_string())

    @staticmethod
    def full_name():
        return '%s %s' % (LauncherInfo.name, LauncherInfo.version_string())

    @staticmethod
    def short_full_name():
        return '%s %s' % (LauncherInfo.short_name, LauncherInfo.version_string())

    @staticmethod
    def channel():
        return ['DEV', 'RELEASE'][LauncherInfo.version[0]]

    @staticmethod
    def version_name():
        return f'Version {LauncherInfo.version[1]}'

    @staticmethod
    def branch():
        return f'Branch {LauncherInfo.version[2]}'

    @staticmethod
    def build():
        return f'Build {LauncherInfo.version[3]}'

    @staticmethod
    def version_string():
        return '%d.%d.%d.%d' % LauncherInfo.version

    @staticmethod
    def long_version_string():
        return '%s %s %s %s' % (LauncherInfo.channel(), LauncherInfo.version_name(),
                                LauncherInfo.branch(), LauncherInfo.build())
