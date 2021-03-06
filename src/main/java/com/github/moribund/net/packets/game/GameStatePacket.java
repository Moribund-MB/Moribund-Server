package com.github.moribund.net.packets.game;

import com.github.moribund.net.packets.OutgoingPacket;
import com.github.moribund.net.packets.data.PlayerData;
import it.unimi.dsi.fastutil.objects.ObjectList;
import lombok.Value;

/**
 * The game state packet. This packet is here to ensure the server and client
 * are always in sync. This packet is sent by the server every 100 MS and provides
 * all the locations and rotational axis the server thinks the players are. Moribund
 * operates with a priority to the server, so all existing configurations of players
 * locations and rotations will be overridden with these configurations sent by
 * the server.
 */
@Value
public class GameStatePacket implements OutgoingPacket {
    private ObjectList<PlayerData> playerData;
}