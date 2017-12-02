package code.joshuali.readmoreview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

/**
 * Created by liyaozhong on 2017/11/28.
 */
class ReadMoreView : View{


    private var text: String = ""
    private var moreText:String = "more"
    private var textColor = Color.BLACK
    private var moreTextColor = Color.RED
    private var textSize = 30f
    private var faddingLength = 40f
    private var faddingPadding = 8f
    private var truncateLines: Int = Int.MAX_VALUE

    private var staticLayout : StaticLayout? = null
    private var moreStaticLayout : StaticLayout? = null
    private var textPaint = TextPaint()
    private var moreTextPaint = TextPaint()
    private var moreBgPaint = Paint()
    private var clearPaint = Paint()
    private lateinit var gradient : LinearGradient

    private var singleLineHeight = 0
    private var moreWidth = 0f
    private var moreHeight = 0
    private var showAll = false

    init {
        isEnabled = true
        textPaint.isAntiAlias = true
        textPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, textSize, resources.displayMetrics)
        textPaint.color = textColor

        moreTextPaint.isAntiAlias = true
        moreTextPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, textSize, resources.displayMetrics)
        moreTextPaint.color = moreTextColor

        clearPaint.color = Color.WHITE
    }

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx,attrs) {
        getAttr(ctx, attrs)
    }

    private fun getAttr(ctx: Context, attrs: AttributeSet) {
        val typedArray = ctx.obtainStyledAttributes(attrs, R.styleable.ReadMoreViewAttr)
        text = typedArray.getString(R.styleable.ReadMoreViewAttr_text) ?: ""
        moreText = typedArray.getString(R.styleable.ReadMoreViewAttr_moreText) ?: moreText
        textColor = typedArray.getColor(R.styleable.ReadMoreViewAttr_textColor, textColor)
        moreTextColor = typedArray.getColor(R.styleable.ReadMoreViewAttr_moreTextColor, moreTextColor)
        textSize = typedArray.getDimension(R.styleable.ReadMoreViewAttr_textSize, textSize)
        faddingLength = typedArray.getDimension(R.styleable.ReadMoreViewAttr_faddingLength, 0f)
        faddingPadding = typedArray.getDimension(R.styleable.ReadMoreViewAttr_faddingPadding, 0f)
        truncateLines = typedArray.getInt(R.styleable.ReadMoreViewAttr_showLine, Int.MAX_VALUE)
        typedArray.recycle()

        setBackgroundColor(Color.WHITE)
        textPaint.color = textColor
        textPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, textSize, resources.displayMetrics)
        moreTextPaint.color = moreTextColor
        moreTextPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, textSize, resources.displayMetrics)
    }

    fun setText(text: String) {
        this.text = text
        staticLayout = StaticLayout(text,0, text.length, textPaint, width - paddingLeft - paddingRight, Layout.Alignment.ALIGN_NORMAL, 1f, 0f ,false)
        showAll = staticLayout!!.height <= singleLineHeight * truncateLines
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth - paddingLeft - paddingRight
        if (width > 0 && staticLayout == null) {
            moreStaticLayout = StaticLayout(moreText, moreTextPaint, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
            singleLineHeight = StaticLayout("", textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0f ,false).height
            moreWidth = moreStaticLayout!!.getLineWidth(0)
            moreHeight = moreStaticLayout!!.height
            val totalWidth = moreWidth + faddingLength + faddingPadding
            gradient = LinearGradient(0f, 0f, totalWidth, moreHeight.toFloat(), intArrayOf(Color.parseColor("#00FFFFFF"), Color.parseColor("#FFFFFFFF"), Color.parseColor("#FFFFFFFF")), floatArrayOf(0f, faddingLength/totalWidth, 1f), Shader.TileMode.CLAMP)
            moreBgPaint.shader = gradient
            staticLayout = StaticLayout(text,0, text.length, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0f ,false)
            showAll = staticLayout!!.height <= singleLineHeight * truncateLines
        }

        setMeasuredDimension(measuredWidth, (if (showAll) staticLayout!!.height else Math.min(staticLayout!!.height, singleLineHeight * truncateLines)) + paddingTop + paddingBottom)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null && !showAll) {
            if (event.x >= width - paddingRight - moreWidth && event.y >= height - paddingBottom - moreHeight) {
                showAll = true
                requestLayout()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.save()
        canvas?.translate(paddingLeft.toFloat(), paddingTop.toFloat())
        staticLayout?.draw(canvas)
        if (!showAll) {
            canvas?.drawRect(0f, (height - moreHeight).toFloat() + singleLineHeight - paddingTop - paddingBottom, width.toFloat(), height.toFloat(), clearPaint)
            canvas?.translate(width - moreWidth - faddingLength - faddingPadding - paddingRight, (height - moreHeight).toFloat() - paddingTop - paddingBottom)
            canvas?.drawRect(0f, 0f, moreWidth + faddingLength + faddingPadding, moreHeight.toFloat(), moreBgPaint)
            canvas?.translate(faddingLength + faddingPadding, 0f)
            moreStaticLayout?.draw(canvas)
        }
        canvas?.restore()
        super.onDraw(canvas)
    }

}