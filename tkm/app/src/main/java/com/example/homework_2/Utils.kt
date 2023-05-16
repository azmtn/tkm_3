package com.example.homework_2

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import com.example.homework_2.data.model.Reaction
import com.example.homework_2.data.model.ReactionCounterItem
import com.example.homework_2.data.model.SELF_USER_ID
import com.google.android.material.snackbar.Snackbar
import java.io.InputStream
import java.io.OutputStream
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class Utils {
    companion object {

        internal fun getReactionForMessage(reactions: List<Reaction>): List<ReactionCounterItem> {
            return reactions
                .groupBy { reaction -> reaction.emojiCode }
                .map { emoji -> ReactionCounterItem(emoji.key, emoji.value.size) }
                .map { emojiWithCount ->
                    val selfReaction = reactions.firstOrNull { reaction ->
                        reaction.userId == SELF_USER_ID && reaction.emojiCode == emojiWithCount.code
                    }
                    if (selfReaction != null) emojiWithCount.selectedByCurrentUser = true
                    emojiWithCount
                }
        }

        internal fun getDateTimeFromTimestamp(timestamp: Long): LocalDateTime {
            return LocalDateTime.ofInstant(
                Instant.ofEpochSecond(timestamp),
                TimeZone.getDefault().toZoneId()
            )
        }

        internal fun View.showSnackBarWithRetryAction(
            text: CharSequence,
            duration: Int,
            action: () -> Unit
        ) {
            Snackbar.make(this, text, duration).apply {
                setAction(context.getString(R.string.retry_action_snack_bar)) { action() }
            }
                .show()
        }


        internal fun fromHexToDecimal(hexCode: String): String {
            return hexCode.split("-")
                .map { String(Character.toChars(it.toInt(16))) }.joinToString()
        }

        internal fun copy(source: InputStream, target: OutputStream) {
            val buf = ByteArray(BUFFER_SIZE)
            var length: Int
            while (source.read(buf).also { length = it } > 0) {
                target.write(buf, 0, length)
            }
        }

        internal fun getFileName(context: Context, contentUri: Uri): String {
            val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
            var fileName = ""
            context.contentResolver.query(contentUri, projection, null, null, null)
                ?.use { metaCursor ->
                    if (metaCursor.moveToFirst()) {
                        fileName = metaCursor.getString(0)
                    }
                }
            return fileName
        }

        internal fun hasPermissions(context: Context, vararg permissions: String): Boolean =
            permissions.all {
                ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }

        private const val BUFFER_SIZE = 8192
    }

}