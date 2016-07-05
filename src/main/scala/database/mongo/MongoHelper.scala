package database.mongo

import java.util.UUID
import java.util.logging.Logger

import com.mongodb.casbah.Imports._
import play.api.libs.json.{JsValue, Json}

import scala.collection.mutable

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

    val doc = MongoDBObject (
      "_id" -> UUID.randomUUID().toString,
      "lastName" -> user.\("lastName").as[String],
      "firstName" -> user.\("firstName").as[String],
      "rank" -> "user"
    )
    coll.insert(doc)
    Json.parse(doc.toString)
  }

  /**
    * Update user in database
    * @param user user to update
    * @return The user updated
    */
  def updateUser (user : JsValue) : JsValue = {
    val query = MongoDBObject("_id" -> user.\("_id").as[String])
    val update = MongoDBObject(
      "lastName" -> user.\("lastName").as[String],
      "firstName" -> user.\("firstName").as[String],
      "rank" -> user.\("rank").as[String]
    )
    val result = coll.update(query, update)
    Json.parse(result.toString)
  }

  /**
    * Delete user in database
    * @param user Project to delete
    * @return The user deleted
    */
  def deleteUser (user : JsValue) : JsValue = {
    val query = MongoDBObject("_id" -> user.\("_id").as[String])
    val result = coll.remove( query )
    Json.parse(result.toString)
  }

  /**
    * Return all users
    * @param filters fields to filter the result
    * @return users matching with the specified filters
    */
  def getUsers (filters : Option[JsValue]) : mutable.Set[JsValue] = {
    // Filters to do
    val result : mutable.Set[JsValue] = mutable.Set[JsValue] ()
    val allDocs = coll.find()
    allDocs.foreach(doc => result += Json.parse(doc.toString))
    result
  }

  /**
    * Get the user matching with the specified id
    * @param user a json containing the id of the searched user
    * @return the user matching with the specified id
    */
  def getUser (user : JsValue) : Option[JsValue]  = {
    val query = MongoDBObject("_id" -> user.\("_id").as[String])
    val result = coll.findOne(query)
    if (result.nonEmpty) Some(Json.parse(result.get.toString)) else None
  }
}
