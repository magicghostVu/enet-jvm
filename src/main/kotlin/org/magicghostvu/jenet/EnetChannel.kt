package org.magicghostvu.jenet

import java.util.LinkedList

@OptIn(ExperimentalUnsignedTypes::class)
class EnetChannel {
    var outgoingReliableSequenceNumber: UShort = 0.toUShort();
    var outgoingUnreliableSequenceNumber: UShort = 0.toUShort();
    var usedReliableWindows: UShort = 0.toUShort();
    var reliableWindows: UShortArray = UShortArray(EnetConstant.ENET_PEER_RELIABLE_WINDOWS);
    var incomingReliableSequenceNumber: UShort = 0.toUShort();
    var incomingUnreliableSequenceNumber: UShort = 0.toUShort();


    val incomingReliableCommands: LinkedList<Any> = LinkedList()
    val incomingUnreliableCommands: LinkedList<Any> = LinkedList()
}