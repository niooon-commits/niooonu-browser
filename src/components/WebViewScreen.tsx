import React, { useState, useEffect } from 'react';
import {
  RotateCw,
  Share2,
  Copy,
  Bookmark,
  Monitor,
  Smartphone,
  X,
  Search,
  ExternalLink,
  Check,
  Heart,
  MessageCircle,
  Bookmark as BookmarkIcon,
  Grid,
  Tv,
  UserCheck,
} from 'lucide-react';
import { InBrowserTopBar } from './InBrowserTopBar';
import { BrowserMenuPopup } from './BrowserMenuPopup';

interface WebViewScreenProps {
  currentUrl: string;
  tabCount: number;
  onHomeClick: () => void;
  onNewTabClick: () => void;
  onTabsClick: () => void;
  onNavigateToUrl: (url: string) => void;
  onCloseWebView: () => void;
}

export const WebViewScreen: React.FC<WebViewScreenProps> = ({
  currentUrl,
  tabCount,
  onHomeClick,
  onNewTabClick,
  onTabsClick,
  onNavigateToUrl,
  onCloseWebView,
}) => {
  const [loadingProgress, setLoadingProgress] = useState<number>(0);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [showUrlModal, setShowUrlModal] = useState<boolean>(false);
  const [urlInput, setUrlInput] = useState<string>(currentUrl);
  const [showMenu, setShowMenu] = useState<boolean>(false);
  const [isDesktopMode, setIsDesktopMode] = useState<boolean>(false);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  // Trigger loading animation whenever currentUrl changes
  useEffect(() => {
    setIsLoading(true);
    setLoadingProgress(15);
    setUrlInput(currentUrl);

    const timer1 = setTimeout(() => setLoadingProgress(65), 180);
    const timer2 = setTimeout(() => setLoadingProgress(100), 400);
    const timer3 = setTimeout(() => setIsLoading(false), 550);

    return () => {
      clearTimeout(timer1);
      clearTimeout(timer2);
      clearTimeout(timer3);
    };
  }, [currentUrl]);

  const showToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 2400);
  };

  const handleUrlSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const clean = urlInput.trim();
    if (!clean) return;

    let target = clean;
    if (!clean.startsWith('http://') && !clean.startsWith('https://')) {
      if (clean.includes('.') && !clean.includes(' ')) {
        target = `https://${clean}`;
      } else {
        target = `https://www.google.com/search?q=${encodeURIComponent(clean)}`;
      }
    }
    setShowUrlModal(false);
    onNavigateToUrl(target);
  };

  // Determine page type
  const isInstagram = currentUrl.toLowerCase().includes('instagram.com');
  const isGoogleSearch = currentUrl.toLowerCase().includes('google.com/search');
  const searchQuery = isGoogleSearch
    ? new URLSearchParams(currentUrl.split('?')[1] || '').get('q') || 'search query'
    : '';

  return (
    <div
      id="webview-screen"
      className="relative w-full h-full flex flex-col bg-[#0b0f19] text-white select-none overflow-hidden"
    >
      {/* 1. In-Browser Top Bar (Home, Pill URL, New Tab +, Tab counter [N], 3-Dots Menu) */}
      <InBrowserTopBar
        currentUrl={currentUrl}
        tabCount={tabCount}
        onHomeClick={onHomeClick}
        onUrlClick={() => {
          setUrlInput(currentUrl);
          setShowUrlModal(true);
        }}
        onNewTabClick={onNewTabClick}
        onTabsClick={onTabsClick}
        onMenuClick={() => setShowMenu(true)}
      />

      {/* 2. Loading Progress Bar */}
      {isLoading && (
        <div className="w-full h-[2.5px] bg-blue-900/30 overflow-hidden shrink-0">
          <div
            className="h-full bg-gradient-to-r from-blue-500 via-sky-400 to-blue-600 transition-all duration-300"
            style={{ width: `${loadingProgress}%` }}
          />
        </div>
      )}

      {/* 3. Main Web Page Content (Full Height, NO BOTTOM NAVIGATION BAR) */}
      <main className="flex-1 overflow-y-auto relative bg-[#000000]">
        {isInstagram ? (
          /* Instagram Profile Page View matching the user's screenshot */
          <div id="instagram-view" className="w-full max-w-lg mx-auto bg-black text-white min-h-full pb-16">
            {/* IG Header */}
            <div className="flex items-center justify-between px-4 py-3 border-b border-neutral-900">
              <div className="flex items-center gap-2">
                <span className="font-bold text-base tracking-tight">nio_oon</span>
                <span className="w-2 h-2 rounded-full bg-blue-500 inline-block" />
              </div>
              <div className="flex items-center gap-4 text-sm font-semibold">
                <button type="button" className="px-3.5 py-1 bg-neutral-800 rounded-lg text-xs font-semibold">
                  Following
                </button>
                <button type="button" className="px-3.5 py-1 bg-neutral-800 rounded-lg text-xs font-semibold">
                  Message
                </button>
              </div>
            </div>

            {/* Profile Bio Section */}
            <div className="px-4 pt-4 pb-2">
              <div className="flex items-center justify-between gap-4 mb-4">
                {/* Avatar with gradient ring */}
                <div className="relative p-0.5 rounded-full bg-gradient-to-tr from-amber-500 via-rose-500 to-fuchsia-600 shrink-0">
                  <div className="w-18 h-18 rounded-full bg-neutral-900 p-0.5 overflow-hidden flex items-center justify-center font-bold text-2xl text-white">
                    <img
                      src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=240&q=80"
                      alt="nio_oon"
                      className="w-full h-full object-cover rounded-full"
                      onError={(e) => {
                        e.currentTarget.style.display = 'none';
                      }}
                    />
                  </div>
                </div>

                {/* Stats */}
                <div className="flex-1 flex justify-around text-center">
                  <div>
                    <div className="font-bold text-base">42</div>
                    <div className="text-xs text-neutral-400">posts</div>
                  </div>
                  <div>
                    <div className="font-bold text-base">14.8k</div>
                    <div className="text-xs text-neutral-400">followers</div>
                  </div>
                  <div>
                    <div className="font-bold text-base">312</div>
                    <div className="text-xs text-neutral-400">following</div>
                  </div>
                </div>
              </div>

              {/* Bio description */}
              <div className="text-xs space-y-1">
                <div className="font-semibold text-sm">Niooon Browser Project</div>
                <div className="text-neutral-300">Modern Liquid Glassmorphism Android Web Browser</div>
                <div className="text-neutral-400">Minimalist • Blazing Fast • Tab Switcher • Jetpack Compose</div>
                <div className="text-blue-400 font-medium">github.com/niooon-commits/niooonu-browser</div>
              </div>

              {/* Highlights */}
              <div className="flex gap-4 overflow-x-auto py-4 scrollbar-none">
                {['Releases', 'UI Glass', 'Updates', 'Builds', 'Theme'].map((item, idx) => (
                  <div key={item} className="flex flex-col items-center gap-1 shrink-0">
                    <div className="w-14 h-14 rounded-full border border-neutral-700 bg-neutral-900 flex items-center justify-center text-lg font-bold text-neutral-300">
                      {item[0]}
                    </div>
                    <span className="text-[10px] text-neutral-400">{item}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Grid Tabs */}
            <div className="flex border-t border-neutral-800 text-neutral-400">
              <button type="button" className="flex-1 py-3 flex justify-center text-white border-b-2 border-white">
                <Grid className="w-5 h-5" />
              </button>
              <button type="button" className="flex-1 py-3 flex justify-center hover:text-white">
                <Tv className="w-5 h-5" />
              </button>
              <button type="button" className="flex-1 py-3 flex justify-center hover:text-white">
                <BookmarkIcon className="w-5 h-5" />
              </button>
            </div>

            {/* Photo Grid */}
            <div className="grid grid-cols-3 gap-0.5">
              {[
                'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=400&q=80',
                'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=400&q=80',
                'https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?auto=format&fit=crop&w=400&q=80',
                'https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?auto=format&fit=crop&w=400&q=80',
                'https://images.unsplash.com/photo-1634017839464-5c339ebe3cb4?auto=format&fit=crop&w=400&q=80',
                'https://images.unsplash.com/photo-1508739773434-c26b3d09e071?auto=format&fit=crop&w=400&q=80',
              ].map((imgSrc, i) => (
                <div key={i} className="aspect-square bg-neutral-900 overflow-hidden relative group cursor-pointer">
                  <img src={imgSrc} alt="post" className="w-full h-full object-cover group-hover:scale-105 transition-transform" />
                </div>
              ))}
            </div>
          </div>
        ) : isGoogleSearch ? (
          /* Google Search Results View */
          <div id="google-search-view" className="max-w-2xl mx-auto p-4 text-slate-200">
            <div className="text-xs text-slate-400 mb-4 pb-2 border-b border-slate-800 flex items-center justify-between">
              <span>About 1,840,000 results for &quot;{searchQuery}&quot; (0.28 seconds)</span>
            </div>

            <div className="space-y-6">
              {[
                {
                  title: `${searchQuery} — Official Guide & Overview`,
                  url: `https://en.wikipedia.org/wiki/${encodeURIComponent(searchQuery)}`,
                  snippet: `Comprehensive documentation and community overview of ${searchQuery}. Features, latest updates, technical specifications and background details.`,
                },
                {
                  title: `${searchQuery} on GitHub • Open Source Repository`,
                  url: `https://github.com/topics/${encodeURIComponent(searchQuery)}`,
                  snippet: `Explore popular open source projects and tools related to ${searchQuery}. Star, fork, and inspect the code architecture.`,
                },
                {
                  title: `Latest News and Discussions: ${searchQuery}`,
                  url: `https://news.ycombinator.com/item?query=${encodeURIComponent(searchQuery)}`,
                  snippet: `Real-time developer perspectives, discussions, and release announcements regarding ${searchQuery}.`,
                },
              ].map((res, idx) => (
                <article
                  key={idx}
                  onClick={() => onNavigateToUrl(res.url)}
                  className="p-3.5 rounded-xl bg-slate-900/60 hover:bg-slate-900 border border-white/10 cursor-pointer transition-all active:scale-[0.99]"
                >
                  <p className="text-xs text-blue-400 truncate mb-1">{res.url}</p>
                  <h3 className="text-base font-semibold text-blue-300 hover:underline mb-1.5">
                    {res.title}
                  </h3>
                  <p className="text-xs text-slate-300 leading-relaxed">{res.snippet}</p>
                </article>
              ))}
            </div>
          </div>
        ) : (
          /* General Web Page Viewer */
          <div className="w-full h-full flex flex-col items-center justify-center p-6 text-center">
            <div className="w-16 h-16 rounded-2xl bg-blue-600/20 border border-blue-500/30 flex items-center justify-center text-blue-400 mb-4 shadow-lg">
              <ExternalLink className="w-8 h-8" />
            </div>
            <h3 className="text-lg font-bold text-white mb-2">
              Viewing Web Destination
            </h3>
            <p className="text-xs text-slate-400 max-w-sm break-all mb-6">
              {currentUrl}
            </p>
            <div className="flex gap-3">
              <button
                type="button"
                onClick={() => onNavigateToUrl('https://instagram.com/nio_oon/')}
                className="px-4 py-2 rounded-xl bg-gradient-to-r from-pink-600 to-purple-600 hover:opacity-90 text-white text-xs font-semibold shadow-md cursor-pointer"
              >
                Open instagram.com/nio_oon/
              </button>
              <button
                type="button"
                onClick={() => onNavigateToUrl('https://www.google.com/search?q=niooonu+browser')}
                className="px-4 py-2 rounded-xl bg-slate-800 hover:bg-slate-700 text-white text-xs font-semibold border border-white/20 cursor-pointer"
              >
                Search niooonu browser
              </button>
            </div>
          </div>
        )}
      </main>

      {/* URL Edit / Quick Search Modal */}
      {showUrlModal && (
        <div className="absolute inset-0 z-50 bg-black/75 backdrop-blur-md flex items-center justify-center p-4">
          <form
            onSubmit={handleUrlSubmit}
            className="w-full max-w-md bg-slate-900 border border-white/20 rounded-2xl p-5 shadow-2xl animate-in zoom-in-95 duration-150"
          >
            <div className="flex justify-between items-center mb-3">
              <h3 className="text-sm font-bold text-white">Search or Enter URL</h3>
              <button
                type="button"
                onClick={() => setShowUrlModal(false)}
                className="text-slate-400 hover:text-white"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            <div className="relative mb-4">
              <input
                type="text"
                value={urlInput}
                onChange={(e) => setUrlInput(e.target.value)}
                placeholder="e.g. instagram.com/nio_oon/ or search query"
                autoFocus
                className="w-full h-11 pl-4 pr-10 rounded-xl bg-slate-800 border border-white/15 text-white text-sm focus:outline-none focus:border-blue-500"
              />
              {urlInput && (
                <button
                  type="button"
                  onClick={() => setUrlInput('')}
                  className="absolute right-3 top-3 text-slate-400 hover:text-white"
                >
                  <X className="w-5 h-5" />
                </button>
              )}
            </div>

            <div className="flex justify-end gap-2 text-xs">
              <button
                type="button"
                onClick={() => setShowUrlModal(false)}
                className="px-3 py-2 rounded-lg text-slate-300 hover:bg-slate-800 cursor-pointer"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="px-4 py-2 rounded-lg bg-blue-600 hover:bg-blue-500 text-white font-semibold shadow-md cursor-pointer"
              >
                Go
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Three-Dots Menu Popover (Modern Liquid Dark Card matching uploaded image) */}
      {showMenu && (
        <BrowserMenuPopup
          currentUrl={currentUrl}
          isDesktopMode={isDesktopMode}
          onClose={() => setShowMenu(false)}
          onReload={() => {
            setIsLoading(true);
            setTimeout(() => setIsLoading(false), 450);
            showToast('Page refreshed');
          }}
          onNewTab={() => {
            onNewTabClick();
          }}
          onNewIncognitoTab={() => {
            onNewTabClick();
            showToast('New Incognito tab opened');
          }}
          onToggleDesktopMode={() => {
            const nextMode = !isDesktopMode;
            setIsDesktopMode(nextMode);
            showToast(nextMode ? 'Desktop site requested' : 'Mobile site requested');
          }}
          onBookmark={() => {
            showToast('Page bookmarked');
          }}
          onShare={() => {
            if (navigator.clipboard) {
              navigator.clipboard.writeText(currentUrl);
            }
            showToast('Link copied & ready to share');
          }}
          onHistory={() => {
            showToast('Browsing history opened');
          }}
          onDownloads={() => {
            showToast('Downloads manager: niooonu-browser-release.apk');
          }}
          onClearData={() => {
            showToast('Browsing history and cached data cleared');
          }}
          onFindInPage={() => {
            showToast('Find in page activated');
          }}
          onTranslate={() => {
            showToast('Translating page with Google Translate...');
          }}
          onBack={() => {
            onHomeClick();
          }}
          onForward={() => {
            showToast('No forward history');
          }}
        />
      )}

      {/* Floating Toast notification */}
      {toastMessage && (
        <div className="absolute bottom-6 left-1/2 -translate-x-1/2 z-50 bg-slate-900/90 text-white text-xs px-4 py-2 rounded-full border border-white/20 shadow-lg flex items-center gap-2 animate-in fade-in slide-in-from-bottom-2">
          <Check className="w-3.5 h-3.5 text-emerald-400" />
          <span>{toastMessage}</span>
        </div>
      )}
    </div>
  );
};
