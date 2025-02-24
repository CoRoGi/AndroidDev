package playground.main.model

class AdjacencyMatrix<T>: Graph<T> {
    override val vertices = arrayListOf<Vertex<T>>()
    override val weights = arrayListOf<ArrayList<Double?>>()

    override fun createVertex(data: T): Vertex<T> {
        val vertex = Vertex(vertices.count(), data)
        vertices.add(vertex)

        weights.forEach {
            it.add(null)
        }

        val row = ArrayList<Double?>(vertices.count())
        repeat(vertices.count()) {
            row.add(null)
        }

        weights.add(row)
        return vertex
    }

    override fun addDirectedEdge(source: Vertex<T>, destination: Vertex<T>, weight: Double) {
        val currentWeight = weights[source.index][destination.index]
        weights[source.index][destination.index] = if (currentWeight != null) minOf(currentWeight, weight) else weight
    }

    override fun edges(source: Vertex<T>): ArrayList<Edge<T>> {
        val edges = arrayListOf<Edge<T>>()

        (0 until weights.size).forEach {
            val weight = weights[source.index][it]
            if (weight != null) {
                edges.add(Edge(source, vertices[it], weight))
            }
        }

        return edges
    }

    override fun weight(source: Vertex<T>, destination: Vertex<T>): Double? {
        return weights[source.index][destination.index]
    }
}