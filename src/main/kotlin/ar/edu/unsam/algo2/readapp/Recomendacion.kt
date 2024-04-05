@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo2.readapp

/*
 * Los usuarios del sistema pueden crear recomendaciones de una serie de libros para publicar en ReadApp.
 * Estas recomendaciones pueden ser públicas (disponibles para cualquier usuario de la app) o privadas (solo para los amigos).
 * Las recomendaciones, sólo pueden ser editadas por el usuario creador, o por un amigo, si es que este último leyó todos los libros recomendados.
 * El creador no puede agregar libros que no haya leído, y en el caso de que un amigo edite una recomendación, no puede agregar un libro que no haya leído él y el creador.
En todos los casos se debe emitir una reseña o detalle, necesaria/o para cautivar a otros usuarios.
 * Debemos poder determinar el tiempo de lectura que puede llevarle a un usuario leer todos los libros recomendados, el tiempo que se puede ahorrar (si ya leyó alguno/s) y el tiempo neto (si al leer los libros de la recomendación evita la relectura).
Por otro lado, los usuarios que no sean el creador podrán dejarle una valoración siempre y cuando haya leído todos los libros, o si estos pertenecen a un único autor y sea uno de sus autores preferidos. La valoración cuenta con un valor del 1 al 5 y un comentario; los usuarios que ya hayan emitido una valoración no pueden emitir otra nueva, pero sí editar la que emitieron.
 */

/**
 * Recomendaciones.
 *
 * @property esPrivado Indica si la recomendacion es de acceso publico o privado
 * @property creador Indica el nombre del autor de la recomendacion
 * @property librosRecomendados Conjunto de libros incluidos en la recomendacion
 * @property descripcion Indica una pequeña reseña de la recomendacion
 * @property valoraciones Diccionario de valoraciones
 */

class Recomendacion(
    var esPrivado: Boolean,
    val creador: Usuario,
    var librosRecomendados: MutableSet<Libro>,
    var descripcion: String,
    var valoraciones: MutableMap<Usuario, Valoracion>
) {


    /*SETTERS*/
    /**
     *[editarPrivacidad]:
     *
     *  Solo el [creador] puede cambiar la privacidad ([esPrivado]).
     */
    fun editarPrivacidad(usuarioQueEdita: Usuario) {
        if (esCreador(usuarioQueEdita)) {
            alternarPrivacidad()
        }
        //Captura error
    }

    /**
     * [editarDescripcion]:
     *
     * Solo el [creador] y el amigo, pueden editar la [descripcion].
     */
    fun editarDescripcion(usuarioQueEdita: Usuario, descripcionNueva: String) {
        if (esCreador(usuarioQueEdita) || esAmigo(usuarioQueEdita)) {
            cambioDeValoracion(descripcionNueva)
        }
        //Captura error
    }

    /**
     * [agregarALibrosDeRecomendacion]:
     *
     * Agregar un [libro] por el [creador] solo si lo leyo.
     *
     * Agregar un [Libro] por un amigo solo si leyo el libro el y el [creador] y leyo todos los libros de la recomendacion.
     */
    fun agregarALibrosDeRecomendacion(usuarioQueEdita: Usuario, libro: Libro) {
        if (validarEdicion(usuarioQueEdita, libro)) {
            librosRecomendados.add(libro)
        }
        //Captura error
    }

    fun crearValoracion(valor: Int, comentario: String, usuario: Usuario) {
        if (amigoleidosTodos(usuario) || collecionAutorFavorito(usuario)) {
            valoraciones[usuario] = Valoracion(usuario, valor, comentario)
        }
        // Captura error (asi si es facil @Valen)
    }

    private fun collecionAutorFavorito(usuario: Usuario): Boolean =
        librosRecomendados.all { it.autor == usuario.autorFavorito }

    /*GETTERS*/
    fun leerRecomendacion(usuarioQueLee: Usuario): Boolean = !esPrivado || puedeLeerLaRecomendacion(usuarioQueLee)

    fun tiempoDeLecturaTotal(lector: Usuario): Double = librosRecomendados.sumOf { lector.tiempoDeLectura(it) }

    fun tiempoDeLecturaNeto(lector: Usuario): Double =
        librosRecomendados.subtract(lector.librosLeidos.keys).sumOf { lector.tiempoDeLectura(it) }

    fun tiempoDeLecturaAhorrado(lector: Usuario): Double {
        return tiempoDeLecturaTotal(lector) - tiempoDeLecturaNeto(lector)
    }


    /*AUX*/
    private fun esCreador(usuario: Usuario): Boolean = creador == usuario

    private fun esAmigo(usuario: Usuario) = creador.amigos.contains(usuario)

    private fun puedeLeerLaRecomendacion(usuarioQueLee: Usuario): Boolean =
        esAmigo(usuarioQueLee) || esCreador(usuarioQueLee)

    private fun validarEdicion(usuarioQueEdita: Usuario, libro: Libro): Boolean =
        puedeAgregarCreador(usuarioQueEdita, libro) || puedeAgregarAmigo(usuarioQueEdita, libro)

    private fun puedeAgregarCreador(usuarioQueEdita: Usuario, libro: Libro): Boolean =
        esCreador(usuarioQueEdita) && agregarLibroCreador(libro)

    private fun puedeAgregarAmigo(usuarioQueEdita: Usuario, libro: Libro): Boolean =
        esAmigo(usuarioQueEdita) && agregarLibroAmigo(usuarioQueEdita, libro) && amigoleidosTodos(usuarioQueEdita)

    private fun agregarLibroCreador(libro: Libro): Boolean = !existeLibro(libro) && libroYaFueLeidoPorCreador(libro)

    private fun agregarLibroAmigo(usuarioQueEdita: Usuario, libro: Libro): Boolean =
        agregarLibroCreador(libro) && libroYaFueLeidoPorAmigo(usuarioQueEdita, libro)

    private fun existeLibro(libro: Libro): Boolean = librosRecomendados.contains(libro)

    private fun libroYaFueLeidoPorCreador(libro: Libro): Boolean = creador.librosLeidos.contains(libro)

    private fun libroYaFueLeidoPorAmigo(usuarioQueEdita: Usuario, libro: Libro): Boolean =
        usuarioQueEdita.librosLeidos.contains(libro)

    private fun amigoleidosTodos(usuarioQueEdita: Usuario): Boolean =
        usuarioQueEdita.librosLeidos.keys.containsAll(librosRecomendados)

    private fun alternarPrivacidad() {
        esPrivado = !esPrivado
    }

    private fun cambioDeValoracion(texto: String) {
        descripcion = texto
    }
}