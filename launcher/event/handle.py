import launcher.event


class EventCenter:
    handlelist = {}

    def register(self, obj, name: str):
        handle = EventHandle(name, self, obj)
        obj.event_handle = handle

        self.handlelist[name] = handle

    def parse(self, event: launcher.event.Event, to) -> bool:
        try:
            self.handlelist[to].receive(event)
            return True

        except KeyError:
            return False


class EventHandle:

    def __init__(self, name: str, center: EventCenter, target):
        self.name = name
        self.center: EventCenter = center
        self.target = target

        self.current_event = []

    def send(self, event: launcher.event.Event, to: str) -> bool:
        return self.center.parse(event, to)

    def receive(self, event: launcher.event.Event):
        self.current_event.append(event)

    def get(self) -> list:
        events = self.current_event
        self.current_event = []
        return events
