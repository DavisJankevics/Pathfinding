package backend

class Tile (val gridLocation: GridLocation, var gCost : Int, var hCost : Int, var fCost : Int, val parent : Tile = null  ) {
  override def toString: String = {
    s"Tile : $gridLocation , $gCost , $hCost , $fCost"
  }
  override def equals(obj: Any): Boolean = {
    var equal : Boolean = false

    if(this.getClass == obj.getClass){
      obj match {
        case obj : Tile => {
          if(obj.isInstanceOf[Tile] && obj.gridLocation == this.gridLocation) {
            equal = true
          }
        }
        case _ => equal = false
      }
    }
    equal
  }

}
