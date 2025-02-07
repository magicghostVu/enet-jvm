package org.magicghostvu.jenet.protocol

class ENetProtocolConnect(
    override val header: ENetProtocolCommandHeader = ENetProtocolCommandHeader(),
) : ENetProtocol() {
    var outgoingPeerID: UShort = 0.toUShort()
    var incomingSessionID: UByte = 0.toUByte()
    var outgoingSessionID: UByte = 0.toUByte()
    var mtu: UInt = 0.toUInt()
    var windowSize: UInt = 0.toUInt()
    var channelCount: UInt = 0.toUInt()
    var incomingBandwidth: UInt = 0.toUInt()
    var outgoingBandwidth: UInt = 0.toUInt()
    var packetThrottleInterval: UInt = 0.toUInt()
    var packetThrottleAcceleration: UInt = 0.toUInt()
    var packetThrottleDeceleration: UInt = 0.toUInt()
    var connectID: UInt = 0.toUInt()
    var data: UInt = 0.toUInt()
}