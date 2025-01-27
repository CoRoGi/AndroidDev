package playground.main.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import playground.main.model.AdjacencyMatrix
import playground.main.model.Graph
import playground.main.ui.model.TileModel

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideTileModelGraph(): Graph<TileModel> {
        return AdjacencyMatrix<TileModel>()
    }
}