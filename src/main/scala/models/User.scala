package models

/**
  * Created by plarboul on 05/04/2016.
  */
 case class User(id: String,
                firstName: String,
                lastName: String,
                rank : String,
                author: String)
  extends Ordered[User] {
  def compare(that: User): Int = that.lastName compare this.lastName
}



