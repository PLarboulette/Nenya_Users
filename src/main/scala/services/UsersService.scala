package services

import scala.collection.mutable

import models.User


/**
  * Created by pierre on 19/04/16.
  */
class UsersService {

  def getUsers: mutable.Set[User] = {

    // Use Redis cache or MongoDB
    val result: mutable.Set[User] = mutable.Set[User] ()
    result
  }

  def getUserById (userId : String) : Option[User] = {
    val posts : mutable.Set[User] = getUsers.filter(user => user.id == userId)
    posts.headOption
  }

  def createUser (): Unit = {
    // TODO
  }

  def updateUser (id : String) : Unit = {
    // TODO
  }

  def deleteUser (id : String) : Unit = {
    // TODO
  }
}