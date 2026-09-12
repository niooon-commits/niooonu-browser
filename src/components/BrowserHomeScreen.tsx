import React, { useState } from 'react';
import {
  Search,
  Mic,
  Camera,
  Plus,
  ArrowLeft,
  ArrowRight,
  MoreVertical,
  User,
  Sparkles,
  ExternalLink,
} from 'lucide-react';
import { ShortcutItem, DiscoverArticle } from '../types';

interface BrowserHomeScreenProps {
  tabCount: number;
  onNavigateToUrl: (url: string) => void;
  onTabsClick: () => void;
}

const DEFAULT_SHORTCUTS: ShortcutItem[] = [
  {
    id: '1',
    title: 'Instagram',
    url: 'https://instagram.com/nio_oon/',
    iconBg: 'from-pink-500 via-rose-500 to-amber-500',
    iconLetter: 'IG',
  },
  {
    id: '2',
    title: 'Google',
    url: 'https://www.google.com',
    iconBg: 'from-blue-500 to-cyan-500',
    iconLetter: 'G',
  },
  {
    id: '3',
    title: 'YouTube',
    url: 'https://www.youtube.com',
    iconBg: 'from-red-600 to-rose-700',
    iconLetter: 'YT',
  },
  {
    id: '4',
    title: 'GitHub',
    url: 'https://github.com/niooon-commits/niooonu-browser',
    iconBg: 'from-slate-800 to-neutral-950',
    iconLetter: 'GH',
  },
  {
    id: '5',
    title: 'Wikipedia',
    url: 'https://www.wikipedia.org',
    iconBg: 'from-neutral-600 to-neutral-800',
    iconLetter: 'W',
  },
  {
    id: '6',
    title: 'Twitter / X',
    url: 'https://twitter.com',
    iconBg: 'from-black to-neutral-900',
    iconLetter: '𝕏',
  },
  {
    id: '7',
    title: 'Reddit',
    url: 'https://www.reddit.com',
    iconBg: 'from-orange-500 to-red-600',
    iconLetter: 'R',
  },
];

const DISCOVER_ARTICLES: DiscoverArticle[] = [
  {
    id: 'a1',
    title: 'Introducing Liquid Glass Design: The Future of Android Interfaces',
    category: 'Design & Tech',
    timeAgo: '2h ago',
    imageUrl:
      'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=600&q=80',
    articleUrl: 'https://instagram.com/nio_oon/',
  },
  {
    id: 'a2',
    title: 'Jetpack Compose 2025: Modern Android Architecture and Edge-to-Edge',
    category: 'Android',
    timeAgo: '4h ago',
    imageUrl:
      'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=600&q=80',
    articleUrl: 'https://developer.android.com/jetpack/compose',
  },
  {
    id: 'a3',
    title: 'AI Models Driving High Efficiency Autonomous Browsers',
    category: 'Artificial Intelligence',
    timeAgo: '6h ago',
    imageUrl:
      'https://images.unsplash.com/photo-1677442136019-21780efad99a?auto=format&fit=crop&w=600&q=80',
    articleUrl: 'https://news.google.com',
  },
];

export const BrowserHomeScreen: React.FC<BrowserHomeScreenProps> = ({
  tabCount,
  onNavigateToUrl,
  onTabsClick,
}) => {
  const [searchInput, setSearchInput] = useState<string>('');
  const [shortcuts, setShortcuts] = useState<ShortcutItem[]>(DEFAULT_SHORTCUTS);
  const [showAddShortcut, setShowAddShortcut] = useState<boolean>(false);
  const [newTitle, setNewTitle] = useState<string>('');
  const [newUrl, setNewUrl] = useState<string>('');

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const query = searchInput.trim();
    if (!query) return;

    if (query.startsWith('http://') || query.startsWith('https://')) {
      onNavigateToUrl(query);
    } else if (query.includes('.') && !query.includes(' ')) {
      onNavigateToUrl(`https://${query}`);
    } else {
      onNavigateToUrl(`https://www.google.com/search?q=${encodeURIComponent(query)}`);
    }
  };

  const handleAddShortcut = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTitle.trim() || !newUrl.trim()) return;

    const formattedUrl = newUrl.startsWith('http') ? newUrl : `https://${newUrl}`;
    const newShortcut: ShortcutItem = {
      id: Date.now().toString(),
      title: newTitle.trim(),
      url: formattedUrl,
      iconBg: 'from-blue-600 to-indigo-600',
      iconLetter: newTitle.trim().slice(0, 2).toUpperCase(),
    };

    setShortcuts([...shortcuts, newShortcut]);
    setNewTitle('');
    setNewUrl('');
    setShowAddShortcut(false);
  };

  return (
    <div
      id="browser-home-screen"
      className="relative w-full h-full flex flex-col justify-between overflow-hidden bg-gradient-to-b from-[#90caf9] via-[#bbdefb] to-[#e3f2fd] text-slate-800 select-none"
    >
      {/* Background Liquid Glass Ambient Glows */}
      <div className="absolute -top-24 -left-20 w-80 h-80 rounded-full bg-blue-300/40 blur-3xl pointer-events-none" />
      <div className="absolute top-1/3 -right-24 w-80 h-80 rounded-full bg-cyan-200/50 blur-3xl pointer-events-none" />

      {/* Top Status & Profile Bar */}
      <div className="relative z-10 w-full px-5 pt-3 pb-2 flex items-center justify-between">
        <span className="text-sm font-bold text-slate-800 tracking-tight">9:41</span>
        <div className="flex items-center gap-2.5">
          <span className="text-xs font-semibold text-slate-700">100%</span>
          <div className="w-8 h-8 rounded-full bg-white/60 hover:bg-white/80 border border-white/80 backdrop-blur-md flex items-center justify-center text-blue-600 shadow-xs cursor-pointer">
            <User className="w-4 h-4" />
          </div>
        </div>
      </div>

      {/* Main Scrollable Content */}
      <div className="relative z-10 flex-1 overflow-y-auto px-4 pt-4 pb-28 space-y-6">
        {/* Brand Title / 3D Glass Logo */}
        <div className="flex flex-col items-center justify-center pt-3 pb-2">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-white/40 border border-white/60 backdrop-blur-md mb-2 shadow-xs">
            <Sparkles className="w-3.5 h-3.5 text-blue-600" />
            <span className="text-[11px] font-semibold text-blue-900 tracking-wide">
              LIQUID GLASS BROWSER
            </span>
          </div>
          <h1 className="text-3xl font-extrabold tracking-tight text-slate-900 drop-shadow-xs">
            niooonu
          </h1>
          <p className="text-xs text-slate-600 font-medium mt-0.5">
            fast • fluid • minimalist
          </p>
        </div>

        {/* Omnibox Search Bar */}
        <form onSubmit={handleSearchSubmit} className="relative w-full max-w-md mx-auto">
          <div className="relative w-full h-14 rounded-full bg-white/85 hover:bg-white/95 backdrop-blur-xl border border-white/90 shadow-lg shadow-blue-500/10 flex items-center px-4 transition-all">
            <Search className="w-5 h-5 text-blue-600 shrink-0 mr-3" />
            <input
              type="text"
              value={searchInput}
              onChange={(e) => setSearchInput(e.target.value)}
              placeholder="Search or enter web address"
              className="flex-1 bg-transparent text-sm font-medium text-slate-900 placeholder:text-slate-500 focus:outline-none"
            />
            <div className="flex items-center gap-2 shrink-0 text-slate-500 ml-2">
              <button
                type="button"
                title="Voice search"
                className="w-8 h-8 rounded-full hover:bg-black/5 flex items-center justify-center cursor-pointer"
              >
                <Mic className="w-4 h-4 text-blue-600" />
              </button>
              <button
                type="button"
                title="Google Lens / Camera"
                className="w-8 h-8 rounded-full hover:bg-black/5 flex items-center justify-center cursor-pointer"
              >
                <Camera className="w-4 h-4 text-amber-600" />
              </button>
            </div>
          </div>
        </form>

        {/* Shortcuts Section */}
        <div className="max-w-md mx-auto">
          <div className="flex items-center justify-between mb-3 px-1">
            <span className="text-xs font-bold uppercase tracking-wider text-slate-700">
              Shortcuts
            </span>
            <button
              type="button"
              onClick={() => setShowAddShortcut(true)}
              className="text-xs font-semibold text-blue-700 hover:text-blue-900 flex items-center gap-1 cursor-pointer"
            >
              <Plus className="w-3.5 h-3.5" />
              Add
            </button>
          </div>

          <div className="grid grid-cols-4 gap-3">
            {shortcuts.map((sc) => (
              <button
                key={sc.id}
                type="button"
                onClick={() => onNavigateToUrl(sc.url)}
                className="group flex flex-col items-center gap-1.5 p-2 rounded-2xl hover:bg-white/40 active:scale-95 transition-all cursor-pointer"
              >
                <div
                  className={`w-12 h-12 rounded-2xl bg-gradient-to-tr ${sc.iconBg} text-white flex items-center justify-center font-bold text-sm shadow-md border border-white/40 group-hover:shadow-lg transition-all`}
                >
                  {sc.iconLetter}
                </div>
                <span className="text-[11px] font-semibold text-slate-800 truncate max-w-[64px]">
                  {sc.title}
                </span>
              </button>
            ))}

            {/* Add Button */}
            <button
              type="button"
              onClick={() => setShowAddShortcut(true)}
              className="flex flex-col items-center gap-1.5 p-2 rounded-2xl hover:bg-white/40 active:scale-95 transition-all cursor-pointer"
            >
              <div className="w-12 h-12 rounded-2xl bg-white/50 border border-white/70 backdrop-blur-md text-slate-700 flex items-center justify-center shadow-xs">
                <Plus className="w-5 h-5" />
              </div>
              <span className="text-[11px] font-semibold text-slate-700">Add</span>
            </button>
          </div>
        </div>

        {/* Discover Feed */}
        <div className="max-w-md mx-auto space-y-3 pt-2">
          <div className="flex items-center justify-between px-1">
            <span className="text-xs font-bold uppercase tracking-wider text-slate-700">
              Discover
            </span>
          </div>

          <div className="space-y-3">
            {DISCOVER_ARTICLES.map((article) => (
              <article
                key={article.id}
                onClick={() => onNavigateToUrl(article.articleUrl)}
                className="group rounded-2xl bg-white/70 hover:bg-white/90 border border-white/80 backdrop-blur-md p-3.5 shadow-sm hover:shadow-md transition-all cursor-pointer flex gap-3 items-center"
              >
                <div className="flex-1 space-y-1">
                  <div className="flex items-center gap-2">
                    <span className="text-[10px] font-bold text-blue-600 uppercase tracking-wider">
                      {article.category}
                    </span>
                    <span className="text-[10px] text-slate-400">• {article.timeAgo}</span>
                  </div>
                  <h4 className="text-xs font-bold text-slate-900 line-clamp-2 leading-snug">
                    {article.title}
                  </h4>
                </div>
                <img
                  src={article.imageUrl}
                  alt={article.title}
                  className="w-16 h-16 rounded-xl object-cover border border-white/50 shrink-0"
                />
              </article>
            ))}
          </div>
        </div>
      </div>

      {/* Floating Glass Dock (Fixed at bottom ONLY on Home Screen!) */}
      <div className="absolute bottom-4 left-0 right-0 z-30 px-6 flex justify-center pointer-events-none">
        <div className="pointer-events-auto w-full max-w-sm h-15 rounded-full bg-white/80 hover:bg-white/90 backdrop-blur-2xl border border-white/90 shadow-xl shadow-blue-500/15 flex items-center justify-between px-4">
          {/* Back */}
          <button
            type="button"
            className="w-10 h-10 rounded-full flex items-center justify-center text-slate-400 hover:text-slate-800 transition-colors"
          >
            <ArrowLeft className="w-5 h-5" />
          </button>

          {/* Forward */}
          <button
            type="button"
            className="w-10 h-10 rounded-full flex items-center justify-center text-slate-400 hover:text-slate-800 transition-colors"
          >
            <ArrowRight className="w-5 h-5" />
          </button>

          {/* Center Search Pill */}
          <button
            type="button"
            onClick={() => onNavigateToUrl('https://instagram.com/nio_oon/')}
            title="Search or test Instagram profile"
            className="px-4 py-2 rounded-full bg-gradient-to-r from-blue-500 to-cyan-500 hover:opacity-90 active:scale-95 text-white font-semibold text-xs shadow-md flex items-center gap-1.5 transition-all cursor-pointer"
          >
            <Search className="w-3.5 h-3.5" />
            <span>Search</span>
          </button>

          {/* Tab Switcher Button */}
          <button
            type="button"
            onClick={onTabsClick}
            title="Open tabs switcher"
            className="w-10 h-10 rounded-full flex items-center justify-center hover:bg-black/5 active:scale-95 transition-all cursor-pointer text-slate-800"
          >
            <div className="w-[22px] h-[22px] rounded-[6px] border-2 border-slate-800 bg-white/40 flex items-center justify-center">
              <span className="text-[11px] font-bold text-slate-900 leading-none">
                {tabCount}
              </span>
            </div>
          </button>

          {/* Menu */}
          <button
            type="button"
            className="w-10 h-10 rounded-full flex items-center justify-center hover:bg-black/5 text-slate-800 transition-colors cursor-pointer"
          >
            <MoreVertical className="w-5 h-5" />
          </button>
        </div>
      </div>

      {/* Modal: Add Shortcut */}
      {showAddShortcut && (
        <div className="absolute inset-0 z-50 bg-black/50 backdrop-blur-xs flex items-center justify-center p-4">
          <form
            onSubmit={handleAddShortcut}
            className="w-full max-w-sm bg-white rounded-2xl p-5 shadow-2xl border border-white/60 space-y-3 animate-in zoom-in-95 duration-150"
          >
            <h3 className="font-bold text-slate-900 text-sm">Add New Shortcut</h3>
            <input
              type="text"
              placeholder="Name (e.g. Google Docs)"
              value={newTitle}
              onChange={(e) => setNewTitle(e.target.value)}
              className="w-full h-10 px-3 rounded-xl border border-slate-300 text-sm focus:outline-blue-500"
              autoFocus
            />
            <input
              type="text"
              placeholder="URL (e.g. docs.google.com)"
              value={newUrl}
              onChange={(e) => setNewUrl(e.target.value)}
              className="w-full h-10 px-3 rounded-xl border border-slate-300 text-sm focus:outline-blue-500"
            />
            <div className="flex justify-end gap-2 pt-1 text-xs">
              <button
                type="button"
                onClick={() => setShowAddShortcut(false)}
                className="px-3 py-2 text-slate-600 hover:bg-slate-100 rounded-lg cursor-pointer"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-lg shadow-xs cursor-pointer"
              >
                Save
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
};
