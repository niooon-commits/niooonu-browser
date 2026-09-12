import React, { useState } from 'react';
import {
  ArrowLeft,
  ArrowRight,
  Star,
  Download,
  RotateCw,
  SquarePlus,
  LayoutGrid,
  Copy,
  History,
  Trash2,
  SlidersHorizontal,
  FolderDown,
  MonitorSmartphone,
  Share2,
  FileSearch,
  Languages,
  BookOpen,
  Monitor,
  Check,
  Shield,
  X,
} from 'lucide-react';

interface BrowserMenuPopupProps {
  currentUrl: string;
  isDesktopMode: boolean;
  onClose: () => void;
  onReload: () => void;
  onNewTab: () => void;
  onNewIncognitoTab: () => void;
  onToggleDesktopMode: () => void;
  onBookmark: () => void;
  onShare: () => void;
  onHistory: () => void;
  onDownloads: () => void;
  onClearData: () => void;
  onFindInPage: () => void;
  onTranslate: () => void;
  onBack?: () => void;
  onForward?: () => void;
}

export const BrowserMenuPopup: React.FC<BrowserMenuPopupProps> = ({
  currentUrl,
  isDesktopMode,
  onClose,
  onReload,
  onNewTab,
  onNewIncognitoTab,
  onToggleDesktopMode,
  onBookmark,
  onShare,
  onHistory,
  onDownloads,
  onClearData,
  onFindInPage,
  onTranslate,
  onBack,
  onForward,
}) => {
  const [isBookmarked, setIsBookmarked] = useState<boolean>(false);

  const handleBookmarkClick = () => {
    setIsBookmarked(!isBookmarked);
    onBookmark();
  };

  return (
    <div
      id="browser-menu-backdrop"
      onClick={onClose}
      className="absolute inset-0 z-50 bg-black/40 backdrop-blur-[2px] flex justify-end p-2 sm:p-3 overflow-hidden select-none"
    >
      <div
        id="browser-menu-popup"
        onClick={(e) => e.stopPropagation()}
        className="w-[280px] sm:w-[290px] max-h-[85vh] bg-[#222428] text-slate-100 rounded-[26px] shadow-[0_12px_45px_rgba(0,0,0,0.85)] border border-white/[0.09] flex flex-col overflow-hidden animate-in fade-in zoom-in-95 duration-150 origin-top-right"
      >
        {/* TOP ROW: Quick Action Buttons (Back, Forward, Bookmark, Download, Reload) */}
        <div className="p-2.5 pb-2 bg-[#2a2c31] border-b border-white/[0.07] flex items-center justify-between gap-1">
          {/* Back */}
          <button
            type="button"
            onClick={() => {
              if (onBack) onBack();
              onClose();
            }}
            title="Back"
            className="w-10 h-10 rounded-full flex items-center justify-center bg-white/[0.04] hover:bg-white/10 active:scale-95 text-slate-200 hover:text-white transition-all cursor-pointer"
          >
            <ArrowLeft className="w-5 h-5 stroke-[2]" />
          </button>

          {/* Forward */}
          <button
            type="button"
            onClick={() => {
              if (onForward) onForward();
              onClose();
            }}
            title="Forward"
            className="w-10 h-10 rounded-full flex items-center justify-center bg-white/[0.04] hover:bg-white/10 active:scale-95 text-slate-400 hover:text-white transition-all cursor-pointer"
          >
            <ArrowRight className="w-5 h-5 stroke-[2]" />
          </button>

          {/* Bookmark Star */}
          <button
            type="button"
            onClick={handleBookmarkClick}
            title={isBookmarked ? 'Bookmarked' : 'Add to bookmarks'}
            className={`w-10 h-10 rounded-full flex items-center justify-center transition-all cursor-pointer active:scale-95 ${
              isBookmarked
                ? 'bg-amber-400/20 text-amber-300'
                : 'bg-white/[0.04] hover:bg-white/10 text-slate-200 hover:text-white'
            }`}
          >
            <Star
              className={`w-5 h-5 stroke-[2] ${isBookmarked ? 'fill-amber-400 text-amber-400' : ''}`}
            />
          </button>

          {/* Download */}
          <button
            type="button"
            onClick={() => {
              onDownloads();
              onClose();
            }}
            title="Download page"
            className="w-10 h-10 rounded-full flex items-center justify-center bg-white/[0.04] hover:bg-white/10 active:scale-95 text-slate-200 hover:text-white transition-all cursor-pointer"
          >
            <Download className="w-5 h-5 stroke-[2]" />
          </button>

          {/* Reload */}
          <button
            type="button"
            onClick={() => {
              onReload();
              onClose();
            }}
            title="Reload"
            className="w-10 h-10 rounded-full flex items-center justify-center bg-white/[0.04] hover:bg-white/10 active:scale-95 text-slate-200 hover:text-white transition-all cursor-pointer"
          >
            <RotateCw className="w-5 h-5 stroke-[2]" />
          </button>
        </div>

        {/* MENU ITEMS LIST (Scrollable) */}
        <div className="flex-1 overflow-y-auto py-1.5 px-1.5 scrollbar-thin scrollbar-thumb-white/10">
          {/* 1. New Tab */}
          <button
            type="button"
            onClick={() => {
              onNewTab();
              onClose();
            }}
            className="w-full flex items-center gap-3.5 px-3.5 py-2.5 rounded-xl hover:bg-white/[0.08] active:bg-white/[0.14] text-slate-200 hover:text-white transition-colors cursor-pointer text-left"
          >
            <SquarePlus className="w-[19px] h-[19px] text-slate-300 stroke-[1.9] shrink-0" />
            <span className="text-[14px] font-medium tracking-tight">New tab</span>
          </button>

          {/* 2. New Incognito Tab */}
          <button
            type="button"
            onClick={() => {
              onNewIncognitoTab();
              onClose();
            }}
            className="w-full flex items-center gap-3.5 px-3.5 py-2.5 rounded-xl hover:bg-white/[0.08] active:bg-white/[0.14] text-slate-200 hover:text-white transition-colors cursor-pointer text-left"
          >
            {/* Custom Incognito Hat + Glasses SVG Icon */}
            <svg
              className="w-[19px] h-[19px] text-slate-300 shrink-0"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="M4 11h16" />
              <path d="M7 11c0-4 2-7 5-7s5 3 5 7" />
              <circle cx="8" cy="16" r="3" />
              <circle cx="16" cy="16" r="3" />
              <path d="M11 16h2" />
            </svg>
            <span className="text-[14px] font-medium tracking-tight">New Incognito tab</span>
          </button>

          {/* 3. Move tab to group */}
          <button
            type="button"
            onClick={() => {
              onClose();
            }}
            className="w-full flex items-center gap-3.5 px-3.5 py-2.5 rounded-xl hover:bg-white/[0.08] active:bg-white/[0.14] text-slate-200 hover:text-white transition-colors cursor-pointer text-left"
          >
            <LayoutGrid className="w-[19px] h-[19px] text-slate-300 stroke-[1.9] shrink-0" />
            <span className="text-[14px] font-medium tracking-tight">Move tab to group</span>
          </button>

          {/* 4. Manage windows */}
          <button
            type="button"
            onClick={() => {
              onClose();
            }}
            className="w-full flex items-center gap-3.5 px-3.5 py-2.5 rounded-xl hover:bg-white/[0.08] active:bg-white/[0.14] text-slate-200 hover:text-white transition-colors cursor-pointer text-left"
          >
            <Copy className="w-[19px] h-[19px] text-slate-300 stroke-[1.9] shrink-0" />
            <span className="text-[14px] font-medium tracking-tight">Manage windows</span>
          </button>

          {/* Divider */}
          <div className="h-[1px] bg-white/[0.08] my-1 mx-2" />

          {/* 5. History */}
          <button
            type="button"
            onClick={() => {
              onHistory();
              onClose();
            }}
            className="w-full flex items-center gap-3.5 px-3.5 py-2.5 rounded-xl hover:bg-white/[0.08] active:bg-white/[0.14] text-slate-200 hover:text-white transition-colors cursor-pointer text-left"
          >
            <History className="w-[19px] h-[19px] text-slate-300 stroke-[1.9] shrink-0" />
            <span className="text-[14px] font-medium tracking-tight">History</span>
          </button>

          {/* 6. Delete browsing data */}
          <button
            type="button"
            onClick={() => {
              onClearData();
              onClose();
            }}
            className="w-full flex items-center gap-3.5 px-3.5 py-2.5 rounded-xl hover:bg-white/[0.08] active:bg-white/[0.14] text-slate-200 hover:text-white transition-colors cursor-pointer text-left"
          >
            <Trash2 className="w-[19px] h-[19px] text-slate-300 stroke-[1.9] shrink-0" />
            <span className="text-[14px] font-medium tracking-tight">Delete browsing data</span>
          </button>

          {/* 7. Site controls */}
          <button
            type="button"
            onClick={() => {
              onClose();
            }}
            className="w-full flex items-center gap-3.5 px-3.5 py-2.5 rounded-xl hover:bg-white/[0.08] active:bg-white/[0.14] text-slate-200 hover:text-white transition-colors cursor-pointer text-left"
          >
            <SlidersHorizontal className="w-[19px] h-[19px] text-slate-300 stroke-[1.9] shrink-0" />
            <span className="text-[14px] font-medium tracking-tight">Site controls</span>
          </button>

          {/* Divider */}
          <div className="h-[1px] bg-white/[0.08] my-1 mx-2" />

          {/* 8. Downloads */}
          <button
            type="button"
            onClick={() => {
              onDownloads();
              onClose();
            }}
            className="w-full flex items-center gap-3.5 px-3.5 py-2.5 rounded-xl hover:bg-white/[0.08] active:bg-white/[0.14] text-slate-200 hover:text-white transition-colors cursor-pointer text-left"
          >
            {/* Checked tray / download icon from screenshot */}
            <svg
              className="w-[19px] h-[19px] text-slate-300 shrink-0"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="M4 17v2a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-2" />
              <polyline points="7 11 10 14 17 7" />
            </svg>
            <span className="text-[14px] font-medium tracking-tight">Downloads</span>
          </button>

          {/* 9. Bookmarks */}
          <button
            type="button"
            onClick={() => {
              onBookmark();
              onClose();
            }}
            className="w-full flex items-center gap-3.5 px-3.5 py-2.5 rounded-xl hover:bg-white/[0.08] active:bg-white/[0.14] text-slate-200 hover:text-white transition-colors cursor-pointer text-left"
          >
            <Star className="w-[19px] h-[19px] text-slate-300 stroke-[1.9] shrink-0" />
            <span className="text-[14px] font-medium tracking-tight">Bookmarks</span>
          </button>

          {/* 10. Recent tabs */}
          <button
            type="button"
            onClick={() => {
              onClose();
            }}
            className="w-full flex items-center gap-3.5 px-3.5 py-2.5 rounded-xl hover:bg-white/[0.08] active:bg-white/[0.14] text-slate-200 hover:text-white transition-colors cursor-pointer text-left"
          >
            <MonitorSmartphone className="w-[19px] h-[19px] text-slate-300 stroke-[1.9] shrink-0" />
            <span className="text-[14px] font-medium tracking-tight">Recent tabs</span>
          </button>

          {/* Divider */}
          <div className="h-[1px] bg-white/[0.08] my-1 mx-2" />

          {/* 11. Share... (With WhatsApp badge on the right as in user's image) */}
          <button
            type="button"
            onClick={() => {
              onShare();
              onClose();
            }}
            className="w-full flex items-center justify-between px-3.5 py-2.5 rounded-xl hover:bg-white/[0.08] active:bg-white/[0.14] text-slate-200 hover:text-white transition-colors cursor-pointer text-left"
          >
            <div className="flex items-center gap-3.5">
              <Share2 className="w-[19px] h-[19px] text-slate-300 stroke-[1.9] shrink-0" />
              <span className="text-[14px] font-medium tracking-tight">Share...</span>
            </div>

            {/* Green WhatsApp circle badge exactly like in screenshot */}
            <div
              title="Quick share via WhatsApp"
              className="w-6 h-6 rounded-full bg-[#25D366] flex items-center justify-center text-white shadow-sm shrink-0"
            >
              <svg className="w-3.5 h-3.5 fill-current" viewBox="0 0 24 24">
                <path d="M12.031 6.172c-3.181 0-5.767 2.586-5.768 5.766-.001 1.298.38 2.27 1.019 3.287l-.582 2.128 2.182-.573c.978.58 1.911.928 3.145.929 3.178 0 5.767-2.587 5.768-5.766.001-3.187-2.575-5.77-5.764-5.771zm3.392 8.244c-.144.405-.837.774-1.17.824-.299.045-.677.063-1.092-.069-.252-.08-.575-.187-.988-.365-1.739-.751-2.874-2.502-2.961-2.617-.087-.116-.708-.94-.708-1.793s.448-1.273.607-1.446c.159-.173.346-.217.462-.217l.332.006c.106.005.249-.04.39.298.144.347.491 1.2.534 1.288.043.088.072.188.014.304-.058.116-.087.188-.173.289l-.26.304c-.087.086-.177.18-.076.353.101.173.449.741.964 1.2.662.591 1.221.774 1.394.86.173.086.275.072.376-.043.101-.116.433-.506.549-.679.116-.173.231-.145.39-.087s1.011.477 1.184.564.289.13.332.202c.043.072.043.419-.101.824z" />
              </svg>
            </div>
          </button>

          {/* 12. Find in page */}
          <button
            type="button"
            onClick={() => {
              onFindInPage();
              onClose();
            }}
            className="w-full flex items-center gap-3.5 px-3.5 py-2.5 rounded-xl hover:bg-white/[0.08] active:bg-white/[0.14] text-slate-200 hover:text-white transition-colors cursor-pointer text-left"
          >
            <FileSearch className="w-[19px] h-[19px] text-slate-300 stroke-[1.9] shrink-0" />
            <span className="text-[14px] font-medium tracking-tight">Find in page</span>
          </button>

          {/* 13. Translate... */}
          <button
            type="button"
            onClick={() => {
              onTranslate();
              onClose();
            }}
            className="w-full flex items-center gap-3.5 px-3.5 py-2.5 rounded-xl hover:bg-white/[0.08] active:bg-white/[0.14] text-slate-200 hover:text-white transition-colors cursor-pointer text-left"
          >
            <Languages className="w-[19px] h-[19px] text-slate-300 stroke-[1.9] shrink-0" />
            <span className="text-[14px] font-medium tracking-tight">Translate...</span>
          </button>

          {/* 14. Desktop site toggle with checkmark */}
          <button
            type="button"
            onClick={() => {
              onToggleDesktopMode();
              onClose();
            }}
            className="w-full flex items-center justify-between px-3.5 py-2.5 rounded-xl hover:bg-white/[0.08] active:bg-white/[0.14] text-slate-200 hover:text-white transition-colors cursor-pointer text-left"
          >
            <div className="flex items-center gap-3.5">
              <Monitor className="w-[19px] h-[19px] text-slate-300 stroke-[1.9] shrink-0" />
              <span className="text-[14px] font-medium tracking-tight">Desktop site</span>
            </div>

            <div
              className={`w-4.5 h-4.5 rounded border flex items-center justify-center transition-colors ${
                isDesktopMode
                  ? 'bg-blue-600 border-blue-500 text-white'
                  : 'border-white/30 bg-white/5'
              }`}
            >
              {isDesktopMode && <Check className="w-3 h-3 stroke-[3]" />}
            </div>
          </button>
        </div>
      </div>
    </div>
  );
};
