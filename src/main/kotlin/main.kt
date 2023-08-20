fun main() {
    val service = ServiceMessages()
    for (i in 0 .. 5) service.addchat(Chat(name = "CHAT_$i"))

    printList(service.getChatsList())

    service.addMessage(DirectMessages(1), 1)
    service.addMessage(DirectMessages(1), 1)
    printList(service.getMessagesChat(1))

    service.editMessage(DirectMessages(2, text = "Edit Message", idMessage = 0), 1)
    printList(service.getMessagesChat(1))
}

fun <T> printList(list: MutableList<T>) = run {
    for (i in list) println(i)
    println("count objects = ${list.size}")
    println()
}