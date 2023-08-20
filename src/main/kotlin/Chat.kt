data class DirectMessages(
    val idAuthor: Int,
    val idMessage: Int = 0,
    val idMessageChat: Int = 0,
    val flagRead: Boolean = false,
    val flagInMessage: Boolean = false,
    val text: String = "text"
)

//data class User(
//    val idUser: Int = 0,
//    val name: String = "User",
//)

data class Chat(
    val idChat: Int = 0,
    val name: String = "UserChat",
    var countMessage: Int = 0,
    var freeMessageID: Int = 0,
    var messages: MutableList<DirectMessages> = mutableListOf(),
    var users: HashSet<Int> = hashSetOf()
)

interface Service<E> {
    fun addObj(obj: E): Int
    fun deleteObj(id: Int): Int
    fun editObj(obj: E, id: Int): Int      // возвращает 1: успех или -1: ошибка
}
