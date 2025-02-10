package org.magicghostvu.jenet

class ENetBuffer(
    val dataLength: Long,
    val data: ByteArray,
) {

    companion object{
        val emptyBuffer = ENetBuffer(0L, ByteArray(0))
    }
}