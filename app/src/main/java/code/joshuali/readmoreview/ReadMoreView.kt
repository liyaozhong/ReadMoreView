package code.joshuali.readmoreview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

/**
 * Created by liyaozhong on 2017/11/28.
 */
class ReadMoreView : View{


    var descriptionText: String = ""

    private val truncateLines: Int = 4

    private var staticLayout : StaticLayout? = null
    private var moreStaticLayout : StaticLayout? = null

    private var textPaint = TextPaint()
    private var moreTextPaint = TextPaint()
    private var moreBgPaint = Paint()
    private lateinit var gradient : LinearGradient

    private var singleLineHeight = 0
    private var moreWidth = 0f
    private var moreHeight = 0

    private var showAll = false

    init {
        isEnabled = true
        textPaint.isAntiAlias = true
        textPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, resources.displayMetrics)
        textPaint.color = Color.BLACK

        moreTextPaint.isAntiAlias = true
        moreTextPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, resources.displayMetrics)
        moreTextPaint.color = Color.RED

    }

    constructor(ctx: Context) : super(ctx) {
    }
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx,attrs) {
    }

    fun setText(text: String) {
        descriptionText = text
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        if (width > 0 && descriptionText.isNotEmpty() && staticLayout == null) {
            moreStaticLayout = StaticLayout("more", moreTextPaint, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
            singleLineHeight = StaticLayout("", textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0f ,false).height
            moreWidth = moreStaticLayout!!.getLineWidth(0)
            moreHeight = moreStaticLayout!!.height
            val totalWidth = moreWidth + dpToPx(40)

            gradient = LinearGradient(0f, 0f, totalWidth, moreHeight.toFloat(), intArrayOf(Color.parseColor("#00FFFFFF"), Color.parseColor("#FFFFFFFF"), Color.parseColor("#FFFFFFFF")), floatArrayOf(0f, (dpToPx(32)/totalWidth), 1f), Shader.TileMode.CLAMP)
            moreBgPaint.shader = gradient
            staticLayout = StaticLayout(descriptionText,0, descriptionText.length, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0f ,false)
            showAll = staticLayout!!.height <= singleLineHeight * truncateLines
        }
        setMeasuredDimension(width, if (showAll) staticLayout!!.height else Math.min(staticLayout!!.height, singleLineHeight * truncateLines))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null && !showAll) {
            if (event.x >= moreWidth && event.y >= moreHeight) {
                showAll = true
                requestLayout()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        staticLayout?.draw(canvas)
        if (!showAll) {
            canvas?.save()
            canvas?.translate(width - moreWidth - dpToPx(40), (height - moreHeight).toFloat())
            canvas?.drawRect(0f, 0f, moreWidth + dpToPx(40), moreHeight.toFloat(), moreBgPaint)
            canvas?.translate(dpToPx(40).toFloat(), 0f)
            moreStaticLayout?.draw(canvas)
            canvas?.restore()
        }
        super.onDraw(canvas)
    }

    private fun dpToPx(dp: Int): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

}