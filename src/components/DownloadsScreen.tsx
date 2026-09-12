import React, { useState } from 'react';
import {
  Download,
  Settings,
  X,
  FileCode,
  Image,
  Video,
  FileText,
  File,
  MoreVertical,
  Share2,
  Trash2,
  ExternalLink,
  CheckCircle2,
} from 'lucide-react';
import { DownloadItem } from '../types';

interface DownloadsScreenProps {
  onClose: () => void;
  downloads: DownloadItem[];
  onCancelDownload?: (id: string) => void;
  onDeleteDownload?: (id: string) => void;
}

export const DownloadsScreen: React.FC<DownloadsScreenProps> = ({
  onClose,
  downloads,
  onCancelDownload,
  onDeleteDownload,
}) => {
  const [selectedCategory, setSelectedCategory] = useState<string>('All');
  const [selectedItemForAction, setSelectedItemForAction] = useState<DownloadItem | null>(null);
  const [showSettings, setShowSettings] = useState<boolean>(false);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const showToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 2500);
  };

  const categories = ['All', 'APK', 'Images', 'Videos', 'Docs'];

  const activeDownloads = downloads.filter((d) => d.status === 'downloading');
  const completedDownloads = downloads.filter((d) => d.status !== 'downloading');

  const filteredList = completedDownloads.filter((item) => {
    if (selectedCategory === 'All') return true;
    if (selectedCategory === 'APK') return item.category === 'apk';
    if (selectedCategory === 'Images') return item.category === 'image';
    if (selectedCategory === 'Videos') return item.category === 'video';
    if (selectedCategory === 'Docs') return item.category === 'doc';
    return true;
  });

  const getCategoryIcon = (category: string) => {
    switch (category) {
      case 'apk':
        return <FileCode className="w-5 h-5 text-emerald-400" />;
      case 'image':
        return <Image className="w-5 h-5 text-purple-400" />;
      case 'video':
        return <Video className="w-5 h-5 text-pink-400" />;
      case 'doc':
        return <FileText className="w-5 h-5 text-blue-400" />;
      default:
        return <File className="w-5 h-5 text-slate-400" />;
    }
  };

  const getCategoryBg = (category: string) => {
    switch (category) {
      case 'apk':
        return 'bg-emerald-950/60 border border-emerald-500/30';
      case 'image':
        return 'bg-purple-950/60 border border-purple-500/30';
      case 'video':
        return 'bg-pink-950/60 border border-pink-500/30';
      case 'doc':
        return 'bg-blue-950/60 border border-blue-500/30';
      default:
        return 'bg-slate-800 border border-white/10';
    }
  };

  return (
    <div
      id="downloads-screen"
      className="absolute inset-0 z-40 bg-[#140C0B] text-white flex flex-col overflow-hidden select-none animate-in fade-in duration-200"
    >
      {/* 1. Header Bar */}
      <div className="flex items-center justify-between px-5 pt-4 pb-2">
        <h2 className="text-xl font-semibold tracking-tight text-slate-100">Downloads</h2>
        <div className="flex items-center gap-1">
          <button
            type="button"
            onClick={() => setShowSettings(true)}
            className="w-9 h-9 rounded-full hover:bg-white/10 flex items-center justify-center text-slate-300 transition-colors cursor-pointer"
          >
            <Settings className="w-5 h-5" />
          </button>
          <button
            type="button"
            onClick={onClose}
            className="w-9 h-9 rounded-full hover:bg-white/10 flex items-center justify-center text-slate-300 transition-colors cursor-pointer"
          >
            <X className="w-5 h-5" />
          </button>
        </div>
      </div>

      {/* 2. Subtitle: Storage Info */}
      <p className="px-5 text-xs text-[#B0A4A4] font-normal mb-3">
        Using {downloads.length > 0 ? '14.8 MB' : '0.00 KB'} of 110.77 GB
      </p>

      {/* 3. Categories Row */}
      <div className="flex items-center gap-2 px-4 overflow-x-auto pb-2 scrollbar-none">
        {categories.map((cat) => {
          const isSelected = selectedCategory === cat;
          return (
            <button
              key={cat}
              type="button"
              onClick={() => setSelectedCategory(cat)}
              className={`px-3.5 py-1.5 rounded-full text-xs font-medium shrink-0 transition-all cursor-pointer ${
                isSelected
                  ? 'bg-blue-600 text-white shadow-sm shadow-blue-500/30'
                  : 'bg-[#232529] hover:bg-[#2e3137] text-slate-300 border border-white/5'
              }`}
            >
              {cat}
            </button>
          );
        })}
      </div>

      {/* 4. Active Downloads Section (Real-time progress bars) */}
      {activeDownloads.length > 0 && (
        <div className="px-4 py-2 space-y-2 shrink-0">
          <p className="text-xs font-semibold text-blue-400">
            Downloading ({activeDownloads.length})
          </p>
          {activeDownloads.map((task) => (
            <div
              key={task.id}
              className="p-3 rounded-2xl bg-[#1E2430] border border-blue-500/30 shadow-md"
            >
              <div className="flex items-center justify-between mb-2">
                <div className="flex items-center gap-3 overflow-hidden">
                  <div className="w-8 h-8 rounded-full bg-blue-600 flex items-center justify-center shrink-0">
                    <Download className="w-4 h-4 text-white animate-bounce" />
                  </div>
                  <div className="overflow-hidden">
                    <p className="text-xs font-medium text-white truncate max-w-[200px]">
                      {task.name}
                    </p>
                    <p className="text-[11px] text-blue-300">
                      {task.speedText || '3.2 MB/s'} • {task.progress ?? 0}%
                    </p>
                  </div>
                </div>

                {onCancelDownload && (
                  <button
                    type="button"
                    onClick={() => onCancelDownload(task.id)}
                    className="w-7 h-7 rounded-full hover:bg-white/10 flex items-center justify-center text-slate-400 cursor-pointer"
                  >
                    <X className="w-4 h-4" />
                  </button>
                )}
              </div>

              {/* Progress Bar */}
              <div className="w-full h-1.5 rounded-full bg-slate-800 overflow-hidden">
                <div
                  className="h-full bg-gradient-to-r from-blue-500 to-sky-400 transition-all duration-300 rounded-full"
                  style={{ width: `${task.progress ?? 0}%` }}
                />
              </div>
            </div>
          ))}
        </div>
      )}

      {/* 5. Main Content: Empty State or Filtered Downloads List */}
      <div className="flex-1 overflow-y-auto px-4 py-2">
        {filteredList.length === 0 && activeDownloads.length === 0 ? (
          /* Exact reproduction of screenshot's Hexagon Badge empty state */
          <div className="w-full h-full flex flex-col items-center justify-center text-center p-6 -mt-8">
            {/* Blue Hexagon with Arrow */}
            <div
              className="w-24 h-24 bg-[#2563EB] flex items-center justify-center text-white mb-6 shadow-xl shadow-blue-500/20"
              style={{
                clipPath: 'polygon(50% 0%, 100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%)',
              }}
            >
              <Download className="w-12 h-12 stroke-[2]" />
            </div>

            <h3 className="text-lg font-semibold text-slate-100 mb-2">
              You'll find your downloads here
            </h3>
            <p className="text-xs text-slate-400 max-w-xs leading-relaxed">
              You can save images, APKs and files to view offline or share with other apps
            </p>
          </div>
        ) : (
          <div className="space-y-1">
            {filteredList.map((item) => (
              <div
                key={item.id}
                onClick={() => {
                  showToast(`Opening ${item.name}`);
                }}
                className="flex items-center justify-between p-2.5 rounded-xl hover:bg-white/5 transition-colors cursor-pointer group"
              >
                <div className="flex items-center gap-3 overflow-hidden">
                  <div
                    className={`w-10 h-10 rounded-xl flex items-center justify-center shrink-0 ${getCategoryBg(
                      item.category
                    )}`}
                  >
                    {getCategoryIcon(item.category)}
                  </div>
                  <div className="overflow-hidden">
                    <p className="text-sm font-medium text-slate-100 truncate max-w-[220px]">
                      {item.name}
                    </p>
                    <p className="text-xs text-slate-400">
                      {item.sizeText} • {item.dateText}
                    </p>
                  </div>
                </div>

                <button
                  type="button"
                  onClick={(e) => {
                    e.stopPropagation();
                    setSelectedItemForAction(item);
                  }}
                  className="w-8 h-8 rounded-full hover:bg-white/10 flex items-center justify-center text-slate-400 hover:text-white cursor-pointer"
                >
                  <MoreVertical className="w-4 h-4" />
                </button>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Item Action Modal */}
      {selectedItemForAction && (
        <div
          onClick={() => setSelectedItemForAction(null)}
          className="absolute inset-0 z-50 bg-black/60 backdrop-blur-xs flex items-end sm:items-center justify-center p-3 animate-in fade-in duration-150"
        >
          <div
            onClick={(e) => e.stopPropagation()}
            className="w-full max-w-sm bg-[#232529] border border-white/10 rounded-2xl p-4 shadow-2xl space-y-3 animate-in slide-in-from-bottom-3 duration-200"
          >
            <div className="flex items-center justify-between border-b border-white/10 pb-3">
              <p className="text-sm font-semibold text-white truncate max-w-[240px]">
                {selectedItemForAction.name}
              </p>
              <button
                type="button"
                onClick={() => setSelectedItemForAction(null)}
                className="text-slate-400 hover:text-white"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            <div className="space-y-1 text-sm">
              <button
                type="button"
                onClick={() => {
                  showToast(`Opening ${selectedItemForAction.name}`);
                  setSelectedItemForAction(null);
                }}
                className="w-full flex items-center gap-3 px-3 py-2.5 rounded-xl hover:bg-white/10 text-slate-200 cursor-pointer"
              >
                <ExternalLink className="w-4 h-4 text-blue-400" />
                <span>Open file</span>
              </button>

              <button
                type="button"
                onClick={() => {
                  showToast('Share link copied');
                  setSelectedItemForAction(null);
                }}
                className="w-full flex items-center gap-3 px-3 py-2.5 rounded-xl hover:bg-white/10 text-slate-200 cursor-pointer"
              >
                <Share2 className="w-4 h-4 text-emerald-400" />
                <span>Share file</span>
              </button>

              {onDeleteDownload && (
                <button
                  type="button"
                  onClick={() => {
                    onDeleteDownload(selectedItemForAction.id);
                    setSelectedItemForAction(null);
                    showToast('File deleted');
                  }}
                  className="w-full flex items-center gap-3 px-3 py-2.5 rounded-xl hover:bg-red-500/20 text-red-400 cursor-pointer font-medium"
                >
                  <Trash2 className="w-4 h-4" />
                  <span>Delete</span>
                </button>
              )}
            </div>
          </div>
        </div>
      )}

      {/* Settings Modal */}
      {showSettings && (
        <div
          onClick={() => setShowSettings(false)}
          className="absolute inset-0 z-50 bg-black/60 backdrop-blur-xs flex items-center justify-center p-4 animate-in fade-in duration-150"
        >
          <div
            onClick={(e) => e.stopPropagation()}
            className="w-full max-w-sm bg-[#232529] border border-white/10 rounded-2xl p-5 shadow-2xl space-y-4"
          >
            <h3 className="text-base font-semibold text-white">Download Settings</h3>
            <div className="space-y-2 text-xs">
              <p className="text-slate-400">Download location:</p>
              <p className="text-blue-400 font-medium bg-black/40 p-2 rounded-lg break-all">
                /storage/emulated/0/Download/Niooonu
              </p>
              <div className="pt-2 text-slate-300 space-y-1.5">
                <p className="flex items-center gap-2">
                  <span className="w-1.5 h-1.5 rounded-full bg-emerald-400" />
                  Real-time notification enabled
                </p>
                <p className="flex items-center gap-2">
                  <span className="w-1.5 h-1.5 rounded-full bg-emerald-400" />
                  Background downloads enabled
                </p>
                <p className="flex items-center gap-2">
                  <span className="w-1.5 h-1.5 rounded-full bg-emerald-400" />
                  Download any link like Chrome
                </p>
              </div>
            </div>
            <button
              type="button"
              onClick={() => setShowSettings(false)}
              className="w-full py-2 bg-blue-600 hover:bg-blue-500 rounded-xl text-xs font-semibold text-white cursor-pointer"
            >
              Done
            </button>
          </div>
        </div>
      )}

      {/* Toast */}
      {toastMessage && (
        <div className="absolute bottom-6 left-1/2 -translate-x-1/2 z-50 bg-slate-900/95 text-white text-xs px-4 py-2 rounded-full border border-white/20 shadow-lg flex items-center gap-2 animate-in fade-in slide-in-from-bottom-2">
          <CheckCircle2 className="w-3.5 h-3.5 text-emerald-400" />
          <span>{toastMessage}</span>
        </div>
      )}
    </div>
  );
};
