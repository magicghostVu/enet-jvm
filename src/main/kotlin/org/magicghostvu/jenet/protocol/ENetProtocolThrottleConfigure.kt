package org.magicghostvu.jenet.protocol

class ENetProtocolThrottleConfigure(
   override val header: ENetProtocolCommandHeader,
    val packetThrottleInterval: UInt,
    val packetThrottleAcceleration: UInt,
    val packetThrottleDeceleration: UInt,
) : ENetProtocol() {
}