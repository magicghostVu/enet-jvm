package org.magicghostvu.jenet.protocol

sealed class ENetProtocol {
    abstract val header: ENetProtocolCommandHeader

    fun sizeByte(): Int {
        return sizeOfAllChild[header.command.toInt()]
    }

    companion object {
        private val sizeOfAllChild: IntArray = intArrayOf(
            0,
            8,
            48,
            44,
            8,
            4,
            6,
            8,
            24,
            8,
            12
        )
    }
}