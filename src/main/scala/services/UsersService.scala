package services

import database.mongo.MongoHelper

import scala.collection.mutable
import models.User
import play.api.libs.json.JsValue


/**
  * Created by pierre on 19/04/16.
  */
object UsersService {

  def getUsers: mutable.Set[User] = {

    // Use Redis cache or MongoDB
    val result: mutable.Set[User] = mutable.Set[User] ()
    result
  }

  def getUserById (userId : String) : Option[User] = {
    val posts : mutable.Set[User] = getUsers.filter(user => user.id == userId)
    posts.headOption
  }

  // Insert new user in database
  def createUser (user : JsValue): JsValue = {
    MongoHelper.createUser(user)
  }

  def updateUser (id : String) : Unit = {
    // TODO
  }

  def deleteUser (id : String) : Unit = {
    // TODO
  }
}