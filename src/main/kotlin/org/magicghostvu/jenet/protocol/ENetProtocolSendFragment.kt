package org.magicghostvu.jenet.protocol

class ENetProtocolSendFragment(
    override val header: ENetProtocolCommandHeader,
    val startSequenceNumber: UShort,
    val dataLength: UShort,
    val fragmentCount: UInt,
    val fragmentNumber: UInt,
    val totalLength: UInt,
    val fragmentOffset: UInt,
) : ENetProtocol() {
}