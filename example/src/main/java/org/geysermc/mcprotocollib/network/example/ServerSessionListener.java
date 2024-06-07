package org.geysermc.mcprotocollib.network.example;

import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.ConnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.DisconnectingEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerSessionListener extends SessionAdapter {
    private static final Logger log = LoggerFactory.getLogger(ServerSessionListener.class);

    @Override
    public void packetReceived(Session session, Packet packet) {
        if (packet instanceof PingPacket) {
            log.info("SERVER Received: {}", ((PingPacket) packet).getPingId());
            session.send(packet);
        }
    }

    @Override
    public void packetSent(Session session, Packet packet) {
        if (packet instanceof PingPacket) {
            log.info("SERVER Sent: {}", ((PingPacket) packet).getPingId());
        }
    }

    @Override
    public void connected(ConnectedEvent event) {
        log.info("SERVER Connected");
    }

    @Override
    public void disconnecting(DisconnectingEvent event) {
        log.info("SERVER Disconnecting: {}", event.getReason());
    }

    @Override
    public void disconnected(DisconnectedEvent event) {
        log.info("SERVER Disconnected: {}", event.getReason());
    }
}
