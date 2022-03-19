
document.addEventListener("click", function (event) {
    const tile = {x: Math.floor(event.layerX / tileSize), y: Math.floor(event.layerY / tileSize)};
    console.log(tile)
    socket.emit("destination", JSON.stringify(tile));
});
