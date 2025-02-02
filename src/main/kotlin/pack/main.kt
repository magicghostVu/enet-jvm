package pack

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.nio.channels.SelectionKey
import java.nio.channels.Selector

fun main() {
    val udpChannel = DatagramChannel.open()
    udpChannel.configureBlocking(false)
    udpChannel.bind(InetSocketAddress(9900))
    val selector = Selector.open()
    udpChannel.register(selector, SelectionKey.OP_READ)

    // sleep this thread if not event happen to save cpu power
    // 2500 byte is enough for every udp packet
    val buffer = ByteBuffer.allocate(2500)
    while (true) {
        selector.select(1)
        val selectedKeys = selector.selectedKeys()
        if (selectedKeys.isNotEmpty()) {
            val iter = selectedKeys.iterator()
            while (iter.hasNext()) {
                val key = iter.next()
                iter.remove()
                if (key.isValid && key.isReadable) {
                    val channelSend = key.channel() as DatagramChannel
                    val numByteRead = channelSend.read(buffer)

                    // allocate new byte array
                    val bufferSendToLogic = ByteArray(numByteRead)

                    val sourceOfData = channelSend.remoteAddress as InetSocketAddress

                    // todo: send buffer read and address to logic engine

                    // change to read mode
                    buffer.flip();
                    buffer.get(bufferSendToLogic)

                    // clear all old data to next read
                    buffer.clear();
                }
            }
        }
    }

}