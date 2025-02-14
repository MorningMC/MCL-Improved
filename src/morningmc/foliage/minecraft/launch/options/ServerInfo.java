package morningmc.foliage.minecraft.launch.options;

import java.util.Objects;

public class ServerInfo {
    private String host;
    private int port;

    public ServerInfo(String host) {
        this(host, 25565);
    }

    public ServerInfo(String host, int port) {
        Objects.requireNonNull(host);
        if (port < 0) {
            throw new IllegalArgumentException("port < 0");
        }

        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return host + ':' + port;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ServerInfo) {
            ServerInfo another = (ServerInfo) obj;
            return port == another.port
                    && host.equals(another.host);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}
