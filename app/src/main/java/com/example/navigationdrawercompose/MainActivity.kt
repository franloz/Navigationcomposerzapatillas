package com.example.navigationdrawercompose


import com.example.navigationdrawercompose.retrofitjsonjetpack.UserInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navigationdrawercompose.ui.theme.NavigationDrawerComposeTheme
import com.example.navigationdrawercompose.ui.theme.UserInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.URL



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationDrawerComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }
}


@Composable
fun MainScreen() {

    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scope = scope, scaffoldState = scaffoldState) },
        drawerContent = {
            Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
        }
    ) {
        Navigation(navController = navController)
    }

}

@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState) {

    TopAppBar(
        title = { Text(text = "Actividad 1º", fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Filled.PlayArrow, "")
            }
        },
        backgroundColor = Color(0xFF03DAC5),
        contentColor = Color.Black
    )

}

@Composable
fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {

    val items = listOf(
        NavegacionItem.Insertar,
        NavegacionItem.Borrar,
        NavegacionItem.Mostrar,
        /*NavegacionItem.Share,
        NavegacionItem.Contact*/
    )

    Column(
        modifier = Modifier
            .background(color = Color(0xFF5C6364))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color(0xFF11ACFA)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.zapa),
                contentDescription = "zapatillas",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(8.dp)
            )

        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { items ->
            DrawerItem(item = items, selected = currentRoute == items.route, onItemClick = {

                navController.navigate(items.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }

                scope.launch {
                    scaffoldState.drawerState.close()
                }

            })
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Práctica Compose",
            color = Color.White,
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.CenterHorizontally)
        )

    }
}

@Composable
fun DrawerItem(item: NavegacionItem, selected: Boolean, onItemClick: (NavegacionItem) -> Unit) {
    val background = if (selected) R.color.teal_200 else android.R.color.transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) }
            .height(60.dp)
            .background(colorResource(id = background))
            .padding(start = 15.dp)
    ) {

        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            colorFilter = ColorFilter.tint(Color.White),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(24.dp)
                .width(24.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = item.title,
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
        )

    }

}

@Composable
fun PantallaInsertar() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Insert()
        /*Text(
            text = "Home Screen",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )*/

    }
}




/////////insertar
@Composable
fun Insert() {
    var textFieldValueMarca by rememberSaveable { mutableStateOf("") }
    var textFieldValueModelo by rememberSaveable { mutableStateOf("") }
    var textFieldValueTalla by rememberSaveable { mutableStateOf("") }
    val context= LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally


    ) {

        Text(
            text = "Insertar datos",
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            color = Color(0xFFF2F4F5),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)

        )
        Spacer(Modifier.height(30.dp) )
        TextField(
            shape = RoundedCornerShape(50.dp),
            value = textFieldValueMarca,
            onValueChange = { nuevo ->
                textFieldValueMarca = nuevo
            },
            label = {
                Text(text = "Introducir marca",
                    color = Color(0xFFF2F4F5),
                )
            },
            modifier = Modifier
                .padding(10.dp),

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(textAlign = TextAlign.Center,color = Color(0xFFF2F4F5))
        )


        TextField(
            shape = RoundedCornerShape(50.dp),
            value = textFieldValueModelo,
            onValueChange = { nuevo ->
                textFieldValueModelo = nuevo
            },
            label = {
                Text(text = "Introducir modelo",
                    color = Color(0xFFF2F4F5))

            },
            modifier = Modifier
                .padding( 10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(textAlign = TextAlign.Center,color = Color(0xFFF2F4F5))
        )


        TextField(
            shape = RoundedCornerShape(50.dp),
            value = textFieldValueTalla,
            onValueChange = { nuevo ->
                textFieldValueTalla = nuevo
            },
            label = {
                Text(text = "Introducir talla",
                    color = Color(0xFFF2F4F5))
            },
            modifier = Modifier
                .padding( 10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(textAlign = TextAlign.Center,color = Color(0xFFF2F4F5))
        )

        Spacer(Modifier.height(20.dp) )


        Button(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .size(width = 100.dp, height = 50.dp)

            ,


            onClick = {
                if(textFieldValueMarca.isEmpty()||textFieldValueModelo.isEmpty()||textFieldValueTalla.isEmpty()){
                        Toast.makeText(context,"Rellene los campos",Toast.LENGTH_LONG).show()
                }else{
                    insertar(textFieldValueMarca,textFieldValueModelo,textFieldValueTalla)
                    textFieldValueMarca=""
                    textFieldValueModelo=""
                    textFieldValueTalla=""
                    Toast.makeText(context,"Insertado correcto",Toast.LENGTH_LONG).show()
                }

            }
        ){
            Icon(Icons.Filled.Add, "",
            tint= Color(0xFFF2F4F5))
        }

    }

}

fun insertar(marca:String,modelo:String,talla:String){

    val url = "http://iesayala.ddns.net/zapatilla/insertzapatilla.php/?marca=$marca&modelo=$modelo&talla=$talla"

    leerUrl(url)

}

fun leerUrl(urlString:String){
    GlobalScope.launch(Dispatchers.IO)   {
        val response = try {
            URL(urlString)
                .openStream()
                .bufferedReader()
                .use { it.readText() }
        } catch (e: IOException) {
            "Error with ${e.message}."
            Log.d("io", e.message.toString())
        } catch (e: Exception) {
            "Error with ${e.message}."
            Log.d("io", e.message.toString())
        }
    }

    return
}



@Composable
fun PantallaBorrar() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Delete()
    }
}

@Composable
fun Delete() {
    var textFieldValueModelo by rememberSaveable { mutableStateOf("") }
    val context= LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = "Borrar datos",
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            color = Color(0xFFF2F4F5),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
        Spacer(Modifier.height(30.dp) )

        TextField(
            shape = RoundedCornerShape(50.dp),
            value = textFieldValueModelo,
            onValueChange = { nuevo ->
                textFieldValueModelo = nuevo
            },
            label = {
                Text(text = "Introducir modelo",
                    color = Color(0xFFF2F4F5),)
            },
            modifier = Modifier
                .padding( 10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(textAlign = TextAlign.Right,color = Color(0xFFF2F4F5))
        )

        Spacer(Modifier.height(20.dp) )

        Button(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .size(width = 100.dp, height = 50.dp)
            ,


            onClick = {
                if(textFieldValueModelo.isEmpty()){
                    Toast.makeText(context,"Rellene los campos",Toast.LENGTH_LONG).show()
                }else {
                    borrar(textFieldValueModelo)
                    textFieldValueModelo = ""
                    Toast.makeText(context,"Borrado correcto",Toast.LENGTH_LONG).show()
                }
            }
        ){
            Icon(Icons.Filled.Delete, "",
                tint= Color(0xFFF2F4F5))
        }


    }

}

fun borrar(modelo:String){
    val url = "http://iesayala.ddns.net/zapatilla/deletezapatilla.php/?modelo=$modelo"

    leerUrl(url)
}

@Composable
fun Mostrar() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Llamada()

    }
}
@Composable
fun cargarJson(): UserInfo {

    var users by rememberSaveable { mutableStateOf(UserInfo()) }
    val user = UserInstance.userInterface.userInformation()
    user.enqueue(object : Callback<UserInfo> {
        override fun onResponse(
            call: Call<UserInfo>,
            response: Response<UserInfo>
        ) {
            val userInfo: UserInfo? = response.body()
            if (userInfo != null) {

                users = userInfo

            }
        }

        override fun onFailure(call: Call<UserInfo>, t: Throwable)
        {
            Log.d("datos",users.toString())
        }

    })

    return users
}


@Composable
fun Llamada() {
    var lista= cargarJson()
    Row(){
        Column(modifier = Modifier.weight(3f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text="Marca",
                color = Color(0xFF03DAC5),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(20.dp),

                )
        }
        Column(modifier = Modifier.weight(3f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text="Modelo",
                color = Color(0xFF03DAC5),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(20.dp),

                )
        }
        Column(modifier = Modifier.weight(3f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text="Talla",
                color = Color(0xFF03DAC5),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(20.dp),

                )
        }
    }
    LazyColumn()

    {
        items(lista) { usu ->
            Box(
                Modifier
                    .background(Color(0xFF03DAC5))
                    .width(400.dp)){
               Row(){
                   Column(modifier = Modifier.weight(3f),
                       verticalArrangement = Arrangement.Center,
                       horizontalAlignment = Alignment.CenterHorizontally){
                       Text(text=usu.marca,
                           color = Color(0xFFF2F4F5),
                           textAlign = TextAlign.Center,
                           fontSize = 24.sp,
                           modifier = Modifier
                               .padding(20.dp),

                       )
                   }
                   Column(modifier = Modifier.weight(3f),
                       verticalArrangement = Arrangement.Center,
                       horizontalAlignment = Alignment.CenterHorizontally){
                       Text(text=usu.modelo,
                           color = Color(0xFFF2F4F5),
                           fontSize = 24.sp,
                           textAlign = TextAlign.Center,
                           modifier = Modifier
                               .padding(20.dp),
                       )
                   }
                   Column(modifier = Modifier.weight(3f),
                       verticalArrangement = Arrangement.Center,
                       horizontalAlignment = Alignment.CenterHorizontally){
                       Text(text=usu.talla.toString(),
                           color = Color(0xFFF2F4F5),
                           fontSize = 24.sp,
                           textAlign = TextAlign.Center,
                           modifier = Modifier
                               .padding(20.dp),
                       )
                   }

               }


            }
            Spacer(
                modifier = Modifier
                    .height(5.dp)
            )


        }
    }


}

@Composable
fun Navigation(navController: NavHostController) {

    NavHost(navController, startDestination = NavegacionItem.Insertar.route) {

        composable(NavegacionItem.Insertar.route) {
            PantallaInsertar()
        }

        composable(NavegacionItem.Borrar.route) {
            PantallaBorrar()
        }

        composable(NavegacionItem.Mostrar.route) {
            Mostrar()
        }

    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NavigationDrawerComposeTheme {
        MainScreen()
    }
}