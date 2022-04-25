package com.grigorevmp.catwidget.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.CalendarContract
import android.view.View
import android.widget.RemoteViews
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.AppWidgetTarget
import com.bumptech.glide.request.target.Target
import com.grigorevmp.catwidget.R
import com.grigorevmp.catwidget.data.dto.CatImageDto
import com.grigorevmp.catwidget.data.dto.DogImageDto
import com.grigorevmp.catwidget.data.network.CatImageService
import com.grigorevmp.catwidget.data.network.DogImageService
import com.grigorevmp.catwidget.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class BaseWidget : AppWidgetProvider() {

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)

        val appWidgetManager = AppWidgetManager.getInstance(context)

        if (oldWidgetIds != null) {
            for (appWidgetId in oldWidgetIds) {
                if (context != null) {
                    if (appWidgetManager != null) {
                        updateAppWidget(context, appWidgetManager, appWidgetId)
                    }
                }
            }
        }
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        if (appWidgetIds != null) {
            for (appWidgetId in appWidgetIds) {
                if (context != null) {
                    if (appWidgetManager != null) {
                        updateAppWidget(context, appWidgetManager, appWidgetId)
                    }
                }
            }
        }
    }

    private fun getImage(context: Context, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.base_widget)
        views.setTextViewText(R.id.tvTextDay, getDay())
        views.setTextViewText(R.id.tvTextMonth, getMonth(Utils.getMonthLong()))

        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        val appWidgetTarget = AppWidgetTarget(context, R.id.imageView, views, appWidgetId)

        views.setViewVisibility(R.id.pbLoading, View.GONE)
        views.setViewVisibility(R.id.imageView, View.VISIBLE)

        Glide.with(context.applicationContext)
            .asBitmap()
            .load(Utils.getUrl())
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            })
            .placeholder(circularProgressDrawable)
            .override(500, 500)
            .into(appWidgetTarget)
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {

        val views = RemoteViews(context.packageName, R.layout.base_widget)
        if (Utils.getReload()) {
            views.setOnClickPendingIntent(
                R.id.widget, getPendingSelfIntentFullUpdate(context, appWidgetId)
            )
        } else {
            views.setOnClickPendingIntent(
                R.id.widget, getPendingSelfIntent(context, appWidgetId)
            )
        }
        getImage(context, appWidgetId)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getPendingSelfIntent(
        context: Context,
        appWidgetId: Int
    ): PendingIntent {
        // Toast.makeText(context, context.getString(R.string.syncing), Toast.LENGTH_SHORT).show()
        val intent =
            Intent(context, BaseWidget::class.java)
        intent.action = "LOCAL_UPDATE"
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
        return PendingIntent.getBroadcast(
            context, appWidgetId,
            intent, FLAG_IMMUTABLE
        )
    }

    private fun getPendingSelfIntentFullUpdate(
        context: Context,
        appWidgetId: Int
    ): PendingIntent {
        // Toast.makeText(context, context.getString(R.string.updating), Toast.LENGTH_SHORT).show()
        val intent =
            Intent(context, BaseWidget::class.java)
        intent.action = "FULL_UPDATE"
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
        return PendingIntent.getBroadcast(
            context, appWidgetId,
            intent, FLAG_IMMUTABLE
        )
    }

    private fun callUpdate(
        context: Context?, intent: Intent,
        appWidgetManager: AppWidgetManager, isDog: Boolean
    ) {
        if (isDog) {
            val dogImageService = DogImageService()

            dogImageService.getPicture(context!!.applicationContext)
                .enqueue(object : Callback<DogImageDto> {
                    override fun onFailure(call: Call<DogImageDto>, t: Throwable) {
                    }

                    override fun onResponse(
                        call: Call<DogImageDto>,
                        response: Response<DogImageDto>
                    ) {
                        response.body()?.message?.let { Utils.setUrl(it) }
                        val extras = intent.extras
                        val appWidgetId = extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)

                        if (appWidgetId != null) {
                            updateAppWidget(
                                context,
                                appWidgetManager,
                                appWidgetId
                            )
                        }
                    }
                })
        } else {
            val catImageService = CatImageService()

            catImageService.getPicture(context!!.applicationContext)
                .enqueue(object : Callback<CatImageDto> {
                    override fun onFailure(call: Call<CatImageDto>, t: Throwable) {
                    }

                    override fun onResponse(
                        call: Call<CatImageDto>,
                        response: Response<CatImageDto>
                    ) {
                        response.body()?.file?.let { Utils.setUrl(it) }

                        val extras = intent.extras
                        val appWidgetId = extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)

                        if (appWidgetId != null) {
                            updateAppWidget(
                                context,
                                appWidgetManager,
                                appWidgetId
                            )
                        }
                    }
                })
        }
    }

    override fun onReceive(context: Context?, intent: Intent) {
        super.onReceive(context, intent)

        val appWidgetManager = AppWidgetManager.getInstance(context)

        if (intent.action == "FULL_UPDATE") {
            callUpdate(context, intent, appWidgetManager, Utils.getAnimal() == "dog")
        } else if (intent.action == "LOCAL_UPDATE") {
            if (Utils.getCalendar()) {
                val calendarUri = CalendarContract.CONTENT_URI
                    .buildUpon()
                    .appendPath("time")
                    .build()
                val intent2 = Intent(Intent.ACTION_VIEW, calendarUri)
                intent2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context?.startActivity(intent2)
            }
            val extras = intent.extras
            val appWidgetId = extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)

            if (context != null) {
                if (appWidgetId != null) {
                    updateAppWidget(
                        context,
                        appWidgetManager,
                        appWidgetId
                    )
                }
            }
        } else {
            val extras = intent.extras
            val appWidgetId = extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)

            if (context != null) {
                if (appWidgetId != null) {
                    updateAppWidget(
                        context,
                        appWidgetManager,
                        appWidgetId
                    )
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getDay(): String {
        val sdf = SimpleDateFormat("dd")
        return sdf.format(Date())
    }

    @SuppressLint("SimpleDateFormat")
    fun getMonth(isFullMonth: Boolean): String {
        val sdf =
            if (isFullMonth) SimpleDateFormat("LLLL")
            else SimpleDateFormat("MMM")
        return sdf.format(Date())
    }

}

