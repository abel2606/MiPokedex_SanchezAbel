package itson.eduardo.mypokedex_sanchezabel

data class Pokemon(var name: String = "", var number: Int = 0, var thumbnail: String = "") {
    constructor() : this("", 0, "")
}