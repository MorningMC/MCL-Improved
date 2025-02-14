import addons


def startup():
    packet_manager = addons.PacketManager()
    addon_manager = addons.AddonManager(packet_manager)

    addon_manager.find_addons()
    addon_manager.load_addons()
    addon_manager.run_addons()

    addon_manager.run()


if __name__ == '__main__':
    startup()
