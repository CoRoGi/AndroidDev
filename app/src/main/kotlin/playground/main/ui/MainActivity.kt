package playground.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.FloatRange
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import playground.main.ui.state.TileColor
import playground.main.ui.vm.CountViewModel
import playground.main.ui.vm.TileViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Makes the OS bottom button row transparent
        enableEdgeToEdge()
        // Hides the default action bar
        actionBar?.hide()
        setContent {
//            Counter()
//            BasicShapeCanvas()

//            Box(
//                modifier = Modifier.fillMaxSize()
//            ) {
//                Row(
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier
//                        .background(Color.Gray)
//                        .fillMaxHeight()
//                        .align(Alignment.Center)
//
//                ) {
//                    Column(
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier
//                            .background(Color.Black)
//                            .fillMaxHeight()
//                            .align(Alignment.CenterVertically)
//                            .padding(7.5.dp)
//                    ) {
//                        Tile(
//                            viewModel = hiltViewModel<TileViewModel>(key = "tile1"),
//                            centerX = 45.0f,
//                            centerY = 45.0f,
//                            startingColor = TileColor.Green()
//                        )
//                        Tile(
//                            viewModel = hiltViewModel<TileViewModel>(key = "tile2"),
//                            centerX = 45.0f,
//                            centerY = 45.0f,
//                            startingColor = TileColor.Green()
//                        )
//                    }
//                    Column(
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier
//                            .background(Color.Black)
//                            .fillMaxHeight()
//                            .offset(y = -15.dp)
//                            .align(Alignment.CenterVertically)
//                            .padding(7.5.dp)
//                    ) {
//                        Tile(
//                            viewModel = hiltViewModel<TileViewModel>(key = "tile3"),
//                            centerX = 45.0f,
//                            centerY = 45.0f,
//                            startingColor = TileColor.Green()
//                        )
//                        Tile(
//                            viewModel = hiltViewModel<TileViewModel>(key = "tile4"),
//                            centerX = 45.0f,
//                            centerY = 45.0f,
//                            startingColor = TileColor.Green()
//                        )
//                    }
//                }
//            }
            TileGrid(size = 5)
//            Tile(
//                viewModel = hiltViewModel<TileViewModel>(key = "tile2"),
//                centerX = 400.0f,
//                centerY = 400.0f,
//                startingColor = TileColor.Green()
//            )
        }
    }

//    @Composable
//    fun Counter(viewModel: CountViewModel = viewModel(), modifier: Modifier = Modifier) {
//        val uiState by viewModel.uiState.collectAsState()
//        Row(
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically ,
//            modifier = Modifier.fillMaxSize()
//        ) {
////        Text(
////            text = "Hello $name"
////        )
//            Button(
//                content = { Text(text = "count is ${uiState.currentCount}") },
//                onClick = {  viewModel.incrementCount() }
//            )
//        }
//    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        Counter()
    }
}

@Composable
fun Counter(viewModel: CountViewModel = viewModel(), modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
//        Text(
//            text = "Hello $name"
//        )
        Button(
            content = { Text(text = "count is ${uiState.currentCount}") },
            onClick = { viewModel.incrementCount() }
        )
    }
}

@Composable
fun BasicShapeCanvas(viewModel: TileViewModel = hiltViewModel(key = "basic")) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        Box(
            modifier = Modifier
                .drawWithCache {
                    val roundedPolygon = RoundedPolygon(
                        numVertices = 6,
                        radius = 30f,
                        centerX = size.width / 2,
                        centerY = size.height / 3
                    )
                    val roundedPolygonPath = roundedPolygon
                        .toPath()
                        .asComposePath()
                    onDrawBehind {
                        drawPath(roundedPolygonPath, color = uiState.color.color)
                    }
                }
                .size(30.dp, 30.dp)
                .clickable { viewModel.updateTile() }
        )

    Box(
        modifier = Modifier
            .drawWithCache {
                val roundedPolygon = RoundedPolygon(
                    numVertices = 6,
                    radius = 30f,
                    centerX = size.width / 2,
                    centerY = size.height / 3 + (size.minDimension / 8)
                )
                val roundedPolygonPath = roundedPolygon
                    .toPath()
                    .asComposePath()
                onDrawBehind {
                    drawPath(roundedPolygonPath, color = uiState.color.color)
                }
            }
            .size(30.dp, 30.dp)
            .clickable { viewModel.updateTile() }
    )

    Box(
        modifier = Modifier
            .drawWithCache {
                val roundedPolygon = RoundedPolygon(
                    numVertices = 6,
                    radius = 30f,
                    centerX = size.width / 2 + (size.minDimension / 11),
                    centerY = size.height / 3 + (size.minDimension / 16)
                )
                val roundedPolygonPath = roundedPolygon
                    .toPath()
                    .asComposePath()
                onDrawBehind {
                    drawPath(roundedPolygonPath, color = uiState.color.color)
                }
            }
            .size(30.dp, 30.dp)
            .clickable { viewModel.updateTile() }
    )

    Box(
        modifier = Modifier
            .drawWithCache {
                val roundedPolygon = RoundedPolygon(
                    numVertices = 6,
                    radius = 30f,
                    centerX = size.width / 2 - (size.minDimension / 11),
                    centerY = size.height / 3 + (size.minDimension / 16)
                )
                val roundedPolygonPath = roundedPolygon
                    .toPath()
                    .asComposePath()
                onDrawBehind {
                    drawPath(roundedPolygonPath, color = uiState.color.color)
                }
            }
            .size(30.dp, 30.dp)
            .clickable { viewModel.updateTile() }
    )
}

@Composable
fun Tile(viewModel: TileViewModel, centerX: Float, centerY: Float, startingColor: TileColor, position: Pair<Int, Int>) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier
            .drawWithCache {
                val roundedPolygon = RoundedPolygon(
                    numVertices = 6,
                    radius = 60f,
                    centerX = centerX,
                    centerY = centerY
                )
                val roundedPolygonPath = roundedPolygon
                    .toPath()
                    .asComposePath()
                onDrawBehind {
                    drawPath(roundedPolygonPath, color = uiState.color.color)
                }
            }
            .size(30.dp)
            .padding(top = 7.5.dp, bottom = 10.dp)
            .clickable {
                viewModel.updateTile()
                println("TilePosition is: $position")
            }
    )
    viewModel.setColor(startingColor)
}

@Composable
fun TileGrid(size: Int) {
    val grid = ArrayList<ArrayList<@Composable () -> Unit>>()
    for (i in 0 until size) {
        val column = ArrayList<@Composable () -> Unit>()
        for (j in 0 until size) {
            println("Column size is ${column.size}")
            column.add {
                Tile(
                    hiltViewModel<TileViewModel>(key = "$i,$j"),
                    45.0f,
                    45.0f,
                    TileColor.Green(),
                    Pair<Int, Int>(i, j)
                )
            }
        }
        grid.add(column)
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(20.dp))
        Timer(
            totalTime = 10L * 1000L,
            handleColor = Color.Green,
            inactiveBarColor = Color.DarkGray,
            activeBarColor = Color(0xFF37B900),
            modifier = Modifier.size(100.dp)
        )
        Box(modifier = Modifier.size(20.dp))
        for (i in 0 until size) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                for (j in 0 until size) {
                    if (j % 2 == 0) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .background(Color.Black)
                                .align(Alignment.CenterVertically)
                                .offset(y = -15.dp)
                                .padding(7.5.dp)
                        ) {
                            grid[i][j]()
                        }
                    } else {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .background(Color.Black)
                                .align(Alignment.CenterVertically)
                                .padding(7.5.dp)
                        ) {
                            grid[i][j]()
                        }
                    }
                }
            }
        }
        var runForwards by remember { mutableStateOf(false) }
        val progress: Float by animateFloatAsState(
            if (runForwards) 1f else 0f,
            animationSpec = tween(
                durationMillis = 10_000,
                easing = LinearEasing
            )
        )
        SegmentedProgressIndicator(progress = progress, modifier = Modifier.padding(top = 64.dp, start = 32.dp, end = 32.dp).fillMaxWidth())
        Button(
            onClick = { runForwards = !runForwards },
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Text(
                text = if (runForwards) "Reverse Animation" else "Forward Animation"
            )
        }
    }
}

@Composable
fun Timer(
    totalTime: Long,
    handleColor: Color,
    activeBarColor: Color,
    inactiveBarColor: Color,
    modifier: Modifier = Modifier,
    initialValue: Float = 1f,
    strokeWidth: Dp = 5.dp
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    var value by remember {
        mutableFloatStateOf(initialValue)
    }
    var currentTime by remember {
        mutableLongStateOf(totalTime)
    }
    var isTimerRunning by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if (currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.onSizeChanged {
            size = it
        }
    ) {
        Canvas(modifier = modifier){
            drawArc(
                color = inactiveBarColor,
                startAngle = -215f,
                sweepAngle = 250f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(),cap= StrokeCap.Round)
            )
            drawArc(
                color = activeBarColor,
                startAngle = -215f,
                sweepAngle = 250f * value,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(),cap= StrokeCap.Round)
            )
            val center = Offset(size.width/2f,size.height/2f)
            val beta = (250f * value + 145f) * (PI / 180f).toFloat()
            val r = size.width/2f
            val a = cos(beta) * r
            val b = sin(beta) * r

            drawPoints(
                listOf(Offset(center.x + a,center.y + b)),
                pointMode = PointMode.Points,
                color = handleColor,
                strokeWidth = (strokeWidth * 3f).toPx(),
                cap = StrokeCap.Round
            )
        }

        Text(
            text = (currentTime / 1000L).toString(),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

//        Button(
//            onClick = {
//                if(currentTime <= 0L) {
//                    currentTime = totalTime
//                    isTimerRunning = true
//                } else {
//                    isTimerRunning = !isTimerRunning
//                }
//            },
//            modifier = Modifier
//                .align(Alignment.BottomCenter),
//            colors = ButtonDefaults.buttonColors(containerColor =
//            if (!isTimerRunning || currentTime <= 0L) {
//                Color.Green
//            }
//            else {
//                Color.Red
//            }
//            )
//        ) {
//            Text(
//                text =
//                if (isTimerRunning && currentTime>0L) "Stop"
//                else if(!isTimerRunning && currentTime>=0L) "start"
//                else "restart"
//            )
//        }
        if (currentTime <= 0L) {
            currentTime = totalTime
            isTimerRunning = true
        }
    }
}

private const val BackgroundOpacity = 0.25f
private const val NumberOfSegments = 8
private val ProgressHeight = 4.dp
private val SegmentGap = 8.dp

@Composable
fun SegmentedProgressIndicator(
    /*@FloatRange(from = 0.0, to = 1.0)*/
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = Color.Blue,
    backgroundColor: Color = color.copy(alpha = BackgroundOpacity),
    progressHeight: Dp = ProgressHeight,
    numberOfSegments: Int = NumberOfSegments,
    segmentGap: Dp = SegmentGap
) {
    check(progress in 0f..1f) { "Invalid progress $progress"}
    check(numberOfSegments > 0) { "Number of segments must be greater than 1" }

    val gap: Float
    val barHeight: Float
    with(LocalDensity.current) {
        gap = segmentGap.toPx()
        barHeight = progressHeight.toPx()
    }
    Canvas(
        modifier
            .progressSemantics(progress)
            .height(progressHeight)
    ) {
        drawSegments(1f, backgroundColor, barHeight, numberOfSegments, gap)
        drawSegments(progress, color, barHeight, numberOfSegments, gap)
    }
}

private fun DrawScope.drawSegments(
    progress: Float,
    color: Color,
    segmentHeight: Float,
    segments: Int,
    segmentGap: Float,
) {
    val width = size.width
    val start = 0f
    val gaps = (segments - 1) * segmentGap
    val segmentWidth = (width - gaps) / segments
    val barsWidth = segmentWidth * segments
    // 1
    val end = barsWidth * progress + (progress * segments).toInt() * segmentGap

    repeat(segments) { index ->
        val offset = index * (segmentWidth + segmentGap)
        if (offset < end) {
            val segmentEnd = (offset + segmentWidth).coerceAtMost(end)
            val segmentStart = start + offset
            drawRect(
                color,
                Offset(segmentStart, 0f),
                Size(segmentEnd - segmentStart, segmentHeight)
            )
        }
    }
}

