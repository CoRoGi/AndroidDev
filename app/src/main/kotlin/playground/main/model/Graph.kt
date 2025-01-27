package playground.main.model

interface Graph<T> {
    val vertices: ArrayList<Vertex<T>>
    val weights: ArrayList<ArrayList<Double?>>


    fun createVertex(data: T): Vertex<T>

    fun addEdge(edgeType: EdgeType,
                source: Vertex<T>,
                destination: Vertex<T>,
                weight: Double?)
    {
        when (edgeType) {
            EdgeType.DIRECTED -> addDirectedEdge(source, destination, weight)
            EdgeType.UNDIRECTED -> addUndirectedEdge(source, destination, weight)
        }
    }

    fun addDirectedEdge(source: Vertex<T>, destination: Vertex<T>, weight: Double?)

    fun addUndirectedEdge(source: Vertex<T>,
                          destination: Vertex<T>,
                          weight: Double?)
    {
        addDirectedEdge(source, destination, weight)
        addDirectedEdge(destination, source, weight)
    }

    fun edges(source: Vertex<T>): ArrayList<Edge<T>>

    fun weight(source: Vertex<T>, destination: Vertex<T>): Double?

}