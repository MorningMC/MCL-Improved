import auth
from requests import post
from requests import get
import json
import webbrowser


class MicrosoftAuth:
    type = 'mca'

    def login(self):
        authserver = 'https://login.live.com/oauth20_authorize.srf?client_id=00000000402b5328&response_type=code&scope=service%3A%3Auser.auth.xboxlive.com%3A%3AMBI_SSL&redirect_uri=https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf'
        webbrowser.open(authserver)
