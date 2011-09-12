package sutil.jdbc

import java.sql.{ Types, Statement, ResultSet, PreparedStatement, Connection, Date }

object RichJDBC {

  case class Field(name: String)

  implicit def str2field(name: String) = Field(name)

  private def strm[X](f: RichResultSet ⇒ X, rs: ResultSet): Stream[X] =
    if (rs.next) Stream.cons(f(new RichResultSet(rs)), strm(f, rs))
    else { rs.close(); Stream.empty };

  implicit def query[X](s: String, f: RichResultSet ⇒ X)(implicit stat: Statement) = {
    strm(f, stat.executeQuery(s));
  }

  implicit def conn2Statement(conn: Connection): Statement = conn.createStatement;

  implicit def rrs2Boolean(rs: RichResultSet) = rs.nextBoolean;
  implicit def rrs2Byte(rs: RichResultSet) = rs.nextByte;
  implicit def rrs2Int(rs: RichResultSet) = rs.nextInt;
  implicit def rrs2Long(rs: RichResultSet) = rs.nextLong;
  implicit def rrs2Float(rs: RichResultSet) = rs.nextFloat;
  implicit def rrs2Double(rs: RichResultSet) = rs.nextDouble;
  implicit def rrs2String(rs: RichResultSet) = rs.nextString;
  implicit def rrs2Date(rs: RichResultSet) = rs.nextDate;

  implicit def resultSet2Rich(rs: ResultSet) = new RichResultSet(rs);
  implicit def rich2ResultSet(r: RichResultSet) = r.rs;

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
        else throw error(format("Type %s not supported for field %s", e, field.name))
      r.asInstanceOf[Option[T]]
    }

  }

  implicit def ps2Rich(ps: PreparedStatement) = new RichPreparedStatement(ps);
  implicit def rich2PS(r: RichPreparedStatement) = r.ps;

  implicit def str2RichPrepared(s: String)(implicit conn: Connection): RichPreparedStatement = conn prepareStatement (s);
  implicit def conn2Rich(conn: Connection) = new RichConnection(conn);

  implicit def st2Rich(s: Statement) = new RichStatement(s);
  implicit def rich2St(rs: RichStatement) = rs.s;

  class RichPreparedStatement(val ps: PreparedStatement) {
    var pos = 1;
    private def inc = { pos = pos + 1; this }

    def execute[X](f: RichResultSet ⇒ X): Stream[X] = {
      pos = 1; strm(f, ps.executeQuery)
    }
    def <<![X](f: RichResultSet ⇒ X): Stream[X] = execute(f);

    def execute = { pos = 1; ps.execute }
    def <<! = execute;

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

  class RichConnection(val conn: Connection) {
    def <<(sql: String) = new RichStatement(conn.createStatement) << sql;
    def <<(sql: Seq[String]) = new RichStatement(conn.createStatement) << sql;
  }

  class RichStatement(val s: Statement) {
    def <<(sql: String) = { s.execute(sql); this }
    def <<(sql: Seq[String]) = { for (x ← sql) s.execute(x); this }
  }

}
