package controllers

import javax.inject.Inject
import com.twitter.finagle.httpx.Request
import com.twitter.finatra._

import models.User
import services.UsersService

/**
  * Created by pierre on 27/05/16.

  */

class UsersController @Inject () (usersService: UsersService) extends Controller {

  /**
    * Return all the users in database
    */
  get("/users") {request : Request =>
    val users: Seq[User] = usersService.getUsers().toSeq.sorted
    Map("posts" -> users, "count" -> users.size)
  }

  /**
    * Return the user identified by the id taken in parameter
    */
  get("/users/:id") {request : Request =>

    val id : String = request.params.getOrElse("id","Fail")
    val post : Option[User] =  usersService.getUserById(id)

    if (post.nonEmpty) Map("posts" -> post.get)
    else Map("Error" -> "ID not recognized")
  }
}
