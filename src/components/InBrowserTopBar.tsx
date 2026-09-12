import React from 'react';
import { Home, Plus, MoreVertical } from 'lucide-react';

interface InBrowserTopBarProps {
  currentUrl: string;
  tabCount: number;
  onHomeClick: () => Unit;
  onUrlClick: () => Unit;
  onNewTabClick: () => Unit;
  onTabsClick: () => Unit;
  onMenuClick: () => Unit;
}

type Unit = void;

export const InBrowserTopBar: React.FC<InBrowserTopBarProps> = ({
  currentUrl,
  tabCount,
  onHomeClick,
  onUrlClick,
  onNewTabClick,
  onTabsClick,
  onMenuClick,
}) => {
  // Format URL nicely like in the user's screenshot (e.g. "instagram.com/nio_oon/")
  const formatDisplayUrl = (raw: string): string => {
    let clean = raw.trim();
    clean = clean.replace(/^https?:\/\//i, '');
    clean = clean.replace(/^www\./i, '');
    return clean || 'Search or type URL';
  };

  return (
    <header
      id="in-browser-top-bar"
      className="w-full bg-[#111827]/95 backdrop-blur-xl border-b border-white/10 px-2 py-2 flex items-center gap-1.5 z-40 select-none text-white transition-all shadow-md"
    >
      {/* 1. Home Button (🏠) */}
      <button
        id="top-bar-home-btn"
        type="button"
        onClick={onHomeClick}
        title="Go to Home"
        className="w-10 h-10 rounded-full flex items-center justify-center hover:bg-white/10 active:scale-95 transition-all text-white/90 hover:text-white cursor-pointer shrink-0"
      >
        <Home className="w-5 h-5 stroke-[2]" />
      </button>

      {/* 2. Pill Address Bar */}
      <div
        id="top-bar-address-pill"
        onClick={onUrlClick}
        title="Click to edit URL or search"
        className="flex-1 h-9 px-3.5 rounded-full bg-slate-800/80 hover:bg-slate-700/80 border border-white/20 flex items-center cursor-pointer active:scale-[0.99] transition-all overflow-hidden"
      >
        <span className="text-[13.5px] font-normal text-slate-100 truncate tracking-tight">
          {formatDisplayUrl(currentUrl)}
        </span>
      </div>

      {/* 3. New Tab Button (+) */}
      <button
        id="top-bar-new-tab-btn"
        type="button"
        onClick={onNewTabClick}
        title="New Tab"
        className="w-9 h-9 rounded-full flex items-center justify-center hover:bg-white/10 active:scale-95 transition-all text-white/90 hover:text-white cursor-pointer shrink-0"
      >
        <Plus className="w-5 h-5 stroke-[2.2]" />
      </button>

      {/* 4. Tab Counter Badge ([ N ]) */}
      <button
        id="top-bar-tab-count-btn"
        type="button"
        onClick={onTabsClick}
        title={`View all ${tabCount} tabs`}
        className="w-9 h-9 rounded-full flex items-center justify-center hover:bg-white/10 active:scale-95 transition-all text-white cursor-pointer shrink-0"
      >
        <div className="w-[22px] h-[22px] rounded-[6px] border-[1.5px] border-white/90 bg-white/10 flex items-center justify-center shadow-xs">
          <span className="text-[11px] font-bold text-white leading-none">
            {tabCount > 99 ? '99+' : tabCount}
          </span>
        </div>
      </button>

      {/* 5. Three-Dots Menu (⋮) */}
      <button
        id="top-bar-menu-btn"
        type="button"
        onClick={onMenuClick}
        title="More options"
        className="w-9 h-9 rounded-full flex items-center justify-center hover:bg-white/10 active:scale-95 transition-all text-white/90 hover:text-white cursor-pointer shrink-0"
      >
        <MoreVertical className="w-5 h-5 stroke-[2.2]" />
      </button>
    </header>
  );
};
