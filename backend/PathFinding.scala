package backend

import backend.physics.PhysicsVector

import scala.collection.mutable.ArrayBuffer

object PathFinding {

  def findPath(start: GridLocation, end: GridLocation, map: List[List[MapTile]]): List[GridLocation] = {
    var found : Boolean = false
    val openTiles : ArrayBuffer[Tile] = new ArrayBuffer[Tile]()
    val visitedTiles : ArrayBuffer[Tile] = new ArrayBuffer[Tile]()
    var pathTiles : ArrayBuffer[GridLocation] = new ArrayBuffer[GridLocation]()
    val startTile = new Tile(start, 0,0,0)

    openTiles.append(startTile)

    while (openTiles.nonEmpty && !found) {
      var currentLocation = openTiles(0)
      var currentIndex: Int = 0
      var counter: Int = 0
      for (tile <- openTiles) {
        if (tile.fCost < currentLocation.fCost) {
          currentLocation = tile
          currentIndex = counter
        }
        counter += 1
      }
      openTiles.remove(currentIndex)
      visitedTiles.append(currentLocation)

      if (currentLocation.gridLocation.x == end.x && currentLocation.gridLocation.y == end.y) {
        println("Found " + currentLocation)

        var current: Tile = currentLocation
        while (current != null) {
          pathTiles.append(current.gridLocation)
          current = current.parent
          found = true
        }
      }
      val children: ArrayBuffer[Tile] = new ArrayBuffer[Tile]()
      for (n <- 1 to 4) { //8 for diagonal
        var xLocation: Int = currentLocation.gridLocation.x
        var yLocation: Int = currentLocation.gridLocation.y
        if (n <= 2) {
          xLocation += math.pow(-1, n).toInt
        } else if ( n <=4 ) {
          yLocation += math.pow(-1, n).toInt
        }
//        for diagonal movement
//        else if( n <= 6){
//          xLocation += math.pow(-1, n).toInt
//          yLocation += math.pow(-1, n).toInt
//        }else{
//          xLocation += math.pow(-1, n-1).toInt
//          yLocation += math.pow(-1, n).toInt
//        }
        val newTileLocation = new GridLocation(xLocation, yLocation)
        if (newTileLocation.x <= map(0).length - 1 && newTileLocation.x >= 0 && newTileLocation.y <= map.length - 1 && newTileLocation.y >= 0) {
          if (map(newTileLocation.y)(newTileLocation.x).passable) {
            val newTile: Tile = new Tile(newTileLocation, 100000, 100000, 200000, currentLocation)
            children.append(newTile)
          }
        }
      }
      for (child <- children) {
        if (!visitedTiles.contains(child)) {
          child.gCost = currentLocation.gCost + 1
          child.hCost = (math.pow((child.gridLocation.x - end.x), 2) + math.pow((child.gridLocation.y - end.y), 2)).toInt
          child.fCost = child.gCost + child.hCost
        }
        if(!openTiles.contains(child)){
          openTiles.append(child)
        } else {
          val indexOfTile = openTiles.indexOf(child)
          val openTile = openTiles(indexOfTile)
          if (child.gCost < openTile.gCost) {
            openTiles(indexOfTile) = child
          }
        }
      }
    }
    pathTiles = pathTiles.reverse
    println(pathTiles)
    pathTiles.toList
  }


  def getVelocity(path: List[GridLocation], currentLocation: PhysicsVector): PhysicsVector = {
    val curX : Double = math.floor(currentLocation.x)
    val curY : Double = math.floor(currentLocation.y)
    var endVector : PhysicsVector = new PhysicsVector(0.0,0.0)

    if(path.nonEmpty) {
      if(curX != path.last.x || curY != path.last.y) {
        for (place <- path.indices) {
          val tile: GridLocation = path(place)
          if (tile.x == curX && tile.y == curY) {
            if (place != path.length - 1) {
              val nextTile: GridLocation = path(place + 1)
              endVector.x = nextTile.x - currentLocation.x + 0.5
              endVector.y = nextTile.y - currentLocation.y + 0.5
            }

          }
        }
      }
      else {
        if ((path.last.x + 0.5) - currentLocation.x >= 0.1) {
          endVector.x += 0.5
        }else if((path.last.x + 0.5) - currentLocation.x <= -0.1){
          endVector.x -= 0.5
        }
        if ((path.last.y + 0.5) - currentLocation.y >= 0.1){
          endVector.y += 0.5
        }else if ((path.last.y + 0.5) - currentLocation.y <= -0.1){
          endVector.y -= 0.5
        }
      }
    }

    endVector = endVector.normal2d()
    endVector.x *= 5
    endVector.y *= 5
    endVector
  }

}
