import mclilib


class Main(mclilib.Addon):
    name = 'mcli'
    display_name = 'MCL Improved'
    channel = 'dev'
    version = '0.0.0.1'

    def run(self):
        try:
            while True:
                for packet in self.packeter.get():
                    if packet.header == mclilib.QUIT:
                        raise KeyboardInterrupt

        except KeyboardInterrupt:
            pass
