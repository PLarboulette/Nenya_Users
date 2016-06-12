package database.redis

import models.User
import play.api.libs.json.JsValue

import scala.collection.mutable

/**
  * Created by Pierre on 27/05/16.
  */

object RedisHelper {

  def getUsers (filters : JsValue) : mutable.Set[User] = {
    val result = mutable.Set[User]()
    result
  }

}
