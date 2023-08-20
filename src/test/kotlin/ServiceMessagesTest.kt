import org.junit.Test

import org.junit.Assert.*

class ServiceMessagesTest {
    val service = ServiceMessages()
    fun addChat() = run {
        for (i in 0..5) service.addchat(Chat(name = "CHAT_$i"))
    }

    fun addMessage() = run {
        for (i in 0..2) service.addMessage(DirectMessages(1), i)
    }


    @Test
    fun addMessageOK() {
        addChat()
        val result = service.addMessage(DirectMessages(1), 1)
        assertEquals(result, 1)
    }

    @Test
    fun addMessageNoChat() {
        addChat()
        val result = service.addMessage(DirectMessages(1), 7)
        assertEquals(result, -1)
    }

    @Test
    fun addMessageNoChats() {
        val result = service.addMessage(DirectMessages(1), 7)
        assertEquals(result, -1)
    }

    @Test
    fun addMessageAddUser() {
        val user = 7
        addChat()
        service.addMessage(DirectMessages(user), 1)
        var result: Int = -1
        val chat = service.getChat(1)
        if (chat != null) {
            if (chat.users.contains(user)) result = 1
        }
        assertEquals(result, 1)
    }

    @Test
    fun deleteMessageOK() {
        addChat()
        service.addMessage(DirectMessages(1), 1)
        service.addMessage(DirectMessages(1), 1)
        val result = service.deleteMessage(1, 1)
        assertEquals(result, 1)
    }

    @Test
    fun deleteMessageNoChat() {
        addChat()
        service.addMessage(DirectMessages(1), 1)
        service.addMessage(DirectMessages(1), 1)
        val result = service.deleteMessage(1, 8)
        assertEquals(result, -1)
    }

    @Test
    fun deleteMessageNoMessage() {
        addChat()
        service.addMessage(DirectMessages(1), 1)
        service.addMessage(DirectMessages(1), 1)
        val result = service.deleteMessage(3, 1)
        assertEquals(result, -1)
    }

    @Test
    fun editMessageOK() {
        addChat()
        service.addMessage(DirectMessages(1), 1)
        service.addMessage(DirectMessages(1), 1)
        val result = service.editMessage(DirectMessages(1, idMessage = 1, text = "EDIT MESSAGE"), 1)
        assertEquals(result, 1)
    }

    @Test
    fun editMessageNoChat() {
        addChat()
        service.addMessage(DirectMessages(1), 1)
        service.addMessage(DirectMessages(1), 1)
        val result = service.editMessage(DirectMessages(1, idMessage = 1, text = "EDIT MESSAGE"), 8)
        assertEquals(result, -1)
    }

    @Test
    fun editMessageNoMessage() {
        addChat()
        service.addMessage(DirectMessages(1), 1)
        service.addMessage(DirectMessages(1), 1)
        val result = service.editMessage(DirectMessages(1, idMessage = 3, text = "EDIT MESSAGE"), 1)
        assertEquals(result, -1)
    }


    @Test
    fun getChatOk() {
        val chat = Chat(name = "New CHAT")
        service.addchat(chat)
        val result = service.getChat(0)
        assertEquals(result, chat)
    }

    @Test
    fun getChatNoId() {
        val chat = Chat(name = "New CHAT")
        service.addchat(chat)
        val result = service.getChat(1)
        assertEquals(result, null)
    }

    @Test
    fun getUnreadChats() {
        addChat()
        addMessage()
        val lst = mutableListOf(service.getChat(0), service.getChat(1), service.getChat(2))
        val result = service.getUnreadChats()
        assertEquals(result, lst)
    }

    @Test
    fun getCountUnreadChats() {
        addChat()
        addMessage()
        val result = service.getCountUnreadChats()
        assertEquals(result, 3)
    }

    @Test
    fun getChatsList() {
        addChat()
        val lst = mutableListOf(
            service.getChat(0), service.getChat(1),
            service.getChat(2), service.getChat(3), service.getChat(4), service.getChat(5)
        )
        val result = service.getChatsList()
        assertEquals(result, lst)
    }

    @Test
    fun lastMessageChats() {
        addChat()
        addMessage()
        val lst = mutableListOf(
            DirectMessages(idMessage = 0, idAuthor = 1, idMessageChat = 0),
            DirectMessages(idMessage = 0, idAuthor = 1, idMessageChat = 1),
            DirectMessages(idMessage = 0, idAuthor = 1, idMessageChat = 2)
        )
        val result = service.lastMessageChats()
        assertEquals(result, lst)
    }

    @Test
    fun getCountUnReadMessageChats() {
        addChat()
        addMessage()
        val result = service.getCountUnReadMessageChats()
        assertEquals(result, 3)
    }

    @Test
    fun unReadMessageChat() {
        addChat()
        addMessage()
        val lst = mutableListOf(
            DirectMessages(idMessage = 0, idAuthor = 1, idMessageChat = 0)
        )
        val result = service.unReadMessageChat(0)
        assertEquals(result, lst)
    }

    @Test
    fun getCountUnReadMessageChat() {
        addChat()
        addMessage()
        val result = service.getCountUnReadMessageChat(2)
        assertEquals(result, 1)
    }

    @Test
    fun getMessagesChat() {
        addChat()
        addMessage()
        service.addMessage(DirectMessages(idAuthor = 1), 0)
        val lst = mutableListOf(
            DirectMessages(idMessage = 0, idAuthor = 1, idMessageChat = 0),
            DirectMessages(idMessage = 1, idAuthor = 1, idMessageChat = 0)
        )
        val result = service.getMessagesChat(0)
        assertEquals(result, lst)
    }

    @Test
    fun addchat() {
        val result = service.addchat(Chat())
        assertEquals(result, 1)
    }

    @Test
    fun delchatOk() {
        addChat()
        val result = service.delchat(3)
        assertEquals(result, 1)
    }

    @Test
    fun delchatFall() {
        addChat()
        val result = service.delchat(33)
        assertEquals(result, -1)
    }

    @Test
    fun editchat() {
        addChat()
        val result = service.editchat(Chat(name = "EDIT CHAT"), 2)
        assertEquals(result, 1)
    }

    @Test
    fun getCountChats() {
        addChat()
        val result = service.getCountChats()
        assertEquals(result, 6)
    }

    @Test
    fun getUsersChat() {
        addChat()
        service.addMessage(DirectMessages(idAuthor = 1), 2)
        service.addMessage(DirectMessages(idAuthor = 5), 2)
        service.addMessage(DirectMessages(idAuthor = 15), 2)
        service.addMessage(DirectMessages(idAuthor = 25), 2)
        val lst = hashSetOf(1, 5, 15, 25)
        val result = service.getUsersChat(2)
        assertEquals(result, lst)
    }
}