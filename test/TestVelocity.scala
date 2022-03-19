package test

import backend.physics.PhysicsVector
import backend.{GridLocation, PathFinding}
import org.scalatest._

class TestVelocity extends FunSuite{
  test("A"){
    val path : List[GridLocation] = List(new GridLocation(1,0), new GridLocation(2,0),new GridLocation(3,0))
    val retVal = PathFinding.getVelocity(path, new PhysicsVector(1,0))
    println( retVal.magnitude() )
  }
}
