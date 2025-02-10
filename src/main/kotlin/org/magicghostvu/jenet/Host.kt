package org.magicghostvu.jenet

import org.magicghostvu.jenet.event.EnetEvent
import org.magicghostvu.jenet.protocol.ENetProtocol
import org.magicghostvu.jenet.protocol.ENetProtocolCommand
import org.magicghostvu.jenet.protocol.ENetProtocolCommandHeader
import org.magicghostvu.jenet.protocol.ENetProtocolConnect
import java.net.InetSocketAddress
import java.util.LinkedList
import kotlin.math.abs

class Host(
    val maximumPeers: Int,
    val channelLimit: Int,
    val addressBind: InetSocketAddress?,
    val outgoingBandwidth: Int, //in case of server host, it should be 0 (un-limit)
    val incomingBandwidth: Int, //in case of server host, it should be 0 (un-limit)
) {

    // to check allow new peer connect
    private var activePeers: Int = 0;

    // active peer
    private val idToPeer: MutableMap<Int, Peer> = mutableMapOf()


    // is this safe to increase it ??


    var bandwidthThrottleEpoch: Int = 0;
    var mtu: Int = EnetConstant.ENET_HOST_DEFAULT_MTU;


    var randomSeed: Int = abs(System.currentTimeMillis().toInt())

    var recalculateBandwidthLimits: Int = 0;

    var serviceTime: UInt = 0.toUInt();

    val dispatchQueue: LinkedList<Any> = LinkedList()

    var continueSending: Int = 0;

    var packetSize: Long = 0;

    var headerFlags: UShort = 0.toUShort();


    // mảng chứa command cho host, maybe sử dụng list
    val commands = Array<ENetProtocol>(ProtocolConstants.ENET_PROTOCOL_MAXIMUM_PACKET_COMMANDS) {
        ENetProtocolCommandHeader()
    }


    var commandCount: Long = 0;


    val buffers: Array<ENetBuffer> = Array(1 + 2 * ProtocolConstants.ENET_PROTOCOL_MAXIMUM_PACKET_COMMANDS) {
        ENetBuffer.emptyBuffer
    }


    var bufferCount: Long = 0;

    lateinit var checksum: (buffers: Array<ENetBuffer>, bufferCount: Long) -> Int


    var compressor: ENetCompressor? = null

    var packetData: Array<ByteArray> = Array(2) {
        ByteArray(ProtocolConstants.ENET_PROTOCOL_MAXIMUM_MTU)
    }

    lateinit var receivedAddress: InetSocketAddress;

    var receivedData = ByteArray(0)
    var receivedDataLength: Long = 0;


    /**< total data sent, user should reset to 0 as needed to prevent overflow */
    var totalSentData: UInt = 0.toUInt();


    /**< total UDP packets sent, user should reset to 0 as needed to prevent overflow */
    var totalSentPackets: UInt = 0.toUInt();


    /**< total data received, user should reset to 0 as needed to prevent overflow */
    var totalReceivedData: UInt = 0.toUInt();


    /**< total UDP packets received, user should reset to 0 as needed to prevent overflow */
    var totalReceivedPackets: UInt = 0.toUInt();


    lateinit var intercept: (host: Host, event: EnetEvent) -> Int;


    // should refactor
    var connectedPeers: UInt = 0.toUInt();


    var bandwidthLimitedPeers: UInt = 0.toUInt();

    /**< optional number of allowed peers from duplicate IPs, defaults to ENET_PROTOCOL_MAXIMUM_PEER_ID */
    var duplicatePeers: UInt = 0.toUInt();


    /**< the maximum allowable packet size that may be sent or received on a peer */
    var maximumPacketSize: UInt = 0.toUInt();


    /**< the maximum aggregate amount of buffer space a peer may use waiting for packets to be delivered */
    var maximumWaitingData: UInt = 0.toUInt();


    fun findNewPeerId(): Int? {
        return if (idToPeer.size >= maximumPeers) {
            null
        } else {
            // find min key available

            TODO()
        }
    }

    // enet host update
    // blocking in timeoutReadMillis(maximum)
    // name same as origin host_service(timeOut)
    // but will return many events in a single update
    fun hostService(timeoutReadMillis: Int): List<EnetEvent> {
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