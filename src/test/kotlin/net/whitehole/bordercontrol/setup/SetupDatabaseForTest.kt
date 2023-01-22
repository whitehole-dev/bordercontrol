package net.whitehole.bordercontrol.setup

import de.bwaldvogel.mongo.MongoServer
import de.bwaldvogel.mongo.backend.memory.MemoryBackend
import net.whitehole.bordercontrol.BorderControl
import net.whitehole.bordercontrol.server.RestApi
import net.whitehole.bordercontrol.server.RestApiVerify
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.reactivestreams.getCollection

var borderControl: BorderControl? = null

fun setupDatabase() {
    val server = MongoServer(MemoryBackend())
    borderControl = BorderControl(server.bindAndGetConnectionString())
}