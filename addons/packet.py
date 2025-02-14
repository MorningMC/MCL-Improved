class Packet:

    def __init__(self, header, body=None):
        self.header = header
        self.body = body


class PacketManager:

    def __init__(self):
        self.packeter_list = {}

    def register(self, obj):
        p = Packeter(self)
        obj.set_packeter(p)
        self.packeter_list[obj.name] = p

    def parse(self, packet: Packet, to: str):
        try:
            p = self.packeter_list[to]
            p.receive(packet)

        except KeyError:
            pass


class Packeter:

    def __init__(self, manager: PacketManager):
        self.manager = manager
        self.pending = []

    def send(self, packet: Packet, to: str):
        self.manager.parse(packet, to)

    def receive(self, packet: Packet):
        self.pending.append(packet)

    def get(self):
        return self.pending
