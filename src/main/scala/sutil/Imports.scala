package sutil

import math.{DigitImports, MathImports}
import path.{GlobImports, RichFileImports}
import jdbc.JDBCImports
import control.ControlImports
import unit.binary.BinaryImports

trait Imports

object Imports
  extends Imports
          with RichFileImports
          with GlobImports
          with BinaryImports
          with MathImports
          with DigitImports
          with ControlImports
          with JDBCImports

