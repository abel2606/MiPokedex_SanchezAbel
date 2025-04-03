package itson.eduardo.mypokedex_sanchezabel

data class Pokemon (var name: String, var number: Int, var thumbnail: String){
    override fun toString() = name + "\t" + number+ "\t"+ thumbnail
}