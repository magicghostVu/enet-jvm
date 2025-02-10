package org.magicghostvu.jenet

import org.magicghostvu.jenet.protocol.ENetProtocol
import java.util.LinkedList

@OptIn(ExperimentalUnsignedTypes::class)
class ENetIncomingCommand {
    val incomingCommandList: LinkedList<Any> = LinkedList();
    var reliableSequenceNumber: UShort = 0.toUShort();
    var unreliableSequenceNumber: UShort = 0.toUShort();
    lateinit var command: ENetProtocol;
    var fragmentCount: UInt = 0.toUInt();
    var fragmentsRemaining: UInt = 0.toUInt();
    var fragments: UIntArray = UIntArray(0);
    lateinit var packet: ENetPacket;
}