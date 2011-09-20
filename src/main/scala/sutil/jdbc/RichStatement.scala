package sutil.jdbc

import java.sql.Statement

class RichStatement(val s: Statement) {

  def <<(sql: String) = {
    s.execute(sql)
    this
  }

  def <<(sql: Seq[String]) = {
    for (x ← sql) s.execute(x)
    this
  }
  
}
