import os

import launcher.main
import launcher.dirinfo
import launcher.event
import launcher.event.handle
import launcher.ui
import launcher.utils.config


def startup():
    launcher.dirinfo.DirectoryInfo.fixfiles()
    os.chdir(launcher.dirinfo.DirectoryInfo.working_root)

    config = launcher.utils.config.Config.load()

    main_thread = launcher.main.Main()
    ui_thread = launcher.ui.UIMain()

    event_center = launcher.event.handle.EventCenter()
    event_center.register(main_thread, 'main')
    event_center.register(ui_thread, 'ui')

    main_thread.ui_thread = ui_thread
    main_thread.config = config

    main_thread.run()


if __name__ == '__main__':
    startup()
