

<?php

$server = "localhost";
$user = "root";
$pass = "clave";
$bd = "zapatillas";

//Creamos la conexión
$conexion = mysqli_connect($server, $user, $pass,$bd)
or die("Ha sucedido un error inesperado en la conexion de la base de datos");

//generamos la consulta
//$marca = $_GET["marca"];
$modelo = $_GET["modelo"];
//$talla = $_GET["talla"];
    //delete from zapatilla where modelo=$modelo
  $sql = "DELETE FROM zapatilla WHERE modelo='$modelo'";
echo $sql;

mysqli_set_charset($conexion, "utf8"); //formato de datos utf8
if (mysqli_query($conexion, $sql)) {
      echo "New record created successfully";
} else {
      echo "Error: " . $sql . "<br>" . mysqli_error($conexion);
}

//desconectamos la base de datos
$close = mysqli_close($conexion)
or die("Ha sucedido un error inesperado en la desconexion de la base de datos");




?>