package database.mongo

import java.util.logging.Logger

import play.api.libs.json.{JsValue, Json}
import scala.collection.mutable
import com.mongodb.casbah.Imports._

/**
  * Created by pierre on 27/05/16.
  */
object MongoHelper {

  val logger : Logger = Logger.getLogger("MongoHelper Logger")

  val mongoClient = MongoClient("localhost", 27030)
  val db = mongoClient("users")
  val coll = db("users")

  // TODO Manage errors in parsing
  /**
    * Create new user
    * @param user = the user to insert
    * @return = a JSON with the data of the new user
    */
  def createUser (user : JsValue) : JsValue = {

    val doc = MongoDBObject(
      "lastName" -> user.\("lastName").as[String],
      "firstName" -> user.\("firstName").as[String],
      "rank" -> "user"
    )

    coll.insert(doc)
    Json.parse(doc.toString)
  }

  def getUsers ( filters : JsValue) : JsValue = {
    filters
  }

  def getUser (idUser : JsValue) : JsValue = {
    /*val id = idUser.\("idUser").as[String]
    collection.find(equal("$oid", id)).first().
    idUser*/
    idUser
  }
}
