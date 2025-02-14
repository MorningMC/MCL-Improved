from launcher.ui.widgets import *
from launcher.utils.config import Config


class UIRules:

    @staticmethod
    def get_map(config: Config, page=None, tab=None):
        size = config['windowsize']

        base_map = Map(0, 0, size.width, size.height)

        pages = {
            'base': [Image(0, 0, size.width, size.height, Assets('banner.png')),
                     Frame(0, 0, 210, size.height, (30, 30, 30), 96),
                     Frame(5, 67, 200, 1, (60, 60, 60)),
                     Frame(5, 199, 200, 1, (60, 60, 60))],
            'page:account': {
                'base': [PageButton(0, 69, 210, 64, 'page:launch', False, 'Launch', icon=Assets('grass_block.png')),
                         PageButton(0, 201, 210, 64, 'page:download', False, 'Download', icon=Assets('piston.png')),
                         PageButton(0, 265, 210, 64, 'page:process', False,
                                    'Process', icon=Assets('redstone_lamp.png')),
                         PageButton(0, size.height - 64, 210, 64, 'page:settings', False,
                                    'Settings', icon=Assets('command_block.png'))]
            },
            'page:launch': {
                'base': [PageButton(0, 69, 210, 64, 'page:launch', True, 'Launch', icon=Assets('grass_block.png')),
                         PageButton(0, 201, 210, 64, 'page:download', False, 'Download', icon=Assets('piston.png')),
                         PageButton(0, 265, 210, 64, 'page:process', False,
                                    'Process', icon=Assets('redstone_lamp.png')),
                         PageButton(0, size.height - 64, 210, 64, 'page:settings', False,
                                    'Settings', icon=Assets('command_block.png'))]
            },
            'page:download': {
                'base': [PageButton(0, 69, 210, 64, 'page:launch', False, 'Launch', icon=Assets('grass_block.png')),
                         PageButton(0, 201, 210, 64, 'page:download', True, 'Download', icon=Assets('piston.png')),
                         PageButton(0, 265, 210, 64, 'page:process', False,
                                    'Process', icon=Assets('redstone_lamp.png')),
                         PageButton(0, size.height - 64, 210, 64, 'page:settings', False,
                                    'Settings', icon=Assets('command_block.png'))]
            },
            'page:process': {
                'base': [PageButton(0, 69, 210, 64, 'page:launch', False, 'Launch', icon=Assets('grass_block.png')),
                         PageButton(0, 201, 210, 64, 'page:download', False, 'Download', icon=Assets('piston.png')),
                         PageButton(0, 265, 210, 64, 'page:process', True,
                                    'Process', icon=Assets('redstone_lamp.png')),
                         PageButton(0, size.height - 64, 210, 64, 'page:settings', False,
                                    'Settings', icon=Assets('command_block.png'))]
            },
            'page:settings': {
                'base': [PageButton(0, 69, 210, 64, 'page:launch', False, 'Launch', icon=Assets('grass_block.png')),
                         PageButton(0, 201, 210, 64, 'page:download', False, 'Download', icon=Assets('piston.png')),
                         PageButton(0, 265, 210, 64, 'page:process', False,
                                    'Process', icon=Assets('redstone_lamp.png')),
                         PageButton(0, size.height - 64, 210, 64, 'page:settings', True,
                                    'Settings', icon=Assets('command_block.png'))]
            }
        }

        base_map.append_widget(pages['base'])

        if page is not None:
            base_map.append_widget(pages[page]['base'])

            if tab is not None:
                base_map.append_widget(pages[page][tab])

        return base_map
