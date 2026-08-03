import React from 'react';

export const DashboardShell = ({
  quickActions = [],
  tabs = [],
  activeTab,
  onTabChange,
  children
}) => {
  return (
    <div className="flex flex-col h-full overflow-hidden bg-[#F3F6FF] font-sans text-slate-700">
      {/* ── Quick Actions Row ── */}
      {quickActions.length > 0 && (
        <div className="flex items-center gap-2.5 overflow-x-auto px-4 py-2 border-b border-slate-200/60 bg-slate-50/50 shrink-0 no-scrollbar">
          {quickActions.map((btn, i) => {
            const Icon = btn.icon;
            return (
              <button 
                key={i} 
                onClick={btn.action}
                className="flex flex-col items-center justify-center min-w-[100px] h-[68px] px-3 py-2 bg-white border border-slate-100 rounded-xl shadow-2xs hover:shadow-xs transition-all text-[10px] font-bold text-slate-800 shrink-0"
              >
                <span className={`${btn.bg || 'bg-indigo-50'} ${btn.color || 'text-indigo-600'} p-1 rounded-md mb-1`}>
                  <Icon size={16} />
                </span>
                <span className="leading-tight text-center whitespace-pre-line">{btn.label}</span>
              </button>
            );
          })}
        </div>
      )}

      {/* ── Main Navigation Tabs ── */}
      {tabs.length > 0 && (
        <div className="flex items-center gap-6 px-4 bg-white border-b border-slate-200 overflow-x-auto text-xs font-semibold shrink-0 h-11 no-scrollbar">
          {tabs.map((tab, i) => {
            const tabId = typeof tab === 'string' ? tab : tab.id;
            const tabLabel = typeof tab === 'string' ? tab : tab.label;
            const TabIcon = typeof tab === 'object' ? tab.icon : null;
            const isActive = activeTab === tabId || (!activeTab && i === 0);
            return (
              <button 
                key={i}
                onClick={() => onTabChange && onTabChange(tabId)}
                className={`h-full border-b-2 whitespace-nowrap flex items-center gap-1.5 transition-all text-xs font-semibold ${
                  isActive ? 'border-[#5145CD] text-[#5145CD] font-bold' : 'border-transparent text-slate-500 hover:text-slate-800'
                }`}
              >
                {TabIcon && <TabIcon size={15} className={isActive ? 'text-[#5145CD]' : 'text-slate-400'} />}
                {tabLabel}
              </button>
            );
          })}
        </div>
      )}

      {/* ── Dashboard Content Container (Fills viewport without root scroll) ── */}
      <div className="flex-1 overflow-hidden p-3 md:p-4 flex flex-col gap-3 min-height-0">
        {children}
      </div>
    </div>
  );
};

export const DashboardGrid = ({ left, center, right }) => (
  <div className="flex-1 grid grid-cols-1 lg:grid-cols-12 gap-3 min-h-0 overflow-hidden">
    {left && <div className="lg:col-span-3 flex flex-col gap-3 min-h-0 overflow-y-auto pr-0.5">{left}</div>}
    {center && <div className={(left && right) ? "lg:col-span-6 flex flex-col min-h-0 overflow-hidden" : left ? "lg:col-span-9 flex flex-col min-h-0 overflow-hidden" : right ? "lg:col-span-9 flex flex-col min-h-0 overflow-hidden" : "lg:col-span-12 flex flex-col min-h-0 overflow-hidden"}>{center}</div>}
    {right && <div className="lg:col-span-3 flex flex-col gap-3 min-h-0 overflow-y-auto pr-0.5">{right}</div>}
  </div>
);

export const BottomRow = ({ recentActivities, aiAssistant, quickSearch, pharmacyRecentBills, pharmacyLowStock }) => (
  <div className="grid grid-cols-1 lg:grid-cols-12 gap-3 shrink-0">
    {recentActivities && <div className="lg:col-span-4">{recentActivities}</div>}
    {aiAssistant && <div className="lg:col-span-4 flex flex-col">{aiAssistant}</div>}
    {quickSearch && <div className="lg:col-span-4">{quickSearch}</div>}
    {pharmacyRecentBills && <div className="lg:col-span-6">{pharmacyRecentBills}</div>}
    {pharmacyLowStock && <div className="lg:col-span-6">{pharmacyLowStock}</div>}
  </div>
);
