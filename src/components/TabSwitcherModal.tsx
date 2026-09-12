import React from 'react';
import { Plus, X, Globe, Layers } from 'lucide-react';
import { BrowserTab } from '../types';

interface TabSwitcherModalProps {
  tabs: BrowserTab[];
  activeTabId: string;
  onSelectTab: (tabId: string) => void;
  onCloseTab: (tabId: string) => void;
  onNewTab: () => void;
  onCloseAll: () => void;
  onDismiss: () => void;
}

export const TabSwitcherModal: React.FC<TabSwitcherModalProps> = ({
  tabs,
  activeTabId,
  onSelectTab,
  onCloseTab,
  onNewTab,
  onCloseAll,
  onDismiss,
}) => {
  return (
    <div
      id="tab-switcher-modal"
      className="absolute inset-0 z-50 bg-[#090e1a]/95 backdrop-blur-2xl flex flex-col p-4 animate-in fade-in duration-200"
    >
      {/* Top Header */}
      <div className="flex items-center justify-between py-2 px-1 border-b border-white/10 mb-3">
        <div className="flex items-center gap-2">
          <Layers className="w-5 h-5 text-blue-400" />
          <h2 className="text-lg font-bold text-white tracking-tight">
            Tabs ({tabs.length})
          </h2>
        </div>

        <div className="flex items-center gap-2">
          <button
            id="tab-modal-new-tab-btn"
            type="button"
            onClick={onNewTab}
            className="flex items-center gap-1.5 px-3 py-1.5 rounded-full bg-blue-600 hover:bg-blue-500 active:scale-95 text-white text-xs font-semibold shadow-md transition-all cursor-pointer"
          >
            <Plus className="w-3.5 h-3.5" />
            <span>New Tab</span>
          </button>

          <button
            id="tab-modal-close-btn"
            type="button"
            onClick={onDismiss}
            className="w-8 h-8 rounded-full bg-white/10 hover:bg-white/20 active:scale-95 flex items-center justify-center text-white/80 hover:text-white transition-all cursor-pointer"
          >
            <X className="w-4 h-4" />
          </button>
        </div>
      </div>

      {/* Tabs Grid */}
      <div className="flex-1 overflow-y-auto pr-1 grid grid-cols-2 gap-3 auto-rows-max pb-4">
        {tabs.map((tab) => {
          const isActive = tab.id === activeTabId;
          const displayUrl = tab.url
            ? tab.url.replace(/^https?:\/\//i, '').replace(/^www\./i, '')
            : 'New Tab (Home)';

          return (
            <div
              key={tab.id}
              id={`tab-card-${tab.id}`}
              onClick={() => onSelectTab(tab.id)}
              className={`group relative rounded-2xl p-3 flex flex-col justify-between h-44 cursor-pointer transition-all ${
                isActive
                  ? 'bg-blue-950/40 border-2 border-blue-500 shadow-lg shadow-blue-500/20'
                  : 'bg-slate-800/60 hover:bg-slate-800 border border-white/15'
              }`}
            >
              {/* Header row with title & close button */}
              <div className="flex items-center justify-between gap-1 w-full">
                <span className="text-xs font-semibold text-white truncate flex-1">
                  {tab.title || 'Untitled Tab'}
                </span>
                <button
                  type="button"
                  onClick={(e) => {
                    e.stopPropagation();
                    onCloseTab(tab.id);
                  }}
                  title="Close tab"
                  className="w-6 h-6 rounded-full bg-black/40 hover:bg-red-500 text-white/80 hover:text-white flex items-center justify-center transition-colors cursor-pointer shrink-0"
                >
                  <X className="w-3.5 h-3.5" />
                </button>
              </div>

              {/* URL snippet */}
              <p className="text-[10.5px] text-slate-400 truncate mt-0.5">
                {displayUrl}
              </p>

              {/* Visual Card Preview */}
              <div className="mt-2 flex-1 rounded-xl bg-slate-900/80 border border-white/10 flex flex-col items-center justify-center p-2 text-center overflow-hidden">
                <Globe className={`w-6 h-6 mb-1 ${isActive ? 'text-blue-400' : 'text-slate-500'}`} />
                <span className="text-[10px] text-slate-300 font-medium truncate max-w-full">
                  {tab.url ? new URL(tab.url.startsWith('http') ? tab.url : `https://${tab.url}`).hostname : 'niooonu home'}
                </span>
              </div>

              {isActive && (
                <div className="mt-1.5 flex items-center justify-center">
                  <span className="text-[10px] text-blue-400 font-semibold tracking-wide">
                    ACTIVE
                  </span>
                </div>
              )}
            </div>
          );
        })}
      </div>

      {/* Bottom Footer */}
      {tabs.length > 1 && (
        <div className="pt-2 border-t border-white/10 flex justify-between items-center text-xs">
          <button
            type="button"
            onClick={onCloseAll}
            className="text-red-400 hover:text-red-300 font-medium cursor-pointer"
          >
            Close All Tabs
          </button>
          <span className="text-slate-400">{tabs.length} open</span>
        </div>
      )}
    </div>
  );
};
