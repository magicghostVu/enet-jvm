package org.magicghostvu.jenet.main

import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.nio.channels.SelectionKey
import java.nio.channels.Selector

fun main() {

    System.setProperty("log4j.configurationFile", "./log4j2.xml")
    val logger = LoggerFactory.getLogger("io")

    logger.info("logger work")



    val udpChannel = DatagramChannel.open()
    udpChannel.configureBlocking(false)
    udpChannel.bind(InetSocketAddress(9900))
    val selector = Selector.open()
    udpChannel.register(selector, SelectionKey.OP_READ)

    // 2500 byte is enough for every udp packet
    val buffer = ByteBuffer.allocate(2500)
    while (true) {
        // sleep this thread if there is no event happen to save cpu power
        selector.select(1)
        val selectedKeys = selector.selectedKeys()
        // selected key had only one reference all the time
        if (selectedKeys.isNotEmpty()) {
            val iter = selectedKeys.iterator()
            // why not use for each and clear after all
            while (iter.hasNext()) {
                val key = iter.next()
                // remove from the set
                iter.remove()
                if (key.isValid && key.isReadable) {
                    val channelSend = key.channel() as DatagramChannel

                    // not block here
                    val sourceOfData = channelSend.receive(buffer)


                    // change to read mode
                    buffer.flip()
                    // allocate new byte array
                    val bufferSendToLogic = ByteArray(buffer.remaining())


                    buffer.get(bufferSendToLogic)

                    //todo: send buffer read and address to logic engine
                    // call engine update
                    // same as host update

                    // clear all old data to next read
                    logger.info("received {} bytes from {}", bufferSendToLogic.size, sourceOfData)

                    buffer.clear()
                }
            }
        }
        // dispatch event to logic engine
        // new packet come, new peer connect, peer disconnect
        // process command from out-side
        // request disconnect peer, send packet to peer,
        // broadcast packet to some peer
    }

}