import React from 'react';
import { 
  BarChart3, Calendar, ChevronDown, Users, 
  DollarSign, Clock, ArrowUp, ArrowDown, ArrowRight 
} from 'lucide-react';

const MiniLineChart = ({ color }) => {
  let path = "M 0 20 Q 5 18, 10 15 T 20 18 T 30 10 T 40 12 T 50 5 T 60 0";
  if (color === 'green') path = "M 0 20 Q 5 22, 10 18 T 25 18 T 35 15 T 45 10 T 60 5";
  if (color === 'orange') path = "M 0 15 Q 10 18, 15 15 T 30 18 T 45 15 T 60 5";
  
  const hex = color === 'blue' ? '#4F46E5' : color === 'green' ? '#10B981' : '#F97316';
  
  return (
    <svg width="60" height="24" viewBox="0 0 60 24" fill="none" className="opacity-80">
      <path d={path} stroke={hex} strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" fill="none"/>
    </svg>
  );
};

const MetricCard = ({ title, value, icon: Icon, colorTheme, trend, trendUp }) => {
  const themes = {
    blue: { text: 'text-[#4F46E5]', iconBg: 'bg-[#EEF2FF]', trendText: 'text-[#059669]', trendBg: 'bg-[#D1FAE5]', wave: '#EEF2FF' },
    green: { text: 'text-[#10B981]', iconBg: 'bg-[#ECFDF5]', trendText: 'text-[#059669]', trendBg: 'bg-[#D1FAE5]', wave: '#ECFDF5' },
    orange: { text: 'text-[#F97316]', iconBg: 'bg-[#FFF7ED]', trendText: 'text-[#059669]', trendBg: 'bg-[#D1FAE5]', wave: '#FFF7ED' },
  };
  const theme = themes[colorTheme];
  
  return (
    <div className={`relative overflow-hidden rounded-2xl bg-white border border-gray-100 p-5 shadow-[0_2px_10px_rgba(0,0,0,0.02)] flex items-center justify-between`}>
      <div className="flex items-center gap-4 relative z-10">
        <div className={`w-14 h-14 rounded-full ${theme.iconBg} flex items-center justify-center shrink-0`}>
          <Icon className={`w-6 h-6 ${theme.text}`} />
        </div>
        <div>
          <p className={`text-[11px] font-bold uppercase tracking-wider ${theme.text} mb-1`}>{title}</p>
          <p className="text-3xl font-bold text-gray-900 leading-none">{value}</p>
          <div className="flex items-center gap-2 mt-2">
            <span className={`inline-flex items-center gap-0.5 px-1.5 py-0.5 rounded text-[10px] font-bold ${theme.trendBg} ${theme.trendText}`}>
              {trendUp ? <ArrowUp className="w-3 h-3" /> : <ArrowDown className="w-3 h-3" />}
              {trend}
            </span>
            <span className="text-[11px] text-gray-500">vs last 7 days</span>
          </div>
        </div>
      </div>
      <div className="relative z-10 hidden sm:block shrink-0">
         <MiniLineChart color={colorTheme} />
      </div>
      {/* Wave Background */}
      <div className={`absolute right-0 bottom-0 pointer-events-none opacity-40`}>
         <svg width="150" height="100" viewBox="0 0 150 100" fill="none">
           <path d="M0 100 C 50 100, 80 40, 150 0 L 150 100 Z" fill={theme.wave}/>
         </svg>
      </div>
    </div>
  );
};

const SummaryItem = ({ icon: Icon, theme, title, value, trend, trendUp = true }) => {
  const colors = {
    blue: { iconBg: 'bg-[#EEF2FF]', iconText: 'text-[#4F46E5]' },
    green: { iconBg: 'bg-[#ECFDF5]', iconText: 'text-[#10B981]' },
    orange: { iconBg: 'bg-[#FFF7ED]', iconText: 'text-[#F97316]' },
  };
  const c = colors[theme];
  return (
    <div className="flex items-center gap-4">
      <div className={`w-10 h-10 rounded-xl ${c.iconBg} flex items-center justify-center shrink-0`}>
        <Icon className={`w-5 h-5 ${c.iconText}`} />
      </div>
      <div className="flex-1">
        <p className="text-[11px] text-gray-500 font-semibold mb-0.5">{title}</p>
        <div className="flex items-center justify-between">
          <p className="text-[15px] font-bold text-gray-900 leading-none">{value}</p>
          <span className={`flex items-center text-[11px] font-bold text-[#059669]`}>
            {trendUp ? <ArrowUp className="w-3 h-3 mr-0.5" /> : <ArrowDown className="w-3 h-3 mr-0.5" />}
            {trend}
          </span>
        </div>
      </div>
    </div>
  );
};

const BranchPerformance = () => {
  return (
    <div className="w-full h-full flex flex-col space-y-5 px-6 lg:px-8 py-6" style={{ fontFamily: "'Inter', sans-serif" }}>
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div className="flex items-center gap-4">
          <div className="w-12 h-12 rounded-xl bg-[#EEF2FF] flex items-center justify-center shrink-0">
            <BarChart3 className="w-6 h-6 text-[#4F46E5]" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-gray-900 leading-tight">Branch Performance</h1>
            <p className="text-[13px] text-gray-500 mt-0.5">
              Analyze daily patient footfall, revenue, and clinical metrics.
            </p>
          </div>
        </div>
        <button className="flex items-center gap-2 px-4 py-2.5 bg-white border border-gray-200 rounded-xl text-[13px] font-semibold text-gray-700 hover:bg-gray-50 transition-colors shadow-sm shrink-0">
          <Calendar className="w-4 h-4 text-gray-400" />
          May 20 – May 26, 2025
          <ChevronDown className="w-4 h-4 text-gray-400 ml-1" />
        </button>
      </div>

      {/* Metrics Row */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-5">
        <MetricCard title="Daily Footfall" value="142" icon={Users} colorTheme="blue" trend="12.5%" trendUp={true} />
        <MetricCard title="Gross Revenue" value="$12,450" icon={DollarSign} colorTheme="green" trend="8.3%" trendUp={true} />
        <MetricCard title="Avg Wait Time" value="14 min" icon={Clock} colorTheme="orange" trend="5.2%" trendUp={false} />
      </div>

      {/* Bottom Section */}
      <div className="flex flex-col lg:flex-row gap-5">
        {/* Chart Section */}
        <div className="flex-1 bg-white rounded-2xl border border-gray-100 shadow-[0_2px_10px_rgba(0,0,0,0.02)] p-6 overflow-hidden">
          <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-8">
            <div>
              <h2 className="text-[17px] font-bold text-gray-900">Monthly Performance Trends</h2>
              <p className="text-[13px] text-gray-500 mt-0.5">Track your branch performance over time</p>
            </div>
            <div className="flex flex-wrap items-center gap-3 mt-4 sm:mt-0">
              <div className="flex items-center bg-gray-50 p-1 rounded-xl border border-gray-100">
                <button className="px-4 py-1.5 bg-white text-[#4F46E5] text-[12px] font-bold rounded-lg shadow-sm">Footfall</button>
                <button className="px-4 py-1.5 text-gray-500 text-[12px] font-semibold hover:text-gray-700">Revenue</button>
                <button className="px-4 py-1.5 text-gray-500 text-[12px] font-semibold hover:text-gray-700">Wait Time</button>
              </div>
              <button className="flex items-center gap-2 px-3 py-1.5 bg-white border border-gray-200 rounded-xl text-[12px] font-semibold text-gray-700 hover:bg-gray-50">
                Last 6 Months
                <ChevronDown className="w-3.5 h-3.5 text-gray-400" />
              </button>
            </div>
          </div>
          
          {/* Chart Area */}
          <div className="relative w-full h-[280px]">
            {/* Y-Axis labels */}
            <div className="absolute left-0 top-0 bottom-8 w-8 flex flex-col justify-between text-[11px] text-gray-400 font-semibold text-right pr-2 z-10">
              <span>250</span>
              <span>200</span>
              <span>150</span>
              <span>100</span>
              <span>50</span>
              <span>0</span>
            </div>
            {/* Grid lines */}
            <div className="absolute left-10 right-0 top-0 bottom-8 flex flex-col justify-between z-0">
              {[...Array(6)].map((_, i) => (
                <div key={i} className="w-full h-px border-t border-dashed border-gray-100" />
              ))}
            </div>
            {/* SVG Curve */}
            <div className="absolute left-10 right-0 top-0 bottom-8 z-10">
              <svg width="100%" height="100%" preserveAspectRatio="none" viewBox="0 0 1000 250">
                <defs>
                  <linearGradient id="chartGradient" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0%" stopColor="#4F46E5" stopOpacity="0.15" />
                    <stop offset="100%" stopColor="#4F46E5" stopOpacity="0.0" />
                  </linearGradient>
                </defs>
                <path 
                  d="M 0 125 C 100 125, 100 90, 200 90 C 300 90, 300 120, 400 120 C 500 120, 500 80, 600 80 C 700 80, 700 40, 800 40 C 900 40, 900 90, 1000 90 L 1000 250 L 0 250 Z" 
                  fill="url(#chartGradient)" 
                />
                <path 
                  d="M 0 125 C 100 125, 100 90, 200 90 C 300 90, 300 120, 400 120 C 500 120, 500 80, 600 80 C 700 80, 700 40, 800 40 C 900 40, 900 90, 1000 90" 
                  fill="none" 
                  stroke="#2B4AFE" 
                  strokeWidth="3" 
                  strokeLinecap="round"
                />
                {/* Dot at the end */}
                <circle cx="1000" cy="90" r="5" fill="white" stroke="#2B4AFE" strokeWidth="2.5" />
                <circle cx="1000" cy="90" r="12" fill="#2B4AFE" fillOpacity="0.15" />
              </svg>
            </div>
            {/* X-Axis labels */}
            <div className="absolute left-10 right-0 bottom-0 flex justify-between text-[11px] text-gray-400 font-semibold z-10 pt-2">
              <span>Dec 2024</span>
              <span>Jan 2025</span>
              <span>Feb 2025</span>
              <span>Mar 2025</span>
              <span>Apr 2025</span>
              <span>May 2025</span>
            </div>
          </div>
        </div>

        {/* Sidebar Right */}
        <div className="w-full lg:w-80 flex flex-col shrink-0">
          <div className="bg-white rounded-2xl border border-gray-100 shadow-[0_2px_10px_rgba(0,0,0,0.02)] p-6 flex-1 flex flex-col">
            <h3 className="text-[14px] font-bold text-gray-900 mb-6">Performance Summary</h3>
            
            <div className="space-y-5 flex-1">
              <SummaryItem icon={Users} theme="blue" title="Total Footfall" value="892" trend="15.7%" />
              <div className="w-full h-px bg-gray-50" />
              <SummaryItem icon={DollarSign} theme="green" title="Total Revenue" value="$68,450" trend="10.2%" />
              <div className="w-full h-px bg-gray-50" />
              <SummaryItem icon={Clock} theme="orange" title="Avg Wait Time" value="14 min" trend="4.8%" trendUp={false} />
            </div>

            <button className="w-full mt-6 py-3 bg-[#EEF2FF] text-[#4F46E5] rounded-xl text-[13px] font-bold hover:bg-[#E0E7FF] transition-colors flex items-center justify-center gap-2">
              View Detailed Report <ArrowRight className="w-4 h-4" />
            </button>
          </div>
        </div>
      </div>

    </div>
  );
};

export default BranchPerformance;
