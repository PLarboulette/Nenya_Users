package models

/**
  * Created by Pierre on 05/04/2016.
  */
 case class User(id: String,
                firstName: String,
                lastName: String,
                rank : String
                )
  extends Ordered[User] {
  def compare(that: User): Int = that.lastName compare this.lastName
}



