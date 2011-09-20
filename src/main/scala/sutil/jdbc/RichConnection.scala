package sutil.jdbc

import java.sql.Connection

class RichConnection(val conn: Connection) {
  def <<(sql: String) = new RichStatement(conn.createStatement) << sql;
  def <<(sql: Seq[String]) = new RichStatement(conn.createStatement) << sql;
}
