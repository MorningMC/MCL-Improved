import auth


class OfflineAuth:
    type = 'Legacy'

    def __init__(self, username, uuid='{}'):
        self.username = username
        self.uuid = uuid

    def login(self):
        auth_ = auth.AuthInfo(OfflineAuth, self.username, self.uuid)
        self.auth = auth_
        return auth_
