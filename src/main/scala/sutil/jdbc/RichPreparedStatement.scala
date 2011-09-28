package sutil.jdbc

import java.sql._
import JDBC._

class RichPreparedStatement(val ps: PreparedStatement) {

  var pos = 1;

  private def inc = {
    pos = pos + 1
    this
  }

  def execute[X](f: RichResultSet ⇒ X): Stream[X] = {
    pos = 1
    strm(f, ps.executeQuery)
  }

  def <<![X](f: RichResultSet ⇒ X): Stream[X] = execute(f);

  def execute = { pos = 1; ps.execute }

  def <<! = execute

  def <<(x: Option[Any]): RichPreparedStatement = {
    x match {
      case None ⇒
        ps.setNull(pos, Types.NULL)
        inc
      case Some(y) ⇒ (this << y)
    }
  }

  def <<(x: Any): RichPreparedStatement = {
    x match {
      case z: Boolean ⇒
        ps.setBoolean(pos, z)
      case z: Byte ⇒
        ps.setByte(pos, z)
      case z: Int ⇒
        ps.setInt(pos, z)
      case z: Long ⇒
        ps.setLong(pos, z)
      case z: Float ⇒
        ps.setFloat(pos, z)
      case z: Double ⇒
        ps.setDouble(pos, z)
      case z: String ⇒
        ps.setString(pos, z)
      case z: Date ⇒
        ps.setDate(pos, z)
      case z ⇒ ps.setObject(pos, z)
    }
    inc
  }

}
