

<?php

$server = "localhost";
$user = "root";
$pass = "clave";
$bd = "zapatillas";

//Creamos la conexión
$conexion = mysqli_connect($server, $user, $pass,$bd)
or die("Ha sucedido un error inexperado en la conexion de la base de datos");

//generamos la consulta
$sql = "SELECT * FROM zapatilla";
mysqli_set_charset($conexion, "utf8"); //formato de datos utf8

if(!$result = mysqli_query($conexion, $sql)) die();

$zapatillas = array(); //creamos un array

while($row = mysqli_fetch_array($result))
{
    $marca=$row['marca'];
    $modelo=$row['modelo'];
    $talla=$row['talla'];


    $zapatillas[] = array('marca'=> $marca, 'modelo'=> $modelo, 'talla'=> $talla);

}

//desconectamos la base de datos
$close = mysqli_close($conexion)
or die("Ha sucedido un error inexperado en la desconexion de la base de datos");


//Creamos el JSON
$json_string = json_encode($zapatillas);

echo $json_string;
?>



