package xyz.kejiyu.hongguo

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import java.net.HttpURLConnection
import java.net.URL

object UpdateChecker {

    const val REPO_URL = "https://github.com/KEJIYUNB/hongguo"
    const val RELEASES_URL = "$REPO_URL/releases/latest"
    const val TG_CHANNEL_URL = "https://t.me/Kmodify"

    @Volatile var currentVersion: String = ""
        private set
    @Volatile var latestVersion: String? = null
        private set
    @Volatile var hasUpdate: Boolean = false
        private set
    @Volatile var checking: Boolean = false
        private set
    @Volatile var lastError: String? = null
        private set

    @Volatile private var shownOnce = false

    private val callbacks = mutableListOf<(String?) -> Unit>()
    private val mainHandler = Handler(Looper.getMainLooper())

    // 已拦截：直接返回，不执行网络检查
    fun checkUpdate(version: String, onResult: ((String?) -> Unit)? = null) {
        currentVersion = version
        onResult?.invoke(null)
    }

    // 已拦截：直接返回，永不弹窗
    fun showUpdateDialogIfNeeded(ctx: Context) {
        // 装死，啥也不干
    }

    fun resetShown() { shownOnce = false }

    // 保留原方法，防止外部代码报错
    fun showUpdateDialog(ctx: Context, current: String, latest: String) {
        // 留空即可
    }

    // 保留原方法，防止外部代码报错
    private fun fetchLatestTag(): String? {
        return null
    }

    fun compareVersions(a: String, b: String): Int {
        fun parse(s: String): List<Int> = s.trim().removePrefix("v")
            .split('.')
            .map { seg -> seg.filter { it.isDigit() }.toIntOrNull() ?: 0 }
        val pa = parse(a)
        val pb = parse(b)
        for (i in 0 until maxOf(pa.size, pb.size)) {
            val x = pa.getOrNull(i) ?: 0
            val y = pb.getOrNull(i) ?: 0
            if (x != y) return x.compareTo(y)
        }
        return 0
    }

    fun openUrl(ctx: Context, url: String) {
        try {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ctx.applicationContext.startActivity(i)
        } catch (e: Exception) {
            LogUtil.error("打开链接失败 $url", e)
        }
    }
}
