export interface BrowserTab {
  id: string;
  title: string;
  url: string;
  isCurrent?: boolean;
}

export interface ShortcutItem {
  id: string;
  title: string;
  url: string;
  iconBg: string;
  iconLetter: string;
  iconUrl?: string;
}

export interface DiscoverArticle {
  id: string;
  title: string;
  category: string;
  timeAgo: string;
  imageUrl: string;
  articleUrl: string;
}
