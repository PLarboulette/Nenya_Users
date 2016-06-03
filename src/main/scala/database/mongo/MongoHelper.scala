package database.mongo

import org.mongodb.scala.bson.Document
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, MongoDatabase, Observer}
import play.api.libs.json.JsValue
import play.api.libs.json.{JsValue, Json}

/**
  * Created by pierre on 27/05/16.
  */
object MongoHelper {

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
      override def onNext(result: Completed): Unit = println("Inserted user " + user)
      override def onError(e: Throwable): Unit = println(e.getMessage);
      override def onComplete(): Unit = println("Completed")
    })

    Json.parse(doc.toBsonDocument.toString)
  }

}
