package org.magicghostvu.jenet

class ENetPacket {
    var referenceCount: Int = 0;

    val dataLength:Int = 0;
    val data: ByteArray = ByteArray(dataLength)
}