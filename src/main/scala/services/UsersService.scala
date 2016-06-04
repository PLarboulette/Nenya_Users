package services

import java.util.logging.Logger
import database.mongo.MongoHelper
import play.api.libs.json.JsValue

/**
  * Created by pierre on 19/04/16.
  */
object UsersService {

  val logger : Logger = Logger.getLogger("UsersService Logger")

  // AUTHENTICATION
  /**
    * Check if user exists
    * @param user login and password to check
    * @return the user data if it exists, a empty json otherwise
    */
  def login (user : JsValue) : JsValue = {
    logger.info("Login request / Data :" + user)
    user
  }

  /**
    * Insert new user in database
    * @param user User to insert
    * @return The user created
    */
  def createUser (user : JsValue): JsValue = {
    logger.info("Create_user request / Data :" + user)
    MongoHelper.createUser(user)
  }

  def updateUser (user : JsValue) : JsValue = {
    // TODO
    user
  }

  def deleteUser (user : JsValue) : JsValue = {
    // TODO
    user
  }

  def getUsers (filters : JsValue) : JsValue = {

    // Use Redis cache or MongoDB
    /*val result: mutable.Set[User] = mutable.Set[User] ()
    result*/
    filters
  }

  def getUser (user : JsValue) : JsValue = {
    /*val posts : mutable.Set[User] = getUsers.filter(user => user.id == userId)
    posts.headOption*/
    user
  }
}