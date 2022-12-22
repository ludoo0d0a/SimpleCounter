package fr.geoking.simplecounter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.geoking.simplecounter.ui.theme.SimpleCounterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleCounterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Counter()
                }
            }
        }
    }
}

// https://semicolonspace.com/android-jetpack-compose-state/

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Counter() {
    val counter = remember {
        mutableStateOf(value = 0)
    }

    Scaffold(
        topBar = {
            TopAppBar() {
                Text("Counter App")
            }
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                counter.value++
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },

        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                SimpleStateButton()
                StatefulButton();
                ViewModelButton()
                Text("UI Float counter ", color = Color.Black)
                Text("${counter.value}", color = Color.Blue)
            }
        }
    )

}


@Composable
fun SimpleStateButton() {
    var count by remember { mutableStateOf(0) }
    Log.d("Before Button()", "Count = $count")
    Button(
        onClick = {
            count++
            Log.d("Inside onClick", "Count = $count")
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Yellow
        )
    ) {
        Text(text = "Simple Count $count")
    }
}

// https://developer.android.com/codelabs/jetpack-compose-state#0
// https://www.youtube.com/watch?v=PMMY23F0CFg
@Composable
fun StatefulButton() {
    //var count by rememberSaveable { mutableStateOf(0) }
    var count by remember { mutableStateOf(0) }
    Log.d("Before Button()", "Count = $count")
    StatelessButton(count = count, onIncrement = {count++})
}

// UDF component (Unidirectional Data Flow)
// state goes down, event goes up
@Composable
private fun StatelessButton(
    count: Int,
    onIncrement: () -> Unit,
) {
    Button(
        onClick = onIncrement,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Gray
        )
    ) {
        Text(text = "Stateless/full Count $count")
    }
}



class MyViewModel : ViewModel() {
    var count = MutableLiveData<Int>(0)
    fun increment() {
        count.value = count.value?.plus(1)
    }
}

@Composable
fun ViewModelButton(myViewModel: MyViewModel = viewModel()) {
    // LiveData + ViewModel
    val clickCount by myViewModel.count.observeAsState(0)
    Button(
        onClick = {
            myViewModel.increment()
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Green
        )
    ) {
        Text(text = "VM Count $clickCount")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SimpleCounterTheme {
        Counter()
    }
}