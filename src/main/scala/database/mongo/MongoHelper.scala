package database.mongo

import java.util.logging.Logger

import org.mongodb.scala.bson.Document
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, MongoDatabase, Observer}
import play.api.libs.json.{JsValue, Json}

/**
  * Created by pierre on 27/05/16.
  */
object MongoHelper {

  val logger : Logger = Logger.getLogger("MongoHelper Logger")

  val mongoClient: MongoClient = MongoClient("mongodb://localhost:27030")
  val database: MongoDatabase = mongoClient.getDatabase("users")
  val collection: MongoCollection[Document] = database.getCollection("users")

  // Insert new user in database
  // TODO Manage errors in parsing
  def createUser (user : JsValue) : JsValue = {

    val doc: Document = Document(
      "lastName" -> user.\("lastName").as[String],
      "firstName" -> user.\("firstName").as[String],
      "rank" -> "user")

    collection.insertOne(doc).subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = {}
      override def onError(e: Throwable): Unit = logger.warning(e.getMessage)
      override def onComplete(): Unit = logger.info("User inserted / Data "+doc.toBsonDocument.toString)
    })

    Json.parse(doc.toBsonDocument.toString)
  }
}
