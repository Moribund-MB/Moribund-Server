package com.github.moribund.net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.esotericsoftware.kryonet.Server;
import com.github.moribund.net.packets.account.CreateNewPlayerPacket;
import com.github.moribund.net.packets.account.CreateNewPlayerRequestPacket;
import com.github.moribund.net.packets.account.DrawNewPlayerPacket;
import com.github.moribund.net.packets.account.ExitGamePacket;
import com.github.moribund.net.packets.combat.DeathPacket;
import com.github.moribund.net.packets.combat.ProjectileCollisionPacket;
import com.github.moribund.net.packets.data.GroundItemData;
import com.github.moribund.net.packets.data.PlayerData;
import com.github.moribund.net.packets.game.GameStatePacket;
import com.github.moribund.net.packets.graphics.*;
import com.github.moribund.net.packets.input.*;
import com.github.moribund.net.packets.items.*;
import com.github.moribund.net.packets.login.LoginPacket;
import com.github.moribund.net.packets.login.LoginResponse;
import com.github.moribund.net.packets.login.LoginResponsePacket;
import com.github.moribund.net.packets.movement.LocationPacket;
import com.github.moribund.net.packets.movement.RotationPacket;
import com.github.moribund.objects.nonplayable.ItemType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.io.IOException;

/**
 * The {@code NetworkBootstrapper} class is responsible for giving the
 * initial instructions to start the networking process and listeners.
 */
public class NetworkBootstrapper {

    /**
     * The port to access.
     */
    private static final int PORT = 43594;

    /**
     * Connects to the {@link com.esotericsoftware.kryonet.Client} using our
     * {@link Server}. This method registers the packets before starting the
     * {@link com.esotericsoftware.kryonet.Connection}.
     */
    public void connect() {
        Server server = new Server();
        server.addListener(new ServerListener());
        registerPackets(server.getKryo());

        server.start();

        try {
            server.bind(PORT, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers packets that are serialized by {@link Kryo}. Packets
     * are NOT required to implement {@link Kryo} or {@link com.esotericsoftware.kryo.KryoSerializable}.
     * @param kryo The {@link Server}'s {@link Kryo}.
     */
    private void registerPackets(Kryo kryo) {
        kryo.register(DrawNewPlayerPacket.class);
        kryo.register(CreateNewPlayerPacket.class);
        kryo.register(CreateNewPlayerRequestPacket.class);
        kryo.register(ObjectArrayList.class, new JavaSerializer());
        kryo.register(Integer.class, new JavaSerializer());
        kryo.register(KeyPressedPacket.class);
        kryo.register(KeyPressedResponsePacket.class);
        kryo.register(KeyUnpressedPacket.class);
        kryo.register(KeyUnpressedResponsePacket.class);
        kryo.register(LocationPacket.class);
        kryo.register(RotationPacket.class);
        kryo.register(GameStatePacket.class);
        kryo.register(ExitGamePacket.class);
        kryo.register(LoginPacket.class);
        kryo.register(LoginResponse.class, new JavaSerializer());
        kryo.register(LoginResponsePacket.class);
        kryo.register(ItemType.class, new JavaSerializer());
        kryo.register(GroundItemData.class, new JavaSerializer());
        kryo.register(PlayerData.class);
        kryo.register(PickupItemPacket.class);
        kryo.register(ProjectileCollisionPacket.class);
        kryo.register(ItemOnItemPacket.class);
        kryo.register(EquipItemPacket.class);
        kryo.register(UpdateAppearancePacket.class);
        kryo.register(MouseClickedPacket.class);
        kryo.register(AnimationProjectilePacket.class);
        kryo.register(TimeLeftRefreshPacket.class);
        kryo.register(UnequipItemPacket.class);
        kryo.register(LobbyTimeLeftRefreshPacket.class);
        kryo.register(DeathPacket.class);
        kryo.register(NewGroundItemPacket.class);
        kryo.register(DropItemPacket.class);
        kryo.register(VictoryRoyalePacket.class);
    }
}
