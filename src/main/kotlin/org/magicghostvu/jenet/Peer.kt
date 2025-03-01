package org.magicghostvu.jenet

import org.magicghostvu.jenet.protocol.ENetProtocol
import org.magicghostvu.jenet.protocol.ENetProtocolCommand
import org.magicghostvu.jenet.protocol.ENetProtocolSendUnreliable
import org.magicghostvu.jenet.protocol.ENetProtocolSendUnsequenced
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.util.LinkedList

@OptIn(ExperimentalUnsignedTypes::class)
class Peer(val channelCount: Int) {

    val dispatchList: LinkedList<Any> = LinkedList()

    lateinit var host: Host

    var outgoingPeerID: UShort = 0.toUShort()
    var incomingPeerID: UShort = 0.toUShort()

    // peer Id
    var connectID: UInt = 0.toUInt()

    var outgoingSessionID: UByte = 0.toUByte()
    var incomingSessionID: UByte = 0.toUByte()

    lateinit var address: InetSocketAddress


    var state: ENetPeerState = ENetPeerState.ENET_PEER_STATE_CONNECTING

    val channels: MutableMap<Int, EnetChannel> = mutableMapOf()


    val incomingBandwidth = 0.toUInt()

    /**< Downstream bandwidth of the client in bytes/second */
    val outgoingBandwidth = 0.toUInt()

    /**< Upstream bandwidth of the client in bytes/second */
    val incomingBandwidthThrottleEpoch = 0.toUInt()
    val outgoingBandwidthThrottleEpoch = 0.toUInt()
    val incomingDataTotal = 0.toUInt()
    var outgoingDataTotal = 0.toUInt()
    val lastSendTime = 0.toUInt()
    val lastReceiveTime = 0.toUInt()
    val nextTimeout = 0.toUInt()
    val earliestTimeout = 0.toUInt()
    val packetLossEpoch = 0.toUInt()
    val packetsSent = 0.toUInt()
    val packetsLost = 0.toUInt()
    val packetLoss = 0.toUInt()

    /**< mean packet loss of reliable packets as a ratio with respect to the constant ENET_PEER_PACKET_LOSS_SCALE */
    val packetLossVariance = 0.toUInt()
    val packetThrottle = 0.toUInt()
    val packetThrottleLimit = 0.toUInt()
    val packetThrottleCounter = 0.toUInt()
    val packetThrottleEpoch = 0.toUInt()
    val packetThrottleAcceleration = 0.toUInt()
    val packetThrottleDeceleration = 0.toUInt()
    val packetThrottleInterval = 0.toUInt()
    val pingInterval = 0.toUInt()
    val timeoutLimit = 0.toUInt()
    val timeoutMinimum = 0.toUInt()
    val timeoutMaximum = 0.toUInt()
    val lastRoundTripTime = 0.toUInt()
    val lowestRoundTripTime = 0.toUInt()
    val lastRoundTripTimeVariance = 0.toUInt()
    val highestRoundTripTimeVariance = 0.toUInt()
    val roundTripTime = 0.toUInt()

    /**< mean round trip time (RTT), in milliseconds, between sending a reliable packet and receiving its acknowledgement */
    val roundTripTimeVariance = 0.toUInt()
    val mtu = 0.toUInt()
    var windowSize = 0.toUInt()
    val reliableDataInTransit = 0.toUInt()

    var outgoingReliableSequenceNumber: UShort = 0.toUShort();


    // need refactor to type-safe list
    val acknowledgements: LinkedList<Any> = LinkedList();
    val sentReliableCommands: LinkedList<Any> = LinkedList();
    val sentUnreliableCommands: LinkedList<Any> = LinkedList();
    val outgoingCommands: LinkedList<ENetOutgoingCommand> = LinkedList();
    val dispatchedCommands: LinkedList<ENetIncomingCommand> = LinkedList();


    var flags: UShort = 0.toUShort()
    var reserved: UShort = 0.toUShort()
    var incomingUnsequencedGroup: UShort = 0.toUShort()
    var outgoingUnsequencedGroup: UShort = 0.toUShort()

    val unsequencedWindow: UIntArray = UIntArray(EnetConstant.ENET_PEER_UNSEQUENCED_WINDOW_SIZE / 32);
    var eventData: UInt = 0.toUInt();
    var totalWaitingData: Long = 0;


    private val logger: Logger = LoggerFactory.getLogger("peer-io")


    // clone of enet_peer_queue_outgoing_command
    fun enqueueOutgoingCommand(
        command: ENetProtocol,
        packet: ENetPacket?,
        offset: UInt,
        length: UShort,
    ): ENetOutgoingCommand {
        val outgoingCommand = ENetOutgoingCommand(command, packet)
        outgoingCommand.fragmentOffset = offset
        outgoingCommand.fragmentLength = length
        outgoingCommand.packet = packet
        if (packet != null) {
            packet.referenceCount++
        }
        setupOutgoingCommand(outgoingCommand)
        return outgoingCommand
    }

    fun onConnect() {
        if (this.state != ENetPeerState.ENET_PEER_STATE_CONNECTED && this.state != ENetPeerState.ENET_PEER_STATE_DISCONNECT_LATER) {
            if (this.incomingBandwidth != 0.toUInt()) {
                ++this.host.bandwidthLimitedPeers;
            }
            ++this.host.connectedPeers;
        }
    }

    fun onDisconnect() {
        if (this.state == ENetPeerState.ENET_PEER_STATE_CONNECTED || this.state == ENetPeerState.ENET_PEER_STATE_DISCONNECT_LATER) {
            if (this.incomingBandwidth != 0.toUInt()) {
                --this.host.bandwidthLimitedPeers;
            }
            --this.host.connectedPeers;
        }
    }

    fun changeState(state: ENetPeerState) {
        if (state == ENetPeerState.ENET_PEER_STATE_CONNECTED || state == ENetPeerState.ENET_PEER_STATE_DISCONNECT_LATER)
            onConnect()
        else
            onDisconnect()

        this.state = state;
    }


    fun receive(): Pair<ENetPacket, UByte>? {
        if (dispatchedCommands.isEmpty())
            return null;
        val incomingCommand: ENetIncomingCommand = dispatchedCommands.removeFirst()

        val channelId = incomingCommand.command.header.channelID
        val packet = incomingCommand.packet;
        totalWaitingData -= packet.dataLength
        return Pair(packet, channelId)
    }

    fun setupOutgoingCommand(outgoingCommand: ENetOutgoingCommand) {
        val channelId = outgoingCommand.command.header.channelID.toInt()
        val commandId = outgoingCommand.command.header.command;
        val channel = channels[channelId]
        if (channel != null) {
            outgoingDataTotal += (outgoingCommand.command.sizeByte() + outgoingCommand.fragmentLength.toInt()).toUInt()

            // channel 255
            if (channelId == 0xff) {
                outgoingReliableSequenceNumber++;
                outgoingCommand.reliableSequenceNumber = outgoingReliableSequenceNumber
                outgoingCommand.unreliableSequenceNumber = 0.toUShort();
            }
            // gửi ack
            else if (commandId.toInt() and ENetProtocolFlag.ENET_PROTOCOL_COMMAND_FLAG_ACKNOWLEDGE != 0) {
                ++channel.outgoingReliableSequenceNumber;
                channel.outgoingUnreliableSequenceNumber = 0.toUShort();

                outgoingCommand.reliableSequenceNumber = channel.outgoingReliableSequenceNumber;
                outgoingCommand.unreliableSequenceNumber = 0.toUShort();
            } else if (commandId.toInt() and ENetProtocolFlag.ENET_PROTOCOL_COMMAND_FLAG_UNSEQUENCED != 0) {
                ++outgoingUnsequencedGroup;

                outgoingCommand.reliableSequenceNumber = 0.toUShort();
                outgoingCommand.unreliableSequenceNumber = 0.toUShort();
            } else {
                if (outgoingCommand.fragmentOffset == 0.toUInt()) {
                    ++channel.outgoingUnreliableSequenceNumber;
                }
                outgoingCommand.reliableSequenceNumber = channel.outgoingReliableSequenceNumber;
                outgoingCommand.unreliableSequenceNumber = channel.outgoingUnreliableSequenceNumber;
            }

            outgoingCommand.sendAttempts = 0.toUShort();
            outgoingCommand.sentTime = 0.toUInt();
            outgoingCommand.roundTripTimeout = 0.toUInt();
            outgoingCommand.roundTripTimeoutLimit = 0.toUInt();
            outgoingCommand.command.header.reliableSequenceNumber = outgoingCommand.reliableSequenceNumber;

            val checkCommandType =
                outgoingCommand.command.header.command.toInt() and ENetProtocolCommand.ENET_PROTOCOL_COMMAND_MASK
            when (checkCommandType) {
                ENetProtocolCommand.ENET_PROTOCOL_COMMAND_SEND_UNRELIABLE -> {
                    val internalCommand = outgoingCommand.command as ENetProtocolSendUnreliable
                    internalCommand.unreliableSequenceNumber = outgoingCommand.unreliableSequenceNumber;
                }

                ENetProtocolCommand.ENET_PROTOCOL_COMMAND_SEND_UNSEQUENCED -> {
                    val internalCommand = outgoingCommand.command as ENetProtocolSendUnsequenced
                    internalCommand.unsequencedGroup = outgoingUnsequencedGroup;
                }

                else -> {}
            }

            outgoingCommands.add(outgoingCommand)
        } else {
            logger.warn("channel {} not exist", channelId)
        }
    }
}