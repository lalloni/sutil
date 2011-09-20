package sutil.jdbc

import java.sql.Date
import java.sql.ResultSet

class RichResultSet(val rs: ResultSet) {

  lazy val metadata = rs.getMetaData
  lazy val columns = 1.to(metadata.getColumnCount).map(metadata.getColumnName)
  lazy val types = 1.to(metadata.getColumnCount).map(metadata.getColumnClassName).map(Class.forName)

  def index(field: Field) = columns.indexOf(field.name) + 1

  private var pos = 1

  def position = pos

  def apply(i: Int) = { pos = i; this }

  def nextBoolean: Option[Boolean] = { val ret = rs.getBoolean(pos); pos = pos + 1; if (rs.wasNull) None else Some(ret) }
  def nextByte: Option[Byte] = { val ret = rs.getByte(pos); pos = pos + 1; if (rs.wasNull) None else Some(ret) }
  def nextInt: Option[Int] = { val ret = rs.getInt(pos); pos = pos + 1; if (rs.wasNull) None else Some(ret) }
  def nextLong: Option[Long] = { val ret = rs.getLong(pos); pos = pos + 1; if (rs.wasNull) None else Some(ret) }
  def nextFloat: Option[Float] = { val ret = rs.getFloat(pos); pos = pos + 1; if (rs.wasNull) None else Some(ret) }
  def nextDouble: Option[Double] = { val ret = rs.getDouble(pos); pos = pos + 1; if (rs.wasNull) None else Some(ret) }
  def nextString: Option[String] = { val ret = rs.getString(pos); pos = pos + 1; if (rs.wasNull) None else Some(ret) }
  def nextDate: Option[Date] = { val ret = rs.getDate(pos); pos = pos + 1; if (rs.wasNull) None else Some(ret) }

  def boolean(field: Field): Option[Boolean] = { val r = rs.getBoolean(index(field)); if (rs.wasNull) None else Some(r) }
  def byte(field: Field): Option[Byte] = { val r = rs.getByte(index(field)); if (rs.wasNull) None else Some(r) }
  def int(field: Field): Option[Int] = { val r = rs.getInt(index(field)); if (rs.wasNull) None else Some(r) }
  def long(field: Field): Option[Long] = { val r = rs.getLong(index(field)); if (rs.wasNull) None else Some(r) }
  def float(field: Field): Option[Float] = { val r = rs.getFloat(index(field)); if (rs.wasNull) None else Some(r) }
  def double(field: Field): Option[Double] = { val r = rs.getDouble(index(field)); if (rs.wasNull) None else Some(r) }
  def string(field: Field): Option[String] = { val r = rs.getString(index(field)); if (rs.wasNull) None else Some(r) }
  def date(field: Field): Option[Date] = { val r = rs.getDate(index(field)); if (rs.wasNull) None else Some(r) }

  def foldLeft[X](init: X)(f: (ResultSet, X) ⇒ X): X = rs.next match {
    case false ⇒ init
    case true  ⇒ foldLeft(f(rs, init))(f)
  }
  
  def map[X](f: ResultSet ⇒ X) = {
    var ret = List.empty[X]
    while (rs.next()) ret = f(rs) :: ret
    ret.reverse
  }
  
  def apply[T](field: Field)(implicit m: Manifest[T]): Option[T] = {
    val e = m.erasure
    val r =
      if (e == classOf[String]) string(field)
      else if (e == classOf[Date]) date(field)
      else if (e == classOf[Int]) int(field)
      else if (e == classOf[Long]) byte(field)
      else if (e == classOf[Boolean]) boolean(field)
      else if (e == classOf[Byte]) byte(field)
      else if (e == classOf[Float]) float(field)
      else if (e == classOf[Double]) double(field)
      else sys.error("Type %s not supported for field %s" format (e, field.name))
    r.asInstanceOf[Option[T]]
  }

}
