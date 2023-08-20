class ServiceMessages {
    private var servChat: ServiceChats = ServiceChats()
    private var countunReadMessageChats: Int = 0
    private var countUnreadChats: Int = 0
    private var countUnReadMessageChat: Int = 0

    fun addMessage(obj: DirectMessages, idChat: Int): Int {
        if (servChat.getCountChats() == 0) {
            println("Сначала нужно создать чат!")
            return -1
        }
        val index = getIndexChat(idChat)
        return if (index != -1) {
            servChat.chats[index].messages.add(
                obj.copy(
                    idMessage = servChat.chats[index].freeMessageID,
                    idMessageChat = idChat
                )
            )
            servChat.chats[index].countMessage++
            servChat.chats[index].freeMessageID++
            if (!servChat.chats[index].users.contains(obj.idAuthor)) servChat.chats[index].users.add(obj.idAuthor)
            1
        } else index
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

    fun getChat(id: Int): Chat? {                                                        // получить чат
        return servChat.chats.firstOrNull { it.idChat == id }
    }

    fun getUnreadChats (): MutableList<Chat> {                                          // получить список чатов с непрочитанными сообщениями
        countUnreadChats = 0
        var list = mutableListOf<Chat>()
        for (chat in servChat.chats) {
            val messages = chat.messages.filter { !it.flagRead }
            if (messages.isNotEmpty()) {
                list.add(chat)
                countUnreadChats++
            }
         }
        return list
    }

    fun getCountUnreadChats(): Int {                                                    // кол-во чатов с непрочитанными сообщениями
        getUnreadChats ()
        return countUnreadChats
    }

    fun getChatsList(): MutableList<Chat> {                                             // получить все чаты
        return servChat.chats
    }

    fun lastMessageChats(): MutableList<DirectMessages> {                             // последние(непрочитанные) сообщения всех чатов
        countunReadMessageChats = 0
        var list = mutableListOf<DirectMessages>()
        for (chat in servChat.chats) {
            list.addAll(chat.messages.filter { !it.flagRead })
        }
        for(message in list) {
            editMessage(message.copy(flagRead = true), message.idMessageChat)                          // читаем сообщения
        }
        countunReadMessageChats = list.size
        return list
    }

    fun getCountUnReadMessageChats(): Int {                                             // кол-во непрочитанных сообщений всех чатов
        lastMessageChats()
        return countunReadMessageChats
    }

    fun unReadMessageChat(idChat: Int): MutableList<DirectMessages> {                   // непрочитанные сообщения чата с idChat
        countUnReadMessageChat = 0
        var list = mutableListOf<DirectMessages>()
        val indexChat = getIndexChat(idChat)
        list.addAll(servChat.chats[indexChat].messages.filter { !it.flagRead })
        for(message in list) {
            editMessage(message.copy(flagRead = true), idChat)                          // читаем сообщения
        }
        countUnReadMessageChat = list.size
        return list
    }

    fun getCountUnReadMessageChat(idChat: Int): Int {                                   // кол-во непрочитанных сообщений чата с idChat
        unReadMessageChat(idChat)
        return countUnReadMessageChat
    }

    fun getMessagesChat(idChat: Int): MutableList<DirectMessages> {                     // все сообщения чата с idChat
        val index = getIndexChat(idChat)
        return servChat.chats[index].messages
    }

    fun addchat(chat: Chat): Int {
        return servChat.addObj(chat)
    }

    fun delchat(id: Int): Int {
        return servChat.deleteObj(id)
    }

    fun editchat(chat: Chat, id: Int): Int {
        return servChat.editObj(chat, id)
    }

    fun getCountChats(): Int {
        return servChat.getCountChats()
    }

    fun getUsersChat(idChats: Int): HashSet<Int> {
        val index = getIndexChat(idChats)
        return servChat.chats[index].users
    }

    private class ServiceChats : Service<Chat> {
        var chats = mutableListOf<Chat>()
        private var countChats: Int = 0
        override fun addObj(obj: Chat): Int {
            chats.add(obj.copy(idChat = countChats++))
            return 1
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