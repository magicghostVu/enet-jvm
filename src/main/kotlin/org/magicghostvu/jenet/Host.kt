package org.magicghostvu.jenet

import org.magicghostvu.jenet.protocol.ENetProtocolCommand
import org.magicghostvu.jenet.protocol.ENetProtocolConnect
import java.net.InetSocketAddress
import kotlin.math.abs

class Host(
    val maximumPeers: Int,
    val numChannelPerPeer: Int,
    val addressBind: InetSocketAddress?,
    val outgoingBandwidth: Int, //in case of server host, it should be 0 (un-limit)
    val incomingBandwidth: Int, //in case of server host, it should be 0 (un-limit)
) {

    // to check allow new peer connect
    private var activePeers: Int = 0;

    // active peer
    private val idToPeer: MutableMap<Int, Peer> = mutableMapOf()


    // is this safe to increase it ??
    var randomSeed: Int = abs(System.currentTimeMillis().toInt())


    fun findNewPeerId(): Int? {
        return if (idToPeer.size >= maximumPeers) {
            null
        } else {
            // find min key available

            TODO()
        }
    }

    // enet host update
    // blocking maximum
    // should return list of event
    fun update(timeoutReadMillis: Int) {
        TODO()
    }


    fun connectToRemoteHost(
        hostAddress: InetSocketAddress,
        channelCount: Int,
        data: Int,
    ): Peer {
        if (addressBind != null) {
            throw IllegalArgumentException("server host can not connect to other host")
        }
        require(
            channelCount in ProtocolConstants.ENET_PROTOCOL_MINIMUM_CHANNEL_COUNT
                    ..ProtocolConstants.ENET_PROTOCOL_MAXIMUM_CHANNEL_COUNT
        ) {
            "num channel invalid: $channelCount"
        }
        val currentPeer = Peer(channelCount)
        currentPeer.state = PeerState.ENET_PEER_STATE_CONNECTING
        currentPeer.address = hostAddress
        currentPeer.connectID = (++randomSeed).toUInt()

        if (this.outgoingBandwidth == 0)
            currentPeer.windowSize = ProtocolConstants.ENET_PROTOCOL_MAXIMUM_WINDOW_SIZE.toUInt();
        else {
            val t =
                (this.outgoingBandwidth / EnetConstant.ENET_PEER_WINDOW_SIZE_SCALE) * ProtocolConstants.ENET_PROTOCOL_MINIMUM_WINDOW_SIZE.toInt();
            currentPeer.windowSize = t.toUInt()
        }


        if (currentPeer.windowSize < ProtocolConstants.ENET_PROTOCOL_MINIMUM_WINDOW_SIZE)
            currentPeer.windowSize = ProtocolConstants.ENET_PROTOCOL_MINIMUM_WINDOW_SIZE;
        else if (currentPeer.windowSize > ProtocolConstants.ENET_PROTOCOL_MAXIMUM_WINDOW_SIZE)
            currentPeer.windowSize = ProtocolConstants.ENET_PROTOCOL_MAXIMUM_WINDOW_SIZE;


        val command = ENetProtocolConnect()

        command.header.command =
            (ENetProtocolCommand.ENET_PROTOCOL_COMMAND_CONNECT or ENetProtocolFlag.ENET_PROTOCOL_COMMAND_FLAG_ACKNOWLEDGE).toUByte();
        command.header.channelID = (0xFF).toUByte();
        command.outgoingPeerID = currentPeer.incomingPeerID;
        command.incomingSessionID = currentPeer.incomingSessionID;
        command.outgoingSessionID = currentPeer.outgoingSessionID;
        command.mtu = (currentPeer.mtu);
        command.windowSize = (currentPeer.windowSize);
        command.channelCount = (channelCount.toUInt());
        command.incomingBandwidth = this.incomingBandwidth.toUInt();
        command.outgoingBandwidth = this.outgoingBandwidth.toUInt();
        command.packetThrottleInterval = (currentPeer.packetThrottleInterval);
        command.packetThrottleAcceleration = (currentPeer.packetThrottleAcceleration);
        command.packetThrottleDeceleration = (currentPeer.packetThrottleDeceleration);
        command.connectID = currentPeer.connectID;
        command.data = data.toUInt();

        currentPeer.enqueueOutgoingCommand(command, null, 0.toUInt(), 0.toUShort());

        return currentPeer
    }

    companion object {
        fun createNewHost(maximumPeers: Int, addressToBind: InetSocketAddress?, numChannelPerPeer: Int): Host {
            return Host(maximumPeers, numChannelPerPeer, addressToBind, 0, 0)
        }
    }
}