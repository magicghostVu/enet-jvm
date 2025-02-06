package org.magicghostvu.jenet.protocol

class ENetProtocolVerifyConnect(
   override val header:ENetProtocolCommandHeader,
    val outgoingPeerID: UShort,
    val  incomingSessionID: UByte,
    val  outgoingSessionID: UByte,
    val mtu: UInt,
    val windowSize: UInt,
    val channelCount: UInt,
    val incomingBandwidth: UInt,
    val outgoingBandwidth: UInt,
    val packetThrottleInterval: UInt,
    val packetThrottleAcceleration: UInt,
    val packetThrottleDeceleration: UInt,
    val connectID: UInt,
): ENetProtocol() {
}