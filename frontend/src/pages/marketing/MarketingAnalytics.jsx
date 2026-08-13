import React from 'react';
import { BarChart, ArrowLeft, TrendingUp, Users, MousePointerClick, MailOpen } from 'lucide-react';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import Card from '../../components/ui/Card';
import { fadeIn } from '../../components/ui/motion';

const MarketingAnalytics = () => {
  return (
    <motion.div 
      initial="hidden" 
      animate="visible" 
      variants={fadeIn}
      className="max-w-6xl mx-auto space-y-6"
    >
      <div className="flex items-center justify-between gap-4">
        <div>
          <Link to="/marketing" className="inline-flex items-center text-xs font-semibold text-[var(--color-navy-600)] hover:underline mb-2 gap-1">
            <ArrowLeft className="w-3.5 h-3.5" /> Back to Dashboard
          </Link>
          <h1 className="text-2xl sm:text-3xl font-bold font-display text-[var(--color-navy-900)] m-0 flex items-center gap-2">
            <BarChart className="w-7 h-7 text-indigo-600" />
            Campaign Analytics
          </h1>
          <p className="text-sm text-[var(--color-text-muted)] m-0 mt-1">
            Track engagement, open rates, and conversion metrics. (Mocked UI)
          </p>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card className="bg-indigo-50 border-indigo-100">
          <Card.Body className="p-5 flex items-center gap-4">
            <div className="w-12 h-12 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 shrink-0">
              <Users size={24} />
            </div>
            <div>
              <p className="text-xs font-bold uppercase tracking-wider text-indigo-800">Total Reach</p>
              <p className="text-2xl font-bold text-indigo-900 mt-1">12,450</p>
            </div>
          </Card.Body>
        </Card>
        
        <Card className="bg-emerald-50 border-emerald-100">
          <Card.Body className="p-5 flex items-center gap-4">
            <div className="w-12 h-12 rounded-full bg-emerald-100 flex items-center justify-center text-emerald-600 shrink-0">
              <MailOpen size={24} />
            </div>
            <div>
              <p className="text-xs font-bold uppercase tracking-wider text-emerald-800">Avg. Open Rate</p>
              <p className="text-2xl font-bold text-emerald-900 mt-1">42.8%</p>
            </div>
          </Card.Body>
        </Card>

        <Card className="bg-blue-50 border-blue-100">
          <Card.Body className="p-5 flex items-center gap-4">
            <div className="w-12 h-12 rounded-full bg-blue-100 flex items-center justify-center text-blue-600 shrink-0">
              <MousePointerClick size={24} />
            </div>
            <div>
              <p className="text-xs font-bold uppercase tracking-wider text-blue-800">Avg. CTR</p>
              <p className="text-2xl font-bold text-blue-900 mt-1">15.2%</p>
            </div>
          </Card.Body>
        </Card>

        <Card className="bg-purple-50 border-purple-100">
          <Card.Body className="p-5 flex items-center gap-4">
            <div className="w-12 h-12 rounded-full bg-purple-100 flex items-center justify-center text-purple-600 shrink-0">
              <TrendingUp size={24} />
            </div>
            <div>
              <p className="text-xs font-bold uppercase tracking-wider text-purple-800">Conversions</p>
              <p className="text-2xl font-bold text-purple-900 mt-1">843</p>
            </div>
          </Card.Body>
        </Card>
      </div>

      <Card>
        <Card.Header>
          <h2 className="text-lg font-bold text-[var(--color-navy-900)]">Recent Campaign Performance</h2>
        </Card.Header>
        <Card.Body className="p-0">
          <div className="p-8 text-center bg-slate-50 border-b border-slate-200">
            <BarChart className="w-16 h-16 text-slate-300 mx-auto mb-4" />
            <h3 className="text-lg font-bold text-slate-700">Analytics Charts Pending</h3>
            <p className="text-sm text-slate-500 max-w-md mx-auto mt-2">
              Detailed reporting graphs and charts will be implemented in the next iteration of the marketing portal. Data shown above is mock data for UI preview purposes.
            </p>
          </div>
          
          <table className="w-full text-left text-sm opacity-70">
            <thead className="bg-slate-50 text-slate-500 uppercase text-[10px] font-bold tracking-wider">
              <tr>
                <th className="p-4 border-b border-slate-200">Campaign Name</th>
                <th className="p-4 border-b border-slate-200 text-right">Sent</th>
                <th className="p-4 border-b border-slate-200 text-right">Opened</th>
                <th className="p-4 border-b border-slate-200 text-right">Clicked</th>
                <th className="p-4 border-b border-slate-200 text-right">Converted</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              <tr className="hover:bg-slate-50">
                <td className="p-4 font-semibold text-[var(--color-navy-900)]">Flu Shot Reminder 2026</td>
                <td className="p-4 text-right">4,500</td>
                <td className="p-4 text-right">2,100 (46%)</td>
                <td className="p-4 text-right">850 (18%)</td>
                <td className="p-4 text-right">320 (7%)</td>
              </tr>
              <tr className="hover:bg-slate-50">
                <td className="p-4 font-semibold text-[var(--color-navy-900)]">Dental Checkup Promo</td>
                <td className="p-4 text-right">3,200</td>
                <td className="p-4 text-right">1,250 (39%)</td>
                <td className="p-4 text-right">420 (13%)</td>
                <td className="p-4 text-right">180 (5%)</td>
              </tr>
              <tr className="hover:bg-slate-50">
                <td className="p-4 font-semibold text-[var(--color-navy-900)]">New Clinic Branch Opening</td>
                <td className="p-4 text-right">12,000</td>
                <td className="p-4 text-right">6,800 (56%)</td>
                <td className="p-4 text-right">3,100 (25%)</td>
                <td className="p-4 text-right">N/A</td>
              </tr>
            </tbody>
          </table>
        </Card.Body>
      </Card>
    </motion.div>
  );
};

export default MarketingAnalytics;
