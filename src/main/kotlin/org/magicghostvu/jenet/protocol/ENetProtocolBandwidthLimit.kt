package org.magicghostvu.jenet.protocol

class ENetProtocolBandwidthLimit(
    override val header: ENetProtocolCommandHeader,
    val incomingBandwidth: UInt,
    val outgoingBandwidth: UInt,
) : ENetProtocol() {
}