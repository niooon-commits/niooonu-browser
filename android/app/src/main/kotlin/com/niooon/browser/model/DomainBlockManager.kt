package com.niooon.browser.model

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf

object DomainBlockManager {
    private const val PREFS_NAME = "niooonu_domain_blocks"
    private const val KEY_BLOCKED_DOMAINS = "blocked_domain_list"

    // Default known aggressive intrusive ad/redirect trackers
    private val DEFAULT_BLOCKED_DOMAINS = setOf(
        "popads.net",
        "popcash.net",
        "adservice.google.com",
        "doubleclick.net",
        "exoclick.com",
        "adcolony.com",
        "propellerads.com",
        "trafficjunky.com",
        "adsterra.com",
        "bet365.com",
        "1xbet.com",
        "onclickpredictiv.com",
        "syndication.exoclick.com",
        "ad-delivery.net"
    )

    private var preferences: SharedPreferences? = null
    val blockedDomains = mutableStateListOf<String>()

    fun init(context: Context) {
        if (preferences == null) {
            preferences = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            loadDomains()
        }
    }

    private fun loadDomains() {
        val prefs = preferences ?: return
        val saved = prefs.getStringSet(KEY_BLOCKED_DOMAINS, null)
        val initialSet = if (saved == null || saved.isEmpty()) {
            DEFAULT_BLOCKED_DOMAINS
        } else {
            saved
        }

        blockedDomains.clear()
        blockedDomains.addAll(initialSet.sorted())

        // Save default set if it was first run
        if (saved == null) {
            prefs.edit().putStringSet(KEY_BLOCKED_DOMAINS, initialSet).apply()
        }
    }

    /**
     * Extracts host from any URL or string.
     * e.g., "https://ads.tracker.com/click?id=123" -> "ads.tracker.com"
     */
    fun extractDomain(urlOrHost: String): String {
        var clean = urlOrHost.trim().lowercase()
        if (clean.isEmpty()) return ""

        try {
            if (!clean.startsWith("http://") && !clean.startsWith("https://")) {
                clean = "https://$clean"
            }
            val uri = Uri.parse(clean)
            val host = uri.host
            if (!host.isNullOrBlank()) {
                return host.removePrefix("www.")
            }
        } catch (_: Exception) {
            // fallback simple string processing
        }

        return clean.removePrefix("https://")
            .removePrefix("http://")
            .removePrefix("www.")
            .substringBefore('/')
            .substringBefore(':')
    }

    /**
     * Checks whether the given URL or host is blocked.
     * Checks exact match and parent domain match (e.g., sub.ads.com is blocked if ads.com is blocked).
     */
    fun isBlocked(urlOrHost: String): Boolean {
        val host = extractDomain(urlOrHost)
        if (host.isEmpty()) return false

        for (blocked in blockedDomains) {
            val cleanBlocked = blocked.trim().lowercase()
            if (host == cleanBlocked || host.endsWith(".$cleanBlocked")) {
                return true
            }
        }
        return false
    }

    /**
     * Adds a domain to the block list. Returns the normalized blocked domain name.
     */
    fun blockDomain(urlOrHost: String): String {
        val domain = extractDomain(urlOrHost)
        if (domain.isBlank()) return ""

        if (!blockedDomains.contains(domain)) {
            blockedDomains.add(0, domain)
            save()
        }
        return domain
    }

    /**
     * Removes a domain from the block list.
     */
    fun unblockDomain(domain: String) {
        val clean = extractDomain(domain)
        if (blockedDomains.remove(clean) || blockedDomains.remove(domain)) {
            save()
        }
    }

    /**
     * Restores default list of blocked domains.
     */
    fun resetToDefaults() {
        blockedDomains.clear()
        blockedDomains.addAll(DEFAULT_BLOCKED_DOMAINS.sorted())
        save()
    }

    /**
     * Clears all blocked domains.
     */
    fun clearAll() {
        blockedDomains.clear()
        save()
    }

    private fun save() {
        preferences?.edit()?.putStringSet(KEY_BLOCKED_DOMAINS, blockedDomains.toSet())?.apply()
    }
}
