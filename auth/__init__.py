from .microsoft import MicrosoftAuth
from .offline import OfflineAuth


class AuthInfo:
    def __init__(self, type_, name, uuid='{}', access_token='{}'):
        self.type = type_
        self.name = name
        self.uuid = uuid
        self.access_token = access_token

        self.cmdtype = type_.type

    def __eq__(self, other):
        if self.type != other.type:
            return False

        elif self.type == OfflineAuth:
            return self.name == other.name

        else:
            return self.uuid == other.uuid
