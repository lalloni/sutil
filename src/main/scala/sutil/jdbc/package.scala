package sutil

package jdbc {

  case class Field(name: String)

}

package object jdbc {

  import java.sql.{ Statement, ResultSet, PreparedStatement, Connection }

  implicit def str2field(name: String) = Field(name)

  private[jdbc] def strm[X](f: RichResultSet ⇒ X, rs: ResultSet): Stream[X] =
    if (rs.next) Stream.cons(f(new RichResultSet(rs)), strm(f, rs))
    else {
      rs.close()
      Stream.empty
    }

  def query[X](s: String, f: RichResultSet ⇒ X)(implicit stat: Statement) =
    strm(f, stat.executeQuery(s))

  implicit def conn2Statement(conn: Connection): Statement = conn.createStatement

  implicit def rrs2Boolean(rs: RichResultSet) = rs.nextBoolean
  implicit def rrs2Byte(rs: RichResultSet) = rs.nextByte
  implicit def rrs2Int(rs: RichResultSet) = rs.nextInt
  implicit def rrs2Long(rs: RichResultSet) = rs.nextLong
  implicit def rrs2Float(rs: RichResultSet) = rs.nextFloat
  implicit def rrs2Double(rs: RichResultSet) = rs.nextDouble
  implicit def rrs2String(rs: RichResultSet) = rs.nextString
  implicit def rrs2Date(rs: RichResultSet) = rs.nextDate

  implicit def resultSet2Rich(rs: ResultSet) = new RichResultSet(rs)
  implicit def rich2ResultSet(r: RichResultSet) = r.rs

  implicit def ps2Rich(ps: PreparedStatement) = new RichPreparedStatement(ps)
  implicit def rich2PS(r: RichPreparedStatement) = r.ps

  implicit def str2RichPrepared(s: String)(implicit conn: Connection): RichPreparedStatement = conn prepareStatement (s)
  implicit def conn2Rich(conn: Connection) = new RichConnection(conn)

  implicit def st2Rich(s: Statement) = new RichStatement(s)
  implicit def rich2St(rs: RichStatement) = rs.s

}
