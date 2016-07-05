package services

import java.util.logging.Logger
import database.mongo.MongoHelper
import play.api.libs.json.JsValue
import scala.collection.mutable

/**
  * Created by Pierre on 19/04/16.
  */

object UsersService {

  val logger : Logger = Logger.getLogger("UsersService Logger")

  // AUTHENTICATION
  /**
    * Check if user exists
    * @param user login and password to check
    * @return the user data if it exists
    */
  def login (user : JsValue) : Option[JsValue] = {
    logger.info("Login request / Data :" + user)
     val filteredUsers : mutable.Set[JsValue] = MongoHelper.getUsers(None).filter (
       userDb => userDb.\("login").equals(user.\("login")) && userDb.\("password").equals(user.\("password"))
     )
    filteredUsers.headOption
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

  /**
    * Update user in database
    * @param user user to update
    * @return The user updated
    */
  def updateUser (user : JsValue) : JsValue = {
    logger.info("update_user request / Data :" + user)
    MongoHelper.updateUser(user)
  }

  /**
    * Delete user in database
    * @param user Project to delete
    * @return The user deleted
    */
  def deleteUser (user : JsValue) : JsValue = {
    logger.info("delete_user request / Data :" + user)
    MongoHelper.deleteUser(user)
  }

  /**
    * Return all users
    * @param filters fields to filter the result
    * @return users matching with the specified filters
    */
  def getUsers (filters : JsValue) : JsValue = {
    logger.info("get_users request / Data :" + filters)
    MongoHelper.getUsers(filters)
  }

  /**
    * Get the user matching with the specified id
    * @param user a json containing the id of the searched user
    * @return the user matching with the specified id
    */
  def getUser (user : JsValue) : Option[JsValue]  = {
    logger.info("get_user request / Data :" + user)
    MongoHelper.getUser(user)
  }

}