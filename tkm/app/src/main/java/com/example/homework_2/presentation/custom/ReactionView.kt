package com.example.homework_2.presentation.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.example.homework_2.R
import com.example.homework_2.Utils.Companion.fromHexToDecimal
import com.example.homework_2.data.model.ReactionCounterItem
import com.example.homework_2.presentation.adapter.OnReactionClickListener

class ReactionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val textPaint = TextPaint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val textBounds = Rect()
    private val textPoint = PointF()
    var reaction = ""
    private var minWidth = DEFAULT_WIDTH
    var messageId = 0L

    var reactionCount = 0
        set(value) {
            val oldValue = field
            field = value
            if (oldValue.toString().length != value.toString().length) {
                requestLayout()
            }
        }

    private val textToDraw: String
        get() = "$reaction $reactionCount"

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ReactionView)
        reactionCount = typedArray.getInt(R.styleable.ReactionView_reactionCount, DEFAULT_COUNT_REACTION)
        reaction = typedArray.getString(R.styleable.ReactionView_reaction) ?: DEFAULT_REACTION
        minWidth = typedArray.getDimensionPixelSize(R.styleable.ReactionView_minWidth, DEFAULT_WIDTH)
        textPaint.color = typedArray.getColor(R.styleable.ReactionView_textColor, Color.WHITE)
        textPaint.textSize = typedArray.getDimension(
            R.styleable.ReactionView_textSize,
            DEFAULT_DIMENS_SP * resources.displayMetrics.density
        )
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (visibility == VISIBLE) {
            textPaint.getTextBounds(textToDraw, 0, textToDraw.length, textBounds)

            val textWidth = textBounds.width()
            val textHeight = textBounds.height()

            var sumWidth = textWidth + paddingLeft + paddingRight
            val sumHeight = textHeight + paddingTop + paddingBottom

            if (sumWidth < minWidth) sumWidth = minWidth

            setMeasuredDimension(
                resolveSize(sumWidth, widthMeasureSpec),
                resolveSize(sumHeight, heightMeasureSpec)
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        textPoint.x = w / 2f - textBounds.width() / 2f
        textPoint.y = h / 2f + textBounds.height() / 2f - textPaint.descent()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(textToDraw, textPoint.x, textPoint.y, textPaint)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + SUPPORTED_DRAWABLE_STATE.size)
        if (isSelected) {
            mergeDrawableStates(drawableState, SUPPORTED_DRAWABLE_STATE)
        }
        return drawableState
    }

    companion object {
        private const val DEFAULT_DIMENS_SP = 15
        private const val DEFAULT_COUNT_REACTION = 1
        private const val DEFAULT_WIDTH = 40
        private const val DEFAULT_REACTION = "\uD83D\uDE02"
        private val SUPPORTED_DRAWABLE_STATE = intArrayOf(android.R.attr.state_selected)

        internal fun createEmojiWithCountView(
            flexBox: FlexBoxLayout,
            reactionCounterItem: ReactionCounterItem,
            messageId: Long,
            reactionClickListener: OnReactionClickListener
        ): ReactionView {
            val reactionView = LayoutInflater.from(flexBox.context).inflate(
                R.layout.default_reaction_view,
                flexBox,
                false
            ) as ReactionView
            reactionView.messageId = messageId
            reactionView.reaction = if (reactionCounterItem.code.any { it in 'a'..'f' }) {
                fromHexToDecimal(reactionCounterItem.code)
            } else {
                reactionCounterItem.code
            }
            reactionView.setOnClickListener {
                reactionClickListener.onReactionClick(reactionView)
            }
            reactionView.reactionCount = reactionCounterItem.count
            return reactionView
        }
    }
}
