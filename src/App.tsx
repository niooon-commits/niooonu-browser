import React from 'react';
import {
  Download,
  Github,
  CheckCircle2,
  ExternalLink,
  Shield,
  Smartphone,
  Cpu,
  Layers,
  FolderDown,
} from 'lucide-react';

export default function App() {
  const repositoryUrl = 'https://github.com/niooon-commits/niooonu-browser';
  const releasesUrl = 'https://github.com/niooon-commits/niooonu-browser/releases';
  const actionsUrl = 'https://github.com/niooon-commits/niooonu-browser/actions';

  const nativeModules = [
    {
      name: 'DomainBlockManager.kt',
      role: 'Intercepts & blocks ad domains and rogue redirect popups to eliminate black screens',
      badge: 'Security',
    },
    {
      name: 'NiooonuDownloadManager.kt',
      role: 'Real-time Chrome-like background downloads with system notifications & MIME resolver',
      badge: 'Downloads',
    },
    {
      name: 'DownloadsScreen.kt',
      role: 'Categorized file browser (All, APK, Images, Videos, Docs) with storage gauge & file actions',
      badge: 'UI Screen',
    },
    {
      name: 'WebViewScreen.kt',
      role: 'Full-featured Chrome-based web rendering with download listener & domain protection',
      badge: 'Core Engine',
    },
    {
      name: 'LiquidGlassBackground.kt & GlassSearchBar.kt',
      role: 'Dynamic frosted liquid glass UI with fluid physics and dock animations',
      badge: 'Styling',
    },
    {
      name: 'file_paths.xml & FileProvider',
      role: 'Secure scoped storage file sharing and APK direct install support',
      badge: 'Manifest',
    },
  ];

  return (
    <div className="min-h-screen bg-[#0E0F12] text-slate-100 flex flex-col font-sans selection:bg-blue-600 selection:text-white">
      {/* Header Bar */}
      <header className="border-b border-white/10 bg-[#13151B]/80 backdrop-blur-md sticky top-0 z-20">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 py-4 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-2xl bg-gradient-to-tr from-blue-600 to-cyan-400 flex items-center justify-center shadow-lg shadow-blue-500/20">
              <Smartphone className="w-5 h-5 text-white" />
            </div>
            <div>
              <h1 className="text-lg font-bold tracking-tight text-white flex items-center gap-2">
                niooonu browser
                <span className="text-[11px] font-semibold uppercase tracking-wider px-2 py-0.5 rounded-full bg-blue-500/20 text-blue-400 border border-blue-500/30">
                  Android Native
                </span>
              </h1>
              <p className="text-xs text-slate-400">Kotlin &bull; Jetpack Compose &bull; Material 3</p>
            </div>
          </div>

          <div className="flex items-center gap-2.5">
            <a
              href={releasesUrl}
              target="_blank"
              rel="noreferrer"
              className="inline-flex items-center gap-2 px-4 py-2 rounded-xl bg-blue-600 hover:bg-blue-500 text-white text-xs font-semibold shadow-md shadow-blue-600/30 transition-all active:scale-95"
            >
              <Download className="w-4 h-4" />
              <span>Get APK (v1.0.0)</span>
            </a>
            <a
              href={repositoryUrl}
              target="_blank"
              rel="noreferrer"
              className="inline-flex items-center gap-2 px-3.5 py-2 rounded-xl bg-white/5 hover:bg-white/10 border border-white/10 text-slate-300 text-xs font-medium transition-colors"
            >
              <Github className="w-4 h-4" />
              <span className="hidden sm:inline">GitHub</span>
            </a>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="flex-1 max-w-6xl mx-auto w-full px-4 sm:px-6 py-8 space-y-8">
        {/* Status Hero Card */}
        <div className="rounded-3xl bg-gradient-to-b from-[#181B24] to-[#12141A] border border-white/10 p-6 sm:p-8 shadow-2xl relative overflow-hidden">
          <div className="absolute top-0 right-0 w-96 h-96 bg-blue-500/10 rounded-full blur-3xl pointer-events-none" />
          <div className="relative z-10 space-y-4">
            <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-emerald-500/15 border border-emerald-500/30 text-emerald-400 text-xs font-medium">
              <CheckCircle2 className="w-3.5 h-3.5" />
              <span>Pure Android Native Codebase (Web Preview Code Stripped)</span>
            </div>

            <h2 className="text-2xl sm:text-3xl font-extrabold text-white tracking-tight">
              Android Production Build &amp; Release Ready
            </h2>
            <p className="text-slate-300 text-sm sm:text-base max-w-3xl leading-relaxed">
              As requested, the web companion UI components have been completely removed. The application is centered around the native Android Kotlin source files located in{' '}
              <code className="px-2 py-0.5 rounded-md bg-black/40 text-blue-300 text-xs font-mono">
                android/app/src/main/kotlin/com/niooon/browser/
              </code>
              , compiled via automated GitHub Actions into signed release APKs.
            </p>

            <div className="pt-2 flex flex-wrap items-center gap-3 text-xs">
              <a
                href={actionsUrl}
                target="_blank"
                rel="noreferrer"
                className="inline-flex items-center gap-1.5 px-3.5 py-2 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-200 border border-white/10 transition-colors"
              >
                <Cpu className="w-4 h-4 text-cyan-400" />
                <span>GitHub Actions Workflows</span>
                <ExternalLink className="w-3 h-3 text-slate-400" />
              </a>

              <a
                href={releasesUrl}
                target="_blank"
                rel="noreferrer"
                className="inline-flex items-center gap-1.5 px-3.5 py-2 rounded-xl bg-blue-600/20 hover:bg-blue-600/30 text-blue-300 border border-blue-500/30 transition-colors"
              >
                <FolderDown className="w-4 h-4 text-blue-400" />
                <span>niooonu-browser-release.apk (v1.0.0)</span>
              </a>
            </div>
          </div>
        </div>

        {/* Native Architecture Grid */}
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <h3 className="text-lg font-bold text-white flex items-center gap-2">
              <Layers className="w-5 h-5 text-blue-400" />
              <span>Native Android Modules</span>
            </h3>
            <span className="text-xs text-slate-400">android/ directory</span>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {nativeModules.map((mod) => (
              <div
                key={mod.name}
                className="rounded-2xl bg-[#161820] border border-white/10 p-5 space-y-2.5 hover:border-blue-500/40 transition-colors"
              >
                <div className="flex items-center justify-between">
                  <span className="text-xs font-mono font-bold text-blue-400 truncate max-w-[200px]">
                    {mod.name}
                  </span>
                  <span className="text-[10px] uppercase font-semibold px-2 py-0.5 rounded-full bg-white/5 border border-white/10 text-slate-300">
                    {mod.badge}
                  </span>
                </div>
                <p className="text-xs text-slate-400 leading-relaxed">{mod.role}</p>
              </div>
            ))}
          </div>
        </div>

        {/* Security & Stability Specs */}
        <div className="rounded-2xl bg-[#13151D] border border-white/10 p-6 space-y-3">
          <h4 className="text-sm font-semibold text-slate-200 flex items-center gap-2">
            <Shield className="w-4 h-4 text-emerald-400" />
            <span>Black Screen Prevention &amp; Rogue Ad Domain Protection</span>
          </h4>
          <p className="text-xs text-slate-400 leading-relaxed">
            The browser continuously scans URLs with <code className="text-slate-300">DomainBlockManager.kt</code>. When malicious redirect popups, known rogue ad networks, or empty advertiser trackers attempt to load in the background, they are dropped instantly, preventing unresponsive blank or black screens on Android devices.
          </p>
        </div>
      </main>

      {/* Footer */}
      <footer className="border-t border-white/10 bg-[#0A0B0E] py-6 text-center text-xs text-slate-500">
        <p>niooonu browser &bull; Built with Kotlin &amp; Jetpack Compose &bull; Repository: niooon-commits/niooonu-browser</p>
      </footer>
    </div>
  );
}
