@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo2.readapp

/***
 *  De los libros conocemos:La cantidad de palabras, páginas, ediciones y ventas semanales. También si es de lectura compleja.
Sabemos que un libro es desafiante si es de lectura compleja o es largo (tiene más de 600 páginas).
Un libro llega a ser best seller cuando se cumplen todas las siguientes condiciones:
Sus ventas semanales son de por lo menos 10.000 unidades.
Tiene varias ediciones (el criterio actual es más de 2), o muchas traducciones en distintos lenguajes (en este caso no menos de 5).
***/
class Libro(
    val nombre: String,
    val editorial: String,
    val paginas: Int,
    val cantPalabras: Int,
    var lecturaCompleja: Boolean,
    var ediciones: Int,
    var lenguajes: List<Lenguaje>,
    var ventasSemanales: Int
) {
    companion object {
        const val MINIMO_DE_VENTAS_SEMANALES: Int = 10000
        const val MINIMO_DE_PAGINAS: Int = 600
        const val MINIMO_DE_EDICIONES: Int = 2
        const val MINIMO_DE_TRADUCCIONES: Int = 5
    }
    fun esDesafiante(): Boolean = this.lecturaCompleja || this.paginas > MINIMO_DE_PAGINAS
    fun esBestSeller(): Boolean = this.ventasSemanales >=MINIMO_DE_VENTAS_SEMANALES && (this.ediciones > MINIMO_DE_EDICIONES || this.numeroDeLenguajes() >= MINIMO_DE_TRADUCCIONES)

    /*GETTER*/
    private fun numeroDeLenguajes(): Int = this.lenguajes.size
    }

/*APARTIR DE ACA ESTA CODIGO DE PRUEBA PARA QUE FUNCIONE ESTA SECCION, RECORDAR REFACTORIZAR O REMOVER*/
