package com.example.homework_2.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.*
import com.example.homework_2.databinding.MessageViewGroupBinding
import com.example.homework_2.measuredHeightWithMargins
import com.example.homework_2.measuredWidthWithMargins

class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {

    var binding: MessageViewGroupBinding =
        MessageViewGroupBinding.inflate(LayoutInflater.from(context), this)

    var messageId = 0L
    private var messageBlockStart = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val avatar = binding.avatar
        val messageBlock = binding.messageBlock
        val flexBox = binding.flexBox

        measureChildWithMargins(
            avatar,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            0
        )

        measureChildWithMargins(
            messageBlock,
            widthMeasureSpec,
            avatar.measuredWidth,
            heightMeasureSpec,
            0
        )

        var totalWidth =
            avatar.measuredWidthWithMargins() + messageBlock.measuredWidthWithMargins()
        var totalHeight =
             messageBlock.measuredHeightWithMargins()

        measureChildWithMargins(
            flexBox,
            widthMeasureSpec,
            avatar.measuredWidth,
            heightMeasureSpec,
            0
        )

        if (flexBox.measuredWidthWithMargins() > messageBlock.measuredWidthWithMargins()) {
            totalWidth += flexBox.measuredWidthWithMargins() - messageBlock.measuredWidthWithMargins()
        }
        totalHeight += flexBox.measuredHeightWithMargins()

        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec),
            resolveSize(totalHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val avatar = binding.avatar
        val messageBlock = binding.messageBlock
        val flexBox = binding.flexBox
        avatar.layout(
            avatar.marginStart,
            avatar.marginTop,
            avatar.marginStart + avatar.measuredWidth,
            avatar.marginTop + avatar.measuredHeight
        )

        messageBlockStart = avatar.right + avatar.marginStart + avatar.marginEnd
        messageBlock.layout(
            messageBlockStart + messageBlock.marginStart,
            0,
            messageBlockStart + messageBlock.measuredWidth + messageBlock.marginEnd,
            messageBlock.marginBottom + messageBlock.measuredHeight
        )

        val flexBoxTop = messageBlock.measuredHeightWithMargins()
        val firstChild = flexBox.getChildAt(0)

        flexBox.layout(
            messageBlockStart - firstChild.marginStart,
            flexBoxTop,
            messageBlockStart + flexBox.measuredWidthWithMargins() - firstChild.marginStart,
            flexBoxTop + flexBox.marginBottom + flexBox.measuredHeight
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }
}
