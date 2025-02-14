import os


class DirectoryInfo:
    install_root = os.path.abspath('')
    working_root = os.path.join(os.environ.get('APPDATA'), '.crystal_launcher')

    assets_root = os.path.join(install_root, 'assets')

    profiles_root = os.path.join(working_root, 'profiles')
    config_path = os.path.join(working_root, 'config.dat')

    @staticmethod
    def fixfiles():
        dirs = [DirectoryInfo.working_root,
                DirectoryInfo.assets_root,
                DirectoryInfo.profiles_root]

        for dir_ in dirs:
            if not os.path.exists(dir_):
                os.mkdir(dir_)
