package org.magicghostvu.jenet.protocol

class ENetProtocolSendUnreliable(
   override val header: ENetProtocolCommandHeader,
    var unreliableSequenceNumber: UShort,
    val dataLength: UShort,
) : ENetProtocol() {
}