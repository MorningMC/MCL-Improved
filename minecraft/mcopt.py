import os
import json
from auth import AuthInfo
from .version import Profile
from .mcdir import MinecraftDir
from window import WindowSize
import locals
from log import Log


class MinecraftOpt:
    def __init__(self, mcdir: MinecraftDir, profile: Profile | None, winsize: WindowSize, auth: AuthInfo | None, log: Log, demo=False):
        if profile is None:
            raise locals.LauncherException(log, 'Invalid profile: {}'.format(profile))

        elif auth is None:
            raise locals.LauncherException(log, 'Invalid auth: {}'.format(auth))

        self.mcdir = mcdir
        self.profile = profile
        self.winsize = winsize
        self.auth = auth
        self.demo = demo

    def genmcargs(self, template):
        template = self.profile.version.mainclass + template

        template = template.replace('${auth_player_name}', self.auth.name)
        template = template.replace('${version_name}', self.profile.name)
        template = template.replace('${game_directory}', self.profile.dir.path)
        template = template.replace('${assets_root}', self.mcdir.assets)
        template = template.replace('${assets_index_name}', self.profile.version.indexname)
        template = template.replace('${auth_uuid}', self.auth.uuid)
        template = template.replace('${auth_access_token}', self.auth.access_token)
        template = template.replace('${clientid}', self.profile.version.name)
        template = template.replace('${auth_xuid}', '{}')
        template = template.replace('${user_type}', self.auth.cmdtype)
        template = template.replace('${version_type}', self.profile.version.type)

        if self.winsize.fullscreen:
            template = template.replace('${resolution_width}', self.winsize.width)
            template = template.replace('${resolution_height}', self.winsize.height)

        else:
            template = template.replace('--width ${resolution_width} --height ${resolution_height} ', '')

        if not self.demo:
            template = template.replace('--demo ', '')

        return template
