package org.geysermc.mcprotocollib.protocol.example;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.AuthenticationService;
import com.github.steveice10.mc.auth.service.MojangAuthenticationService;
import com.github.steveice10.mc.auth.service.SessionService;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.geysermc.mcprotocollib.network.ProxyInfo;
import org.geysermc.mcprotocollib.network.Server;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.server.ServerAdapter;
import org.geysermc.mcprotocollib.network.event.server.ServerClosedEvent;
import org.geysermc.mcprotocollib.network.event.server.SessionAddedEvent;
import org.geysermc.mcprotocollib.network.event.server.SessionRemovedEvent;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.tcp.TcpClientSession;
import org.geysermc.mcprotocollib.network.tcp.TcpServer;
import org.geysermc.mcprotocollib.protocol.MinecraftConstants;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodec;
import org.geysermc.mcprotocollib.protocol.data.ProtocolState;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerSpawnInfo;
import org.geysermc.mcprotocollib.protocol.data.status.PlayerInfo;
import org.geysermc.mcprotocollib.protocol.data.status.ServerStatusInfo;
import org.geysermc.mcprotocollib.protocol.data.status.VersionInfo;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundLoginPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.BitSet;

public class MinecraftProtocolTest {
    private static final Logger log = LoggerFactory.getLogger(MinecraftProtocolTest.class);
    private static final boolean SPAWN_SERVER = true;
    private static final boolean VERIFY_USERS = false;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 25565;
    private static final ProxyInfo PROXY = null;
    private static final Proxy AUTH_PROXY = Proxy.NO_PROXY;
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";

    public static void main(String[] args) {
        if (SPAWN_SERVER) {
            SessionService sessionService = new SessionService();
            sessionService.setProxy(AUTH_PROXY);

            Server server = new TcpServer(HOST, PORT, MinecraftProtocol::new);
            server.setGlobalFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService);
            server.setGlobalFlag(MinecraftConstants.VERIFY_USERS_KEY, VERIFY_USERS);
            server.setGlobalFlag(MinecraftConstants.SERVER_INFO_BUILDER_KEY, session ->
                    new ServerStatusInfo(
                            new VersionInfo(MinecraftCodec.CODEC.getMinecraftVersion(), MinecraftCodec.CODEC.getProtocolVersion()),
                            new PlayerInfo(100, 0, new ArrayList<>()),
                            Component.text("Hello world!"),
                            null,
                            false
                    )
            );

            server.setGlobalFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY, session ->
                    session.send(new ClientboundLoginPacket(
                            0,
                            false,
                            new Key[]{Key.key("minecraft:world")},
                            0,
                            16,
                            16,
                            false,
                            false,
                            false,
                            new PlayerSpawnInfo(
                                    0,
                                    Key.key("minecraft:world"),
                                    100,
                                    GameMode.SURVIVAL,
                                    GameMode.SURVIVAL,
                                    false,
                                    false,
                                    null,
                                    100
                            ),
                            true
                    ))
            );

            server.setGlobalFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD, 100);
            server.addListener(new ServerAdapter() {
                @Override
                public void serverClosed(ServerClosedEvent event) {
                    log.info("Server closed.");
                }

                @Override
                public void sessionAdded(SessionAddedEvent event) {
                    event.getSession().addListener(new SessionAdapter() {
                        @Override
                        public void packetReceived(Session session, Packet packet) {
                            if (packet instanceof ServerboundChatPacket) {
                                GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
                                log.info("{}: {}", profile.getName(), ((ServerboundChatPacket) packet).getMessage());

                                Component msg = Component.text("Hello, ")
                                        .color(NamedTextColor.GREEN)
                                        .append(Component.text(profile.getName())
                                                .color(NamedTextColor.AQUA)
                                                .decorate(TextDecoration.UNDERLINED))
                                        .append(Component.text("!")
                                                .color(NamedTextColor.GREEN));

                                session.send(new ClientboundSystemChatPacket(msg, false));
                            }
                        }
                    });
                }

                @Override
                public void sessionRemoved(SessionRemovedEvent event) {
                    MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
                    if (protocol.getState() == ProtocolState.GAME) {
                        log.info("Closing server.");
                        event.getServer().close(false);
                    }
                }
            });

            server.bind();
        }

        status();
        login();
    }

    private static void status() {
        SessionService sessionService = new SessionService();
        sessionService.setProxy(AUTH_PROXY);

        MinecraftProtocol protocol = new MinecraftProtocol();
        Session client = new TcpClientSession(HOST, PORT, protocol, PROXY);
        client.setFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService);
        client.setFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY, (session, info) -> {
            log.info("Version: {}, {}", info.getVersionInfo().getVersionName(), info.getVersionInfo().getProtocolVersion());
            log.info("Player Count: {} / {}", info.getPlayerInfo().getOnlinePlayers(), info.getPlayerInfo().getMaxPlayers());
            log.info("Players: {}", Arrays.toString(info.getPlayerInfo().getPlayers().toArray()));
            log.info("Description: {}", info.getDescription());
            log.info("Icon: {}", new String(Base64.getEncoder().encode(info.getIconPng()), StandardCharsets.UTF_8));
        });

        client.setFlag(MinecraftConstants.SERVER_PING_TIME_HANDLER_KEY, (session, pingTime) ->
                log.info("Server ping took {}ms", pingTime));

        client.connect();
        while (client.isConnected()) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                log.error("Interrupted while waiting for server to disconnect.", e);
            }
        }
    }

    private static void login() {
        MinecraftProtocol protocol;
        if (VERIFY_USERS) {
            try {
                AuthenticationService authService = new MojangAuthenticationService();
                authService.setUsername(USERNAME);
                authService.setPassword(PASSWORD);
                authService.setProxy(AUTH_PROXY);
                authService.login();

                protocol = new MinecraftProtocol(authService.getSelectedProfile(), authService.getAccessToken());
                log.info("Successfully authenticated user.");
            } catch (RequestException e) {
                log.error("Failed to authenticate user.", e);
                return;
            }
        } else {
            protocol = new MinecraftProtocol(USERNAME);
        }

        SessionService sessionService = new SessionService();
        sessionService.setProxy(AUTH_PROXY);

        Session client = new TcpClientSession(HOST, PORT, protocol, PROXY);
        client.setFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService);
        client.addListener(new SessionAdapter() {
            @Override
            public void packetReceived(Session session, Packet packet) {
                if (packet instanceof ClientboundLoginPacket) {
                    session.send(new ServerboundChatPacket("Hello, this is a test of MCProtocolLib.", Instant.now().toEpochMilli(), 0L, null, 0, new BitSet()));
                } else if (packet instanceof ClientboundSystemChatPacket) {
                    Component message = ((ClientboundSystemChatPacket) packet).getContent();
                    log.info("Received Message: {}", message);
                    session.disconnect("Finished");
                }
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                log.info("Disconnected: {}", event.getReason(), event.getCause());
            }
        });

        client.connect();
    }
}
