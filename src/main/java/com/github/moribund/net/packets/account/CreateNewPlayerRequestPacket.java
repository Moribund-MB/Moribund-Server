package com.github.moribund.net.packets.account;

import com.esotericsoftware.kryonet.Connection;
import com.github.moribund.GraphicalConstants;
import com.github.moribund.MoribundServer;
import com.github.moribund.game.Game;
import com.github.moribund.net.packets.IncomingPacket;
import com.github.moribund.net.packets.data.GroundItemData;
import com.github.moribund.net.packets.data.PlayerData;
import com.github.moribund.objects.playable.players.PlayableCharacter;
import com.github.moribund.objects.playable.players.Player;
import com.github.moribund.utils.ArtificialTime;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import lombok.val;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The request sent by the client to the server that a person is making
 * an account, so it must register that request and follow instructions
 * to handle the player server-sided.
 */
public final class CreateNewPlayerRequestPacket implements IncomingPacket {
    /**
     * A private constructor to ensure the server cannot unexpectedly send this
     * request to the client.
     */
    private CreateNewPlayerRequestPacket() {
    }

    @Override
    public void process(Connection connection) {
        val game = MoribundServer.getInstance().getGameContainer().getAvailableGame();
        val playerId = connection.getID();
        val username = MoribundServer.getInstance().getUsernameMap().get(playerId);
        val player = createNewPlayer(game.getGameId(), playerId, username, connection);

        sendNewPlayerPacket(game, player);
        game.addPlayer(playerId, player);
        sendPlayersToNewPlayer(game, player);
    }


    /**
     * Sends the {@link CreateNewPlayerPacket} to the newly made player. An important thing
     * to note is that this sends a list of players that includes the newly made player
     * him/her self.
     *
     * @param player The newly made {@link Player}.
     */
    private void sendPlayersToNewPlayer(Game game, PlayableCharacter player) {
        // note this includes the newly made player
        ObjectList<PlayerData> playerData = new ObjectArrayList<>();
        ObjectList<GroundItemData> groundItems = new ObjectArrayList<>();
        game.forEachPlayer((playerId, aPlayer) ->
                playerData.add(new PlayerData(playerId, aPlayer.getUsername(), aPlayer.getX(), aPlayer.getY(),
                        aPlayer.getRotation(), aPlayer.getHitpoints(), aPlayer.getInventory().getItemIds(),
                        aPlayer.getEquipment().getItemIds())));
        game.getGroundItems().forEach(item ->
                groundItems.add(new GroundItemData(item.getItemType().getId(), item.getX(), item.getY())));

        val loginPacket = new CreateNewPlayerPacket(player.getGameId(), player.getPlayerId(), playerData, groundItems);
        player.getConnection().sendTCP(loginPacket);
    }

    /**
     * Sends a {@link DrawNewPlayerPacket} to all the existing {@link Player}s in the game.
     *
     * @param newPlayer The newly made {@link Player}.
     */
    private void sendNewPlayerPacket(Game game, PlayableCharacter newPlayer) {
        val newPlayerLoginPacket = new DrawNewPlayerPacket(newPlayer.getGameId(), newPlayer.getPlayerId(),
                newPlayer.getUsername(), newPlayer.getX(), newPlayer.getY(), newPlayer.getRotation(),
                newPlayer.getHitpoints());
        game.forEachPlayer(player -> player.getConnection().sendTCP(newPlayerLoginPacket));
    }

    /**
     * Makes a new {@link Player} using the player ID that is generated by the
     * {@link Connection} and the {@link Connection} itself.
     *
     * @param playerId   The player ID of the newly made player.
     * @param connection The connection of the newly made player.
     * @return The newly made {@link Player}.
     */
    private Player createNewPlayer(int gameId, int playerId, String username, Connection connection) {
        val x = (float) ThreadLocalRandom.current().nextDouble(GraphicalConstants.MINIMUM_X + 100, GraphicalConstants.MAXIMUM_X - 100);
        val y = (float) ThreadLocalRandom.current().nextDouble(GraphicalConstants.MINIMUM_Y + 100, GraphicalConstants.MAXIMUM_Y - 100);
        val player = new Player(gameId, playerId, username, x, y, generateTimeLeft());
        player.setConnection(connection);
        return player;
    }

    private ArtificialTime generateTimeLeft() {
        val minimumSeconds = 180;
        val maximumSeconds = 300;
        return new ArtificialTime(ThreadLocalRandom.current().nextInt(minimumSeconds, maximumSeconds));
    }
}
