package org.magicghostvu.jenet

import org.magicghostvu.jenet.protocol.ENetProtocol
import java.util.LinkedList

class ENetOutgoingCommand(
    val command: ENetProtocol,
    var packet: ENetPacket? = null,
) {
    val outgoingCommandList: LinkedList<Any> = LinkedList()
    var reliableSequenceNumber: UShort = 0.toUShort()
    var unreliableSequenceNumber: UShort = 0.toUShort()


    var sentTime: UInt = 0.toUInt()
    var roundTripTimeout: UInt = 0.toUInt()
    var roundTripTimeoutLimit: UInt = 0.toUInt()
    var fragmentOffset: UInt = 0.toUInt()


    var fragmentLength: UShort = 0.toUShort()
    var sendAttempts: UShort = 0.toUShort()

}