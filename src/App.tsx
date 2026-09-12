import React, { useState } from 'react';
import {
  Smartphone,
  Maximize2,
  Download,
  Github,
  CheckCircle2,
} from 'lucide-react';
import { BrowserTab } from './types';
import { BrowserHomeScreen } from './components/BrowserHomeScreen';
import { WebViewScreen } from './components/WebViewScreen';
import { TabSwitcherModal } from './components/TabSwitcherModal';

export default function App() {
  // Tabs management
  const [tabs, setTabs] = useState<BrowserTab[]>([
    {
      id: 'tab-1',
      title: 'Instagram (@nio_oon)',
      url: 'https://instagram.com/nio_oon/',
    },
    {
      id: 'tab-2',
      title: 'Google Search',
      url: 'https://www.google.com',
    },
    {
      id: 'tab-3',
      title: 'GitHub - niooonu-browser',
      url: 'https://github.com/niooon-commits/niooonu-browser',
    },
  ]);

  const [activeTabIndex, setActiveTabIndex] = useState<number>(0);
  const [isShowingHomeScreen, setIsShowingHomeScreen] = useState<boolean>(false);
  const [showTabSwitcher, setShowTabSwitcher] = useState<boolean>(false);
  const [isPhoneFrame, setIsPhoneFrame] = useState<boolean>(true);

  // Active tab reference
  const currentTab = tabs[activeTabIndex] || tabs[0];

  // Handlers
  const handleNavigateToUrl = (url: string) => {
    const updated = [...tabs];
    const cleanTitle = url.replace(/^https?:\/\//i, '').replace(/^www\./i, '');
    if (activeTabIndex in updated) {
      updated[activeTabIndex] = {
        ...updated[activeTabIndex],
        url,
        title: cleanTitle,
      };
      setTabs(updated);
    } else {
      const newTab: BrowserTab = {
        id: `tab-${Date.now()}`,
        title: cleanTitle,
        url,
      };
      setTabs([...updated, newTab]);
      setActiveTabIndex(updated.length);
    }
    setIsShowingHomeScreen(false);
  };

  const handleNewTab = () => {
    const newTab: BrowserTab = {
      id: `tab-${Date.now()}`,
      title: 'New Tab',
      url: '',
    };
    const newTabs = [...tabs, newTab];
    setTabs(newTabs);
    setActiveTabIndex(newTabs.length - 1);
    setIsShowingHomeScreen(true);
    setShowTabSwitcher(false);
  };

  const handleSelectTab = (tabId: string) => {
    const idx = tabs.findIndex((t) => t.id === tabId);
    if (idx !== -1) {
      setActiveTabIndex(idx);
      setIsShowingHomeScreen(!tabs[idx].url);
    }
    setShowTabSwitcher(false);
  };

  const handleCloseTab = (tabId: string) => {
    if (tabs.length > 1) {
      const remaining = tabs.filter((t) => t.id !== tabId);
      setTabs(remaining);
      setActiveTabIndex((prev) => Math.min(prev, remaining.length - 1));
    } else {
      setTabs([{ id: `tab-${Date.now()}`, title: 'Home', url: '' }]);
      setActiveTabIndex(0);
      setIsShowingHomeScreen(true);
    }
  };

  const handleCloseAllTabs = () => {
    setTabs([{ id: `tab-${Date.now()}`, title: 'Home', url: '' }]);
    setActiveTabIndex(0);
    setIsShowingHomeScreen(true);
    setShowTabSwitcher(false);
  };

  return (
    <div className="w-full min-h-screen bg-slate-950 text-slate-100 flex flex-col items-center justify-start p-2 sm:p-4 selection:bg-blue-600 selection:text-white">
      {/* Top Banner: Status & Controls */}
      <header className="w-full max-w-4xl mb-3 flex flex-wrap items-center justify-between gap-3 px-3 py-2.5 rounded-2xl bg-slate-900/90 border border-white/10 backdrop-blur-md shadow-lg">
        <div className="flex items-center gap-3">
          <div className="w-8 h-8 rounded-xl bg-gradient-to-tr from-blue-600 to-cyan-500 flex items-center justify-center text-white font-bold text-sm shadow-md">
            n
          </div>
          <div>
            <h1 className="text-sm font-bold text-white flex items-center gap-2">
              niooonu browser
              <span className="text-[10px] font-semibold px-2 py-0.5 rounded-full bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 flex items-center gap-1">
                <CheckCircle2 className="w-3 h-3" />
                Live Liquid Glass UI
              </span>
            </h1>
            <p className="text-[11px] text-slate-400">
              Search Flow with Top Bar, Tabs Switcher &amp; Native Release Pipeline
            </p>
          </div>
        </div>

        <div className="flex items-center gap-2">
          {/* Toggle Device / Full View */}
          <button
            type="button"
            onClick={() => setIsPhoneFrame(!isPhoneFrame)}
            className="flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-slate-800 hover:bg-slate-700 text-xs font-medium text-slate-200 border border-white/10 transition-colors cursor-pointer"
          >
            {isPhoneFrame ? (
              <>
                <Maximize2 className="w-3.5 h-3.5" />
                <span className="hidden sm:inline">Expanded View</span>
              </>
            ) : (
              <>
                <Smartphone className="w-3.5 h-3.5" />
                <span className="hidden sm:inline">Phone Frame</span>
              </>
            )}
          </button>

          {/* GitHub Repository */}
          <a
            href="https://github.com/niooon-commits/niooonu-browser/releases"
            target="_blank"
            rel="noopener noreferrer"
            className="flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-blue-600 hover:bg-blue-500 text-xs font-semibold text-white shadow-md transition-all active:scale-95 cursor-pointer"
          >
            <Download className="w-3.5 h-3.5" />
            <span>Download APK (v1.0.0)</span>
          </a>
        </div>
      </header>

      {/* Main Interactive Browser Stage */}
      <main
        className={`relative transition-all duration-300 ${
          isPhoneFrame
            ? 'w-full max-w-[412px] h-[844px] rounded-[44px] p-2 bg-gradient-to-b from-neutral-800 via-neutral-900 to-neutral-950 border-[6px] border-neutral-700/80 shadow-[0_25px_60px_-15px_rgba(0,0,0,0.8)]'
            : 'w-full max-w-4xl h-[780px] rounded-3xl p-1 bg-neutral-900 border border-white/15 shadow-2xl'
        }`}
      >
        {/* Phone Speaker & Camera Notch (when in Phone Frame mode) */}
        {isPhoneFrame && (
          <div className="absolute top-3 left-1/2 -translate-x-1/2 w-28 h-4 rounded-full bg-black/90 border border-white/10 z-50 flex items-center justify-center">
            <div className="w-2.5 h-2.5 rounded-full bg-neutral-950 border border-neutral-800 mr-2" />
            <div className="w-10 h-1 rounded-full bg-neutral-800" />
          </div>
        )}

        {/* Inner Screen Canvas */}
        <div
          id="browser-viewport"
          className="relative w-full h-full rounded-[38px] overflow-hidden bg-black flex flex-col"
        >
          {isShowingHomeScreen || !currentTab.url ? (
            /* Home Screen with Omnibox and Bottom Floating Dock */
            <BrowserHomeScreen
              tabCount={tabs.length}
              onNavigateToUrl={handleNavigateToUrl}
              onTabsClick={() => setShowTabSwitcher(true)}
            />
          ) : (
            /* In-Browser View with Top Bar and Full-Page Content (NO BOTTOM DOCK!) */
            <WebViewScreen
              currentUrl={currentTab.url}
              tabCount={tabs.length}
              onHomeClick={() => setIsShowingHomeScreen(true)}
              onNewTabClick={handleNewTab}
              onTabsClick={() => setShowTabSwitcher(true)}
              onNavigateToUrl={handleNavigateToUrl}
              onCloseWebView={() => handleCloseTab(currentTab.id)}
            />
          )}

          {/* Liquid Glass Multi-Tab Switcher Modal */}
          {showTabSwitcher && (
            <TabSwitcherModal
              tabs={tabs}
              activeTabId={currentTab.id}
              onSelectTab={handleSelectTab}
              onCloseTab={handleCloseTab}
              onNewTab={handleNewTab}
              onCloseAll={handleCloseAllTabs}
              onDismiss={() => setShowTabSwitcher(false)}
            />
          )}
        </div>
      </main>

      {/* Footer Info */}
      <footer className="mt-3 text-center text-[11px] text-slate-500">
        Tap the 🏠 Home icon to return to Home, the pill address bar to edit URL, ➕ for New Tab, or [ {tabs.length} ] to switch tabs.
      </footer>
    </div>
  );
}
