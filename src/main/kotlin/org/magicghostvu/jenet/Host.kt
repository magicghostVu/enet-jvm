package org.magicghostvu.jenet

import java.net.InetSocketAddress
import kotlin.math.abs

class Host(
    val maximumPeers: Int, val numChannelPerPeer: Int,
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
        numChannel: Int,
        outGoingBandwidth: Int,// byte per second
        incomingBandwidth: Int, // byte per second
        data: Int,
    ): Peer {
        if (addressBind != null) {
            throw IllegalArgumentException("server host can not connect to other host")
        }
        require(
            numChannel in ProtocolConstants.ENET_PROTOCOL_MINIMUM_CHANNEL_COUNT
                    ..ProtocolConstants.ENET_PROTOCOL_MAXIMUM_CHANNEL_COUNT
        ) {
            "num channel invalid: $numChannel"
        }
        val peer = Peer(numChannel)
        peer.state = PeerState.ENET_PEER_STATE_CONNECTING
        peer.address = hostAddress
        peer.connectID = (++randomSeed).toUInt()

        if (this.outgoingBandwidth == 0)
            peer.windowSize = ProtocolConstants.ENET_PROTOCOL_MAXIMUM_WINDOW_SIZE.toUInt();
        else {
            val t =
                (this.outgoingBandwidth / EnetConstant.ENET_PEER_WINDOW_SIZE_SCALE) * ProtocolConstants.ENET_PROTOCOL_MINIMUM_WINDOW_SIZE.toInt();
            peer.windowSize = t.toUInt()
        }


        if (peer.windowSize < ProtocolConstants.ENET_PROTOCOL_MINIMUM_WINDOW_SIZE)
            peer.windowSize = ProtocolConstants.ENET_PROTOCOL_MINIMUM_WINDOW_SIZE;
        else if (peer.windowSize > ProtocolConstants.ENET_PROTOCOL_MAXIMUM_WINDOW_SIZE)
            peer.windowSize = ProtocolConstants.ENET_PROTOCOL_MAXIMUM_WINDOW_SIZE;



        return peer
    }

    companion object {
        fun createNewHost(maximumPeers: Int, addressToBind: InetSocketAddress?, numChannelPerPeer: Int): Host {
            return Host(maximumPeers, numChannelPerPeer, addressToBind, 0, 0)
        }
    }
}