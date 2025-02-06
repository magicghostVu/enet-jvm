package org.magicghostvu.jenet.protocol

class ENetProtocolAcknowledge(
    override val header: ENetProtocolCommandHeader,
    val receivedReliableSequenceNumber: UShort,
    val receivedSentTime: UShort,
) : ENetProtocol() {
}