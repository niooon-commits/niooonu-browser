import React, { useState, useEffect, useCallback } from 'react';
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
  RefreshCw,
  Clock,
  Zap,
  Activity,
  AlertCircle,
} from 'lucide-react';

interface WorkflowRunInfo {
  id: number;
  name: string;
  status: string; // 'queued' | 'in_progress' | 'completed'
  conclusion: string | null; // 'success' | 'failure' | null
  htmlUrl: string;
  createdAt: string;
  updatedAt: string;
  headSha: string;
  currentStep?: string;
  completedStepsCount: number;
  totalStepsCount: number;
}

interface ReleaseInfo {
  tag: string;
  name: string;
  updatedAt: string;
  downloadUrl: string;
  sizeBytes: number;
}

export default function App() {
  const repositoryUrl = 'https://github.com/niooon-commits/niooonu-browser';
  const releasesUrl = 'https://github.com/niooon-commits/niooonu-browser/releases';
  const actionsUrl = 'https://github.com/niooon-commits/niooonu-browser/actions';

  const [workflow, setWorkflow] = useState<WorkflowRunInfo | null>(null);
  const [release, setRelease] = useState<ReleaseInfo | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [autoPoll, setAutoPoll] = useState<boolean>(true);
  const [lastChecked, setLastChecked] = useState<Date>(new Date());
  const [pollError, setPollError] = useState<string | null>(null);

  // Client-side fetch directly from GitHub API — CONSUMES 0 AI TOKENS
  const checkWorkflowStatus = useCallback(async () => {
    try {
      setLoading(true);
      setPollError(null);

      // Fetch latest workflow run
      const runRes = await fetch(
        'https://api.github.com/repos/niooon-commits/niooonu-browser/actions/runs?per_page=1'
      );
      if (!runRes.ok) throw new Error(`GitHub API returned ${runRes.status}`);
      const runData = await runRes.json();

      if (runData.workflow_runs && runData.workflow_runs.length > 0) {
        const latest = runData.workflow_runs[0];
        let currentStepName = '';
        let completedCount = 0;
        let totalCount = 0;

        // Try to fetch jobs to see exact step
        try {
          const jobsRes = await fetch(
            `https://api.github.com/repos/niooon-commits/niooonu-browser/actions/runs/${latest.id}/jobs`
          );
          if (jobsRes.ok) {
            const jobsData = await jobsRes.json();
            if (jobsData.jobs && jobsData.jobs[0] && jobsData.jobs[0].steps) {
              const steps = jobsData.jobs[0].steps;
              totalCount = steps.length;
              completedCount = steps.filter((s: { status: string }) => s.status === 'completed').length;
              const runningStep = steps.find((s: { status: string }) => s.status === 'in_progress');
              if (runningStep) {
                currentStepName = runningStep.name;
              } else if (latest.status === 'completed') {
                currentStepName = 'All steps completed';
              }
            }
          }
        } catch {
          // ignore job step fetch errors
        }

        setWorkflow({
          id: latest.id,
          name: latest.name,
          status: latest.status,
          conclusion: latest.conclusion,
          htmlUrl: latest.html_url,
          createdAt: latest.created_at,
          updatedAt: latest.updated_at,
          headSha: latest.head_sha.substring(0, 7),
          currentStep: currentStepName,
          completedStepsCount: completedCount,
          totalStepsCount: totalCount || 8,
        });
      }

      // Fetch latest release APK info
      try {
        const relRes = await fetch(
          'https://api.github.com/repos/niooon-commits/niooonu-browser/releases/tags/v1.0.0'
        );
        if (relRes.ok) {
          const relData = await relRes.json();
          const apkAsset = relData.assets?.find((a: { name: string }) =>
            a.name.endsWith('.apk')
          );
          if (apkAsset) {
            setRelease({
              tag: relData.tag_name,
              name: relData.name || 'niooonu browser (v1.0.0)',
              updatedAt: apkAsset.updated_at,
              downloadUrl: apkAsset.browser_download_url,
              sizeBytes: apkAsset.size,
            });
          }
        }
      } catch {
        // ignore release fetch error
      }

      setLastChecked(new Date());
    } catch (err: unknown) {
      if (err instanceof Error) {
        setPollError(err.message);
      } else {
        setPollError('Failed to fetch status');
      }
    } finally {
      setLoading(false);
    }
  }, []);

  // Initial load
  useEffect(() => {
    checkWorkflowStatus();
  }, [checkWorkflowStatus]);

  // Client-side auto-poll timer without burning AI tokens
  useEffect(() => {
    if (!autoPoll) return;
    const intervalMs = workflow?.status === 'in_progress' ? 6000 : 15000;
    const timer = setInterval(() => {
      checkWorkflowStatus();
    }, intervalMs);
    return () => clearInterval(timer);
  }, [autoPoll, workflow?.status, checkWorkflowStatus]);

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

  const calculateProgressPercent = () => {
    if (!workflow) return 0;
    if (workflow.status === 'completed') return 100;
    if (workflow.status === 'queued') return 10;
    if (workflow.totalStepsCount > 0) {
      const pct = Math.round((workflow.completedStepsCount / workflow.totalStepsCount) * 100);
      return Math.min(Math.max(pct, 20), 90);
    }
    return 45;
  };

  const formatFileSize = (bytes: number) => {
    if (!bytes) return '';
    const mb = bytes / (1024 * 1024);
    return `${mb.toFixed(1)} MB`;
  };

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
            {release?.downloadUrl ? (
              <a
                href={release.downloadUrl}
                target="_blank"
                rel="noreferrer"
                className="inline-flex items-center gap-2 px-4 py-2 rounded-xl bg-blue-600 hover:bg-blue-500 text-white text-xs font-semibold shadow-md shadow-blue-600/30 transition-all active:scale-95"
              >
                <Download className="w-4 h-4" />
                <span>Get APK ({formatFileSize(release.sizeBytes) || 'v1.0.0'})</span>
              </a>
            ) : (
              <a
                href={releasesUrl}
                target="_blank"
                rel="noreferrer"
                className="inline-flex items-center gap-2 px-4 py-2 rounded-xl bg-blue-600 hover:bg-blue-500 text-white text-xs font-semibold shadow-md shadow-blue-600/30 transition-all active:scale-95"
              >
                <Download className="w-4 h-4" />
                <span>Releases Page</span>
              </a>
            )}
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
        {/* Zero-Token Live Build & Workflow Tracker */}
        <div className="rounded-3xl bg-gradient-to-b from-[#181B26] to-[#12141D] border border-blue-500/20 p-6 sm:p-8 shadow-2xl relative overflow-hidden">
          <div className="absolute -top-10 -right-10 w-80 h-80 bg-blue-600/10 rounded-full blur-3xl pointer-events-none" />

          <div className="relative z-10 space-y-6">
            {/* Header / Badges */}
            <div className="flex flex-wrap items-center justify-between gap-4">
              <div className="flex items-center gap-2.5">
                <span className="p-2 rounded-xl bg-blue-500/20 border border-blue-500/30 text-blue-400">
                  <Activity className="w-5 h-5" />
                </span>
                <div>
                  <div className="flex items-center gap-2">
                    <h2 className="text-xl font-bold text-white tracking-tight">
                      Live Android Build Monitor
                    </h2>
                    <span className="inline-flex items-center gap-1 text-[11px] font-semibold px-2.5 py-0.5 rounded-full bg-emerald-500/15 border border-emerald-500/30 text-emerald-400">
                      <Zap className="w-3 h-3 text-emerald-400" />
                      Zero AI Token Cost
                    </span>
                  </div>
                  <p className="text-xs text-slate-400">
                    Runs directly in your browser. You can stay on this page to watch the build finish with 0 token consumption.
                  </p>
                </div>
              </div>

              {/* Polling controls */}
              <div className="flex items-center gap-2">
                <button
                  onClick={() => setAutoPoll(!autoPoll)}
                  className={`px-3 py-1.5 rounded-xl text-xs font-medium border transition-colors flex items-center gap-1.5 ${
                    autoPoll
                      ? 'bg-emerald-500/10 border-emerald-500/30 text-emerald-300'
                      : 'bg-white/5 border-white/10 text-slate-400'
                  }`}
                  title="Toggle automatic status refresh in browser"
                >
                  <span className={`w-2 h-2 rounded-full ${autoPoll ? 'bg-emerald-400 animate-pulse' : 'bg-slate-500'}`} />
                  {autoPoll ? 'Auto-Watch: ON' : 'Auto-Watch: OFF'}
                </button>

                <button
                  onClick={checkWorkflowStatus}
                  disabled={loading}
                  className="px-3 py-1.5 rounded-xl bg-white/5 hover:bg-white/10 border border-white/10 text-slate-300 text-xs font-medium transition-all active:scale-95 flex items-center gap-1.5 disabled:opacity-50"
                >
                  <RefreshCw className={`w-3.5 h-3.5 ${loading ? 'animate-spin' : ''}`} />
                  <span>Refresh</span>
                </button>
              </div>
            </div>

            {/* Status Details Bar */}
            {workflow ? (
              <div className="space-y-4 rounded-2xl bg-[#0D0F14]/70 border border-white/10 p-5">
                <div className="flex flex-wrap items-center justify-between gap-3 text-xs">
                  <div className="flex items-center gap-3">
                    {/* Status Pill */}
                    {workflow.status === 'in_progress' ? (
                      <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-blue-500/20 text-blue-300 border border-blue-500/40 font-semibold animate-pulse">
                        <span className="w-2 h-2 rounded-full bg-blue-400 animate-ping" />
                        Building Release APK...
                      </span>
                    ) : workflow.status === 'completed' && workflow.conclusion === 'success' ? (
                      <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-emerald-500/20 text-emerald-300 border border-emerald-500/40 font-semibold">
                        <CheckCircle2 className="w-4 h-4 text-emerald-400" />
                        Build Succeeded &amp; Released
                      </span>
                    ) : (
                      <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-amber-500/20 text-amber-300 border border-amber-500/40 font-semibold">
                        <AlertCircle className="w-4 h-4 text-amber-400" />
                        {workflow.status} ({workflow.conclusion || 'pending'})
                      </span>
                    )}

                    <span className="text-slate-400">
                      Run <code className="text-slate-200">#{workflow.id}</code> ({workflow.headSha})
                    </span>
                  </div>

                  <div className="flex items-center gap-3 text-slate-400">
                    <span className="flex items-center gap-1">
                      <Clock className="w-3.5 h-3.5 text-slate-500" />
                      Updated {new Date(workflow.updatedAt).toLocaleTimeString()}
                    </span>
                    <a
                      href={workflow.htmlUrl}
                      target="_blank"
                      rel="noreferrer"
                      className="text-blue-400 hover:text-blue-300 flex items-center gap-1 underline underline-offset-2"
                    >
                      <span>Action Logs</span>
                      <ExternalLink className="w-3 h-3" />
                    </a>
                  </div>
                </div>

                {/* Progress bar */}
                <div className="space-y-1.5">
                  <div className="flex justify-between text-xs text-slate-400">
                    <span>
                      {workflow.currentStep
                        ? `Step: ${workflow.currentStep}`
                        : workflow.status === 'in_progress'
                        ? 'Compiling Kotlin & Gradle assembleRelease...'
                        : 'Assembly & Release Done'}
                    </span>
                    <span className="font-mono text-slate-300 font-medium">
                      {calculateProgressPercent()}%
                    </span>
                  </div>
                  <div className="w-full h-2 rounded-full bg-slate-800 overflow-hidden">
                    <div
                      className={`h-full transition-all duration-500 rounded-full ${
                        workflow.status === 'completed' && workflow.conclusion === 'success'
                          ? 'bg-emerald-500'
                          : 'bg-gradient-to-r from-blue-500 to-cyan-400'
                      }`}
                      style={{ width: `${calculateProgressPercent()}%` }}
                    />
                  </div>
                </div>

                {/* Release Artifact Card when complete */}
                {release && (
                  <div className="mt-3 pt-3 border-t border-white/10 flex flex-wrap items-center justify-between gap-3 text-xs">
                    <div className="flex items-center gap-2">
                      <FolderDown className="w-4 h-4 text-blue-400" />
                      <span className="font-semibold text-white">niooonu-browser-release.apk</span>
                      <span className="text-slate-400">({formatFileSize(release.sizeBytes)})</span>
                      <span className="px-2 py-0.5 rounded bg-white/5 text-slate-400 text-[10px]">
                        Tag: {release.tag}
                      </span>
                    </div>

                    <a
                      href={release.downloadUrl}
                      target="_blank"
                      rel="noreferrer"
                      className="px-3.5 py-1.5 rounded-xl bg-blue-600 hover:bg-blue-500 text-white font-semibold flex items-center gap-1.5 shadow-md shadow-blue-600/30 transition-transform active:scale-95"
                    >
                      <Download className="w-3.5 h-3.5" />
                      <span>Direct Download APK</span>
                    </a>
                  </div>
                )}
              </div>
            ) : (
              <div className="p-6 rounded-2xl bg-[#0D0F14]/70 border border-white/10 text-center text-xs text-slate-400">
                {pollError ? `Error: ${pollError}` : 'Loading GitHub Actions status...'}
              </div>
            )}

            <div className="text-[11px] text-slate-400 flex items-center justify-between">
              <span>Last checked: {lastChecked.toLocaleTimeString()}</span>
              <span>CLI helper: <code>npm run wait-workflow</code></span>
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
