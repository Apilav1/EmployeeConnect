package com.employeeconnect.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.employeeconnect.ui.App
import org.jetbrains.anko.db.*

class EmployeeConnectDbHelper(ctx: Context = App.instance!!.applicationContext) : ManagedSQLiteOpenHelper(ctx,
    DB_NAME, null, DB_VERSION){

    companion object {
        const val DB_NAME = "employeeConnect.db"
        const val DB_VERSION = 1
        val instance by lazy { EmployeeConnectDbHelper() }
    }

    override fun onCreate(db: SQLiteDatabase) {
            db.createTable(EmployeeTable.NAME, true,
                EmployeeTable.ID to TEXT + PRIMARY_KEY,
                        EmployeeTable.USERNAME to TEXT,
                        EmployeeTable.PROFILE_IMAGE_URL to TEXT,
                        EmployeeTable.PROFILE_IMAGE to BLOB,
                        EmployeeTable.EMAIL to TEXT,
                        EmployeeTable.GITHUB_USERNAME to TEXT,
                        EmployeeTable.LINKED_IN_USERNAME to TEXT,
                        EmployeeTable.SKILLS to TEXT,
                        EmployeeTable.POSITION to TEXT,
                        EmployeeTable.TEAM_NAME to TEXT,
                        EmployeeTable.CURRENT_PROJECT to TEXT,
                        EmployeeTable.VERIFIED to INTEGER,
                        EmployeeTable.MODERATOR to INTEGER
                )

            db.createTable(EmployeeChatRoomTable.NAME, true,
                EmployeeChatRoomTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                        EmployeeChatRoomTable.EMPLOYEE_ID to TEXT,
                        EmployeeChatRoomTable.CHATROOM_ID to TEXT,
                        EmployeeChatRoomTable.TO_USER_ID to TEXT)


            db.createTable(ChatRoomTable.NAME, true, ChatRoomTable.ID to TEXT)

            db.createTable(ChatRoomMessageTable.NAME, true,
                        ChatRoomMessageTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                                ChatRoomMessageTable.CHATROOM_ID to TEXT,
                                ChatRoomMessageTable.MESSAGE_ID to TEXT)

            db.createTable(MessagesTable.NAME, true,
                        MessagesTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                        MessagesTable.FROM_USER_ID to TEXT,
                        MessagesTable.TO_USER_ID to TEXT,
                        MessagesTable.TEXT to TEXT,
                        MessagesTable.TIMESTAMP to INTEGER,
                        MessagesTable.CHATROOM_ID to TEXT,
                        MessagesTable.SEEN to INTEGER)

            db.createTable(LatestMessagesTable.NAME, true,
                LatestMessagesTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                LatestMessagesTable.FROM_USER_ID to TEXT,
                LatestMessagesTable.TO_USER_ID to TEXT,
                LatestMessagesTable.TEXT to TEXT,
                LatestMessagesTable.TIMESTAMP to INTEGER,
                LatestMessagesTable.CHATROOM_ID to TEXT,
                LatestMessagesTable.SEEN to INTEGER)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

            db.dropTable(EmployeeTable.NAME, true)
            db.dropTable(EmployeeChatRoomTable.NAME, true)
            db.dropTable(ChatRoomTable.NAME, true)
            db.dropTable(ChatRoomMessageTable.NAME, true)
            db.dropTable(MessagesTable.NAME, true)
            db.dropTable(LatestMessagesTable.NAME, true)

            onCreate(db)
    }
}