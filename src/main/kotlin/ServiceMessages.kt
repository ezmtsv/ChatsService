
class ChatServiceException(message: String) : RuntimeException(message)
class ServiceMessages {
    private var servChat: ServiceChats = ServiceChats()

    fun addMessage(obj: DirectMessages, idChat: Int): Int {
        var idNewChat = idChat
        val index = getIndexChat(idNewChat)
        if (index == -1) {
            idNewChat = servChat.addObj(Chat())
            println("Создан новый чат с ID $idNewChat")
        }
        servChat.chats[idNewChat].messages.add(
            obj.copy(
                idMessage = servChat.chats[idNewChat].freeMessageID,
                idMessageChat = idNewChat
            )
        )
        servChat.chats[idNewChat].countMessage++
        servChat.chats[idNewChat].freeMessageID++
        if (!servChat.chats[idNewChat].users.contains(obj.idAuthor)) servChat.chats[idNewChat].users.add(obj.idAuthor)
        return 1
    }

    fun deleteMessage(idMessage: Int, idChat: Int): Int {
        val indexChat = getIndexChat(idChat)
        return if (indexChat == -1) indexChat
        else {
            val indexMessage = getIndexMessage(idMessage, indexChat)
            if (indexMessage == -1) {
                println("Такого сообщения нет в чате!")
                indexMessage
            } else {
                servChat.chats[indexChat].messages.removeAt(indexMessage)
                servChat.chats[indexChat].countMessage--
                1
            }
        }
    }

    fun editMessage(obj: DirectMessages, idChat: Int): Int {
        val index = getIndexChat(idChat)
        return if (index == -1) index
        else {
            val indexMessage = getIndexMessage(obj.idMessage, index)
            if (indexMessage == -1) {
                println("Такого сообщения нет в чате!")
                indexMessage
            } else {
                servChat.chats[index].messages[indexMessage] = obj.copy()
                1
            }
        }
    }

    private fun getIndexMessage(id: Int, idChat: Int): Int {
        var indexChat = getIndexChat(idChat)
        var message: DirectMessages? = null
        if (indexChat != -1) {
            message = servChat.chats[indexChat].messages.firstOrNull { it.idMessage == id }
        }
        if (message != null) {
            return servChat.chats[indexChat].messages.indexOf(message)
        }
        return -1
    }

    private fun getIndexChat(idChat: Int): Int {
        val chat = servChat.chats.firstOrNull { it.idChat == idChat }
        var index = -1
        if (chat != null) {
            index = servChat.chats.indexOf(chat)
        }
        return if (index == -1) {
            println("Чат с ID = $idChat не найден!")
            index
        } else index
    }

    fun getChat(id: Int): Chat? =
        servChat.chats.firstOrNull { it.idChat == id }                          // получить чат

    fun getUnreadChats(): MutableList<Chat> =                                   // получить список чатов с непрочитанными сообщениями
        servChat.chats.asSequence()
            ?.filter { chat -> chat.messages.any { !it.flagRead } }
            ?.toMutableList()
            ?: throw ChatServiceException("Error getUnreadChats()")

    fun getCountUnreadChats(): Int = getUnreadChats().size                      // кол-во чатов с непрочитанными сообщениями


    fun getChatsList(): MutableList<Chat> =
        servChat.chats                                                          // получить все чаты

    fun lastMessageChats(): MutableList<DirectMessages> =                       // последние(непрочитанные) сообщения всех чатов
        servChat.chats.asSequence()
            ?.map { it.messages }
            ?.flatten()
            ?.filter { !it.flagRead }
            ?.map { it.copy(flagRead = true) }
            ?.toMutableList()
            ?: throw ChatServiceException("Error lastMessageChats")


    fun getCountUnReadMessageChats(): Int =
        lastMessageChats().size                                                 // кол-во непрочитанных сообщений всех чатов

    fun unReadMessageChat(idChat: Int): MutableList<DirectMessages> =           // непрочитанные сообщения чата с idChat
        servChat.chats.find { it.idChat == idChat }
            ?.messages?.filter { !it.flagRead }
            ?.asSequence()
            ?.map { it.copy(flagRead = true) }
            ?.toMutableList()
            ?: throw ChatServiceException("Error unReadMessageChat")

    fun getCountUnReadMessageChat(idChat: Int): Int =
        unReadMessageChat(idChat).size                                          // кол-во непрочитанных сообщений чата с idChat

    fun getMessages(idChat: Int, idStartMessage: Int, count: Int) : List<DirectMessages> =
        servChat.chats.find { it.idChat == idChat }
            ?.messages
            ?.filter { it.idMessage >= idStartMessage }
            ?.take(count)
            ?.map { it.copy(flagRead = true) }
            ?.toMutableList()
            ?: throw ChatServiceException("Error getMessages")

    fun getMessagesChat(idChat: Int): MutableList<DirectMessages> =             // все сообщения чата с idChat
        servChat.chats.find { it.idChat == idChat }
            ?.messages
            ?.asSequence()
            ?.toMutableList()
            ?: throw ChatServiceException("Messages not find")

    fun addchat(chat: Chat): Int = servChat.addObj(chat)

    fun delchat(id: Int): Int = servChat.deleteObj(id)

    fun editchat(chat: Chat, id: Int): Int = servChat.editObj(chat, id)

    fun getCountChats(): Int = servChat.getCountChats()

    fun getUsersChat(idChats: Int): HashSet<Int> {
        val index = getIndexChat(idChats)
        return servChat.chats[index].users
    }

    private class ServiceChats : Service<Chat> {
        var chats = mutableListOf<Chat>()
        private var countChats: Int = 0
        override fun addObj(obj: Chat): Int {
            chats.add(obj.copy(idChat = countChats++))
            return countChats - 1
        }

        override fun deleteObj(id: Int): Int {
            if (chats.firstOrNull { it.idChat == id } == null) {
                return -1
            }
            val index = chats.indexOf(chats.first { it.idChat == id })
            return if (index != -1) {
                chats.removeAt(index)
                1
            } else -1
        }

        override fun editObj(obj: Chat, id: Int): Int {
            if (chats.firstOrNull { it.idChat == id } == null) {
                return -1
            }
            val index = chats.indexOf(chats.first { it.idChat == id })
            return if (index != -1) {
                chats[index] = obj.copy(idChat = id)
                1
            } else -1
        }

        fun getCountChats(): Int {
            return countChats
        }
    }

}