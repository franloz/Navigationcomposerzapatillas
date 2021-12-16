package com.example.navigationdrawercompose

sealed class NavegacionItem(var route: String, var icon: Int, var title: String)
{
    object Insertar : NavegacionItem("insertar", R.drawable.add, "Insertar")
    object Borrar : NavegacionItem("borrar", R.drawable.delete, "Borrar")
    object Mostrar : NavegacionItem("mostrar", R.drawable.mostrar, "Mostrar")
    /*object Share : NavegacionItem("share", R.drawable.ic_share, "Share")
    object Contact : NavegacionItem("contact", R.drawable.ic_contact, "Contacto")*/
}