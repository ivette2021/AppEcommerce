package com.ivetteVG.ecommerce.Modelo

import android.net.Uri

class ModeloImageSeleccionada {
    var id = ""
    var imagenUri : Uri?=null
    var imagenUrL : String?= null
    var deInternet = false

    constructor()
    constructor(id: String, imagenUri: Uri?, imagenUrL: String?, deInternet: Boolean) {
        this.id = id
        this.imagenUri = imagenUri
        this.imagenUrL = imagenUrL
        this.deInternet = deInternet
    }

}