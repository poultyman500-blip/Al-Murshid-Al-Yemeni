package com.almurshid.yemeni.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `shared_trips` (
                `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                `tripId` INTEGER NOT NULL,
                `sharedAt` INTEGER NOT NULL,
                `shareId` TEXT NOT NULL,
                `likes` INTEGER NOT NULL DEFAULT 0,
                `views` INTEGER NOT NULL DEFAULT 0,
                `isPublic` INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `challenges` (
                `id` TEXT NOT NULL PRIMARY KEY,
                `title` TEXT NOT NULL,
                `description` TEXT NOT NULL,
                `type` TEXT NOT NULL,
                `targetValue` REAL NOT NULL,
                `startDate` INTEGER NOT NULL,
                `endDate` INTEGER NOT NULL,
                `iconEmoji` TEXT NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `user_progress` (
                `challengeId` TEXT NOT NULL PRIMARY KEY,
                `currentValue` REAL NOT NULL,
                `completed` INTEGER NOT NULL DEFAULT 0,
                `completedAt` INTEGER
            )
            """.trimIndent()
        )
    }
}
